using System;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.FormGroup;
using PikachuLib.Communication.Models.Request;
using RestSharp;
using Newtonsoft.Json.Converters;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Request-Klasse, welche den Payload und den Endpunk einer CreateIntegerComponent Anfrage enthält
    /// </summary>
    public class CreateIntegerComponentRequest : CreateComponentRequest
    {
        [JsonProperty("defaultValue")]
        public int DefaultValue { get; set; }

        [JsonProperty("minValue")]
        public int MinValue { get; set; }

        [JsonProperty("maxValue")]
        public int MaxValue { get; set; }

        public CreateIntegerComponentRequest(int formGroupId, string name, string comment, bool required, int defaultValue, int minValue, int maxValue)
            :base(formGroupId, name, comment, required)
        {
            DefaultValue = defaultValue;
            MinValue = minValue;
            MaxValue = maxValue;
            ComponentType = ComponentType.TaskComponentInteger;
        }

    }
}
