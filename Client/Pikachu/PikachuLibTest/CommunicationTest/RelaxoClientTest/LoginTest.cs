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

namespace PikachuLibTest.CommunicationTest.RelaxoClientTest
{
    /// <summary>
    /// TestKlasse zur Überprüfung der LogIn Funktionalität
    /// </summary>
    [TestClass]
    public class LoginTest
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
                            ""token"": ""87tghjkmnbvcxsw45678iokjnbvcdewsdfghuiok""
                        }
                    }"
                });
            client = new RelaxoClient(restClientMock.Object);
        }

        /// <summary>
        /// Testmethode zum Testen des erflogreichen User-Logins.
        /// </summary>
        [TestMethod]
        public void LogInSuccess()
        {
            var username = "testuser";
            var password = "testpw";
            var user = client.LogIn(username, password);
            Assert.IsTrue(user.Token != null);
        }
    }
}
