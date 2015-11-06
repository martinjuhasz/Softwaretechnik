using System.Collections.Generic;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using PikachuLib.Models.Workflow;

namespace PikachuLib.Communication.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines Get WorkflowItems Requests.
    /// </summary>   
    public class WorkflowitemResponse
    {
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("type")]
        [JsonConverter(typeof(StringEnumConverter))]
        public WorkflowItemType Type { get; set; }

        [JsonProperty("lock")]
        public bool Lock { get; set; }

        [JsonProperty("position")]
        public Position Position { get; set; }

        [JsonProperty("nextItem")]
        public List<int> NextItem { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }
    }
}
