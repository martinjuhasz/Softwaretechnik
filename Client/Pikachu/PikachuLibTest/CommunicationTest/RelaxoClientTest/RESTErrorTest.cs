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
    /// TestKlasse zur Überprüfung des korrekten Exceptionhandlings
    /// </summary>
    [TestClass]
    public class RESTErrorTest
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
                    StatusCode = HttpStatusCode.Unauthorized,
                    Content = @"
                    {
                        ""error"": {
                            ""code"": ""login_invalid"",
                            ""message"":""Username or Password wrong.""
                        }
                    }
                    "
                });
            client = new RelaxoClient(restClientMock.Object);
        }

        /// <summary>
        /// Testmethode zur Überprüfung, dass die RESTException ausgelöst wird bei einem
        /// 404 oder 400er Status-Code
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(RESTException))]
        public void RESTExceptionTest()
        {
            client.LogIn("test", "123");
        }
    }
}
