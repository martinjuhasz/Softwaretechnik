using System;
using Newtonsoft.Json;
using PikachuLib.Communication.CustomJsonConverter;
using PikachuLib.Communication.Models.FormGroup;
using PikachuLib.Communication.Models.Request;
using RestSharp;
using Newtonsoft.Json.Converters;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Request-Klasse, welche den Payload und den Endpunk einer CreateDateComponent Anfrage enthält
    /// </summary>
    public class CreateDateComponentRequest : CreateComponentRequest
    {
        [JsonProperty("defaultValue")]
        [JsonConverter(typeof(IsoDateTimeConverterAdapter))]
        public DateTime DefaultValue { get; set; }

        public CreateDateComponentRequest(int formGroupId, string name, string comment, bool required, DateTime defaultValue)
            :base(formGroupId, name, comment, required)
        {
            DefaultValue = defaultValue;
            ComponentType = ComponentType.TaskComponentDate;
        }

    }
}
