using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;

namespace PikachuLib.Communication.Models.Request
{
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
