using System.Net.Http.Json;
using System.Text.Json;
using DLPClient.Application.Models.Computers;
using DLPClient.Application.Models.Users;
using DLPClient.Application.Services.Https.Basic;
using Serilog;
using static System.Net.Http.HttpMethod;

namespace DLPClient.Application.Services.Https;

public class HttpApiClient(
    HttpClient httpClient,
    JsonSerializerOptions jsonSerializerOptions
)
    : IHttpApiClient
{
    /*public void SendRequest<T>(params T[] requests)
    {
        foreach (var request in requests)
            switch (request)
            {
                case StatusComputerRequest statusComputerRequest:
                    SendStatusComputerRequest(statusComputerRequest);
                    break;
                case StatusUserRequest statusUserRequest:
                    SendStatusUserRequest(statusUserRequest);
                    break;
            }
    }*/

    public void SendComputerStatusRequest(ComputerStatusRequest request)
    {
        SendMethodRequest(Put, "/computer/status", request);
    }

    public void SendUserStatusRequest(UserStatusRequest request)
    {
        SendMethodRequest(Put, "/user/status", request);
    }
    
    public void SendUserAuthRequest(UserSessionRequest request)
    {
        SendMethodRequest(Post, "/user/auth", request);
    }

    private async void SendMethodRequest(HttpMethod httpMethod, string endpoint, object request)
    {
        try
        {
            var url = string.Empty;

            try
            {
                url = $"{httpClient.BaseAddress?.ToString()}{endpoint}";

                Log.Debug(
                    "[{HttpMethod}] {Url} => {Request}",
                    httpMethod,
                    url,
                    request
                );

                var result = httpMethod switch
                {
                    _ when httpMethod == Put => await httpClient.PutAsJsonAsync(url, request, jsonSerializerOptions),
                    _ when httpMethod == Post => await httpClient.PostAsJsonAsync(url, request, jsonSerializerOptions),
                    _ when httpMethod == Patch =>
                        await httpClient.PatchAsJsonAsync(url, request, jsonSerializerOptions),
                    _ when httpMethod == Get => await httpClient.GetAsync(url),
                    _ => throw new NotImplementedException()
                };

                if (!result.IsSuccessStatusCode)
                {
                    Log.Error(
                        "Status send failure {Url} => {ResultReasonPhrase}",
                        url,
                        result.ReasonPhrase
                    );
                    return;
                }

                Log.Debug(
                    "Status send success {Url} => {Result}",
                    url,
                    result.Content.ReadAsStringAsync().Result
                );
            }
            catch (TaskCanceledException ex)
            {
                if (ex.CancellationToken.IsCancellationRequested)
                    Log.Error("Timeout {url}", url);
            }
            catch (Exception ex)
            {
                Log.Error(ex, "Status send failure for {Url}", url);
            }
        }
        catch (Exception ex)
        {
            Log.Error(ex, "Error sending status");
        }
    }
}