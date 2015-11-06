using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using PikachuLib.Communication.CustomJsonConverter;

namespace PikachuLib.Communication.Models.Job
{
    /// <summary>
    /// Repräsentation eines Job Task Response
    /// </summary>
    public class JobTaskResponse
    {
        [JsonProperty("startTime", NullValueHandling = NullValueHandling.Ignore)]
        [JsonConverter(typeof(IsoDateTimeConverter))]
        public DateTime StartTime { get; set; }

        [JsonProperty("endTime", NullValueHandling = NullValueHandling.Ignore)]
        [JsonConverter(typeof(IsoDateTimeConverter))]
        public DateTime EndTime { get; set; }

        [JsonProperty("creatorTime")]
        public string Creator { get; set; }

        [JsonProperty("jobTaskComponentResponses", ItemConverterType = typeof(JobComponentJsonConverter))]
        public List<JobComponentResponse> Components { get; set; }

        public JobTaskResponse()
        {
            Components = new List<JobComponentResponse>();
        }
    }
}
