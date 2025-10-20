using System.Runtime.Versioning;
using System.Text.Json;
using System.Text.Json.Serialization;
using DLPClient.Application.Handlers;
using DLPClient.Application.Models.Settings;
using DLPClient.Application.Services.EventLogs;
using DLPClient.Application.Services.Https;
using DLPClient.Application.Services.Https.Basic;
using DLPClient.Application.Workers;
using Dodo.HttpClientResiliencePolicies;
using Serilog;

namespace DLPClient.Application.Registries;

[SupportedOSPlatform("windows")]
public static class HostRegistry
{
    public static IHost CreateHost(string[] args)
    {
        const string serviceName = ConstRegistry.ServiceName;

        return Host.CreateDefaultBuilder(args)
            .UseWindowsService(options => { options.ServiceName = serviceName; })
            .UseSerilog()
            .ConfigureServices((context, services) =>
                {
                    services.AddWindowsService(options => { options.ServiceName = serviceName; });

                    services.AddSingleton(context.Configuration.GetSection("AppSettings").Get<AppSettings>());
                    services.AddSingleton<JsonSerializerOptions>(provider => new JsonSerializerOptions
                    {
                        WriteIndented = false,
                        DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull
                    });

                    services.AddHttpClient<IHttpApiClient, HttpApiClient>()
                        .ConfigureHttpClient((serviceProvider, client) =>
                        {
                            var settings = serviceProvider.GetRequiredService<AppSettings>();

                            client.BaseAddress = new Uri($"{settings.ApiUrl}{settings.ApiVersion}");
                            client.Timeout = TimeSpan.FromSeconds(settings.Timeout);
                        })
                        .AddResiliencePolicies();

                    services.AddSingleton<UserHandler>();
                    
                    services.AddHostedService<BackgroundWorker>();
                    services.AddHostedService<LoginMonitorWorker>();
                    services.AddHostedService<StatusComputerWorker>();

                    services.Configure<HostOptions>(options =>
                    {
                        options.ServicesStartConcurrently = true;
                        options.ServicesStopConcurrently = false;
                        options.BackgroundServiceExceptionBehavior = BackgroundServiceExceptionBehavior.StopHost;
                    });
                }
            )
            .UseDefaultServiceProvider((context, options) =>
            {
                options.ValidateScopes = context.HostingEnvironment.IsDevelopment();
                options.ValidateOnBuild = true;
            })
            .Build();
    }
}