using System.Text.Json.Serialization;
using DLPClient.User.Handlers;

namespace DLPClient.Application.Models.Users;

public record UserSessionRequest(
    [property: JsonPropertyName("login")] string Login,
    [property: JsonPropertyName("domain")] string Domain,
    [property: JsonPropertyName("authType")] SessionHandler.Type AuthType
);