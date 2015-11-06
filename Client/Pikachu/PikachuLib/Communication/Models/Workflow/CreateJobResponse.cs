using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Workflow
{
    /// <summary> 
    /// Repräsentation eines Create Job Response 
    /// </summary> 
    public class CreateJobResponse
    {
        [JsonProperty("jobId")]
        public int? JobId { get; set; }

        [JsonProperty("taskid")]
        public int? TaskId { get; set; }
    }
}