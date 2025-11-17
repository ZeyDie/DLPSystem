using System.Runtime.Versioning;
using System.Security.Principal;

namespace DLPClient.Application.Utils;

public static class UserUtil
{
    [SupportedOSPlatform("windows")]
    public static WindowsIdentity GetCurrentUser()
    {
        return WindowsIdentity.GetCurrent();
    }

    [SupportedOSPlatform("windows")]
    public static WindowsPrincipal GetPrincipal()
    {
        return GetPrincipal(GetCurrentUser());
    }

    [SupportedOSPlatform("windows")]
    public static WindowsPrincipal GetPrincipal(WindowsIdentity identity)
    {
        return new WindowsPrincipal(identity);
    }

    [SupportedOSPlatform("windows")]
    public static bool IsUser()
    {
        return !IsSystem();
    }

    [SupportedOSPlatform("windows")]
    public static bool IsSystem()
    {
        return GetPrincipal(GetCurrentUser()).IsInRole(WindowsBuiltInRole.SystemOperator);
    }
    
    [SupportedOSPlatform("windows")]
    public static bool IsAdministrator()
    {
        return IsAdministrator(GetCurrentUser());
    }

    [SupportedOSPlatform("windows")]
    public static bool IsAdministrator(WindowsIdentity identity)
    {
        return GetPrincipal(identity).IsInRole(WindowsBuiltInRole.Administrator);
    }
}