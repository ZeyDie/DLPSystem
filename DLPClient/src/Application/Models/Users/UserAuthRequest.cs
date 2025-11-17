using System.Text.Json.Serialization;
using DLPClient.User.Handlers;

namespace DLPClient.Application.Models.Users;

public record UserAuthRequest(
    [property: JsonPropertyName("login")] string Login,
    [property: JsonPropertyName("authType")] AuthHandler.Type AuthType
);