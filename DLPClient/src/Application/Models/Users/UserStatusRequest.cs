using System.Text.Json.Serialization;

namespace DLPClient.Application.Models.Users;

public record UserStatusRequest(
    [property: JsonPropertyName("login")] string Login,
    [property: JsonPropertyName("lastLogin")]
    object? LastLogin
);