using System.Diagnostics;
using System.Runtime.Versioning;
using DLPClient.Application.Registries;

namespace DLPClient.Application.Services.EventLogs;

[SupportedOSPlatform("windows")]
public class EventLogApiClient
{
    public List<EventLogEntry> GetLogonLogs(int entriesOfLast)
    {
        return GetSecurityLogs(entriesOfLast, ConstRegistry.LogonId);
    }

    public List<EventLogEntry> GetLogoffLogs(int entriesOfLast)
    {
        return GetSecurityLogs(entriesOfLast, ConstRegistry.LogoffId);
    }

    private List<EventLogEntry> GetSecurityLogs(int entriesOfLast, int id)
    {
        var list = new List<EventLogEntry>();

        using EventLog securityLog = new("Security");
        var entries = securityLog.Entries;

        if (entries == null) return list;

        entriesOfLast = Math.Min(entriesOfLast, entries.Count);

        for (var i = entries.Count - 1; i >= entries.Count - entriesOfLast; i--)
        {
            var entry = entries[i];

            if (entry.InstanceId == id)
                list.Add(entry);
        }

        return list;
    }
}