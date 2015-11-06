using System;
using System.Collections.Generic;
using System.Text;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using PikachuLib.Communication;
using PikachuLib.Communication.Models;
using PikachuLib.Communication.Models.Usermanagement;
using PikachuLib.Models;
using System.Diagnostics;
using Moq;
using RestSharp;
using System.Net;
using PikachuLib.Models.Usermanagement;

namespace PikachuLibTest.CommunicationTest.UserManagementTest
{
    [TestClass]
    public class CreateUserTest
    {
        private RelaxoClient client;
        private Mock<IRestClient> restClientMock;

        #region TestInit
        [TestInitialize]
        public void init()
        {
            restClientMock = new Mock<IRestClient>();
            restClientMock.Setup(x => x.Execute(It.IsAny<IRestRequest>()))
                .Returns(new RestResponse
                {
                    StatusCode = HttpStatusCode.OK,
                    ContentType = "application/json",
                    Content = @"
                        {
                            ""data"":{
                                ""id"": 42
                                }
                        }"
                });
            client = new RelaxoClient(restClientMock.Object);
        }
        #endregion

        #region CreateUserTest
        /// <summary>
        /// Testen des erfolgreichen Erstellens eines Users.
        /// </summary>
        [TestMethod]
        public void Test_CreateUser()
        {
            var createUserResponse = new CreateUserResponse();
            createUserResponse.Id = 42;

            var user = new User();
            user.Username = "mmoel001";
            user.Password = "testpw";
            user.Prename = "Moritz";
            user.Name = "Moeller";
            user.IsAdmin = true;
            user.IsActive = true;
            var retrievedCreateUserResponse = client.CreateUser(user);

            Assert.AreEqual(createUserResponse.Id, retrievedCreateUserResponse.Id);
        }
        #endregion
    }
}