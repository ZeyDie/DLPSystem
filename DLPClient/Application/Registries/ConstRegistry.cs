namespace DLPClient.Application.Registries;

public static class ConstRegistry
{
    public const string ServiceName = "DLPService";
    public const string ServiceDisplayName = "DLP Service";

    public static readonly string
        AppLocation = Environment.CurrentDirectory; //Assembly.GetExecutingAssembly().Location;

    public static readonly string ComputerName = Environment.MachineName;
    public static readonly string DomainName = Environment.UserDomainName;
}