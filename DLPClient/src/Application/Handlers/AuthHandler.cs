using System.Runtime.Versioning;
using DLPClient.Application.Models.Users;
using DLPClient.Application.Services.Https.Basic;
using Microsoft.Win32;
using Serilog;

namespace DLPClient.Application.Handlers;

[SupportedOSPlatform("windows")]
public class AuthHandler(
    IHttpApiClient httpClient
)
{
    public void Init()
    {
        Log.Debug("Initializing UserHandler...");
        SystemEvents.SessionSwitch += HandleSessionSwitch;
    }

    public void Destroy()
    {
        SystemEvents.SessionSwitch -= HandleSessionSwitch;
        Log.Debug("Destroying UserHandler...");
    }

    private void HandleSessionSwitch(object sender, SessionSwitchEventArgs sessionEvent)
    {
        switch (sessionEvent.Reason)
        {
            case SessionSwitchReason.SessionLogon:
                Log.Information("Session logon detected");
                HandleUser(Type.Logon);
                break;
            case SessionSwitchReason.SessionLogoff:
                Log.Information("Session logoff detected");
                HandleUser(Type.Logoff);
                break;
            case SessionSwitchReason.SessionLock:
                Log.Information("Session locked");
                HandleUser(Type.Lock);
                break;
            case SessionSwitchReason.SessionUnlock:
                Log.Information("Session unlocked");
                HandleUser(Type.Unlock);
                break;
            case SessionSwitchReason.RemoteConnect:
                Log.Information("Remote session connected");
                HandleUser(Type.RemoteConnect);
                break;
            case SessionSwitchReason.RemoteDisconnect:
                Log.Information("Remote session disconnected");
                HandleUser(Type.RemoteDisconnect);
                break;
            case SessionSwitchReason.ConsoleConnect:
                Log.Information("Console connected");
                HandleUser(Type.ConsoleConnect);
                break;
            case SessionSwitchReason.ConsoleDisconnect:
                Log.Information("Console disconnected");
                HandleUser(Type.ConsoleDisconnect);
                break;
            case SessionSwitchReason.SessionRemoteControl:
                Log.Information("Remote control disconnected");
                HandleUser(Type.RemoteControl);
                break;
            default:
                Log.Warning("Unknown session switch reason {Reason}", sessionEvent.Reason);
                HandleUser(Type.Unnamed);
                throw new ArgumentOutOfRangeException();
        }
    }

    private void HandleUser(Type type)
    {
        var user = Environment.UserName;
        var domain = Environment.UserDomainName;

        try
        {
            Log.Information(
                "User {Domain}\\{User} {Type}",
                domain,
                user,
                type
            );

            if (type != Type.Unnamed)
                httpClient.SendUserAuthRequest(
                    new UserAuthRequest(
                        user,
                        type
                    )
                );
        }
        catch (Exception ex)
        {
            Log.Error(ex, "Error handling user login");
        }
    }

    public enum Type
    {
        Logon,
        Logoff,
        Lock,
        Unlock,
        RemoteConnect,
        RemoteDisconnect,
        ConsoleConnect,
        ConsoleDisconnect,
        RemoteControl,
        Unnamed
    }
}