using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Request-Klasse, welche den Endpunkt einer GetFormGroups Anfrage enthält
    /// </summary>
    public class GetFormGroupsRequest : RequestBase
    {
        public GetFormGroupsRequest(int workflowId)
        {
            this.RestUrl = string.Format("workflows/{0}/formgroups", workflowId);
            this.Method = Method.GET;
        }
    }
}
