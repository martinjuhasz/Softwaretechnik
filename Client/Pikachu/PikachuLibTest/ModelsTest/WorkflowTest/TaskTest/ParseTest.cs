using Microsoft.VisualStudio.TestTools.UnitTesting;
using PikachuLib.Communication.Models;
using PikachuLib.Communication.Models.FormGroup;
using PikachuLib.Communication.Models.Workflow;
using PikachuLib.Models.TaskComponent;
using PikachuLib.Models.Workflow;

namespace PikachuLibTest.ModelsTest.WorkflowTest.TaskTest
{
    [TestClass]
    public class ParseTest
    {

        private TasksResponse tasksResponse;

        [TestInitialize]
        public void Init()
        {
            tasksResponse = new TasksResponse
            {
                Id = 12345,
                Name = "Das ist ein Testfeld"
            };

            var formGroup = new TaskComponentGroupResponse
            {
                Id = 156,
                Name = "Test 1"

            };

            tasksResponse.FormGroups.Add(formGroup);
            tasksResponse.Usergroups = new System.Collections.Generic.List<int>();
            formGroup.Components.Add(new TextInputResponse
            {
                Id = 5555,
                Name = "Testfeld 1",
                DefaultValue = "DefaultValue",
                Order = 1,
                RegexPattern = "REGEX",
                Comment = "Kommentar zu 5555",
                Required = true,
                Type = ComponentType.TaskComponentText
            });
        }

        [TestMethod]
        public void Test_Parsing_Is_Ok()
        {
            Task task = Task.Parse(tasksResponse);
            
            Assert.AreEqual(task.Id, tasksResponse.Id);
            Assert.AreEqual(task.Name, tasksResponse.Name);
            Assert.AreEqual(task.FormGroups.Count, tasksResponse.FormGroups.Count);


            Assert.IsInstanceOfType(task.FormGroups[0], typeof(TaskComponentGroup));
            Assert.IsInstanceOfType(task.FormGroups[0].Components[0], typeof(TaskComponentText));
            var text = (TaskComponentText)task.FormGroups[0].Components[0];
            var respText = (TextInputResponse)tasksResponse.FormGroups[0].Components[0];

            Assert.AreEqual(text.Id, respText.Id);
            Assert.AreEqual(text.Name, respText.Name);
            Assert.AreEqual(text.Order, respText.Order);
            Assert.AreEqual(text.Comment, respText.Comment);
            Assert.AreEqual(text.Required, respText.Required);
            Assert.AreEqual(text.Value, respText.DefaultValue);
            Assert.AreEqual(text.RegexExpression, respText.RegexPattern);

        }
    }
}
