using System.Runtime.Versioning;
using Microsoft.Win32;
using Microsoft.Win32.TaskScheduler;
using Serilog;
using Task = Microsoft.Win32.TaskScheduler.Task;

namespace DLPClient.Application.Registries;

public static class AutostartRegistry
{
    [SupportedOSPlatform("windows")]
    public static void RegisterInStartup()
    {
        DeleteTask();
        
        if (GetTask() == null)
            CreateTask();

        const string serviceName = ConstRegistry.ServiceName;

        var registryKey = Registry.CurrentUser.OpenSubKey(@"SOFTWARE\Microsoft\Windows\CurrentVersion\Run", true);

        if (registryKey == null)
        {
            Log.Error("Could not find Run in Registry");
            return;
        }

        if (registryKey.GetValue(serviceName) != null)
            registryKey.DeleteValue(serviceName);

        registryKey.SetValue(serviceName, Environment.CurrentDirectory);
    }

    private static void CreateTask()
    {
        var bootTrigger = new BootTrigger();
        bootTrigger.Delay = TimeSpan.FromSeconds(1);

        var taskDefinition = TaskService.Instance.NewTask();

        taskDefinition.Triggers.Add(bootTrigger);

        taskDefinition.Settings.Hidden = true;
        taskDefinition.Settings.Enabled = true;
        taskDefinition.Settings.StartWhenAvailable = true;
        taskDefinition.Settings.DisallowStartIfOnBatteries = false;
        taskDefinition.Settings.StopIfGoingOnBatteries = false;
        taskDefinition.Settings.ExecutionTimeLimit = TimeSpan.Zero;
        taskDefinition.Settings.AllowHardTerminate = false;
        taskDefinition.Settings.MultipleInstances = TaskInstancesPolicy.StopExisting;

        taskDefinition.Principal.UserId = "SYSTEM";
        taskDefinition.Principal.RunLevel = TaskRunLevel.Highest;
        taskDefinition.Principal.LogonType = TaskLogonType.ServiceAccount;

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
            .FirstOrDefault(x =>
                x.Name.Equals(ConstRegistry.ServiceName, StringComparison.OrdinalIgnoreCase)
            );
    }
}