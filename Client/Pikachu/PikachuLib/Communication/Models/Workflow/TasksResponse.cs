using System.Collections.Generic;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.FormGroup;

namespace PikachuLib.Communication.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines Tasks Response
    /// </summary>
    public class TasksResponse
    {
        /// <summary>
        /// Die ID des Tasks.
        /// </summary>
        [JsonProperty("id")]
        public int Id { get; set; }

        /// <summary>
        /// Der Name des Tasks.
        /// </summary>
        [JsonProperty("name")]
        public string Name { get; set; }

        /// <summary>
        /// Die enthaltenen Formularfelder.
        /// </summary>
        [JsonProperty("formGroups")]
        public List<TaskComponentGroupResponse> FormGroups { get; set; }

        [JsonProperty("usergroups")]
        public List<int> Usergroups { get; set; }

        public TasksResponse()
        {
            FormGroups = new List<TaskComponentGroupResponse>();
        }
    }
}
