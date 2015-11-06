using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace PikachuLib.Communication.Models.Job
{
    /// <summary> 
    /// Repräsentation einer Job Response 
    /// </summary> 
    public class JobsResponse
    {
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("startTime")]
        [JsonConverter(typeof(IsoDateTimeConverter))]
        public DateTime StartTime { get; set; }

        [JsonProperty("active")]
        public bool Active { get; set; }

        [JsonProperty("locked")]
        public bool Locked { get; set; }

        public JobsResponse()
        {
            Active = true;
        }
    }
}
