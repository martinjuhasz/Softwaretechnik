using System;
using System.ComponentModel;
using System.Diagnostics;
using System.Security.Cryptography.X509Certificates;
using PikachuLib;
using PikachuLib.Communication;
using PikachuViewBase.Hilfsklassen;
using PikachuManager.ViewModel.DragNDrop;
using PikachuViewBase.View;
using PikachuViewBase.ViewModel;

namespace PikachuManager.ViewModel
{
    /// <summary>
    /// Containerklasse für den Graphischen Editor eines Worklfows.
    /// </summary>
    public class WorkflowEditorViewModel : ViewModelBase
    {
        #region properties

        /// <summary>
        /// Navigationsmodul
        /// </summary>
        public WorkflowNavigationViewModel WorkflowNavigation { get; set; }

        /// <summary>
        /// Das ViewModel für die Zeichenfläche.
        /// </summary>
        public DesignerCanvasViewModel DesignerCanvas { get; set; }

        /// <summary>
        /// Das ViewModel für die Toolbox.
        /// </summary>
        public ToolboxViewModel Toolbox { get; set; }

        /// <summary>
        /// Das ViewModel für die Bearbeitung eines Workflowitems.
        /// </summary>
        public WorkflowitemEditorViewModel WorkflowitemEditor { get; set; }

        /// <summary>
        /// Gibt true zurück, wenn ein Workflow ausgewählt ist.
        /// </summary>
        private bool isWorkflowSelected;
        public bool IsWorkflowSelected
        {
            get { return isWorkflowSelected; }
            set
            {
                if (isWorkflowSelected != value)
                {
                    isWorkflowSelected = value;
                    OnPropertyChanged();
                }
            }
        }


        private string errorMessage;
        /// <summary>
        /// Fehlermeldung, wenn ein Fehler während dem Bearbeiten im WorkflowEditor auftritt.
        /// </summary>
        public string ErrorMessage 
        {
            get { return errorMessage; }
            private set
            {
                if (!Equals(errorMessage, value))
                {
                    errorMessage = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Wird aufgerufen, wenn sich eine Property in WorkflowEditorDesignerCanvasErrorViewModel ausgelöst wird.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        private void ErrorOnPropertyChanged(object sender, PropertyChangedEventArgs args)
        {
            if (args.PropertyName == "Error")
            {
                ErrorMessage = WorkflowEditorDesignerCanvasErrorViewModel.Instance.Error;
            }
        }
        #endregion

        #region constructor
        
        public WorkflowEditorViewModel(WorkflowNavigationViewModel workflowNavigation)
        {
            WorkflowNavigation = workflowNavigation;

            // erzeuge ViewModels für DesignerCanvas und Toolbox
            Toolbox = new ToolboxViewModel();

            // workaround um die OnPropertyChanged-Event auszulösen, wenn sich der aktuelle Workflow ändert.
            IsWorkflowSelected = ClientSession.Instance.CurrentWorkflow != null;
            ClientSession.Instance.CurrentWorkflowChangedEvent += ev => 
            {
                IsWorkflowSelected = ClientSession.Instance.CurrentWorkflow != null;
            };

            // Erstelle Canvas mit einer Methode um ein Workflowitem zu öffnen.
            DesignerCanvas = new DesignerCanvasViewModel(item =>
            {   
                try
                {
                    WorkflowitemEditor.LoadWorkflowitem(item);
                }
                catch (RESTException ex)
                {
                    WorkflowEditorDesignerCanvasErrorViewModel.Instance.Error = ex.Message;
                }
                catch (Exception)
                {
                    WorkflowEditorDesignerCanvasErrorViewModel.Instance.Error = "Ein unbekannter Fehler ist aufgetreten!";
                }
            });

            // Beim Worfklow Wechseln wird der Workflowitem-Editor geschlossen.
            ClientSession.Instance.CurrentWorkflowChangedEvent += args => WorkflowitemEditor.Close();

            WorkflowitemEditor = new WorkflowitemEditorViewModel();

            // registriere beim Event des Singleton, damit Fehler im View angezeigt werden können.
            WorkflowEditorDesignerCanvasErrorViewModel.Instance.PropertyChanged += ErrorOnPropertyChanged;
        }

        #endregion
    }
}
