using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Task
{
    /// <summary>
    /// Repräsentation einer Anfrage für einen Task.
    /// </summary>
    public class GetTaskDetailRequest : RequestBase
    {
        public GetTaskDetailRequest(int taskId)
        {
            RestUrl = string.Format("tasks/{0}", taskId);
            Method = Method.GET;
        }
    }
}
