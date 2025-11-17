using DLPClient.Application.Registries;
using DLPClient.Application.Utils;
using Serilog;

Log.Logger = LoggerRegistry.CreateLogger();

try
{
    Log.Information(
        "Starting application {AppExeLocation} as {CurrentUser} (Mode: {Administrator})",
        ConstRegistry.AppExeLocation,
        UserUtil.GetCurrentUser().Name,
        UserUtil.IsAdministrator() ? "Administrator" : "User"
    );

    AutostartRegistry.CreateAutostart();

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