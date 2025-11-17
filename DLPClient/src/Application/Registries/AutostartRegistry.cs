using System.Runtime.Versioning;
using DLPClient.System.Registries;
using DLPClient.User.Registries;

namespace DLPClient.Application.Registries;

public static class AutostartRegistry
{
    [SupportedOSPlatform("windows")]
    public static void CreateAutostart()
    {
        AutostartSystemRegistry.CreateAutostart();
        AutostartUserRegistry.CreateAutostart();
    }
}