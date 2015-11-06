using System.Collections.Generic;
using PikachuLib.Communication.Models.Workflow;
using PikachuLib.Models.TaskComponent;


namespace PikachuLib.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines Tasks.
    /// </summary>
    public class Task : WorkflowItem
    {
        #region properties
        /// <summary>
        /// Name des Tasks.
        /// </summary>
        public string Name { get; private set; }

        private Dictionary<int, Job.Job> jobs;
        /// <summary>
        /// Dictionary von JobId auf JobObjekt des Tasks
        /// </summary>
        public Dictionary<int, Job.Job> Jobs
        {
            get
            {
                return jobs;
            }
        }

        /// <summary>
        /// Liste der TaskComponent-Gruppen des Tasks.
        /// </summary>
        public List<TaskComponentGroup> FormGroups { get; private set; }

        /// <summary>
        /// Liste der Benutergruppen, die diesen Task bearbeiten können.
        /// </summary>
        public List<int> Usergroups { get; private set; }

        #endregion


        #region constructors

        public Task()
        {
            Type = WorkflowItemType.TASK;
        }

        public Task(int id, Position position, bool locked, IList<int> nextItem)
            : base(id, position, locked, nextItem)
        {
            Type = WorkflowItemType.TASK;
        }

        public Task(int id, Position position, bool locked, IList<int> nextItem, string name)
            : base(id, position, locked, nextItem)
        {
            Type = WorkflowItemType.TASK;
            Name = name;
        }

        public Task(int id, int positionX, int positionY, IList<int> nextItem)
            : this(id, new Position(positionX, positionY), false, nextItem)
        {
        }

        public Task(int id, string name)
            : this(id, new Position(), false, null)
        {
            Name = name;
            jobs = new Dictionary<int, Job.Job>();
            FormGroups = new List<TaskComponentGroup>();
        }

        public Task(int id)
        {
            Id = id;
        }

        #endregion

        /// <summary>
        /// Parsed ein Schnittstellen-Modell in einen Task
        /// </summary>
        /// <param name="taskResponse">Das Modell der Schnittstelle.</param>
        /// <returns>Ein Task-Objekt.</returns>
        public static Task Parse(TasksResponse taskResponse)
        {
            var task = new Task(taskResponse.Id, taskResponse.Name);

            task.Usergroups = new List<int>(taskResponse.Usergroups);

            foreach (var item in taskResponse.FormGroups)
            {
                if (item == null) continue;

                task.FormGroups.Add(TaskComponentGroup.Parse(item));
            }

            return task;
        }
        

        public override string ToString()
        {
            return Id.ToString("000") + " : " + Name;
        }

        public override bool Equals(object obj)
        {
            var other = obj as Task;
            return other != null &&
                    Id == other.Id;
        }
    }
}
