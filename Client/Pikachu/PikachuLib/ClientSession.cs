using System;
using System.Collections.ObjectModel;
using PikachuLib.Communication;
using PikachuLib.Models.Usermanagement;
using PikachuLib.Models.Workflow;
using PikachuLib.Communication.Event;
using System.Collections.Generic;
using System.Threading;
using System.Windows;


namespace PikachuLib
{

    public delegate void CurrentWorkflowChangedHandler(WorkflowChangedEventArgs arg);
    public delegate void CurrentWorkflowStateChangedHandler(WorkflowEventArgs arg);
    public delegate void WorkflowChangedForContentEditorHandler(WorkflowEventArgs arg);
    public delegate void WorkflowChangedForWorkflowEditorHandler(WorkflowEventArgs arg);
    public delegate void UserUserGroupUpdatedHandler(EventArgs arg);
    public delegate void UserInvalidHandler(EventArgs arg);

    /// <summary>
    /// Alle Eigenschaften die für die aktuelle Session notwendig sind, werden hier gehalten.
    /// </summary>
    public class ClientSession
    {
        #region properties

        public event CurrentWorkflowChangedHandler CurrentWorkflowChangedEvent;
        public event CurrentWorkflowStateChangedHandler CurrentWorkflowStateChangedEvent;
        public event WorkflowChangedForContentEditorHandler WorkflowChangedForContentEditorEvent;
        public event WorkflowChangedForWorkflowEditorHandler WorkflowChangedForWorkflowEditorEvent;
        public event UserUserGroupUpdatedHandler UserUserGroupUpdatedEvent;
        public event UserInvalidHandler UserInvalidEvent;

        /// <summary>
        /// Verweis auf den MainThread
        /// </summary>
        private SynchronizationContext applicationContext = null;

        private User user;
        /// <summary>
        /// Gibt den aktuell angemeldeten Benutzer zurück.
        /// </summary>
        public User User
        {
            get { return user; }
            set { user = value; }
        }

        /// <summary>
        /// Gibt den verwendeten Client zur REST-Kommunikation zurück.
        /// </summary>
        public RelaxoClient RelaxoClient { get; set; }

        /// <summary>
        /// Gibt den verwendeten Client für die Messaging-Kommunikation zurück.
        /// </summary>
        private MessagingClient messageClient;
        public MessagingClient MessageClient { 
            get { return messageClient; }
            set 
            {
                messageClient = value;
                messageClient.AddWorkflowsChangedConsumer(WorkflowsUpdated);
            }
        }


       

        private Workflow currentWorkflow;
        /// <summary>
        /// Der aktuelle Workflow, der in bearbeitung ist.
        /// </summary>
        public Workflow CurrentWorkflow
        {
            get { return currentWorkflow; }
            set 
            {
                var tmp = currentWorkflow;
                currentWorkflow = value;
                if (CurrentWorkflowChangedEvent != null)
                {
                    CurrentWorkflowChangedEvent(new WorkflowChangedEventArgs(this, tmp, currentWorkflow));
                }
            }
        }

        /// <summary>
        /// Liste der verfügbaren Workflows, auf die der Benutzer zugriff hat.
        /// </summary>
        public ObservableCollection<Workflow> Workflows { get; set; }

        /// <summary>
        /// Singleton Instanz-Variable.
        /// </summary>
        private static ClientSession instance;

        /// <summary>
        /// Gibt die Instanz der aktuellen GUI-Session zurück (Singleton).
        /// </summary>
        public static ClientSession Instance
        {
            get { return instance ?? (instance = new ClientSession()); }
        }

        #endregion

        #region constructor

        private ClientSession()
        {
            Workflows = new ObservableCollection<Workflow>();
            applicationContext = SynchronizationContext.Current;
        }

        #endregion

        #region methods

        /// <summary>
        /// Entfernt alle Listener 
        /// </summary>
        public void ResetListeners()
        {
            CurrentWorkflowChangedEvent = null;
            CurrentWorkflowStateChangedEvent = null;
            WorkflowChangedForContentEditorEvent = null;
            WorkflowChangedForWorkflowEditorEvent = null;
            UserUserGroupUpdatedEvent = null;
        }

        /// <summary>
        /// Methode zum befüllen der Workflow-Liste.
        /// Läd alle Workflows eines eingeloggten Users
        /// </summary>
        public void UpdateWorkflows(Workflow selectNewWorkflow)
        {
            var newWorkflows = RelaxoClient.GetWorkflows();

            // wenn es keine neuen Workflows gibt, leeren
            if (newWorkflows.Count <= 0)
            {
                Workflows.Clear();
                return;
            }

            // wenn es keine momentanen workflows gibt, einfach neue workflows nehmen
            if (Workflows.Count <= 0)
            {
                addWorkflows(newWorkflows);
                CurrentWorkflow = Workflows[0];
                return;
            }


            // gelöschte Workflows aus vorhandenen entfernen
            List<Workflow> toRemoveOldWorkflows = new List<Workflow>();
            foreach (Workflow oldWorkflow in Workflows)
            {
                // TODO: lieber equals überschreiben
                bool foundWorkflow = false;
                foreach (Workflow newWorkflow in newWorkflows)
                {
                    if (newWorkflow.Id == oldWorkflow.Id)
                    {
                        foundWorkflow = true;
                        break;
                    }
                }

                if (foundWorkflow == false)
                {
                    toRemoveOldWorkflows.Add(oldWorkflow);
                }
            }
            RemoveWorkflows(toRemoveOldWorkflows);
            

            // ansonsten schon vorhandene aus liste löschen
            foreach (Workflow oldWorkflow in Workflows)
            {
                // TODO: lieber equals überschreiben
                List<Workflow> toRemove = new List<Workflow>();
                foreach (Workflow newWorkflow in newWorkflows)
                {
                    if (newWorkflow.Id == oldWorkflow.Id)
                    {
                        toRemove.Add(newWorkflow);
                    }
                }
                
                // remove
                foreach (Workflow toRemoveWorkflow in toRemove)
                {
                    newWorkflows.Remove(toRemoveWorkflow);
                }

            }

            // fertig, wenn es keine neuen Workflows gibt
            if (newWorkflows.Count <= 0)
            {
                return;
            }

            // ansonsten neue Workflows adden
            addWorkflows(newWorkflows);

            // neuen Workflow auswählen, falls gesetzt
            if (selectNewWorkflow != null)
            {
                foreach (Workflow workflow in Workflows)
                {
                    // Contain/Equals geht nicht, da `selectNewWorkflow` nur ein Dummy von der REST-Response ist
                    if (workflow.Id == selectNewWorkflow.Id)
                    {
                        CurrentWorkflow = workflow;
                    }
                }
            }

        }

        /// <summary>
        /// Entfernt mehrere Workflows sicher
        /// </summary>
        /// <param name="workflows">Liste der zu löschenden Workflows</param>
        public void RemoveWorkflows(List<Workflow> workflows)
        {
            foreach (Workflow toRemoveWorkflow in workflows)
            {
                RemoveWorkflow(toRemoveWorkflow);
            }
        }

        /// <summary>
        /// Entfernt einen Workflow sicher.
        /// </summary>
        /// <param name="workflow">Der zu löschenden Workflow</param>
        public void RemoveWorkflow(Workflow workflow)
        {
            if (workflow.Equals(CurrentWorkflow))
            {
                CurrentWorkflow = null;
            }
            Workflows.Remove(workflow);
        }

        /// <summary>
        /// Fügt einen Workflow der Workflowliste hinzu
        /// </summary>
        /// <param name="workflows"></param>
        private void addWorkflows(List<Workflow> workflows)
        {
            foreach (var workflow in workflows)
            {
                Workflows.Add(workflow);
                SubscribeToWorkflow(workflow);
            }
        }

        /// <summary>
        /// Meldet das System an allen UserEditor releavanten Messages an
        /// </summary>
        public void SubscribeToUserMessages()
        {
            MessageClient.AddUserEditorConsumer(UserUserGroupUpdated);
        }

        /// <summary>
        /// Meldet das System an allen relevanten Workflow bezogenen Messages an
        /// </summary>
        /// <param name="workflow"></param>
        public void SubscribeToWorkflow(Workflow workflow)
        {
            if (user == null)
            {
                return;
            }

            if (user.IsAdmin)
            {
                MessageClient.AddWorkflowContentEditorConsumer(workflow.Id, WorkflowUpdatedForContentEditor);
                MessageClient.AddWorkflowEditorConsumer(workflow.Id, WorkflowUpdatedForWorkflowEditor);
            }
            else
            {
                MessageClient.AddWorkflowClientConsumer(workflow.Id, WorkflowUpdated);
            }

        }

        /// <summary>
        /// Löscht den aktuellen Benutzer und feuert das UserInvalidEvent ab.
        /// </summary>
        public void SetUserInvalid()
        {
            User = null;
            if (UserInvalidEvent != null)
            {
                UserInvalidEvent(new EventArgs());
            }
        }

        #endregion

        #region Event-Delegates

        /// <summary>
        /// Event-Delegate wenn ein Workflow aktualisiert wurde
        /// </summary>
        /// <param name="m"></param>
        private void WorkflowUpdated(EventArgs m)
        {
            WorkflowMessageEventArgs message = (WorkflowMessageEventArgs)m;

            if (currentWorkflow.Id == message.WorkflowId)
            {
                if (CurrentWorkflowStateChangedEvent != null)
                    CurrentWorkflowStateChangedEvent(new WorkflowEventArgs(this, currentWorkflow));
            }
        }

        /// <summary>
        /// Event-Delegate wenn ein Workflow innerhalb des Content-Editors aktualisiert wurde
        /// </summary>
        /// <param name="m"></param>
        private void WorkflowUpdatedForContentEditor(EventArgs m)
        {
            WorkflowMessageEditorEventArgs message = (WorkflowMessageEditorEventArgs)m;

            // Schicke Update Event: Wenn aktueller Workflow == Workflow und aktueller Benutzer != Benutzer der Änderung
            if (currentWorkflow.Id == message.WorkflowId && User.Id != message.UserId)
            {
                if (WorkflowChangedForContentEditorEvent != null)
                    WorkflowChangedForContentEditorEvent(new WorkflowEventArgs(this, currentWorkflow));
            }
        }

        /// <summary>
        /// Event-Delegate wenn ein Workflow innerhalb des Workflow-Editors aktualisiert wurde
        /// </summary>
        /// <param name="m"></param>
        private void WorkflowUpdatedForWorkflowEditor(EventArgs m)
        {
            WorkflowMessageEditorEventArgs message = (WorkflowMessageEditorEventArgs)m;

            // Schicke Update Event: Wenn aktueller Workflow == Workflow und aktueller Benutzer != Benutzer der Änderung
            if (currentWorkflow.Id == message.WorkflowId && User.Id != message.UserId)
            {
                if (WorkflowChangedForWorkflowEditorEvent != null)
                    WorkflowChangedForWorkflowEditorEvent(new WorkflowEventArgs(this, currentWorkflow));
            }
        }

        #endregion

        #region Message-Delegates

        /// <summary>
        /// Message-Delegate wenn eine User-Gruppe aktualisiert wurde
        /// </summary>
        /// <param name="m"></param>
        private void UserUserGroupUpdated(EventArgs m)
        {
            UserUserGroupUpdatedEvent(m);
        }
        
        /// <summary>
        /// Message-Delegate wenn Workflows aktualisiert wurden
        /// </summary>
        /// <param name="m"></param>
        private void WorkflowsUpdated(EventArgs m)
        {
            WorkflowsChangedEventArgs message = (WorkflowsChangedEventArgs)m;
            
            if (User.Id != message.UserId)
            {
                applicationContext.Post(UpdateWorkflowsFromOtherThread, null);
            }
        }

        /// <summary>
        /// Methoden-Wrapper für die UpdateWorkflows Methode um diese über "applicationContext.Post" aufrufen zu können
        /// </summary>
        /// <param name="obj"></param>
        private void UpdateWorkflowsFromOtherThread(object obj)
        {
            UpdateWorkflows(null);
        }

        /// <summary>
        /// Message-Delegate wenn ein Workflow hinzugefügt wurde
        /// </summary>
        /// <param name="workflow"></param>
        public void WorkflowAdded(Workflow workflow)
        {
            UpdateWorkflows(workflow);
        }

       
        #endregion
    }
}
