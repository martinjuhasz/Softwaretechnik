using System.Windows.Input;
using PikachuManager.ViewModel.Workflowitems;
using PikachuViewBase.Hilfsklassen;
using PikachuLib.Communication;
using System;

namespace PikachuManager.ViewModel
{
    /// <summary>
    /// ViewModel für dem Workflowitem-Editor.
    /// </summary>
    public class WorkflowitemEditorViewModel : ViewModelBase
    {
        #region error

        /// <summary>
        /// Property für das Anzeigen von Fehlermeldungen
        /// </summary>
        private string errorMessage;
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

        #endregion

        #region properties

        /// <summary>
        /// Das ViewModel des Workflowitems.
        /// </summary>
        private WorkflowItemViewModel workflowItem;
        public WorkflowItemViewModel WorkflowItem
        {
            get { return workflowItem; }
            set
            {
                if (workflowItem != value)
                {
                    workflowItem = value;
                    OnPropertyChanged();
                }
            }
        }

       
        /// <summary>
        /// Legt fest, ob die View Angezeigt wird, oder nicht. 
        /// NICHT VON AUSERHALB DER KLASSE ÄNDERN -> Close() aufrufen!!
        /// </summary>
        private bool isEnabled;
        public bool IsEnabled
        {
            get { return isEnabled; }
            private set
            {
                if (isEnabled != value)
                {
                    isEnabled = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Gibt die ID-Zurück, die das Workflow-Item identifiziert.
        /// </summary>
        private int workflowitemId;
        public int WorkflowitemId 
        {
            get { return workflowitemId; }
            set 
            {
                if (workflowitemId != value)
                {
                    workflowitemId = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion
        
        #region commands

        /// <summary>
        /// Command für den OK-Knopf.
        /// </summary>
        private ICommand okCommand;
        public ICommand OkCommand
        {
            get
            {
                if (okCommand == null)
                {
                    okCommand = new ActionCommand(x => Close(true));
                }
                return okCommand;
            }
        }

        /// <summary>
        /// Command für den Abbruch-Button.
        /// </summary>
        private ICommand abortCommand;
        public ICommand AbortCommand
        {
            get
            {
                if (abortCommand == null)
                {
                    abortCommand = new ActionCommand(x => Close());
                }
                return abortCommand;
            }
        }

        #endregion

        #region methods

        /// <summary>
        /// Läd ein Workflowitem in den contentbereich der View.
        /// </summary>
        /// <param name="item"></param>
        public void LoadWorkflowitem(WorkflowItemViewModel item)
        {
            ErrorMessage = string.Empty;
            WorkflowitemId = item.Id;
            WorkflowItem = item;
            workflowItem.LoadDetails();
            workflowItem.StartEdit();

            IsEnabled = true;
        }
        
        /// <summary>
        /// Versucht den Workflowitemeditor zu schließen.
        /// </summary>
        /// <param name="save">True, wenn die eingegeben werte an den Server geschickt werden sollen.</param>
        public void Close(bool save = false)
        {
            if (workflowItem != null)
            {
                if (save)
                {
                    try
                    {
                        workflowItem.SaveDetails();
                    }
                    catch (RESTException ex)
                    {
                        ErrorMessage = ex.Message;
                        return;
                    }
                    catch (Exception)
                    {
                        ErrorMessage = "Ein unbekannter Fehler ist aufgetreten!";
                        return;
                    }
                }

                workflowItem.EndEdit();
                workflowItem.IsSelected = false;
            }

            IsEnabled = false;
        }

        #endregion
    }
}
