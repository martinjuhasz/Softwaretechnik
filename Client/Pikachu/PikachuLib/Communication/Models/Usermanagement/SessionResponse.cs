using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Usermanagement
{
    /// <summary>
    /// Repräsentation eine Create Session Response
    /// </summary>
    public class SessionResponse
    {
        [JsonProperty("token")]
        public string Token { get; set; }

        [JsonProperty("admin", DefaultValueHandling = DefaultValueHandling.Populate)]
        public bool IsAdmin { get; set; }

        [JsonProperty("id")]
        public int Id { get; set; }

        public SessionResponse()
        {
            IsAdmin = false;
        }
    }
}
