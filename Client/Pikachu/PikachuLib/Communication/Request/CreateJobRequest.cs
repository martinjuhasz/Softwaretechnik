using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;
using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Request
{
    /// <summary>
    /// Repräsentation eines Create Job Request
    /// </summary>
    public class CreateJobRequest : RequestBase
    {
        public CreateJobRequest(int workflowId)
        {
            RestUrl = String.Format("workflows/{0}/jobs", workflowId);
            Method = Method.PUT;
        }
    }
}
