using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Usermanagement
{
    public class UsergroupResponse
    {
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }
    }
}
