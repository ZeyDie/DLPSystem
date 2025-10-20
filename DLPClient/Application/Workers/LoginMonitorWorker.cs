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
            var logonList = EventLogApiClient.GetLogonLogs(10);
            var logoffList = EventLogApiClient.GetLogoffLogs(10);
            
            //TODO
            
            await Task.Delay(10000, stoppingToken);
        }
    }
}