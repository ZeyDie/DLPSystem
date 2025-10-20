using System.Runtime.Versioning;
using DLPClient.Application.Services.EventLogs;

namespace DLPClient.Application.Workers;

[SupportedOSPlatform("windows")]
public class LoginMonitorWorker(
    EventLogApiClient eventLogApiClient
)
    : BackgroundService
{
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        while (!stoppingToken.IsCancellationRequested)
        {
            var logonList = eventLogApiClient.GetLogonLogs(10);
            var logoffList = eventLogApiClient.GetLogoffLogs(10);
            
            

            await Task.Delay(10000, stoppingToken);
        }
    }
}