using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Usermanagement
{
    /// <summary>
    /// Repräsentation eines Create User Response
    /// </summary>
    public class CreateUserResponse
    {
        [JsonProperty("id")]
        public int Id { get; set; }
    }
}
