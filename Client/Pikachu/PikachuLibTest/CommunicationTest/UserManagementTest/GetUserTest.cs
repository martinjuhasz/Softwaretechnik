using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using PikachuLib.Communication;
using PikachuLib.Models.TaskComponent;
using RestSharp;
using System.Net;

namespace PikachuLibTest.CommunicationTest.UserManagementTest
{
    /// <summary>
    /// Testklasse für getUser.
    /// </summary>
    [TestClass]
    public class GetUserTest
    {
        private RelaxoClient client;
        private Mock<IRestClient> restClientMock;
        private IRestClient restClient;

        #region TestResponse
        private RestResponse TestResponse(IRestRequest request)
        {
            if (request.Resource.Equals("user/1"))
            {
                return new RestResponse
                {
                    StatusCode = HttpStatusCode.OK,
                    ContentType = "application/json",
                    Content = @"
                            {
                                ""data"": 
                                {
                                    ""id"": 1,
                                    ""username"": ""mmoel001"",
                                    ""password"": ""testpw"",
                                    ""prename"": ""Moritz"",
                                    ""name"": ""Moeller"",
                                    ""admin"": true,
                                    ""active"": true
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
                                ""error"": 
                                {
                                    ""code"": ""not_found"",
                                    ""message"":""No user for ID found.""
                                }
                            }"
                };
            }
        }
        #endregion

        #region TestInit
        [TestInitialize]
        public void Init()
        {
            restClientMock = new Mock<IRestClient>();
            restClientMock.Setup(x => x.Execute(It.IsAny<IRestRequest>()))
                .Returns((IRestRequest x) => { return TestResponse(x); });

            restClient = restClientMock.Object;
            client = new RelaxoClient(restClient);

        }
        #endregion

        #region TestGetUser
        [TestMethod]
        public void Test_GetUser()
        {
            var user = client.GetUserById(1);
            Assert.IsNotNull(user);

            // User-Daten Testen
            Assert.AreEqual(user.Id, 1);
            Assert.AreEqual(user.Username, "mmoel001");
            Assert.AreEqual(user.Password, "testpw");
            Assert.AreEqual(user.Prename, "Moritz");
            Assert.AreEqual(user.Name, "Moeller");
            Assert.AreEqual(user.IsAdmin, true);
            Assert.AreEqual(user.IsActive, true);
        }
        #endregion
    }
}

