using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using PikachuLib.Communication;
using PikachuLib.Models.TaskComponent;
using RestSharp;
using System.Net;

namespace PikachuLibTest.CommunicationTest.RelaxoClientTest
{
    /// <summary>
    /// Testklasse für getTask.
    /// Es werden die eingehende Daten der apiary-Schnittstelle geprüft.
    /// </summary>
    [TestClass]
    public class GetTaskTest
    {
        private RelaxoClient client;
        private Mock<IRestClient> restClientMock;
        private IRestClient restClient;

        private RestResponse TestResponse(IRestRequest request) 
        {
            if (request.Resource.Equals("tasks/12345"))
            {
                return new RestResponse
                {
                    StatusCode = HttpStatusCode.OK,
                    ContentType = "application/json",
                    Content = @"{
    ""data"": {
        ""id"": 12345,
        ""name"": ""Task 1"",
        ""formGroups"": [
            {
                ""name"": ""Allgemein"",
                ""id"": 1,
                ""components"": [
                    {
                         ""name"": ""Titel"",
                        ""comment"": ""Titel des Blogposts"",
                        ""required"": true,
                        ""order"": 1,
                        ""type"": ""TaskComponentText"",
                        ""default"": null,
                        ""regex"": null
                    }
                ]
            }
        ],
        ""userGroups"": [
            1,
            8,
            3,
            6
        ]
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
    ""message"":""Task ID not found.""
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

            //restClientMock.Object.BaseUrl.ToString().Equals("/tasks/12345");
            //restClientMock.When((x) => x != 0);
            restClient = restClientMock.Object;
            client = new RelaxoClient(restClient);
            
        }

        [TestMethod]
        public void Test_If_Right_Task_Received()
        {
            var task = client.GetTask(12345);
            Assert.IsNotNull(task);

            // Task-Daten Testen
            Assert.AreEqual(task.Name, "Task 1");
            Assert.AreEqual(task.Id, 12345);
            Assert.AreEqual(task.FormGroups.Count, 1);
            Assert.AreEqual(task.FormGroups[0].Components.Count, 1);

            // Felder-Testen
            //Feld 0:
            var textComponent = (TaskComponentText)task.FormGroups[0].Components[0];
            Assert.AreEqual(textComponent.Name, "Titel");
            Assert.AreEqual(textComponent.Comment, "Titel des Blogposts");
            Assert.AreEqual(textComponent.Required, true);
            Assert.AreEqual(textComponent.Value, null);
            Assert.AreEqual(textComponent.RegexExpression, null);
        }


        [TestMethod]
        [ExpectedException(typeof(RESTException))]
        public void Test_If_Not_Valid_Task_Received()
        {            
            var task = client.GetTask(1234);
        }
    }
}
