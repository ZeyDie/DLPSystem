using System.Runtime.Versioning;
using System.Security;
using Microsoft.Win32;
using Microsoft.Win32.TaskScheduler;
using Serilog;
using Task = Microsoft.Win32.TaskScheduler.Task;

namespace DLPClient.Application.Registries;

public static class AutostartRegistry
{
    [SupportedOSPlatform("windows")]
    public static void CreateAutostart()
    {
        try
        {
            RegistryAutorun();
        }
        catch (SecurityException exception)
        {
            Log.Error(exception.Message);
        }

        try
        {
            RegistryTask();
        }
        catch (SecurityException exception)
        {
            Log.Error(exception.Message);
        }
    }

    [SupportedOSPlatform("windows")]
    private static void RegistryAutorun()
    {
        const string serviceName = ConstRegistry.ServiceName;

        var registryKey = Registry.LocalMachine.OpenSubKey(@"SOFTWARE\Microsoft\Windows\CurrentVersion\Run", true);

        if (registryKey == null)
        {
            Log.Error("Could not find Run in Registry");
            return;
        }

        if (registryKey.GetValue(serviceName) != null)
            registryKey.DeleteValue(serviceName);

        registryKey.SetValue(serviceName, '"' + ConstRegistry.AppExeLocation + '"');
    }

    [SupportedOSPlatform("windows")]
    private static void RegistryTask()
    {
        DeleteTask();

        if (GetTask() == null)
            CreateTask();
    }

    private static void CreateTask()
    {
        var loginTrigger = new LogonTrigger();
        loginTrigger.Delay = TimeSpan.FromSeconds(1);
        loginTrigger.UserId = Environment.UserName;

        var taskDefinition = TaskService.Instance.NewTask();

        taskDefinition.Triggers.Add(loginTrigger);

        taskDefinition.Settings.Hidden = true;
        taskDefinition.Settings.Enabled = true;
        taskDefinition.Settings.StartWhenAvailable = true;
        taskDefinition.Settings.DisallowStartIfOnBatteries = false;
        taskDefinition.Settings.StopIfGoingOnBatteries = false;
        taskDefinition.Settings.ExecutionTimeLimit = TimeSpan.Zero;
        taskDefinition.Settings.AllowHardTerminate = false;
        taskDefinition.Settings.MultipleInstances = TaskInstancesPolicy.StopExisting;

        taskDefinition.Principal.UserId = Environment.UserName;
        taskDefinition.Principal.RunLevel = TaskRunLevel.LUA;
        taskDefinition.Principal.LogonType = TaskLogonType.InteractiveToken;

        taskDefinition.Actions.Add(new ExecAction(ConstRegistry.AppExeLocation, "", ConstRegistry.AppLocation));

        TaskService.Instance.RootFolder.RegisterTaskDefinition(ConstRegistry.ServiceName, taskDefinition);
    }

    private static void DeleteTask()
    {
        var task = GetTask();

        task?.Folder.DeleteTask(task.Name, false);
    }

    private static Task? GetTask()
    {
        return TaskService.Instance
            .AllTasks
            .FirstOrDefault(task =>
                task.Name.Equals(ConstRegistry.ServiceName, StringComparison.OrdinalIgnoreCase));
    }
}