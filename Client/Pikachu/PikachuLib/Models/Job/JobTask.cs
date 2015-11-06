using System;
using System.Collections.Generic;
using PikachuLib.Communication.Models;
using PikachuLib.Communication.Models.FormGroup;
using PikachuLib.Communication.Models.Job;

namespace PikachuLib.Models.Job
{
    /// <summary>
    /// Repräsentation eines JobTask.
    /// </summary>
    public class JobTask
    {
        #region properties
        /// <summary>
        /// Start-Zeitpunkt des JobTasks.
        /// </summary>
        public DateTime StartTime { get; set; }

        /// <summary>
        /// End-Zeitpunkt des JobTasks.
        /// </summary>
        public DateTime EndTime { get; set; }
        
        /// <summary>
        /// Ersteller des JobTasks.
        /// </summary>
        public string Creator { get; set; }

        /// <summary>
        /// Liste der Formularfelder des Jobtasks
        /// </summary>
        public List<JobComponent> Components { get; set; }
        #endregion

        public JobTask()
        {
            Components = new List<JobComponent>();
        }


        /// <summary>
        /// Parsed einen REST-Response in ein Job-Task-Model.
        /// </summary>
        /// <param name="jobTask">Der JobTask, der die Werte der Anfrage beinhaltet.</param>
        /// <returns>Ein JobTask-Objekt.</returns>
        public static JobTask Parse(JobTaskResponse jobTask)
        {
            var task = new JobTask
            {
                StartTime = jobTask.StartTime,
                EndTime = jobTask.EndTime,
                Creator = jobTask.Creator
            };

            foreach (var component in jobTask.Components)
            {
                JobComponent com = null;
                switch (component.Type)
                {
                    case ComponentType.TaskComponentText:
                        com = new JobComponentText { TaskComponentId = component.Id ,Value = ((JobComponentResponseText)component).Value };
                        break;
                    case ComponentType.TaskComponentInteger:
                        com = new JobComponentInteger { TaskComponentId = component.Id, Value = ((JobComponentResponseInteger)component).Value };
                        break;
                    case ComponentType.TaskComponentFloat:
                        com = new JobComponentFloat { TaskComponentId = component.Id, Value = ((JobComponentResponseFloat)component).Value };
                        break;
                    case ComponentType.TaskComponentDate:
                        com = new JobComponentDate{ TaskComponentId = component.Id, Value = ((JobComponentResponseDate)component).Value };
                        break;
                }

                task.Components.Add(com);
            }

            return task;
        }
    }
}
