using System.Runtime.Versioning;
using DLPClient.Application.Services.EventLogs;
using Serilog;

namespace DLPClient.Application.Workers;

[SupportedOSPlatform("windows")]
public class LoginMonitorWorker : BackgroundService
{
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        while (!stoppingToken.IsCancellationRequested)
        {
            var logonTask = EventLogApiClient.GetLogonLogs(10);
            var logoffTask = EventLogApiClient.GetLogoffLogs(10);
            
            await Task.WhenAll(logonTask, logoffTask);
            
            Log.Debug("Logon: {LogonLogs} Logoff: {LogoffLogs}", logonTask.Result.Count, logoffTask.Result.Count);
            
            //TODO
            
            await Task.Delay(10000, stoppingToken);
        }
    }
}