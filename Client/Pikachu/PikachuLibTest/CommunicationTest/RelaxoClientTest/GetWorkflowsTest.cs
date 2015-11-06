using System;
using System.Text;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using PikachuLib.Communication;
using PikachuLib.Models;
using System.Diagnostics;
using Moq;
using RestSharp;
using System.Net;
using PikachuLib.Models.Workflow;

namespace PikachuLibTest.CommunicationTest.RelaxoClientTest
{
    /// <summary>
    /// Testklasse zur Überprüfung der Funktionalität der GetWorkflows Methode
    /// </summary>
    [TestClass]
    public class GetWorkflowsTest
    {

        private RelaxoClient client;
        private Mock<IRestClient> restClientMock;
        private IRestClient restClient;

        private RestResponse TestResponse(IRestRequest request)
        {
            if (request.Resource.Equals("workflows"))
            {
                return new RestResponse
                {
                    StatusCode = HttpStatusCode.OK,
                    ContentType = "application/json",
                    Content = @"
                    {
                        ""data"": [
                            {
                              ""id"": 12345,
                              ""name"": ""Paketshop""
                            },
                            {
                              ""id"": 12346,
                              ""name"": ""Flugabfertigung""
                            } 
                          ]
                    }"

                };
            }
            else
            {
                return new RestResponse
                {
                    StatusCode = HttpStatusCode.NotFound,
                    ContentType = "application/json",
                    Content = @"
{
  ""error"": {
    ""code"": ""not_found"",
    ""message"":""Task ID not found.""
  }
}"
                };
            }

        }


        [TestInitialize]
        public void init()
        {

            restClientMock = new Mock<IRestClient>();
            restClientMock.Setup(x => x.Execute(It.IsAny<IRestRequest>()))
                .Returns((IRestRequest x) => { return TestResponse(x); });

            //restClientMock.Object.BaseUrl.ToString().Equals("/tasks/12345");
            //restClientMock.When((x) => x != 0);
            restClient = restClientMock.Object;
            client = new RelaxoClient(restClient);
        }
        

        [TestMethod]
        public void GetWorkflowsSuccess()
        {
            var workflowsList = new List<Workflow>();
            workflowsList.Add(new Workflow(12345, "Paketshop", true));
            workflowsList.Add(new Workflow(12346, "Flugabfertigung", true));
            
            var retrievedWorkflows = client.GetWorkflows();

            for (int i = 0; i < workflowsList.Count; i++)
            {
                Assert.AreEqual(workflowsList[i].Id, retrievedWorkflows[i].Id);
                Assert.AreEqual(workflowsList[i].Name, retrievedWorkflows[i].Name);
            }
        }
    }
}
