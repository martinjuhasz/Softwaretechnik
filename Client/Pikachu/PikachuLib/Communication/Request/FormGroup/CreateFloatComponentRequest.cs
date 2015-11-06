using System;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.FormGroup;
using PikachuLib.Communication.Models.Request;
using RestSharp;
using Newtonsoft.Json.Converters;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Request-Klasse, welche den Payload und den Endpunk einer CreateFloatComponent Anfrage enthält
    /// </summary>
    public class CreateFloatComponentRequest : CreateComponentRequest
    {
        [JsonProperty("defaultValue")]
        public float DefaultValue { get; set; }

        [JsonProperty("minValue")]
        public float MinValue { get; set; }

        [JsonProperty("maxValue")]
        public float MaxValue { get; set; }

        [JsonProperty("scale")]
        public int Scale { get; set; }

        public CreateFloatComponentRequest(int formGroupId, string name, string comment, bool required, float defaultValue, float minValue, float maxValue, int scale)
            :base(formGroupId, name, comment, required)
        {
            DefaultValue = defaultValue;
            MinValue = minValue;
            MaxValue = maxValue;
            Scale = scale;
            ComponentType = ComponentType.TaskComponentFloat;
        }

    }
}
