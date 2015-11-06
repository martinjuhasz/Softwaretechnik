using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Usermanagement
{
    /// <summary>
    /// Repräsentation eines Create User Response
    /// </summary>
    public class UpdateUserResponse
    {
        [JsonProperty("id")]
        public int Id { get; set; }
    }
}
