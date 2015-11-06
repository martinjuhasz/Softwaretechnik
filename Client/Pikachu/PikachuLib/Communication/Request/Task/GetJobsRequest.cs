using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Task
{
    /// <summary>
    /// Repräsentation eines Job Requests
    /// </summary>
    public class GetJobsRequest : RequestBase
    {
        public GetJobsRequest(int taskId)
        {
            this.RestUrl = string.Format("tasks/{0}/jobs", taskId);
            this.Method = Method.GET;
        }
    }
}
