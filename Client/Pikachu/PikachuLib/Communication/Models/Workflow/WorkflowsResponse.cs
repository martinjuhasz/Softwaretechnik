using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines Workflows Response
    /// </summary>
    public class WorkflowsResponse
    {
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("userCanCreateJob")]
        public bool UserCanCreateJob { get; set; }
    }
}
