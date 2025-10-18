using System.Runtime.Versioning;
using Serilog;
using Serilog.Core;
using Serilog.Events;

namespace DLPClient.Application.Registries;

[SupportedOSPlatform("windows")]
public static class LoggerRegistry
{
    public static Logger CreateLogger()
    {
        return new LoggerConfiguration()
            .ReadFrom.Configuration(
                new ConfigurationBuilder()
                    .SetBasePath(Directory.GetCurrentDirectory())
                    .AddJsonFile("appsettings.json")
                    .AddJsonFile(
                        $"appsettings.{Environment.GetEnvironmentVariable("ASPNETCORE_ENVIRONMENT") ?? "Development"}.json",
                        true
                    )
                    .Build()
            )
            /*.MinimumLevel.Information()
            .MinimumLevel.Override("Microsoft", LogEventLevel.Information)
            .MinimumLevel.Override("System", LogEventLevel.Information)
            .Enrich.FromLogContext()
            .WriteTo.Console(
                outputTemplate: "{Timestamp:HH:mm:ss} [{Level:u3}] {Message:lj}{NewLine}{Exception}"
            )*/
            //.WriteTo.EventLog(ConstRegistry.ServiceName)
            .CreateLogger();
    }
}