using System.Diagnostics;
using System.Runtime.Versioning;
using DLPClient.Application.Registries;
using Serilog;

namespace DLPClient.Application.Services.EventLogs;

[SupportedOSPlatform("windows")]
public static class EventLogApiClient
{
    public static async Task<List<EventLogEntry>> GetLogonLogs(int entriesOfLast)
    {
        return await GetLogs(
            entriesOfLast,
            ConstRegistry.SystemConstantIds.LogName,
            ConstRegistry.SystemConstantIds.WinlogonId
        );
    }

    public static async Task<List<EventLogEntry>> GetLogoffLogs(int entriesOfLast)
    {
        return await GetLogs(
            entriesOfLast,
            ConstRegistry.SystemConstantIds.LogName,
            ConstRegistry.SystemConstantIds.WinlogoffId
        );
    }

    private static async Task<List<EventLogEntry>> GetLogs(int entriesOfLast, string logName, int eventId)
    {
        return await Task.Run(() =>
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
        );
    }
}