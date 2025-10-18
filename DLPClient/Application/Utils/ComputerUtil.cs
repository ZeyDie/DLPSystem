using System.Net;
using System.Net.Sockets;
using System.Runtime.InteropServices;
using DLPClient.Application.Registries;

namespace DLPClient.Application.Utils;

public static class ComputerUtil
{
    public static string GetComputerName()
    {
        return ConstRegistry.ComputerName;
    }

    public static string GetDomainName()
    {
        return ConstRegistry.DomainName;
    }

    /*public static string GetLocalComputerName()
    {
        return Environment.MachineName;
    }

    public static string GetComputerWorkgroup()
    {
        string result;

        if (OperatingSystem.IsWindows())
        {
            result = GetComputerWorkgroupWin();
        }
        else result = GetLocalComputerName();

        if (result.Equals(Environment.UserDomainName))
            return "WORKGROUP";

        return result;
    }

    [SupportedOSPlatform("windows")]
    private static string GetComputerWorkgroupWin()
    {
#if RELEASE
            return Domain.GetComputerDomain().Name;
#else
        return Environment.UserDomainName;
#endif
    }*/

    public static string GetLocalIpAddress()
    {
        return Dns.GetHostEntry(Dns.GetHostName())
            .AddressList
            .FirstOrDefault(ip => ip.AddressFamily == AddressFamily.InterNetwork)
            ?.ToString() ?? "127.0.0.1";
    }

    [DllImport("Kernel32.dll")]
    private static extern long GetTickCount64();

    public static long GetSystemBootMillis()
    {
        return DateTimeOffset.UtcNow.AddMilliseconds(-(double)GetTickCount64()).ToUnixTimeMilliseconds();
    }
}