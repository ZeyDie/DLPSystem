using System.Runtime.Versioning;
using System.Security;
using DLPClient.Application.Registries;
using DLPClient.Application.Utils;
using Microsoft.Win32.TaskScheduler;
using Serilog;
using Task = Microsoft.Win32.TaskScheduler.Task;

namespace DLPClient.User.Registries;

public static class AutostartUserRegistry
{
    [SupportedOSPlatform("windows")]
    public static void CreateAutostart()
    {
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
    private static void RegistryTask()
    {
        DeleteTask();

        if (GetTask() == null)
            CreateTask();
    }

    private static void CreateTask()
    {
        var userId = UserUtil.GetCurrentUser().Name;

        var loginTrigger = new LogonTrigger();
        loginTrigger.Delay = TimeSpan.FromSeconds(1);
        loginTrigger.UserId = userId;

        var taskDefinition = TaskService.Instance.NewTask();

        taskDefinition.Triggers.Add(loginTrigger);

        taskDefinition.Settings.Hidden = true;
        taskDefinition.Settings.Enabled = true;
        taskDefinition.Settings.StartWhenAvailable = true;
        taskDefinition.Settings.DisallowStartIfOnBatteries = false;
        taskDefinition.Settings.StopIfGoingOnBatteries = false;
        taskDefinition.Settings.ExecutionTimeLimit = TimeSpan.Zero;
        taskDefinition.Settings.AllowHardTerminate = false;
        taskDefinition.Settings.MultipleInstances = TaskInstancesPolicy.Parallel;

        taskDefinition.Principal.UserId = userId;
        taskDefinition.Principal.RunLevel = TaskRunLevel.LUA;
        taskDefinition.Principal.LogonType = TaskLogonType.InteractiveToken;

        taskDefinition.Actions.Add(new ExecAction(ConstRegistry.AppExeLocation, "", ConstRegistry.AppLocation));

        TaskService.Instance.RootFolder.RegisterTaskDefinition(GetTaskName(), taskDefinition);

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
                task.Name.Equals(GetTaskName(), StringComparison.OrdinalIgnoreCase));
    }

    private static string GetTaskName() => ConstRegistry.ServiceName + " + " + UserUtil.GetCurrentUser().Name;
}