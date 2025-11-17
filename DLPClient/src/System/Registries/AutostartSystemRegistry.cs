using System.Runtime.Versioning;
using System.Security;
using DLPClient.Application.Registries;
using DLPClient.Application.Utils;
using Microsoft.Win32;
using Microsoft.Win32.TaskScheduler;
using Serilog;
using Task = Microsoft.Win32.TaskScheduler.Task;

namespace DLPClient.System.Registries;

public static class AutostartSystemRegistry
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
        
        Log.Information("Registered autorun in registry");
    }

    [SupportedOSPlatform("windows")]
    private static void RegistryTask()
    {
        //DeleteTask();

        if (GetTask() == null)
            CreateTask();
    }

    private static void CreateTask()
    {
        var userId = "SYSTEM";

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

        taskDefinition.Principal.UserId = userId;
        taskDefinition.Principal.RunLevel = TaskRunLevel.Highest;
        taskDefinition.Principal.LogonType = TaskLogonType.ServiceAccount;

        taskDefinition.Actions.Add(new ExecAction(ConstRegistry.AppExeLocation, "", ConstRegistry.AppLocation));

        TaskService.Instance.RootFolder.RegisterTaskDefinition(ConstRegistry.ServiceName, taskDefinition);
        
        Log.Information("Task for {UserId} created", $"{userId}");
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