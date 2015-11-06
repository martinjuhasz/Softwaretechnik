using System.Collections.Generic;

namespace PikachuLib.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines Workflows.
    /// </summary>
    public class Workflow
    {
        #region properties
        private Dictionary<int, Task> tasks;
        /// <summary>
        /// Dictionary von TaskId auf TaskObjekt des Workflows
        /// </summary>
        public Dictionary<int, Task> Tasks
        {
            get
            {
                return tasks;
            }
        }

        /// <summary>
        /// ID des Worklfows
        /// </summary>
        public int Id { get; private set; }

        /// <summary>
        /// Name des Workflows
        /// </summary>
        public string Name { get; private set; }

        /// <summary>
        /// Gibt an, ob der aktuelle Benutzer einen Job für diesen Workflow erstellen kann.
        /// </summary>
        public bool UserCanCreateJob { get; private set; }
        #endregion

        #region constructors
        public Workflow(int id)
        {
            this.Id = id;
        }

        public Workflow(int id, string name)
        {
            this.Id = id;
            this.Name = name;
        }

        public Workflow(int id, string name, bool userCanCreateJob)
        {
            this.Id = id;
            this.Name = name;
            this.tasks = new Dictionary<int, Task>();
            this.UserCanCreateJob = userCanCreateJob;
        }
        #endregion

        public override string ToString()
        {
            return Name;
        }
    }
}
