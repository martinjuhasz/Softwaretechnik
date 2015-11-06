using System.Windows.Input;
using PikachuLib;
using PikachuLib.Communication.Event;
using PikachuLib.Models.Workflow;
using PikachuManager.ViewModel.Workflowitems;
using PikachuViewBase.Hilfsklassen;
using System;
using System.Collections;
using System.Collections.ObjectModel;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.ComponentModel;
using System.Diagnostics;
using System.Threading;
using PikachuViewBase.ViewModel;
using System.Linq;
using System.Windows.Documents;
using PikachuLib.Communication;

namespace PikachuManager.ViewModel.DragNDrop
{
    /// <summary>
    /// ViewModel für das DesignerCanvas.
    /// Hier wird eine Liste mit WorkflowItemViewModels gehalten und Methoden angeboten diese zu manipulieren.
    /// </summary>
    public class DesignerCanvasViewModel : ViewModelBase
    {
        public delegate void OpenWorkflowitemDelegate(WorkflowItemViewModel item);

        /// <summary>
        /// Liste der WorkflowItems und Connections, die auf dem Canvas sind.
        /// </summary>
        public ObservableCollection<CanvasObjectViewModel> CurrentCanvasObjects { get; private set; }

        /// <summary>
        /// Command um ein Workflowitem zu öffnen.
        /// </summary>
        private readonly ICommand openWorkflowitemCommand;
        public ICommand OpenWorkflowitemCommand
        {
            get { return openWorkflowitemCommand; }
        }


        #region Connection & Selection Stuff
        /// <summary>
        /// WorkflowItem, welches zuerst mit der linken Maustaste geklickt wurde. 
        /// Verbindung wird von diesem zum nächsten geklickten Item gezogen.
        /// </summary>
        private CanvasObjectViewModel lastSelectedItem;

        /// <summary>
        /// ICommand, wird aufgerufen, wenn ein WorkflowItem oder das Canvas mit der linken Maustaste geklickt wird.
        /// </summary>
        public ICommand SelectItemCommand
        {
            get
            {
                return new ActionCommand(evaluateWorkflowItemsConnection);
            }
        }

        /// <summary>
        /// Verarbeitet den Rechtsklick auf das Canvas oder ein CanvasObjectViewModel.
        /// Selektiert ein CanvasObjectViewModel. 
        /// Fügt bei 2 maligem Klick auf WorkflowItems eine neue Verbindung ein und stößt einen Rest-Request an.
        /// </summary>
        /// <param name="o">Das angeklickte Objekt</param>
        public void evaluateWorkflowItemsConnection(object o)
        {
            Console.WriteLine("left-button-down!");
            var item = o as CanvasObjectViewModel;
            if (item != null)
            {
                // es wurde ein CanvasObjectViewModel getroffen
                if (lastSelectedItem != null)
                {
                    // wenn schon ein Element aktiv ist

                    if (item is WorkflowItemViewModel && lastSelectedItem is WorkflowItemViewModel)
                    {
                        
                        // zweiter Klick -> setze Verbindung, wenn erstes und zweites angeklicktes Element = WorkflowItemViewModel
                        try
                        {
                            var wfitem = item as WorkflowItemViewModel;
                            var wflastSelectedItem = lastSelectedItem as WorkflowItemViewModel;
                            wfitem.IsSelected = true;
                            wflastSelectedItem.SetNextItem(wfitem.WorkflowItem);
                            CurrentCanvasObjects.Add(new ConnectorViewModel(wflastSelectedItem, wfitem));
                            Console.WriteLine("two connected:{0} -> {1}", wflastSelectedItem.Id, wfitem.Id);
                        }
                        catch (RESTException e)
                        {
                            WorkflowEditorDesignerCanvasErrorViewModel.Instance.Error = e.Message;
                        }
                        catch (Exception)
                        {
                            WorkflowEditorDesignerCanvasErrorViewModel.Instance.Error = "Es ist ein Fehler aufgetreten. Bitte noch einmal versuchen.";
                        }
                        item.IsSelected = false;
                        lastSelectedItem.IsSelected = false;
                        lastSelectedItem = null;
                    }
                    else
                    {
                        // deselektiere vorheriges und selektiere neues
                        lastSelectedItem.IsSelected = false;
                        item.IsSelected = true;
                        lastSelectedItem = item;
                    }
                    
                }
                else
                {
                    // erster Klick -> merke angeklicktes WorkflowItemViewModel oder ConnectorViewModel
                    item.IsSelected = true;
                    lastSelectedItem = item;
                }
            }
            else
            {
                if (lastSelectedItem != null)
                {
                    lastSelectedItem.IsSelected = false;
                }
                lastSelectedItem = null;
            }
        }



        /// <summary>
        /// ICommand, wird aufgerufen, wenn die DELETE-Taste gedrückt wird.
        /// </summary>
        public ICommand DeleteItemCommand
        {
            get
            {
                return new ActionCommand(deleteItem);
            }
        }

        /// <summary>
        /// Stößt das Löschen des ausgewählten CanvasObjectViewModels an.
        /// Falls erfolgreich, wird das Element auch aus der Liste für die View gelöscht.
        /// </summary>
        /// <param name="o"></param>
        private void deleteItem(object o)
        {
            Debug.WriteLine("DEL!!!!");
            if (lastSelectedItem is WorkflowItemViewModel)
            {
                 DeleteWorkflowItemAndWorkflowItemViewModel((WorkflowItemViewModel)lastSelectedItem);
            }
            else if (lastSelectedItem is ConnectorViewModel)
            {
                // Wenn es eine Connection ist, dann lösche diese
                var connectorViewModel = (ConnectorViewModel)lastSelectedItem;
                try
                {
                    connectorViewModel.Start.DeleteNextItem(connectorViewModel.End.WorkflowItem);
                    CurrentCanvasObjects.Remove(lastSelectedItem);
                    lastSelectedItem = null;
                }
                catch (Exception)
                {
                    WorkflowEditorDesignerCanvasErrorViewModel.Instance.Error = "Die Verbindung konnte nicht gelöscht werden. Bitte noch einmal versuchen!";
                }
                
            }
        }


        #endregion


        #region constructors
        public DesignerCanvasViewModel()
        {
            CurrentCanvasObjects = new AsyncObservableCollection<CanvasObjectViewModel>();

            // rufe aktuellen Workflow ab
            if (ClientSession.Instance.CurrentWorkflow != null)
            {
                getWorkflowItemsToCollection(ClientSession.Instance.CurrentWorkflow);
            }

            // Registriere beim Event wenn sich der Workflow ändert
            ClientSession.Instance.CurrentWorkflowChangedEvent += WorkflowChanged;

            // registriere beim Event, wenn Workflow geupdated wird via Messaging
            ClientSession.Instance.WorkflowChangedForWorkflowEditorEvent += WorkflowUpdated;
        }

        public DesignerCanvasViewModel(OpenWorkflowitemDelegate openWorkflowitem) : this()
        {
            openWorkflowitemCommand = new ActionCommand(x =>
            {
                if (lastSelectedItem is WorkflowItemViewModel)
                {
                    openWorkflowitem((WorkflowItemViewModel) lastSelectedItem);
                    lastSelectedItem = null;
                }
                
            });
        }
        #endregion


        #region Messaging / Events / Workflowwechsel
        /// <summary>
        /// Wird beim WorkflowChangedEvent registriert und stößt dann das Laden des aktuellen
        /// Workflows an.
        /// </summary>
        /// <param name="arg">Eventspezifischer Parameter</param>
        private void WorkflowChanged(WorkflowChangedEventArgs arg)
        {
            CurrentCanvasObjects.Clear();

            lastSelectedItem = null;

            if (arg.Workflow == null)
            {
                return;
            }

            getWorkflowItemsToCollection(arg.Workflow);
        }

        /// <summary>
        /// Wird beim WorkflowChangedForWorkflowEditorEvent registriert und stößt dann das Laden des aktuellen
        /// Workflows an.
        /// </summary>
        /// <param name="arg">Eventspezifischer Parameter</param>
        private void WorkflowUpdated(WorkflowEventArgs arg)
        {
            updateWorkflowItemsInCollection(arg.Workflow);         
        }

        /// <summary>
        /// Hole die WorkflowItems des übergebenen = aktueller Workflows vom Server und updated
        /// sie in der ObservableCollection. Falls ein Item neu erstellt wurde, wird es hinzugefügt.
        /// Wenn ein Item gelöscht wurde, wird es gelöscht.
        /// </summary>
        /// <param name="currentWorkflow">Workflow, von dem die WorkflowItems abgerufen werden sollen.</param>
        private void updateWorkflowItemsInCollection(Workflow currentWorkflow)
        {
            // hole Liste der WorkflowItems vom Server
            var newWorkflowItems = ClientSession.Instance.RelaxoClient.GetWorkflowItemsForWorkflow(currentWorkflow);

            // erstelle Liste der IDs der neu gezogenen WorkflowItems
            var newWorkflowItemsIds = newWorkflowItems.Select(x => x.Id);
            // erstelle Liste der IDs der WorkflowItems auf dem Canvas
            var currentWorkflowItemIds = CurrentCanvasObjects.OfType<WorkflowItemViewModel>().Select(x => x.Id).ToList();
            

            // finde alle Items die gelöscht werden müssen
            var toRemoveWorkflowItemIds = currentWorkflowItemIds.Except(newWorkflowItemsIds).ToList();
            foreach (var toRemove in toRemoveWorkflowItemIds)
            {
                var toRemoveWorkflowItemViewModel = CurrentCanvasObjects.OfType<WorkflowItemViewModel>().SingleOrDefault(x => x.Id == toRemove);
                DeleteWorkflowItemViewModel(toRemoveWorkflowItemViewModel);
            }

            foreach (var workflowItem in newWorkflowItems)
            {
                // Matche bereits existierende WorkflowItems auf gesendete WorkflowItems. Wenn gefunden, dann aktualisiere Daten. Ansonsten erstelle neues ViewModel für WorkflowItem.
                var currentWorkflowItemViewModel = CurrentCanvasObjects.OfType<WorkflowItemViewModel>().SingleOrDefault(x => x.Id == workflowItem.Id);
                if (currentWorkflowItemViewModel != null)
                {
                    if (currentWorkflowItemViewModel.CurrentDragged == false)
                    {
                        currentWorkflowItemViewModel.Left = workflowItem.Position.X;
                        currentWorkflowItemViewModel.Top = workflowItem.Position.Y;
                        currentWorkflowItemViewModel.Lock = workflowItem.Lock;

                        
                        if (currentWorkflowItemViewModel is TaskViewModel && workflowItem is Task)
                        {
                            ((TaskViewModel) currentWorkflowItemViewModel).Name = ((Task) workflowItem).Name;
                        }
                    }
                }
                else
                {
                    // Füge neues WorkflowItem der Liste hinzu
                    currentWorkflowItemViewModel = WorkflowitemFactory.CreateWorkflowitemViewModel(workflowItem);
                    CurrentCanvasObjects.Add(currentWorkflowItemViewModel);
                }


                // crazy Connection Stuff
                // finde alle existierenden Connections von aktuellem WorkflowItem
                var currentConnectorViewModels = CurrentCanvasObjects.OfType<ConnectorViewModel>().Where(x => x.Start.WorkflowItem.Id == workflowItem.Id);
                // hole alle IDs der EndItems, bei dem das aktuelle WorkflowItem StartItem ist
                var tempEndConnectors = currentConnectorViewModels.Select(c => c.End.WorkflowItem.Id).ToList();

                // finde End IDs der Connections, die noch existieren, aber gelöscht werden müssen 
                var toRemoveConnectorViewModels = tempEndConnectors.Except(workflowItem.NextItem).ToList();
                foreach (var rem in toRemoveConnectorViewModels)
                {
                    // lösche ConnectorViewModel
                    var itemConnectorViewModel = CurrentCanvasObjects.OfType<ConnectorViewModel>().SingleOrDefault(x => x.End.WorkflowItem.Id == rem && x.Start.WorkflowItem.Id == workflowItem.Id);
                    CurrentCanvasObjects.Remove(itemConnectorViewModel);

                    // lösche NextItem aus Model von aktuellem WorkflowItem
                    itemConnectorViewModel.Start.WorkflowItem.NextItem.Remove(rem);
                }

                // finde EndIDs von Connections, die erzeugt werden müssen 
                var toCreateConnectorViewModels = workflowItem.NextItem.Except(tempEndConnectors).ToList();
                foreach (var toCreate in toCreateConnectorViewModels)
                {
                    // finde ViewModel von itemId
                    var nextWorkflowItemViewModel = CurrentCanvasObjects.OfType<WorkflowItemViewModel>().SingleOrDefault(x => x.Id == toCreate);
                    // wenn es noch nicht existiert -> erstelle
                    if (nextWorkflowItemViewModel == null)
                    {
                        nextWorkflowItemViewModel = WorkflowitemFactory.CreateWorkflowitemViewModel(workflowItem);
                        CurrentCanvasObjects.Add(nextWorkflowItemViewModel);
                    }
                    currentWorkflowItemViewModel.WorkflowItem.NextItem.Add(toCreate);
                    CurrentCanvasObjects.Add(new ConnectorViewModel(currentWorkflowItemViewModel, nextWorkflowItemViewModel));
                }
            }
        }

        /// <summary>
        /// Hole die WorkflowItems des übergebenen Workflows vom Server und füge sie
        /// der ObservableCollection CurrentCanvasObjects hinzu.
        /// </summary>
        /// <param name="currentWorkflow">Workflow, von dem die WorkflowItems abgerufen werden sollen.</param>
        private void getWorkflowItemsToCollection(Workflow currentWorkflow)
        {
            // hole Liste der WorkflowItems vom Server
            var workflowItems = ClientSession.Instance.RelaxoClient.GetWorkflowItemsForWorkflow(currentWorkflow);

            foreach (var workflowItem in workflowItems)
            {
                // erstelle das dazugehörige WorklowitemViewModel
                var workflowViewModel = WorkflowitemFactory.CreateWorkflowitemViewModel(workflowItem);
                CurrentCanvasObjects.Add(workflowViewModel);
            }

            // erstelle Connections zwischen WorkflowItems
            var workflowItemViewModelList = CurrentCanvasObjects.OfType<WorkflowItemViewModel>();
            var tempConnections = new List<ConnectorViewModel>();
            foreach (var currentWorkflowItemViewmodel in workflowItemViewModelList)
            {
                foreach (var nextItem in currentWorkflowItemViewmodel.WorkflowItem.NextItem)
                {
                    var nextWorkflowItemViewModel = workflowItemViewModelList.SingleOrDefault(x => x.Id == nextItem);
                    if (nextWorkflowItemViewModel != null)
                    {
                        tempConnections.Add(new ConnectorViewModel(currentWorkflowItemViewmodel, nextWorkflowItemViewModel));
                    }
                }
            }

            // füge Connections in ObservableCollection ein
            foreach (var connection in tempConnections)
            {
                CurrentCanvasObjects.Add(connection);
            }
        }
        #endregion


        #region Rest-Anbindung
        /// <summary>
        /// Fügt ein WorkflowItem vom Typ type an der übergebenen Position hinzu und schickt
        /// einen REST-Request um es auf dem Server zu erzeugen.
        /// </summary>
        /// <param name="type">Typ des WorkflowItems</param>
        /// <param name="left">Position auf dem Canvas</param>
        /// <param name="top">Position auf dem Canvas</param>
        public void AddWorkflowItem(Type type, int left, int top)
        {
            try
            {
                // erzeuge WorkflowItem vom Typ type
                var workflowItem = (WorkflowItem) Activator.CreateInstance(type);

                // erstelle das dazugehörige WorklowitemViewModel
                var workflowitemViewModel = WorkflowitemFactory.CreateWorkflowitemViewModel(workflowItem);

                // drop in der Mitte des Objekts
                workflowItem.Position.X = left - workflowitemViewModel.Width / 2;
                workflowItem.Position.Y = top - workflowitemViewModel.Height / 2;

                // erzeuge WorkflowItem auf Server und füge Id ein
                workflowItem =
                    ClientSession.Instance.RelaxoClient.CreateWorkflowItem(ClientSession.Instance.CurrentWorkflow,
                        workflowItem);

                

                CurrentCanvasObjects.Add(workflowitemViewModel);
            }
            catch (RESTException e1)
            {
                WorkflowEditorDesignerCanvasErrorViewModel.Instance.Error = e1.Message;
            }
            catch (Exception)
            {
                WorkflowEditorDesignerCanvasErrorViewModel.Instance.Error = "Es ist ein Fehler aufgetreten. Bitte noch einmal versuchen!";
            }
            
        }

        /// <summary>
        /// Löscht das übergebene WorkflowItemViewModel vom Server und stößt visualle Löschung an.
        /// </summary>
        /// <param name="item">WorkflowItemViewModel, das gelöscht werden soll</param>
        private void DeleteWorkflowItemAndWorkflowItemViewModel(WorkflowItemViewModel item)
        {
            try
            {
                // lösche vom Server
                ClientSession.Instance.RelaxoClient.DeleteWorkflowItem(item.WorkflowItem);

                // lösche alle ConnectionViewModels
                DeleteWorkflowItemViewModel(item);
            }
            catch (RESTException e1)
            {
                WorkflowEditorDesignerCanvasErrorViewModel.Instance.Error = e1.Message;
            }
            catch (Exception e)
            {
                WorkflowEditorDesignerCanvasErrorViewModel.Instance.Error = "Beim Löschen ist ein Fehler aufgetreten. Bitte noch einmal versuchen!";
            }
        }

        /// <summary>
        /// Löscht das übergebene WorkflowItemViewModel aus der Liste CurrentCanvasObjects und prüft
        /// ob noch Verbindungen mit dem WorkflowItem bestehen und löscht diese ebenfalls und updated 
        /// die Models entsprechend.
        /// </summary>
        /// <param name="item">WorkflowItemViewModel, das gelöscht werden soll</param>
        private void DeleteWorkflowItemViewModel(WorkflowItemViewModel item)
        {
            // lösche aus View
            CurrentCanvasObjects.Remove(item);

            // finde alle Connections von aktuellem Workflowitem
            var startConnectorViewModels =
                CurrentCanvasObjects.OfType<ConnectorViewModel>().Where(x => x.Start.WorkflowItem.Id == item.Id).ToList();
            foreach (var rem in startConnectorViewModels)
            {
                CurrentCanvasObjects.Remove(rem);
            }

            // finde alle Connections, die auf aktuelles WorkflowItem zeigen
            var endConnectorViewModels =
                CurrentCanvasObjects.OfType<ConnectorViewModel>().Where(x => x.End.WorkflowItem.Id == item.Id).ToList();
            foreach (var rem in endConnectorViewModels)
            {
                rem.Start.WorkflowItem.NextItem.Remove(item.Id);
                CurrentCanvasObjects.Remove(rem);
            }

            // lösche Model & ViewModel
            item = null;
            lastSelectedItem = null;
        }

        #endregion

    }



    #region AsyncObservableCollection
    /// <summary>
    /// Asynchrone Observable Collection, damit Collection auch vom Messaging Thread geupdatet werden kann.
    /// http://www.thomaslevesque.com/2009/04/17/wpf-binding-to-an-asynchronous-collection/
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public class AsyncObservableCollection<T> : ObservableCollection<T>
    {
        private SynchronizationContext _synchronizationContext = SynchronizationContext.Current;

        public AsyncObservableCollection()
        {
        }

        public AsyncObservableCollection(IEnumerable<T> list)
            : base(list)
        {
        }

        protected override void OnCollectionChanged(NotifyCollectionChangedEventArgs e)
        {
            if (SynchronizationContext.Current == _synchronizationContext)
            {
                // Execute the CollectionChanged event on the current thread
                RaiseCollectionChanged(e);
            }
            else
            {
                // Raises the CollectionChanged event on the creator thread
                _synchronizationContext.Send(RaiseCollectionChanged, e);
            }
        }

        private void RaiseCollectionChanged(object param)
        {
            // We are in the creator thread, call the base implementation directly
            base.OnCollectionChanged((NotifyCollectionChangedEventArgs)param);
        }

        protected override void OnPropertyChanged(PropertyChangedEventArgs e)
        {
            if (SynchronizationContext.Current == _synchronizationContext)
            {
                // Execute the PropertyChanged event on the current thread
                RaisePropertyChanged(e);
            }
            else
            {
                // Raises the PropertyChanged event on the creator thread
                _synchronizationContext.Send(RaisePropertyChanged, e);
            }
        }

        private void RaisePropertyChanged(object param)
        {
            // We are in the creator thread, call the base implementation directly
            base.OnPropertyChanged((PropertyChangedEventArgs)param);
        }
    }
    #endregion
}
