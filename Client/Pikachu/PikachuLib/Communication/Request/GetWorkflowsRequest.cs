using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;

namespace PikachuLib.Communication.Models.Request
{
    /// <summary>
    /// Repräsentation eines Workflows Requests
    /// </summary>
    public class GetWorkflowsRequest : RequestBase
    {
        public GetWorkflowsRequest()
        {
            this.RestUrl = "workflows";
            this.Method = Method.GET;
        }
    }
}
