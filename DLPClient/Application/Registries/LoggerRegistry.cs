using System.Runtime.Versioning;
using Microsoft.Extensions.FileProviders;
using Serilog;
using Serilog.Core;
using Serilog.Events;

namespace DLPClient.Application.Registries;

[SupportedOSPlatform("windows")]
public static class LoggerRegistry
{
    public static Logger CreateLogger()
    {
        var profile = Environment.GetEnvironmentVariable("DOTNET_ENVIRONMENT");
        
        return new LoggerConfiguration()
            .ReadFrom.Configuration(
                new ConfigurationBuilder()
                    .SetBasePath(ConstRegistry.AppLocation)
                    .AddJsonFile(profile != null ? $"appsettings.{profile}.json" : "appsettings.json")
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