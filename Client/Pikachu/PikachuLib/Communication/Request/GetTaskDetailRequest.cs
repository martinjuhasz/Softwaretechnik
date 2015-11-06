using RestSharp;

namespace PikachuLib.Communication.Models.Request
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
