using System;
using System.Text;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using PikachuLib.Communication;
using PikachuLib.Communication.Models;
using PikachuLib.Communication.Models.Workflow;
using PikachuLib.Models;
using System.Diagnostics;
using Moq;
using RestSharp;
using System.Net;
using PikachuLib.Models.Workflow;

namespace PikachuLibTest.CommunicationTest.RelaxoClientTest
{
    /// <summary>
    /// TestKlasse zur Überprüfung der Create Job Funktionalitätt
    /// </summary>
    [TestClass]
    public class CreateJobTest
    {

        private RelaxoClient client;
        private Mock<IRestClient> restClientMock;

        [TestInitialize]
        public void init()
        {
            restClientMock = new Mock<IRestClient>();
            restClientMock.Setup(x => x.Execute(It.IsAny<IRestRequest>()))
                .Returns(new RestResponse
                {
                    ContentType = "application/json",
                    StatusCode = HttpStatusCode.Created,
                    Content = @"
                    {
                        ""data"": {
                            ""jobId"": 2,
                            ""taskId"": null
                        }
                    }"
                });
            client = new RelaxoClient(restClientMock.Object);
        }

        /// <summary>
        /// Testen des erfolgreichen Erstellens ohne Berechtigung den ersten Task zu bearbeiten.
        /// </summary>
        [TestMethod]
        public void CreateJobWithoutFirstTask()
        {
            var createJobResponse = new CreateJobResponse();
            createJobResponse.JobId = 2;
            createJobResponse.TaskId = null;

            var workflow = new Workflow(5);
            var retrievedCreateJobResponse = client.CreateJob(workflow);

            Assert.AreEqual(createJobResponse.JobId, retrievedCreateJobResponse.JobId);
            Assert.AreEqual(createJobResponse.TaskId, retrievedCreateJobResponse.TaskId);
        }

        /// <summary>
        /// Testen des erfolgreichen Erstellens ohne Berechtigung den ersten Task zu bearbeiten.
        /// </summary>
        [TestMethod]
        public void CreateJobWithFirstTask()
        {
            restClientMock = new Mock<IRestClient>();
            restClientMock.Setup(x => x.Execute(It.IsAny<IRestRequest>()))
                .Returns(new RestResponse
                {
                    ContentType = "application/json",
                    StatusCode = HttpStatusCode.Created,
                    Content = @"
                    {
                        ""data"": {
                            ""jobId"": 2,
                            ""taskId"": 5
                        }
                    }"
                });
            client = new RelaxoClient(restClientMock.Object);



            var createJobResponse = new CreateJobResponse();
            createJobResponse.JobId = 2;
            createJobResponse.TaskId = 5;

            var workflow = new Workflow(5);
            var retrievedCreateJobResponse = client.CreateJob(workflow);

            Assert.AreEqual(createJobResponse.JobId, retrievedCreateJobResponse.JobId);
            Assert.AreEqual(createJobResponse.TaskId, retrievedCreateJobResponse.TaskId);
        }
    }
}
