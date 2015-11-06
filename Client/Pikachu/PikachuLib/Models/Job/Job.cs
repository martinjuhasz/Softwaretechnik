using System;
using System.Collections.Generic;
using System.Globalization;
using PikachuLib.Communication.Models;
using PikachuLib.Communication.Models.Job;

namespace PikachuLib.Models.Job
{
    /// <summary>
    /// Repräsentation eines Jobs.
    /// </summary>
    public class Job
    {
        #region properties

        /// <summary>
        /// ID eines Jobs
        /// </summary>
        public int Id { get; private set; }

        /// <summary>
        /// Startzeit eines Jobs
        /// </summary>
        public DateTime StartTime { get; private set; }

        /// <summary>
        /// Endzeit eines Jobs
        /// </summary>
        public DateTime EndTime { get; private set; }

        /// <summary>
        /// Ersteller eines Jobs
        /// </summary>
        public string Creator { get; set; }

        /// <summary>
        /// Gibt an, ob ein Job aktiv ist
        /// </summary>
        public bool Active { get; private set; }

        /// <summary>
        /// Gibt an, ob ein Job gesperrt ist
        /// </summary>
        public bool Locked { get; private set; }

        /// <summary>
        /// Liste der Jobtasks
        /// </summary>
        public List<JobTask> JobTasks { get; set; }

        #endregion


        #region constructors
        public Job(int id, DateTime startTime, bool active)
        {
            JobTasks = new List<JobTask>();
            this.Id = id;
            this.StartTime = startTime;
            this.Active = active;
        }

        public Job(int id, DateTime startTime, bool active, bool locked)
            : this(id, startTime, active)
        {
            this.Locked = locked;
        }

        #endregion

        /// <summary>
        /// Parsed einen REST-Response in ein Model.
        /// </summary>
        /// <param name="jobResponse">Das REST-Modell für einen Job</param>
        /// <returns>Ein Job-Objekt</returns>
        public static Job Parse(JobDetailResponse jobResponse)
        {
            var job = new Job(jobResponse.JobId, jobResponse.StartTime, jobResponse.Active);

            job.EndTime = jobResponse.EndTime;
            job.Creator = jobResponse.Creator;

            foreach (var jobTask in jobResponse.JobTasks)
            {
                job.JobTasks.Add(JobTask.Parse(jobTask));
            }

            return job;
        }
    }
}
