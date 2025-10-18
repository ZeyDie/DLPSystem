using System.Text.Json.Serialization;

namespace DLPClient.Application.Models.Users;

public record StatusUserRequest(
    [property: JsonPropertyName("login")] string Login,
    [property: JsonPropertyName("lastLogin")]
    object? LastLogin
);