using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Task
{
    /// <summary>
    /// Repräsentation eines Unlock Jobs Requests
    /// </summary>
    public class UnlockJobRequest : RequestBase
    {
        public UnlockJobRequest(int taskId, int jobId)
        {
            RestUrl = String.Format("/tasks/{0}/jobs/{1}/lock", taskId, jobId);
            Method = Method.DELETE;
        }
    }
}
