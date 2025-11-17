using System.Reflection;

namespace DLPClient.Application.Registries;

public static class ConstRegistry
{
    public const string ServiceName = "DLPService";
    public const string ServiceDisplayName = "DLP Service";

    public static readonly string AppLocation = Environment.CurrentDirectory;

    public static readonly string AppExeLocation =
        Path.GetFullPath(Assembly.GetExecutingAssembly().Location).Replace(".dll", ".exe");

    public static readonly string ComputerName = Environment.MachineName;
    public static readonly string DomainName = Environment.UserDomainName;

    public static class SecurityConstantIds
    {
        public const string LogName = "Security";

        public const int ProcessCreationId = 4688;
        public const int ProcessTerminationId = 4689;
        public const int SecuritySystemExtensionId = 4611;
        public const int SystemIntegrityId = 5061;
        public const int OtherSystemEventsId = 5058;
        public const int LogonId = 4624;
        public const int LogoffId = 4634;
    }

    public static class SystemConstantIds
    {
        public const string LogName = "System";

        public const int WinlogonId = 7001;
        public const int WinlogoffId = 7002;
    }
}