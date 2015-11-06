using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;

namespace PikachuLib.Communication.Models.Request
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
