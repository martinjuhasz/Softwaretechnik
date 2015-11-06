using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.WorkflowDecision
{
    /// <summary>
    /// Request Klasse um ein Decision Workflowitem zu holen.
    /// </summary>
    public class GetDecisionRequest : RequestBase
    {
        public GetDecisionRequest(int id)
        {
            Method = Method.GET;
            RestUrl = string.Format("workflowdecision/{0}", id);
        }
    }
}