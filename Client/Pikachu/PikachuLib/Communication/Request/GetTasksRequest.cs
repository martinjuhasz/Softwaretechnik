using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;

namespace PikachuLib.Communication.Models.Request
{
    /// <summary>
    /// Repräsentation eines Tasks Requests
    /// </summary>
    public class GetTasksRequest : RequestBase
    {
        public GetTasksRequest(int workflowId) 
        { 
            this.RestUrl = string.Format("workflows/{0}/tasks", workflowId);
            this.Method = Method.GET;
        }
    }
}
