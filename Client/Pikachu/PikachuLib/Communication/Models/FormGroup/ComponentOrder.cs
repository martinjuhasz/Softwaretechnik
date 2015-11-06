using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.FormGroup
{
    /// <summary>
    /// Repräsentation einer Position eines Components für die REST-Schnittstelle
    /// </summary>
    public class ComponentOrder
    {
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("order")]
        public int Order { get; set; }

        public ComponentOrder(int id, int order)
        {
            this.Id = id;
            this.Order = order;
        }
    }
}