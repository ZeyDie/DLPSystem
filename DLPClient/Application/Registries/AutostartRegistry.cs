using System.Runtime.Versioning;
using Microsoft.Win32;
using Serilog;

namespace DLPClient.Application.Registries;

public static class AutostartRegistry
{
    [SupportedOSPlatform("windows")]
    public static void RegisterInStartup()
    {
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
}