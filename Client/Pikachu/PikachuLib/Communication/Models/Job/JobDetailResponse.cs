using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace PikachuLib.Communication.Models.Job
{
    /// <summary>
    /// Repräsentation einer Response der Detail-Ansicht des Job.
    /// </summary>
    public class JobDetailResponse
    {
        [JsonProperty("id")]
        public int JobId { get; set; }

        [JsonProperty("creator")]
        public string Creator { get; set; }

        [JsonProperty("startTime", NullValueHandling=NullValueHandling.Ignore)]
        [JsonConverter(typeof(IsoDateTimeConverter))]
        public DateTime StartTime { get; set; }

        [JsonProperty("endTime", NullValueHandling = NullValueHandling.Ignore)]
        [JsonConverter(typeof(IsoDateTimeConverter))]
        public DateTime EndTime { get; set; }

        [JsonProperty("active")]
        public bool Active { get; set; }

        [JsonProperty("jobTasks")]
        public List<JobTaskResponse> JobTasks { get; set; }

        public JobDetailResponse()
        {
            JobTasks = new List<JobTaskResponse>();
            Active = true;
        }
    }
}
