using System;
using PikachuLib.Models.Job;
using PikachuLib.Models.TaskComponent;

namespace PikachuClient.ViewModel.ComponentTypes
{
    /// <summary>
    /// Factory klasse um ein ViewModel von einem Model zu erstellen.
    /// </summary>
    public class TaskComponentViewModelFactory
    {
        /// <summary>
        /// Erstellt und Füllt ein ViewModel mit den Werten des Job.
        /// </summary>
        /// <param name="component">Die TaskComponent, von der das ViewModel erstellt werden soll.</param>
        /// <param name="jobTask">Jobtask, das die Werte eines Jobs beinhaltet.</param>
        /// <returns>Das ViewModel für die übergebene TaskComponent.</returns>
        public static ComponentViewModelBase CreateAndFill(TaskComponent component, JobTask jobTask)
        {
            ComponentViewModelBase resultViewModel = CreateViewModel(component);

            if (jobTask != null)
            {
                var jobValue = jobTask.Components.Find(j => j.TaskComponentId == component.Id);
                if (jobValue != null)
                {
                    resultViewModel.SetValues(jobValue);
                }
            }

            return resultViewModel;
        }

        /// <summary>
        /// Erstellt und Füllt ein ViewModel für die TaskComponent.
        /// </summary>
        /// <param name="component">Die TaskComponent, von der das ViewModel erstellt werden soll.</param>
        /// <returns>Das ViewModel für die übergebene TaskComponent.</returns>
        public static ComponentViewModelBase CreateViewModel(TaskComponent component)
        {
            if (component is TaskComponentText)
            {
                return new TextComponentViewModel((TaskComponentText)component);
            }

            if (component is TaskComponentInteger)
            {
                return new IntegerComponentViewModel((TaskComponentInteger)component);
            }

            if (component is TaskComponentFloat)
            {
                return new FloatComponentViewModel((TaskComponentFloat)component);
            }

            if (component is TaskComponentDate)
            {
                return new DateComponentViewModel((TaskComponentDate)component);
            }

            throw new Exception(string.Format("Exception: {0} im if nicht definiert", component.GetType()));
        }
    }
}
