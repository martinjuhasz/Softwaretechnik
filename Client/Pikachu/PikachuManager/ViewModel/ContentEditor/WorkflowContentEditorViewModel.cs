using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using PikachuLib.Communication.Models.FormGroup;
using PikachuLib.Models.TaskComponent;
using PikachuViewBase.Hilfsklassen;
using PikachuLib;
using PikachuLib.Communication.Event;
using System.Diagnostics;
using System.Windows.Data;
using PikachuLib.Communication;
using PikachuLib.Models.Workflow;
using System.Collections.ObjectModel;
using System.Windows;
using GongSolutions.Wpf.DragDrop;
using PikachuViewBase.ViewModel;
using DragDrop = GongSolutions.Wpf.DragDrop.DragDrop;
using System.Windows.Threading;

namespace PikachuManager.ViewModel
{

    /// <summary>
    /// Model der View zum Anzeigen der FormGroups mit zugehörigen Components
    /// 
    /// Implementiert das Interface IDropTarget von WPF Drag'n'Drop Framework Gong. 
    /// So kann ein Drop auf die Liste CurrentComponents verarbeitet werden.
    /// </summary>
    public class WorkflowContentEditorViewModel : ViewModelBase, IDropTarget
    {

        #region properties

        /// <summary>
        /// Die Workflownavigation
        /// </summary>
        public WorkflowNavigationViewModel WorkflowNavigation { get; set; }

        /// <summary>
        /// Aktuelle Formulargruppen.
        /// </summary>
        private ListCollectionView currentFormGroups;
        public ListCollectionView CurrentFormGroups
        {
            get { return currentFormGroups; }
            set
            {
                if (currentFormGroups == null || !currentFormGroups.Equals(value))
                {
                    currentFormGroups = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Die aktuellen Formularkomponenten.
        /// </summary>
        public ObservableCollection<TaskComponent> CurrentComponents { set; get; }
        
        /// <summary>
        /// Die ausgewählte Formulargruppe.
        /// </summary>
        private TaskComponentGroup selectedGroup;
        public TaskComponentGroup SelectedGroup
        {
            get { return selectedGroup; }
            set
            {
                if (selectedGroup == null || !selectedGroup.Equals(value))
                {
                    selectedGroup = value;
                    OnPropertyChanged();

                    updateCurrentComponentList();
                }
            }
        }

        /// <summary>
        /// Der ausgewählte Formulartyp.
        /// </summary>
        private ComponentType selectedComponentType;
        public ComponentType SelectedComponentType
        {
            get { return selectedComponentType; }
            set
            {
                if (!selectedComponentType.Equals(value))
                {
                    selectedComponentType = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Liste der möglichen Formularkomponenten.
        /// </summary>
        public IEnumerable<ComponentType> ComponentTypes
        {
            get
            {
                return Enum.GetValues(typeof(ComponentType)).Cast<ComponentType>();
            }
        }

        /// <summary>
        /// BasisViewModel für das erstellen der entsprechenden Formularkomponenten.
        /// </summary>
        private  WorkflowContentEditorBaseViewModel baseEditorViewModel;


        #endregion

        #region button actions

        /// <summary>
        /// Command zur neuen Formulargruppe zu erstellen.
        /// </summary>
        private ICommand createNewFormGroupCommand;
        public ICommand CreateNewFormGroupCommand
        {
            get
            {
                if (createNewFormGroupCommand == null)
                {
                    createNewFormGroupCommand = new ActionCommand(x => baseEditorViewModel.showNewGroupListView(SelectedGroup), 
                        o => ClientSession.Instance.CurrentWorkflow != null);
                }

                return createNewFormGroupCommand;
            }
        }

        /// <summary>
        /// Command zur neuen Formularkomponente erstellen.
        /// </summary>
        private ICommand createNewComponentCommand;
        public ICommand CreateNewComponentCommand
        {
            get
            {
                if (createNewComponentCommand == null)
                {
                    createNewComponentCommand = new ActionCommand(x =>
                    {
                        if (selectedGroup != null) {
                            SwitchToCreateComponentView();
                        }
                    },
                    o => ClientSession.Instance.CurrentWorkflow != null);
                }

                return createNewComponentCommand;
            }
        }

        #endregion

        #region Constructor

        public WorkflowContentEditorViewModel(WorkflowContentEditorBaseViewModel baseEditorViewModel, WorkflowNavigationViewModel workflowNavigation, TaskComponentGroup selectedGroup)
        {
            this.WorkflowNavigation = workflowNavigation;
            this.baseEditorViewModel = baseEditorViewModel;
            // initialisiere Liste für Components der aktuellen Formgroup
            CurrentComponents = new ObservableCollection<TaskComponent>();
            SelectedGroup = selectedGroup;

            LoadComponentGroups();

            ClientSession.Instance.CurrentWorkflowChangedEvent += ClientSessionPropertyChanged;
            ClientSession.Instance.WorkflowChangedForContentEditorEvent += ClientSessionPropertyChanged;
        }

        #endregion

        #region methods

        /// <summary>
        /// Lädt die FormGroups vom Server
        /// </summary>
        public void LoadComponentGroups()
        {
            if (ClientSession.Instance.CurrentWorkflow == null)
            {
                CurrentFormGroups = null;
                return;
            }

            var workflowId = ClientSession.Instance.CurrentWorkflow.Id;
            var formGroups = ClientSession.Instance.RelaxoClient.GetComponentGroups(workflowId);

            // sort formgroup components 
            formGroups.ForEach(group=> group.Components.Sort((c1, c2) => c1.Order-c2.Order));


            TaskComponentGroup newSelectedGroup = preserveSelectedGroup(formGroups);
            CurrentFormGroups = new ListCollectionView(formGroups);
            SelectedGroup = newSelectedGroup;
        }

        /// <summary>
        /// wechselt zur passenden ComponentCreateView
        /// </summary>
        public void SwitchToCreateComponentView()
        {
            baseEditorViewModel.showNewComponentView(SelectedGroup, SelectedComponentType);
        }    

        /// <summary>
        /// Wählt anhand neuer ComponentGroups die zuvor selektierte Gruppe aus der neuen Liste aus und gibt diese zurück
        /// </summary>
        /// <param name="newComponentGroups">aktuelle ComponentGrups</param>
        /// <returns>ein Objekt aus der neuen Liste, welches der zuvor ausgewählten Gruppe entspricht</returns>
        private TaskComponentGroup preserveSelectedGroup(List<TaskComponentGroup> newComponentGroups)
        {
            // keine Auswahl zurückgeben wenn neue gruppen leer ist
            if (newComponentGroups == null || newComponentGroups.Count <= 0)
            {
                return null;
            }

            // erste neue gruppe zurückgeben wenn es vorher keine auswahl gab
            if (SelectedGroup == null)
            {
                return newComponentGroups[0];
            }

            // alte Gruppe wurde in der neuen Liste gefunden
            foreach (TaskComponentGroup group in newComponentGroups)
            {
                if (group.Id == SelectedGroup.Id)
                {
                    return group;
                }
            }

            // ansonsten erste gruppe
            return newComponentGroups[0];
        }

        /// <summary>
        /// Update der Components der Formgroup.
        /// Wird beim Wechsel einer Formgroup aufgerufen.
        /// </summary>
        private void updateCurrentComponentList() {

            // wenn keine gruppe ausgewählt, leere components
            if (SelectedGroup == null)
            {
                CurrentComponents.Clear();
                return;
            }

            // anonsten füge enstrepchende hinzu
            CurrentComponents.Clear();

            // füge Components der ausgewählten Formgroup hinzu
            foreach (var comp in SelectedGroup.Components)
            {
                CurrentComponents.Add(comp);
            }
        }

        /// <summary>
        /// Listener auf Workflowänderungen. Workflow hat sich geändert.
        /// </summary>
        /// <param name="arg">EventContainer, enthält den geänderten Workflow</param>
        private void ClientSessionPropertyChanged(EventArgs arg)
        {
            Application.Current.Dispatcher.Invoke(new Action(() => {
                LoadComponentGroups(); 
            }));
        }

        /// <summary>
        /// Listener wenn ein Component bewegt wird
        /// </summary>
        /// <param name="dropInfo"></param>
        public void DragOver(IDropInfo dropInfo)
        {
            DragDrop.DefaultDropHandler.DragOver(dropInfo);
        }

        /// <summary>
        /// Methode, die einen Drop auf die Liste CurrentComponents handelt.
        /// </summary>
        /// <param name="dropInfo">Infos zum gedroppten Element</param>
        void IDropTarget.Drop(IDropInfo dropInfo)
        {
            // default Drop Handler
            DragDrop.DefaultDropHandler.Drop(dropInfo);

            updateComponentOrder();
            saveComponentOrder();

        }

        /// <summary>
        /// Aktualisiert die Order-Property anhand der aktuellen Sortierreihenfolge der Componenten
        /// </summary>
        void updateComponentOrder() {

            if (CurrentComponents == null || CurrentComponents.Count <= 0)
            {
                return;
            }
            
            int order = 0;
            foreach (TaskComponent component in CurrentComponents) {
                component.Order = order;
                order++;
            }
        }

        /// <summary>
        /// Speichert die Sortierreihenfolge der Componenten
        /// </summary>
        private void saveComponentOrder()
        {
            try
            {
                var workflowId = ClientSession.Instance.CurrentWorkflow.Id;
                RelaxoClient client = ClientSession.Instance.RelaxoClient;

                List<TaskComponent> componentList = CurrentComponents.ToList();
                client.UpdateComponentOrder(SelectedGroup.Id, componentList);
            }
            catch (RESTException e)
            {
                var errorMessage = String.Format("Error-Code: {0}\r\nMessage:{1}", e.Code, e.Message);
                Debug.WriteLine(errorMessage);
                Debug.WriteLine(e.StackTrace);
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.Message);
                Debug.WriteLine(e.StackTrace);
            }
        }



        #endregion

    }
    
    
}
