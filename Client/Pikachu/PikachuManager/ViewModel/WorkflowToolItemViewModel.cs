using PikachuLib.Models.Workflow;
using PikachuViewBase.Hilfsklassen;
using System;

namespace PikachuManager.ViewModel
{
    /// <summary>
    /// ViewModel für die Items der Toolbox des Workflowitem-Editors
    /// </summary>
    public abstract class WorkflowToolItemViewModel : ViewModelBase
    {
        private const string imgPrefix = "pack://application:,,,/PikachuManager;component/Resources/new/";

        /// <summary>
        /// Legt die Bild-Repräsentation des ViewModel fest.
        /// </summary>
        public string Image { get; private set; }

        /// <summary>
        /// Die Titel-Beschriftung des Workflowitems.
        /// </summary>
        public string Title { get; private set; }

        protected WorkflowToolItemViewModel() { }
        
        protected WorkflowToolItemViewModel(string title, string imagePath)
        {
            Title = title;
            Image = String.Concat(imgPrefix, imagePath);
        }

        /// <summary>
        /// Gibt den Workflowitem-Typ zurück.
        /// </summary>
        /// <returns></returns>
        public abstract Type GetWorkflowItemType();
    }

    /// <summary>
    /// ViewModel für die Items der Toolbox des Workflowitem-Editors
    /// </summary>
    /// <typeparam name="T">Typ des Workflowitems</typeparam>
    public class WorkflowToolItemViewModel<T> : WorkflowToolItemViewModel where T : WorkflowItem
    {
        public WorkflowToolItemViewModel() { }

        public WorkflowToolItemViewModel(string title, string imagePath)
            : base(title, imagePath)
        { }


        public override Type GetWorkflowItemType()
        {
            return typeof(T);
        }
    }    
}
