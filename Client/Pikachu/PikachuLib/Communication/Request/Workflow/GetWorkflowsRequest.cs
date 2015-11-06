using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Repräsentation eines Workflows Requests
    /// </summary>
    public class GetWorkflowsRequest : RequestBase
    {
        public GetWorkflowsRequest()
        {
            this.RestUrl = "workflows";
            this.Method = Method.GET;
        }
    }
}
