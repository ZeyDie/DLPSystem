namespace DLPClient.Application.Models.Settings;

public record AppSettings(
    string ApiUrl,
    string ApiVersion,
    int Timeout
);