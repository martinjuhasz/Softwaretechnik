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
using PikachuLib.Models.Usermanagement;

namespace PikachuLibTest.CommunicationTest.UserManagementTest
{
    /// <summary>
    /// Testklasse zur Überprüfung der Funktionalität der GetAllUser Methode
    /// </summary>
    [TestClass]
    public class GetAllUserTest
    {
        private RelaxoClient client;
        private Mock<IRestClient> restClientMock;
        private IRestClient restClient;

        #region TestResponse
        private RestResponse TestResponse(IRestRequest request)
        {
            if (request.Resource.Equals("user"))
            {
                return new RestResponse
                {
                    StatusCode = HttpStatusCode.OK,
                    ContentType = "application/json",
                    Content = @"
                        {
                            ""data"":
                                [{
                                    ""username"": ""mmoel001"",
                                    ""name"": ""Moeller"",
                                    ""prename"": ""Moritz"",
                                    ""id"": 1,
                                    ""active"": true,
                                    ""admin"": true
                                },
                                {
                                    ""username"": ""hvogt001"",
                                    ""name"": ""Vogt"",
                                    ""prename"": ""Henry"",
                                    ""id"": 2,
                                    ""active"": true,
                                    ""admin"": true
                                },
                                {
                                    ""username"": ""mweil002"",
                                    ""name"": ""Weilbächer"",
                                    ""prename"": ""Michael"",
                                    ""id"": 3,
                                    ""active"": true,
                                    ""admin"": false
                                }]
                        }"
                };
            }
            else
            {
                return null;
            }
        }
        #endregion

        #region TestInit
        [TestInitialize]
        public void init()
        {
            restClientMock = new Mock<IRestClient>();
            restClientMock.Setup(x => x.Execute(It.IsAny<IRestRequest>()))
                .Returns((IRestRequest x) => { return TestResponse(x); });
            restClient = restClientMock.Object;
            client = new RelaxoClient(restClient);
        }
        #endregion

        #region TestGetAllUser
        [TestMethod]
        public void Test_GetAllUser()
        {
            var usersList = new List<User>();
            usersList.Add(new User(1, "mmoel001", "Moritz", "Moeller", true, true));
            usersList.Add(new User(2, "hvogt001", "Henry", "Vogt", true, true));
            usersList.Add(new User(3, "mweil002", "Michael", "Weilbächer", false, true));

            var retrievedUsers = client.GetAllUsers();

            for (int i = 0; i < usersList.Count; i++)
            {
                Assert.AreEqual(usersList[i].Id, retrievedUsers[i].Id);
                Assert.AreEqual(usersList[i].Username, retrievedUsers[i].Username);
                Assert.AreEqual(usersList[i].Prename, retrievedUsers[i].Prename);
                Assert.AreEqual(usersList[i].Name, retrievedUsers[i].Name);
                Assert.AreEqual(usersList[i].IsAdmin, retrievedUsers[i].IsAdmin);
                Assert.AreEqual(usersList[i].IsActive, retrievedUsers[i].IsActive);
            }
        }
        #endregion
    }
}
