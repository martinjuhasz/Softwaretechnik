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
using System.Threading.Tasks;

namespace PikachuLibTest.CommunicationTest.RelaxoClientTest
{
    [TestClass]
    public class GetTasksTest
    {

        private RelaxoClient client;
        private Mock<IRestClient> restClientMock;
        private IRestClient restClient;

        private RestResponse TestResponse(IRestRequest request)
        {
            if (request.Resource.Equals("workflows/12345/tasks"))
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
          ""name"": ""Task 1""
        },
        {
          ""id"": 12346,
          ""name"": ""Task 2""
        } 
      ]
    }
"
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
        public void TestIfCanGetRightTaskList()
        {
            var response = client.GetTasks(12345);

            Assert.IsNotNull(response);
            Assert.AreEqual(response.Count, 2);

            Assert.AreEqual(response[0].Id, 12345);
            Assert.AreEqual(response[0].Name, "Task 1");

            Assert.AreEqual(response[1].Id, 12346);
            Assert.AreEqual(response[1].Name, "Task 2");
        }

        [TestMethod]
        [ExpectedException(typeof(RESTException))]
        public void TestIfCanGetInvalidTaskList()
        {
            var response = client.GetTasks(1111);
        }
    }
}
