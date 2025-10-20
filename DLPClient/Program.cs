using DLPClient.Application.Registries;
using DLPClient.Application.Utils;
using Serilog;

Log.Logger = LoggerRegistry.CreateLogger();

UserUtil.CurrentUser = Environment.UserName;

try
{
    Log.Information("Starting application {AppExeLocation}", ConstRegistry.AppExeLocation);

    AutostartRegistry.RegisterInStartup();

    await HostRegistry.CreateHost(args).RunAsync();
}
catch (Exception ex)
{
    Log.Fatal(ex, "Application terminated unexpectedly");
}
finally
{
    Log.CloseAndFlush();
}