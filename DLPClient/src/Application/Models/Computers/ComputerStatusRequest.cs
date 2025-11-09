using System.Text.Json.Serialization;

namespace DLPClient.Application.Models.Computers;

public record ComputerStatusRequest(
    [property: JsonPropertyName("name")] string Name,
    [property: JsonPropertyName("domain")] string Domain,
    [property: JsonPropertyName("ip")] string IpAddress,
    [property: JsonPropertyName("lastStartup")]
    object LastStartup
);