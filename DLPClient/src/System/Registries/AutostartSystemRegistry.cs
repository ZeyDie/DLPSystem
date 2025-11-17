using System.Runtime.Versioning;
using System.Security;
using DLPClient.Application.Registries;
using Microsoft.Win32;
using Serilog;

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
}