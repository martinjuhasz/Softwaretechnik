using System;
using System.Diagnostics;
using PikachuLib;
using PikachuLib.Models.Workflow;
using PikachuManager.ViewModel.DragNDrop;
using PikachuViewBase.Hilfsklassen;

namespace PikachuManager.ViewModel.Workflowitems
{
    /// <summary>
    /// ViewModel für ein WorkflowItem.
    /// Updated das Model und gibt Änderungen an die View weiter.
    /// Stellt Methoden für das WorkflowItem zur REST-Schnittstelle zur Verfügung.
    /// </summary>
    public abstract class WorkflowItemViewModel : CanvasObjectViewModel
    {
        // Properties für WorkflowItem
        #region properties

        /// <summary>
        /// Halte Clientsession, um auf akutellen Workflow und RelaxoClient zuzugreifen.
        /// </summary>
        private ClientSession clientSession;

        /// <summary>
        /// Das echte WorkflowItem-Model
        /// </summary>
        public WorkflowItem WorkflowItem { get; protected set; }

        private string image;
        /// <summary>
        /// Der Pfad zur Bild-Repräsentation des Workflowitems.
        /// </summary>
        public string Image {
            get
            {
                return image;
            }
            protected set {
                if (value != null)
                {
                    var prefix = "pack://application:,,,/PikachuManager;component/Resources/new/";
                    image = String.Concat(prefix, value);
                }
            }
        }

        /// <summary>
        /// Breite des WorkflowItem
        /// </summary>
        public int Width { get; protected set; }

        /// <summary>
        /// Höhe des WorkflowItem
        /// </summary>
        public int Height { get; protected set; }

        /// <summary>
        /// Breite der Border
        /// </summary>
        public int BorderWidth { get; protected set; }

        /// <summary>
        /// Gibt an, ob ein WorkflowItem gerade gedragged wird.
        /// </summary>
        public bool CurrentDragged { get; protected set; }

        /// <summary>
        /// Gibt an, ob ein WorkflowItem gerade editiert wird.
        /// </summary>
        public bool EditLocked { get; protected set; }

        /// <summary>
        /// Der Abstand zur linken Kante.
        /// </summary>
        public int Left
        {
            get { return WorkflowItem.Position.X; }
            set
            {
                if (WorkflowItem.Position.X != value)
                {
                    WorkflowItem.Position.X = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Der Abstand zur oberen Kante.
        /// </summary>
        public int Top
        {
            get { return WorkflowItem.Position.Y; }
            set
            {
                if (WorkflowItem.Position.Y != value)
                {
                    WorkflowItem.Position.Y = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// ID des Workflowitems.
        /// </summary>
        public int Id
        {
            get { return WorkflowItem.Id; }
            set
            {
                if (WorkflowItem.Id != value)
                {
                    WorkflowItem.Id = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Lock-Status des WorkflowItems.
        /// </summary>
        public bool Lock
        {
            get { return WorkflowItem.Lock; }
            set
            {
                if (WorkflowItem.Lock != value)
                {
                    WorkflowItem.Lock = value;
                    OnPropertyChanged();
                }
            }
        }
        
        #endregion

        #region constructor/destructor

        /// <summary>
        /// Destructor wird benötigt um aufzuräumen, wenn der Benutzer während dem Editieren auf schließen klickt.
        /// </summary>
        ~WorkflowItemViewModel()
        {
            if (EditLocked)
            {
                try
                {
                    Debug.Print("Workflowitem wird über den Destructor aufgeräumt.");
                    clientSession.RelaxoClient.UnlockWorkflowItem(WorkflowItem);
                }
                catch (Exception ex)
                {
                    Debug.Print("Fehler im Destructor vom WorkflowitemViewModel:");
                    Debug.Print(ex.Message);
                }
            }
        }

        protected WorkflowItemViewModel(WorkflowItem item)
        {
            WorkflowItem = item;
            clientSession = ClientSession.Instance;
            CurrentDragged = false;

            Width = 40;
            Height = 40;
            BorderWidth = 2;
        }

        #endregion
        
        #region REST-Anbindung (RelaxoClient)

        /// <summary>
        /// Locked das WorkflowItem.
        /// </summary>
        public void LockDrag()
        {
            if(EditLocked) return;

            try
            {
                CurrentDragged = true;
                clientSession.RelaxoClient.LockWorkflowItem(WorkflowItem);
            }
            catch (Exception e)
            {
                CurrentDragged = false;
                throw;
            }
        }

        /// <summary>
        /// Unlocked das WorkflowItem.
        /// </summary>
        public void UnlockDrag()
        {
            CurrentDragged = false;

            if(EditLocked) return;
            try
            {
                clientSession.RelaxoClient.UnlockWorkflowItem(WorkflowItem);
            }
            catch (Exception e)
            {
                Debug.WriteLine(e);
            }
        }

        /// <summary>
        /// Startet das bearbeiten eines Worfklowitems.
        /// </summary>
        public void StartEdit()
        {
            EditLocked = true;
            CurrentDragged = true;
            try
            {
                clientSession.RelaxoClient.LockWorkflowItem(WorkflowItem);
            }
            catch (Exception e)
            {
                Debug.WriteLine(e);
            }
        }


        /// <summary>
        /// Beendet das bearbeiten eines Workflowitems.
        /// </summary>
        public void EndEdit()
        {
            EditLocked = false;
            CurrentDragged = false;
            try
            {
                clientSession.RelaxoClient.UnlockWorkflowItem(WorkflowItem);
            }
            catch (Exception e)
            {
                Debug.WriteLine(e);
            }
        }

        /// <summary>
        /// Schickt die aktuelle Position des WorkflowItems zum Server.
        /// </summary>
        public void PushPositionToServer()
        {
            clientSession.RelaxoClient.SetWorkflowItemPosition(WorkflowItem);
        }

        /// <summary>
        /// Setzt ein NextItem des aktuellen WorkflowItems
        /// </summary>
        /// <param name="nextWorkflowItem">Das Item, dass NextItem werden soll</param>
        public void SetNextItem(WorkflowItem nextWorkflowItem)
        {
            try
            {
                WorkflowItem.NextItem.Add(nextWorkflowItem.Id);
                clientSession.RelaxoClient.SetWorkflowItemNextItem(WorkflowItem);
            }
            catch (Exception)
            {
                WorkflowItem.NextItem.Remove(nextWorkflowItem.Id);
                throw;
            }
        }

        public void DeleteNextItem(WorkflowItem itemToDel)
        {
            try
            {
                WorkflowItem.NextItem.Remove(itemToDel.Id);
                clientSession.RelaxoClient.SetWorkflowItemNextItem(WorkflowItem);
            }
            catch (Exception)
            {
                WorkflowItem.NextItem.Add(itemToDel.Id);
                throw;
            }
        }

        #endregion

        #region methods

        /// <summary>
        /// Läd Detailinformationen in das Viewmodel.
        /// </summary>
        public abstract void LoadDetails();

        /// <summary>
        /// Sendet die in dem ViewModel festgelegten Detailinformationen an den Server.
        /// </summary>
        public abstract void SaveDetails();

        #endregion
    }
}
