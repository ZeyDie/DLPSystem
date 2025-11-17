using System.Runtime.Versioning;
using DLPClient.Application.Utils;
using DLPClient.System.Registries;
using DLPClient.User.Registries;

namespace DLPClient.Application.Registries;

public static class AutostartRegistry
{
    [SupportedOSPlatform("windows")]
    public static void CreateAutostart()
    {
        if (UserUtil.IsAdministrator())
            AutostartSystemRegistry.CreateAutostart();
        
        AutostartUserRegistry.CreateAutostart();
    }
}