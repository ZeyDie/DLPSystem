using System.Reflection;

namespace DLPClient.Application.Registries;

public static class ConstRegistry
{
    public const string ServiceName = "DLPService";
    public const string ServiceDisplayName = "DLP Service";

    public static readonly string
        AppLocation = Environment.CurrentDirectory; //Assembly.GetExecutingAssembly().Location;

    public static readonly string AppExeLocation = Path.GetFullPath(Assembly.GetExecutingAssembly().Location).Replace(".dll", ".exe");

    public static readonly string ComputerName = Environment.MachineName;
    public static readonly string DomainName = Environment.UserDomainName;

    public static class SecurityConstantIds
    {
        public static readonly string LogName = "Security";
        
        public static readonly int ProcessCreationId = 4688;
        public static readonly int ProcessTerminationId = 4689;
        public static readonly int SecuritySystemExtensionId = 4611;
        public static readonly int SystemIntegrityId = 5061;
        public static readonly int OtherSystemEventsId = 5058;
        public static readonly int LogonId = 4624;
        public static readonly int LogoffId = 4634;
    }
    
    public static class SystemConstantIds
    {
        public static readonly string LogName = "System";
        
        public static readonly int WinlogonId = 7001;
        public static readonly int WinlogoffId = 7002;
        
    }
}