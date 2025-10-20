using System.Diagnostics;
using System.Runtime.Versioning;
using DLPClient.Application.Registries;
using Serilog;

namespace DLPClient.Application.Services.EventLogs;

[SupportedOSPlatform("windows")]
public static class EventLogApiClient
{
    public static List<EventLogEntry> GetLogonLogs(int entriesOfLast)
    {
        return GetLogs(
            entriesOfLast,
            ConstRegistry.SystemConstantIds.LogName,
            ConstRegistry.SystemConstantIds.WinlogonId
        );
    }

    public static List<EventLogEntry> GetLogoffLogs(int entriesOfLast)
    {
        return GetLogs(
            entriesOfLast,
            ConstRegistry.SystemConstantIds.LogName,
            ConstRegistry.SystemConstantIds.WinlogoffId
        );
    }

    private static List<EventLogEntry> GetLogs(int entriesOfLast, string logName, int eventId)
    {
        using EventLog eventLog = new(logName);
        var entries = eventLog.Entries;

        if (entries == null || entries.Count == 0) return [];

        entriesOfLast = Math.Min(entriesOfLast, entries.Count);

        Log.Debug(
            "Total entries in {LogName} log: {EntriesCount}, looking for last {EntriesOfLast} entries with ID: {Id}",
            logName,
            entries.Count,
            entriesOfLast,
            eventId
        );

        return eventLog.Entries
            .Cast<EventLogEntry>()
            .Where(entry => entry.InstanceId == eventId)
            .TakeLast(entriesOfLast)
            .ToList();
    }
}