using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Configuration;
using System.Collections.Specialized;
using PikachuLib.Models.Workflow;
using PikachuLib.Communication;

namespace OutputTest
{
    class Program
    {
        static void Main(string[] args)
        {

            var client = new RelaxoClient("http://192.168.178.47:8081/");


            var user = client.LogIn("cmard001", "123456");

            Console.WriteLine(user.Token);



            var workflowItemsList = client.GetWorkflowItemsForWorkflow(new Workflow(5));

            foreach (var w in workflowItemsList)
            {
                Console.WriteLine(w.Id + ": " + w.Type);
            }

            /*
            var workflow = client.CreateWorkflow();
            Console.WriteLine(workflow.Id);


            WorkflowItem task = new PikachuLib.Models.Workflow.Task(200, 200);
            WorkflowItem task2 = new PikachuLib.Models.Workflow.Task(100, 100);


            task = client.CreateWorkflowItem(workflow, task);
            Console.WriteLine("WorkflowItem (Task) erstellt: " + task.Id);
            task2 = client.CreateWorkflowItem(workflow, task2);
            Console.WriteLine("WorkflowItem (Task) erstellt: " + task2.Id);

            task.Position = new PikachuLib.Models.Position(10, 10);
            client.SetWorkflowItemPosition(task);
            Console.WriteLine("WorkflowItem (Task) Position auf 10, 10 geändert.");

            task.NextItem = task2.Id;
            client.SetWorkflowItemNextItem(task);
            Console.WriteLine("WorkflowItem (Task) " + task.Id + ": NextItem = " + task2.Id + " gesetzt.");

            client.LockWorkflowItem(task);
            Console.WriteLine("WorkflowItem (Task) gelocked: " + task.Id);
            client.UnlockWorkflowItem(task);
            Console.WriteLine("WorkflowItem (Task) unlocked: " + task.Id);

            client.DeleteWorkflowItem(task);
            Console.WriteLine("WorkflowItem (Task) gelöscht: " + task.Id);
            client.DeleteWorkflowItem(task2);
            Console.WriteLine("WorkflowItem (Task) gelöscht: " + task2.Id);
            //client.DeleteWorkflow(workflow);
            //Console.WriteLine("Workflow gelöscht.");

            /*var relaxoClient = RelaxoClient.Instance;

            var user = relaxoClient.LogIn("test", "test");
            Console.WriteLine(user.Token);
            

            // hole workflows des users
            var workflowsList = relaxoClient.GetWorkflows(user);
            foreach (var i in workflowsList)
            {
                Console.WriteLine(i.Id + ", " + i.Name);
            }*/

            

            // hole tasks zu workflow
            /*var tasksList = relaxoClient.GetTasks(workflowsList[1]);
            foreach (var i in tasksList)
            {
                Console.WriteLine(i.Id + ", " + i.Name);
            }
             * */

            // hole Jobs zu Task


            Console.ReadKey();
        }
    }
}
