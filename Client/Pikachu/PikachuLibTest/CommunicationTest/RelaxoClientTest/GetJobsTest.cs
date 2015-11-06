using System;
using System.Text;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using PikachuLib.Communication;
using PikachuLib.Models;
using System.Diagnostics;
using Moq;
using PikachuLib.Models.Job;
using RestSharp;
using System.Net;
using PikachuLib.Models.Workflow;
using System.Globalization;

namespace PikachuLibTest.CommunicationTest.RelaxoClientTest
{
    /// <summary>
    /// Testklasse zur Überprüfung der Funktionalität der GetJobs Methode
    /// </summary>
    [TestClass]
    public class GetJobsTest
    {

        private RelaxoClient client;
        private Mock<IRestClient> restClientMock;
        private IRestClient restClient;

        private RestResponse TestResponse(IRestRequest request)
        {
            if (request.Resource.Equals("tasks/12345/jobs"))
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
                              ""creator"": ""ernstaugust"",
                              ""startTime"": ""2014-11-14T23:11:14Z"",
                              ""endTime"": ""2014-11-15T11:00:14Z"",
                              ""active"": false
                            },
                            {
                              ""id"": 12346,
                              ""creator"": ""peter"",
                              ""startTime"": ""2014-10-14T23:11:14Z"",
                              ""endTime"": ""2014-11-15T11:00:14Z"",
                              ""active"": true
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
        public void GetJobsSuccess()
        {
            var jobsList = new List<Job>();
            jobsList.Add(new Job(12345, DateTime.Parse("2014-11-14T23:11:14Z", null, DateTimeStyles.RoundtripKind), false));
            jobsList.Add(new Job(12346, DateTime.Parse("2014-10-14T23:11:14Z", null, DateTimeStyles.RoundtripKind), true));
            //jobsList.Add(new Job(12346, DateTime.ParseExact("2014-10-14T23:11:14Z", "yyyy-MM-ddTHH:mm:ssZ", System.Globalization.CultureInfo.CurrentCulture), false));

            var retrievedJobs = client.GetJobs(new Task(12345, "Test Task 1"));

            for (int i = 0; i < jobsList.Count; i++)
            {
                Assert.AreEqual(jobsList[i].Id, retrievedJobs[i].Id);
                Assert.AreEqual(jobsList[i].StartTime, retrievedJobs[i].StartTime);
                Assert.AreEqual(jobsList[i].Active, retrievedJobs[i].Active);
            }
        }

        [TestMethod]
        [ExpectedException(typeof(RESTException))]
        public void GetJobsFail()
        {
            var jobs = client.GetJobs(new Task(53251, "Test Task 1"));
        }
    }
}
