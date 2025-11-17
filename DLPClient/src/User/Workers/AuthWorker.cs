using System.Runtime.Versioning;
using DLPClient.User.Handlers;
using Serilog;

namespace DLPClient.User.Workers;

[SupportedOSPlatform("windows")]
public class AuthWorker(
    AuthHandler authHandler
) : BackgroundService
{
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        Log.Debug("BackgroundWorker running at: {time}", DateTimeOffset.Now);

        authHandler.Init();

        while (!stoppingToken.IsCancellationRequested) await Task.Delay(10000, stoppingToken);

        authHandler.Destroy();
    }
}