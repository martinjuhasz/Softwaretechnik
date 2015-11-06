using System;
using PikachuViewBase.Hilfsklassen;
using PikachuViewBase.ViewModel;
using Task = PikachuLib.Models.Workflow.Task;
using PikachuLib;

namespace PikachuClient.ViewModel
{
    /// <summary>
    /// Container-Klasse des Benutzerclients.
    /// </summary>
    public class ClientContainerViewModel : ViewModelBase
    {
        #region properties

        /// <summary>
        /// Navigations ViewModel
        /// </summary>
        public WorkflowNavigationViewModel NavViewModel { get; set; }

        /// <summary>
        /// Liste der jobs.
        /// </summary>
        private JobListViewModel jobListViewModel;
        public JobListViewModel JobListViewModel
        {
            get
            {
                return jobListViewModel;
            }
            set
            {
                if (value != jobListViewModel)
                {
                    jobListViewModel = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Liste der Taskformulare.
        /// </summary>
        private TaskFormularViewModel taskFormularViewModel;
        public TaskFormularViewModel TaskFormularViewModel
        {
            get
            {
                return taskFormularViewModel;
            }
            set
            {
                if (value != taskFormularViewModel)
                {
                    taskFormularViewModel = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region constructor

        public ClientContainerViewModel()
        {
            NavViewModel = new WorkflowNavigationViewModel();
            JobListViewModel = new JobListViewModel();
            TaskFormularViewModel = new TaskFormularViewModel();

            jobListViewModel.NavigateToJobEvent += NavigateToJob;

            JobListViewModel.RefreshJobList();
        }

        #endregion

        #region methods

        /// <summary>
        /// Funktion um zu einen Job zu Navigieren.
        /// </summary>
        /// <param name="taskId">Id des Formularfelds an dem der Job ist.</param>
        /// <param name="jobId">Id des Jobs, zu dem navigiert werden soll.</param>
        private void NavigateToJob(int taskId, int jobId)
        {
            TaskFormularViewModel.LoadJobTask(taskId, jobId);
        }

        #endregion
    }
}
