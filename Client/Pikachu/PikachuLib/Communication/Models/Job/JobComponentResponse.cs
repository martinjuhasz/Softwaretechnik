using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using PikachuLib.Communication.CustomJsonConverter;
using PikachuLib.Communication.Models.FormGroup;

namespace PikachuLib.Communication.Models.Job
{
    /// <summary>
    /// Repräsentation der Basisklasse eines JobComponents.
    /// </summary>
    public abstract class JobComponentResponse
    {
        [JsonProperty("taskComponentId")]
        public int Id { get; set; }

        [JsonProperty("type", NullValueHandling = NullValueHandling.Ignore)]
        [JsonConverter(typeof(StringEnumConverter))]
        public ComponentType? Type { get; set; }
    }

    /// <summary>
    /// Repräsentation eines JobComponents für (einzeiligen) Text.
    /// </summary>
    public class JobComponentResponseText : JobComponentResponse
    {
        [JsonProperty("value")]
        public string Value { get; set; }
    }

    /// <summary>
    /// Repräsentation eines JobComponents für eine Ganzzahl.
    /// </summary>
    public class JobComponentResponseInteger : JobComponentResponse
    {
        [JsonProperty("value")]
        public int Value { get; set; }
    }

    /// <summary>
    /// Repräsentation eines JobComponents für eine Fließkommazahl.
    /// </summary>
    public class JobComponentResponseFloat : JobComponentResponse
    {
        [JsonProperty("value")]
        public float Value { get; set; }
    }

    /// <summary>
    /// Repräsentation eines JobComponents für eine Datum-Zeit Angabe.
    /// </summary>
    public class JobComponentResponseDate : JobComponentResponse
    {
        [JsonProperty("value")]
        [JsonConverter(typeof(IsoDateTimeConverterAdapter))]
        public DateTime Value { get; set; }
    }
}
