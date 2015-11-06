using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Usermanagement
{
    /// <summary>
    /// Repräsentation eines User Response
    /// </summary>
    public class UserResponse
    {
        #region properties
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("username")]
        public string Username { get; set;}

        [JsonProperty("password")]
        public string Password { get; set;}

        [JsonProperty("prename")]
        public string Prename { get; set;}

        [JsonProperty("name")]
        public string Name { get; set;}

        [JsonProperty("admin")]
        public bool IsAdmin { get; set;}

        [JsonProperty("active")]
        public bool IsActive { get; set;}
        #endregion
    }
}
