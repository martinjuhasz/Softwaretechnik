using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Usermanagement
{
    /// <summary>
    /// Repräsentation eines Create UserGroup Response
    /// </summary>
    public class CreateUserGroupResponse
    {
        [JsonProperty("id")]
        public int Id { get; set; }
    }
}
