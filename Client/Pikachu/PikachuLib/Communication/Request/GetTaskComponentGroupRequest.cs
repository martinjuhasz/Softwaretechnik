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
    public class GetTaskComponentGroupRequest : RequestBase
    {
        public GetTaskComponentGroupRequest(int workflowId)
        {
            this.RestUrl = string.Format("workflows/{0}/formgroups", workflowId);
            this.Method = Method.GET;
        }
    }
}
