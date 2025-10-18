using DLPClient.Application.Models.Computers;
using DLPClient.Application.Models.Users;

namespace DLPClient.Application.Services.Https.Basic;

public interface IHttpApiClient
{
    void SendRequest<T>(params T[] requests);

    void SendStatusComputerRequest(StatusComputerRequest request);

    void SendStatusUserRequest(StatusUserRequest request);
}