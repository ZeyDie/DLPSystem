using System.Runtime.Versioning;
using Microsoft.Win32;
using Serilog;

namespace DLPClient.Application.Handlers;

[SupportedOSPlatform("windows")]
public class UserHandler
{
    public void Init()
    {
        Log.Debug("Initializing UserHandler...");
        SystemEvents.SessionSwitch += OnSessionSwitch;
    }

    public void Destroy()
    {
        SystemEvents.SessionSwitch -= OnSessionSwitch;
        Log.Debug("Destroying UserHandler...");
    }

    private void OnSessionSwitch(object sender, SessionSwitchEventArgs sessionEvent)
    {
        switch (sessionEvent.Reason)
        {
            case SessionSwitchReason.SessionLogon:
                Log.Information("Session logon detected");
                OnUserLoggedIn();
                break;
            case SessionSwitchReason.SessionLogoff:
                Log.Information("Session logoff detected");
                OnUserLoggedOut();
                break;
            case SessionSwitchReason.SessionLock:
                Log.Information("Session locked");
                break;
            case SessionSwitchReason.SessionUnlock:
                Log.Information("Session unlocked");
                break;
            case SessionSwitchReason.RemoteConnect:
                Log.Information("Remote session connected");
                break;
            case SessionSwitchReason.RemoteDisconnect:
                Log.Information("Remote session disconnected");
                break;
            case SessionSwitchReason.ConsoleConnect:
                Log.Information("Console connected");
                break;
            case SessionSwitchReason.ConsoleDisconnect:
                Log.Information("Console disconnected");
                break;
            case SessionSwitchReason.SessionRemoteControl:
                Log.Information("Remote control disconnected");
                break;
            default:
                Log.Warning("Unknown session switch reason");
                throw new ArgumentOutOfRangeException();
        }
    }

    private void OnUserLoggedIn()
    {
        try
        {
            var userName = Environment.UserName;
            var domain = Environment.UserDomainName;

            Log.Information("User {Domain}\\{User} logged in", domain, userName);

            HandleUserLogin(userName, domain);
        }
        catch (Exception ex)
        {
            Log.Error(ex, "Error handling user login");
        }
    }

    private void OnUserLoggedOut()
    {
        try
        {
            var userName = Environment.UserName;
            var domain = Environment.UserDomainName;

            Log.Information("User {Domain}\\{User} logged out", domain, userName);

            HandleUserLogout(userName, domain);
        }
        catch (Exception ex)
        {
            Log.Error(ex, "Error handling user logout");
        }
    }

    private void HandleUserLogin(string userName, string domain)
    {
        Log.Information("Handling user login");
    }

    private void HandleUserLogout(string userName, string domain)
    {
        Log.Information("Handling user logout");
    }
}