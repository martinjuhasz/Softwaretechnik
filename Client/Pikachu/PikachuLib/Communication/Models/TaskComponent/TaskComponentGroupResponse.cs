using System.Collections.Generic;
using Newtonsoft.Json;
using PikachuLib.Communication.CustomJsonConverter;

namespace PikachuLib.Communication.Models.TaskComponent
{
    public class TaskComponentGroupResponse
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
        [JsonProperty("components", ItemConverterType = typeof(TaskComponentJsonConverter))]
        public List<TaskComponentResponse> Components { get; set; }

        public TaskComponentGroupResponse()
        {
            Components = new List<TaskComponentResponse>();
        }

    }
}
