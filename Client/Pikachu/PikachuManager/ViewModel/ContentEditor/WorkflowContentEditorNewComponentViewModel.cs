using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using PikachuViewBase.Hilfsklassen;
using PikachuLib;
using PikachuLib.Communication.Event;
using System.Diagnostics;
using System.Windows.Data;
using PikachuLib.Communication;
using PikachuViewBase.ViewModel;
using PikachuLib.Models.TaskComponent;

namespace PikachuManager.ViewModel
{
    
    /// <summary>
    /// Model der View zum Erstellen eines neuen Components
    /// </summary>
    public abstract class WorkflowContentEditorNewComponentViewModel : ViewModelBase
    {

        #region properties

        /// <summary>
        /// Der WorkflowitemEditor.
        /// </summary>
        protected WorkflowContentEditorBaseViewModel baseEditorViewModel;

        /// <summary>
        /// Gibt die dazugehörige Formulargruppe an.
        /// </summary>
        protected TaskComponentGroup selectedGroup;

        /// <summary>
        /// Name des Formularfeldes.
        /// </summary>
        protected string componentName;
        public string ComponentName
        {
            get { return componentName; }
            set
            {
                if (componentName != value)
                {
                    componentName = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Kommentar des Formularfeldes
        /// </summary>
        protected string componentComment;
        public string ComponentComment
        {
            get { return componentComment; }
            set
            {
                if (componentComment != value)
                {
                    componentComment = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Gibt an, ob das Feld ein Pflichtfeld ist.
        /// </summary>
        protected bool componentRequired;
        public bool ComponentRequired
        {
            get { return componentRequired; }
            set
            {
                if (componentRequired != value)
                {
                    componentRequired = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region button actions

        private ICommand cancelFormsCommand;
        public ICommand CancelFormsCommand
        {
            get
            {
                if (cancelFormsCommand == null)
                {
                    cancelFormsCommand = new ActionCommand(x =>
                    {
                        baseEditorViewModel.showGroupListView(selectedGroup);
                    });
                }

                return cancelFormsCommand;
            }
        }

        private ICommand saveComponentGroupCommand;
        public ICommand SaveComponentGroupCommand
        {
            get
            {
                if (saveComponentGroupCommand == null)
                {
                    saveComponentGroupCommand = new ActionCommand(x =>
                    {
                        saveComponent();
                        baseEditorViewModel.showGroupListView(selectedGroup);
                    });
                }

                return saveComponentGroupCommand;
            }
        }

        #endregion

        #region Constructor

        public WorkflowContentEditorNewComponentViewModel(WorkflowContentEditorBaseViewModel baseEditorViewModel, TaskComponentGroup selectedGroup)
        {
            this.baseEditorViewModel = baseEditorViewModel;
            this.selectedGroup = selectedGroup;
        }

        #endregion

        #region methods

        /// <summary>
        /// Speichert den Component
        /// </summary>
        protected abstract void saveComponent();

        #endregion

    }
    
    
}
