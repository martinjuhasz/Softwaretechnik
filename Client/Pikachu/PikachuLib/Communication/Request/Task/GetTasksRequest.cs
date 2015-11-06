using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Task
{
    /// <summary>
    /// Repräsentation eines Tasks Requests
    /// </summary>
    public class GetTasksRequest : RequestBase
    {
        public GetTasksRequest(int workflowId) 
        { 
            this.RestUrl = string.Format("workflows/{0}/tasks", workflowId);
            this.Method = Method.GET;
        }
    }
}
