using System;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Job
{
    /// <summary>
    /// Klasse die eine Jobabfrage Repräsentiert.
    /// </summary>
    public class GetJobRequest : RequestBase
    {
        public GetJobRequest(int id)
        {
            RestUrl = String.Format("jobs/{0}", id);
            Method = Method.GET;
        }

        public GetJobRequest(int jobId, int taskId)
        {
            RestUrl = String.Format("jobs/{0}?filter_by_task={1}", jobId, taskId);
            Method = Method.GET;
        }
    }
}
