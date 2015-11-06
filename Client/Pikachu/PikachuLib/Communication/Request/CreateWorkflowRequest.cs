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
    /// Repräsentation eines Create Workflow Requests
    /// </summary>
    public class CreateWorkflowRequest : RequestBase
    {
        public CreateWorkflowRequest() 
        {
            RestUrl = "workflows";
            Method = Method.PUT;
        }
    }
}
