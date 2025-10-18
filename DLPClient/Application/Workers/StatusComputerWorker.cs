using DLPClient.Application.Models.Computers;
using DLPClient.Application.Services.Https.Basic;
using DLPClient.Application.Utils;
using Serilog;

namespace DLPClient.Application.Workers;

public class StatusComputerWorker(
    IHttpApiClient httpClient
) : BackgroundService
{
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        Log.Debug("StatusComputerWorker running at: {time}", DateTimeOffset.Now);

        while (!stoppingToken.IsCancellationRequested)
        {
            var computerRequest = new StatusComputerRequest(
                ComputerUtil.GetComputerName(),
                ComputerUtil.GetDomainName(),
                ComputerUtil.GetLocalIpAddress(),
                ComputerUtil.GetSystemBootMillis()
            );

            httpClient.SendRequest<object>(computerRequest);

            await Task.Delay(10000, stoppingToken);
        }
    }
}