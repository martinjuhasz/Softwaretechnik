using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using PikachuLib.Communication;
using PikachuLib.Models.Workflow;
using RestSharp;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;

namespace PikachuLibTest.CommunicationTest.RelaxoClientTest
{
    [TestClass]
    public class UpdateJobTest
    {
        private RelaxoClient client;
        private Mock<IRestClient> restClientMock;
        private IRestClient restClient;

        private RestResponse TestResponse(IRestRequest request)
        {
            if (request.Resource.Equals("jobs/12345"))
            {
                return new RestResponse
                {
                    StatusCode = HttpStatusCode.OK,
                    Content = ""
                };
            }
            else if (request.Resource.Equals("jobs/66666")) 
            {
                return new RestResponse
                {
                    StatusCode = HttpStatusCode.BadRequest,
                    ContentType = "application/json",
                    Content = @"
{
  ""error"": {
    ""code"": ""invalid_arguments"",
    ""message"":""Das Feld 'Bezeichnung' gibt es nicht.""
  }
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
    ""message"":""Workflow ID not found.""
  }
}"
                };
            }

        }

        [TestInitialize]
        public void Init()
        {
            restClientMock = new Mock<IRestClient>();
            restClientMock.Setup(x => x.Execute(It.IsAny<IRestRequest>()))
                .Returns((IRestRequest x) => { return TestResponse(x); });

            restClient = restClientMock.Object;
            client = new RelaxoClient(restClient);
        }

        [TestMethod]
        public void TestIfCanUpdateJob()
        {
            Task task = new Task(12345, "Task 1");
            client.UpdateJob(task, 12345);
        }

        [TestMethod]
        [ExpectedException(typeof(RESTException))]
        public void TestIfUpdateJobWithWrongId()
        {
            Task task = new Task(12345, "Task 1");
            client.UpdateJob(task, 1111);
        }

        [TestMethod]
        [ExpectedException(typeof(RESTException))]
        public void TestIfUpdateJobWithWrongFormularField()
        {
            Task task = new Task(12345, "Task 1");
            client.UpdateJob(task, 66666);
        }
    }
}
