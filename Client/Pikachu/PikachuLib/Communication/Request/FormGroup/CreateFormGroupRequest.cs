using System;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Request-Klasse, welche den Payload und den Endpunk einer CreateFormGroup Anfrage enthält
    /// </summary>
    public class CreateFormGroupRequest : RequestBase
    {
        [JsonProperty("name")]
        public string Name { get; set; }

        public CreateFormGroupRequest(int workflowId, string name)
        {
            Method = Method.PUT;
            RestUrl = String.Format("workflows/{0}/formgroups", workflowId);
            Name = name;
        }
    }
}
