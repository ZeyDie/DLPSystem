using System.Runtime.Versioning;
using DLPClient.Application.Handlers;
using Serilog;

namespace DLPClient.Application.Workers;

[SupportedOSPlatform("windows")]
public class BackgroundWorker(
    UserHandler userHandler
) : BackgroundService
{
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        Log.Debug("BackgroundWorker running at: {time}", DateTimeOffset.Now);

        userHandler.Init();

        while (!stoppingToken.IsCancellationRequested) await Task.Delay(10000, stoppingToken);

        userHandler.Destroy();
    }
}