using System.Collections.ObjectModel;
using System.Linq;
using PikachuLib.Communication.Models;
using PikachuLib.Communication.Models.Job;
using PikachuLib.Communication.Models.Usermanagement;
using PikachuLib.Communication.Models.Workflow;
using PikachuLib.Communication.Request.Job;
using PikachuLib.Communication.Request.Script;
using PikachuLib.Communication.Request.Session;
using PikachuLib.Communication.Request.Start;
using PikachuLib.Communication.Request.Task;
using PikachuLib.Communication.Request.UserManagement;
using PikachuLib.Communication.Request.Workflow;
using PikachuLib.Communication.Request.WorkflowDecision;
using PikachuLib.Communication.Request.Workflowitem;
using PikachuLib.Models.Job;
using PikachuLib.Models.TaskComponent;
using PikachuLib.Models.Usermanagement;
using PikachuLib.Models.Workflow;
using RestSharp;
using System;
using System.Collections.Generic;
using PikachuLib.Communication.Models.Request;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Net;
using System.Diagnostics;
using Position = PikachuLib.Models.Workflow.Position;
using PikachuLib.Communication.Request.FormGroup;
using PikachuLib.Communication.Models.FormGroup;

namespace PikachuLib.Communication
{   
    /// <summary>
    /// Klasse zur REST Kommunikation zwischen Client und Server.
    /// </summary>
    public class RelaxoClient
    {
        #region properties

        /// <summary>
        /// REST-Client für die Kommunikation.
        /// </summary>
        private readonly IRestClient client;

        /// <summary>
        /// Der Aktive Benutzer dieser Sitzung.
        /// </summary>
        public User ActiveUser { get; private set; }

        #endregion

        #region constructor

        public RelaxoClient(string restServerUrl)
        {
            client = new RestClient(restServerUrl);
        }

        public RelaxoClient(IRestClient client)
        {
            this.client = client;
        }

        #endregion

        #region Session-Requests
        /// <summary>
        /// Methode zum Einloggen auf dem Server via REST
        /// </summary>
        /// <param name="user">Benutzer</param>
        /// <param name="pw">Passwort</param>
        /// <returns>Gibt ein User-Objekt zurück</returns>
        public User LogIn(string user, string pw)
        {
            var sessionResponse = RestRequest<SessionResponse>(new GetSessionRequest(user, pw));
            if (sessionResponse.Token == null)
            {
                return null;
            }

            ActiveUser = new User(user, sessionResponse.Id, sessionResponse.Token, sessionResponse.IsAdmin);
            return ActiveUser;
        }

        /// <summary>
        /// Methode zum Ausloggen via REST.
        /// </summary>
        public void Logout()
        {
            RestRequest<SessionResponse>(new DeleteSessionRequest());
            ActiveUser = null;
        }

        #endregion

        #region Usermanagement-Request

        /// <summary>
        /// Methode zum Abrufen eines Users
        /// </summary>
        /// <param name="userId">Die ID des Users</param>
        /// <returns>Gibt einen User zurück</returns>
        public User GetUserById(int userId) {

            var userResponse = RestRequest<UserResponse>(new GetUserRequest(userId));

            var user = new User(userResponse.Id, userResponse.Username, userResponse.Password, userResponse.Prename, userResponse.Name, userResponse.IsAdmin, userResponse.IsActive);

            return user;
        }

        /// <summary>
        /// Methode zum Abrufen aller User
        /// </summary>
        /// <returns>Gibt eine Liste mit allen Usern zurück</returns>
        public List<User> GetAllUsers()
        {
            var users= new List<User>();
            var requestResult = RestRequest<List<UserResponse>>(new GetAllUserRequest());
            foreach (var userResponse in requestResult)
            {
                users.Add(new User(userResponse.Id, userResponse.Username, userResponse.Prename, userResponse.Name, userResponse.IsAdmin, userResponse.IsActive));
            }

            return users;
        }

        /// <summary>
        /// Methode zum erstellen eines Users
        /// </summary>
        /// <param name="user">Der User, der erstellt werden soll</param>
        /// <returns></returns>
        public User CreateUser(User user)
        {
            var createUserResponse = RestRequest<CreateUserResponse>(new CreateUserRequest(user));
            user.Id = createUserResponse.Id;

            return user;
        }

        /// <summary>
        /// Methode zum updaten eines Users
        /// </summary>
        /// <param name="user">Der User, der geupdated werden soll</param>
        /// <returns></returns>
        public void UpdateUser(User user)
        {
            var updateUserResponse = RestRequest<UpdateUserResponse>(new UpdateUserRequest(user));
        }

        /// <summary>
        /// Methode zum Abrufen der UserGroups eines Users
        /// </summary>
        /// <returns>Gibt eine Liste mit UserGroups eines Users zurück</returns>
        public List<UserGroup> GetUserGroupsForUser(int userId)
        {
            var usergroups = new List<UserGroup>();
            var requestResult = RestRequest<List<UsergroupResponse>>(new GetUserGroupsForUserRequest(userId));
            foreach (var usergroupResponse in requestResult)
            {
                usergroups.Add(new UserGroup(usergroupResponse.Id, usergroupResponse.Name));
            }

            return usergroups;
        }

        /// <summary>
        /// Methode zum Abrufen der UserGroups
        /// </summary>
        /// <returns>Gibt eine Liste mit UserGroups zurück</returns>
        public List<UserGroup> GetAllUserGroups()
        {
            var usergroups = new List<UserGroup>();
            var requestResult = RestRequest<List<UsergroupResponse>>(new GetUsergroupsRequest());
            foreach (var usergroupResponse in requestResult)
            {
                usergroups.Add(new UserGroup(usergroupResponse.Id, usergroupResponse.Name));
            }

            return usergroups;
        }

        /// <summary>
        /// Methode zum Abrufen der User einer UserGroup
        /// </summary>
        /// <returns>Gibt eine Liste mit Usern zurück</returns>
        public List<User> GetUsersByUserGroup(int groupId)
        {
            var users = new List<User>();
            var requestResult = RestRequest<List<UserResponse>>(new GetUsersByUserGroupRequest(groupId));
            foreach (var userResponse in requestResult)
            {
                users.Add(new User(userResponse.Id, userResponse.Username, userResponse.Password, userResponse.Prename, userResponse.Name, userResponse.IsAdmin, userResponse.IsActive));
            }

            return users;
        }

        /// <summary>
        /// Methode zum hinzufügen eines Users zu einer UserGroup
        /// </summary>
        /// <param name="groupId">Id der UserGroup</param>
        /// <param name="userId">Id des Users</param>
        public void AddUserToUserGroup(int groupId, int userId)
        {
            // Es wird kein Rückgabewert erwartet
            RestRequest<Object>(new AddUserToUserGroupRequest(groupId, userId));
        }

        /// <summary>
        /// Methode zum entfernen eines Users aus einer UserGroup
        /// </summary>
        /// <param name="groupId">Id der UserGroup</param>
        /// <param name="userId">Id des Users</param>
        public void RemoveUserFromUserGroup(int groupId, int userId)
        {
            // Es wird kein Rückgabewert erwartet
            RestRequest<Object>(new RemoveUserFromUserGroupRequest(groupId, userId));
        }

        /// <summary>
        /// Methode zum erstellen einer UserGroup
        /// </summary>
        /// <param name="usergroup">Die UserGroup, die erstellt werden soll</param>
        /// <returns></returns>
        public UserGroup CreateUserGroup(UserGroup usergroup)
        {
            var createUserGroupResponse = RestRequest<CreateUserGroupResponse>(new CreateUserGroupRequest(usergroup));
            usergroup.Id = createUserGroupResponse.Id;

            return usergroup;
        }

        #endregion

        #region Workflow-Requests
        /// <summary>
        /// Methode zum erstellen eines Workflows
        /// </summary>
        /// <returns>Den erstellten Workflow</returns>
        public Workflow CreateWorkflow(string name)
        {
            var workflowResponse = RestRequest<CreateWorkflowResponse>(new CreateWorkflowRequest(name));

            var workflow = new Workflow(workflowResponse.WorkflowId, workflowResponse.WorkflowName);
            return workflow;
        }

        /// <summary>
        /// Methode zum löschen eines Workflows
        /// </summary>
        /// <param name="workflow">Das entsprechende Workflow Objekt</param>
        public void DeleteWorkflow(Workflow workflow)
        {
            // Es wird kein Rückgabewert erwartet
            RestRequest<Object>(new DeleteWorkflowRequest(workflow.Id));
        }

        /// <summary>
        /// Methode zum unlocken eines Workflows
        /// </summary>
        /// <param name="workflow">Das entsprechende Workflow Objekt</param>
        public void UnlockWorkflow(Workflow workflow)
        {
            // Es wird kein Rückgabewert erwartet
            RestRequest<Object>(new UnlockWorkflowRequest(workflow.Id));
        }

        /// <summary>
        /// Methode zum locken eines Workflows
        /// </summary>
        /// <param name="workflow">Das entsprechende Workflow Objekt</param>
        public void LockWorkflow(Workflow workflow)
        {
            // Es wird kein Rückgabewert erwartet
            RestRequest<Object>(new LockWorkflowRequest(workflow.Id));
        }

        /// <summary>
        /// Methode zum Abrufen der Workflows eines Benutzers
        /// </summary>
        /// <returns>Gibt eine Liste mit Workflow-Objekten zurück</returns>
        public List<Workflow> GetWorkflows()
        {
            var workflowsResponse = RestRequest<List<WorkflowsResponse>>(new GetWorkflowsRequest());

            // Wandele WorkflowResponse-Objekte in Workflow-Objekte um
            var workflowsList = new List<Workflow>();
            foreach (var wRes in workflowsResponse)
            {
                workflowsList.Add(new Workflow(wRes.Id, wRes.Name, wRes.UserCanCreateJob));
            }

            return workflowsList;
        }

        /// <summary>
        /// Methode zum erstellen eines Jobs für den entsprechenden Workflow
        /// </summary>
        /// <param name="workflow">Das entsprechende Workflow Objekt</param>
        /// <returns></returns>
        public CreateJobResponse CreateJob(Workflow workflow)
        {
            var createJobResponse = RestRequest<CreateJobResponse>(new CreateJobRequest(workflow.Id));

            return createJobResponse;
        }

        /// <summary>
        /// Methode zum erstellen eines WorkflowItems
        /// </summary>
        /// <param name="workflow"></param>
        /// <param name="workflowItem">Das WorkflowItem, dass erstellt werden soll</param>
        /// <returns></returns>
        public WorkflowItem CreateWorkflowItem(Workflow workflow, WorkflowItem workflowItem)
        {
            var workflowItemResponse = RestRequest<CreateWorkflowItemResponse>(new CreateWorkflowItemRequest(workflow.Id, workflowItem));
            workflowItem.Id = workflowItemResponse.WorkflowItemId;

            return workflowItem;
        }

        /// <summary>
        /// Methode zum Abrufen aller WorkflowItems eines Workflows.
        /// </summary>
        /// <param name="workflow">Das entsprechende Workflow Objekt</param>
        /// <returns>Liste mit WorkflowItems für den übergebenen Workflow</returns>
        public List<WorkflowItem> GetWorkflowItemsForWorkflow(Workflow workflow)
        {
            var response = RestRequest<List<WorkflowitemResponse>>(new GetWorkflowItemsForWorkflowRequest(workflow.Id));

            var workflowItems = new List<WorkflowItem>();

            // durchlaufe alle empfangenen WorkflowItems
            foreach (var res in response) 
            {
                var id = res.Id;
                var position = new Position(res.Position.X, res.Position.Y);
                // Erstelle je nach Typ das entsprechende Objekt mit Details
                switch(res.Type) 
                {
                    case WorkflowItemType.START:
                        // workflowItems.Add(new Start(id, position, (int)res.Details.NextItem));
                        workflowItems.Add(new Start(id, position, res.Lock, res.NextItem));
                        break;
                    case WorkflowItemType.END:
                        workflowItems.Add(new End(id, position, res.Lock, res.NextItem));
                        break;
                    case WorkflowItemType.TASK:
                        // workflowItems.Add(new Task(id, position, (int)res.Details.NextItem));
                        workflowItems.Add(new Task(id, position, res.Lock, res.NextItem, res.Name));

                        break;
                    case WorkflowItemType.DECISION:
                        workflowItems.Add(new Decision(id, position, res.Lock, res.NextItem));
                        break;
                    case  WorkflowItemType.SCRIPT:
                        workflowItems.Add(new Script(id, position, res.Lock, res.NextItem));
                        break;
                    case WorkflowItemType.FORK:
                        workflowItems.Add(new Fork(id, position, res.Lock, res.NextItem));
                        break;
                    case WorkflowItemType.JOIN:
                        workflowItems.Add(new Join(id, position, res.Lock, res.NextItem));
                        break;
                }
            }

            return workflowItems;
        }

        /// <summary>
        /// Methode zum Abrufen der Tasks eines Workflows
        /// </summary>
        /// <param name="workflow">Das Workflow-Objekt</param>
        /// <returns>Gibt eine Liste mit Task-Objekten zurück</returns>
        public List<Task> GetTasks(Workflow workflow)
        {
            return GetTasks(workflow.Id);
        }

        /// <summary>
        /// Methode zum Abrufen der Tasks eines Workflows
        /// </summary>
        /// <param name="workflowId">Die ID des Workflows</param>
        /// <returns>Gibt eine Liste mit Task-Objekten zurück</returns>
        public List<Task> GetTasks(int workflowId)
        {
            var tasksResponse = RestRequest<List<TasksResponse>>(new GetTasksRequest(workflowId));

            // Wandele TaskResponse-Objekte in Task-Objekte um 
            var tasksList = new List<Task>();
            foreach (var tRes in tasksResponse)
            {
                tasksList.Add(new Task(tRes.Id, tRes.Name));
            }

            return tasksList;
        }

        #endregion

        #region Workflowitems-Requests
        /// <summary>
        /// Methode zum löschen eines WorkflowItems
        /// </summary>
        /// <param name="workflowItem">Das entsprechende WorkflowItem Objekt</param>
        public void DeleteWorkflowItem(WorkflowItem workflowItem)
        {
            // Es wird kein Rückgabewert erwartet
            RestRequest<Object>(new DeleteWorkflowItemRequest(workflowItem.Id));
        }

        /// <summary>
        /// Methode zum Sperren eines WorkflowItems
        /// </summary>
        /// <param name="workflowItem">Das entsprechende WorkflowItem Objekt</param>
        public void LockWorkflowItem(WorkflowItem workflowItem)
        {
            // Es wird kein Rückgabewert erwartet
            RestRequest<Object>(new LockWorkflowItemRequest(workflowItem.Id));
        }

        /// <summary>
        /// Methode zum entsperren eines WorkflowItems
        /// </summary>
        /// <param name="workflowItem">Das entsprechende WorkflowItem Objekt</param>
        public void UnlockWorkflowItem(WorkflowItem workflowItem)
        {
            // Es wird kein Rückgabewert erwartet
            var unlockRequest = new LockWorkflowItemRequest(workflowItem.Id);
            unlockRequest.Method = Method.DELETE;
            RestRequest<Object>(unlockRequest);
        }

        /// <summary>
        /// Methode zum Setzen der Position eines WorkflowItems
        /// </summary>
        /// <param name="workflowItem">Das entsprechende WorkflowItem Objekt</param>
        public void SetWorkflowItemPosition(WorkflowItem workflowItem)
        {
            // Es wird kein Rückgabewert erwartet
            RestRequest<Object>(new SetWorkflowItemPositionRequest(workflowItem));
        }

        /// <summary>
        /// Methode zum Setzen des nächsten WorkflowItems des aktuellen WorkflowItems
        /// </summary>
        /// <param name="workflowItem">Das entsprechende WorkflowItem Objekt</param>
        public void SetWorkflowItemNextItem(WorkflowItem workflowItem)
        {
            // Es wird kein Rückgabewert erwartet
            RestRequest<Object>(new SetWorkflowItemNextItemRequest(workflowItem));
        }

        #endregion
        
        #region WorkflowStart-Requests

        /// <summary>
        /// Ruft ein Workflowitem des Typs Start vom Server ab.
        /// </summary>
        /// <param name="id">Die ID des Start-Workflowitems.</param>
        /// <returns>Start-Workflowitem.</returns>
        public Start GetWorkflowStart(int id)
        {
            var response = RestRequest<Start>(new GetStartRequest(id));
            return Start.Parse(response);
        }

        /// <summary>
        /// Speichert Änderungen von einem Start-Workflowitem auf dem Server.
        /// </summary>
        /// <param name="start">Das Start-Worklfowitem.</param>
        public void UpdateWorkflowStart(Start start)
        {
            var request = new UpdateStartRequest(start.Id) {Usergroups = start.Usergroups};
            RestRequest<Object>(request);
        }

        #endregion

        #region Task-Requests

        /// <summary>
        /// Ruft ein Workflowitem des Typs Task vom Server ab.
        /// </summary>
        /// <param name="id">Die ID des Task-Workflowitems.</param>
        /// <returns>Task-Workflowitem.</returns>
        public Task GetTask(int id)
        {
            var taskResponse = RestRequest<TasksResponse>(new GetTaskDetailRequest(id));

            // Wandele TaskResponse-Objekte in Task-Objekte um
            var task = Task.Parse(taskResponse);

            return task;
        }
        
        /// <summary>
        /// Speichert Änderungen eines Task auf dem Server.
        /// </summary>
        /// <param name="id">Die ID des Tasks.</param>
        /// <param name="name">Der Name des Tasks.</param>
        /// <param name="groupIds">Die Benutzergruppen-IDs, die auf den Task zugriff haben sollen.</param>
        /// <param name="taskComponents">Die TaskComponents, die dem Task zugeordnet werden sollen.</param>
        public void UpdateTask(int id, string name, List<int> groupIds, List<TaskComponent> taskComponents)
        {   
            var request = new UpdateTaskRequest(id);
            
            // Fügt alle TaskComponents id der Liste hinzu.
            var components = new List<UpdateTaskComponentToTaskRequest>();
            components.AddRange(from c in taskComponents select new UpdateTaskComponentToTaskRequest(c));
            request.TaskComponents = components;


            request.Usergroups = groupIds;
            request.Name = name;

            // Führe Request aus. Kein Rückgabewert erwartet.
            RestRequest<Object>(request);
        }

        public void LockJob(int taskId, int jobId)
        {
            // Es wird kein Rückgabewert erwartet
            RestRequest<Object>(new LockJobRequest(taskId, jobId));
        }

        public void UnlockJob(int taskId, int jobId)
        {
            // Es wird kein Rückgabewert erwartet
            RestRequest<Object>(new UnlockJobRequest(taskId, jobId));
        }
        
        #endregion

        #region WorkflowDecision-Requests

        /// <summary>
        /// Ruft ein Workflowitem des Typs Decision vom Server ab.
        /// </summary>
        /// <param name="id">Die ID des Decision-Workflowitems.</param>
        /// <returns>Decision-Workflowitem.</returns>
        public Decision GetDecision(int id)
        {
            var response = RestRequest<DecisionResponse>(new GetDecisionRequest(id));

            return Decision.Parse(response);
        }

        /// <summary>
        /// Speichert Änderungen des Decision-Workflowitems.
        /// </summary>
        /// <param name="decision">Die Decision, die aktualisiert werden soll.</param>
        public void UpdateDecision(Decision decision)
        {
            RestRequest<Object>(new UpdateDecisionRequest(decision));
        }

        #endregion

        #region WorkflowScript-Requests

        /// <summary>
        /// Ruft ein Workflowitem des Typs Scripts vom Server ab.
        /// </summary>
        /// <param name="id">Die ID des Script-Workflowitems.</param>
        /// <returns>Script-Workflowitem.</returns>
        public Script GetScript(int id)
        {
            var response = RestRequest<ScriptResponse>(new GetScriptRequest(id));

            return Script.Parse(response);
        }

        /// <summary>
        /// Speichert Änderungen des Script-Workflowitems.
        /// </summary>
        /// <param name="script">Das Script, das aktualisiert werden soll.</param>
        public void UpdateScript(Script script)
        {
            RestRequest<Object>(new UpdateScriptRequest(script));
        }

        #endregion

        #region Job-Requests

        /// <summary> 
        /// Methode zum Abrufen der Jobs, die bei einem Task anstehen 
        /// </summary> 
        /// <param name="task">Das Task-Objekt</param> 
        /// <returns>Gibt eine Liste mit Job-Objekten zurück</returns>
        public List<Job> GetJobs(Task task)
        {
            var jobsResponse = RestRequest<List<JobsResponse>>(new GetJobsRequest(task.Id));

            // Wandele JobsResponse-Objekte in Job-Objekte um 
            return jobsResponse.Select(jRes => new Job(jRes.Id, jRes.StartTime, jRes.Active, jRes.Locked)).ToList();
        }

        /// <summary>
        /// Sendet eine Anfrage, um detailierte Job-Informationen zu bekommen.
        /// </summary>
        /// <param name="jobId">Die ID des gewünschten Jobs.</param>
        /// <param name="taskId">Die Task-ID der gewünschten Position.</param>
        /// <returns>Ein Job-Objekt.</returns>
        public Job GetJobDetail(int jobId, int taskId)
        {
            var jobResponse = RestRequest<JobDetailResponse>(new GetJobRequest(jobId, taskId));
            if (jobResponse != null)
            {
                return Job.Parse(jobResponse);
            }

            return null;
        }

        /// <summary>
        /// Methode um den Job mit den Werten des entsprechenden Tasks zu aktualisieren.
        /// </summary>
        /// <param name="currenTask">Die Taskbeschreibung mit dem eingegebenen Inhalt.</param>
        /// <param name="jobId">Die Job-ID des Jobs, dessen Werte aktualisiert werden soll.</param>
        public void UpdateJob(Task currenTask, int jobId)
        {
            var uj = new UpdateJobRequest(jobId);
            uj.TaskId = currenTask.Id;

            foreach (var formGroup in currenTask.FormGroups)
            {
                foreach (var taskComponent in formGroup.Components)
                {
                    if (taskComponent is TaskComponentText)
                    {
                        uj.Components.Add(new JobComponentResponseText
                        {
                            Id = taskComponent.Id,
                            Value = ((TaskComponentText)taskComponent).Value,
                            Type = null
                        });
                    }

                    if (taskComponent is TaskComponentInteger)
                    {
                        uj.Components.Add(new JobComponentResponseInteger
                        {
                            Id = taskComponent.Id,
                            Value = ((TaskComponentInteger)taskComponent).Value,
                            Type = null
                        });
                    }

                    if (taskComponent is TaskComponentFloat)
                    {
                        uj.Components.Add(new JobComponentResponseFloat
                        {
                            Id = taskComponent.Id,
                            Value = ((TaskComponentFloat)taskComponent).Value,
                            Type = null
                        });
                    }
                    if (taskComponent is TaskComponentDate)
                    {
                        uj.Components.Add(new JobComponentResponseDate
                        {
                            Id = taskComponent.Id,
                            Value = ((TaskComponentDate)taskComponent).Value,
                            Type = null
                        });
                    }
                }
            }

            // Es wird kein Rückgabewert erwartet.
            RestRequest<Object>(uj);
        }
        
        #endregion

        #region WorkflowContentEditor-Requests

        /// <summary>
        /// Methode zum Abrufen der FormGroups eines Workflows
        /// </summary>
        /// <param name="workflowId">Die ID des Workflows</param>
        /// <returns>Liste von Component Groups</returns>
        public List<TaskComponentGroup> GetComponentGroups(int workflowId)
        {
            var taskComponentResponse = RestRequest<List<TaskComponentGroupResponse>>(new GetFormGroupsRequest(workflowId));

            var taskComponentGroups = new List<TaskComponentGroup>();
            foreach (var tRes in taskComponentResponse)
            {
                taskComponentGroups.Add(TaskComponentGroup.Parse(tRes));
            }

            return taskComponentGroups;
        }

        /// <summary>
        /// Erstellt eine Component Group
        /// </summary>
        /// <param name="workflowId">Die ID des Workflows</param>
        /// <param name="name">Der Name der Component Group</param>
        public void CreateComponentGroup(int workflowId, string name)
        {
            var createRequest = new CreateFormGroupRequest(workflowId, name);

            // Es wird kein Rückgabewert erwartet.
            RestRequest<Object>(createRequest);
        }

        /// <summary>
        /// Erstellt eine Text Component
        /// </summary>
        /// <param name="formGroupId">ID der FormGroup zu der die Component hinzugefügt werden soll</param>
        /// <param name="name">Name</param>
        /// <param name="comment">Ein hilfreiches Kommentar zur Component</param>
        /// <param name="required">ist die Component ein Pflichtfeld</param>
        /// <param name="defaultValue">Standardwert</param>
        /// <param name="regex">Regex</param>
        public void CreateTextComponent(int formGroupId, string name, string comment, bool required, string defaultValue, string regex)
        {
            var createRequest = new CreateTextComponentRequest(formGroupId, name, comment, required, defaultValue, regex);

            // Es wird kein Rückgabewert erwartet.
            RestRequest<Object>(createRequest);
        }

        /// <summary>
        /// Erstellt eine Integer Component
        /// </summary>
        /// <param name="formGroupId">ID der FormGroup zu der die Component hinzugefügt werden soll</param>
        /// <param name="name">Name</param>
        /// <param name="comment">Ein hilfreiches Kommentar zur Component</param>
        /// <param name="required">ist die Component ein Pflichtfeld</param>
        /// <param name="defaultValue">Standardwert</param>
        /// <param name="minValue">Mindestwert</param>
        /// <param name="maxValue">MaximalWert</param>
        public void CreateIntegerComponent(int formGroupId, string name, string comment, bool required, int defaultValue, int minValue, int maxValue)
        {
           var createRequest = new CreateIntegerComponentRequest(formGroupId, name, comment, required, defaultValue, minValue, maxValue);

            // Es wird kein Rückgabewert erwartet.
           RestRequest<Object>(createRequest);
        }

        /// <summary>
        /// Erstellt eine Float Component
        /// </summary>
        /// <param name="formGroupId">ID der FormGroup zu der die Component hinzugefügt werden soll</param>
        /// <param name="name">Name</param>
        /// <param name="comment">Ein hilfreiches Kommentar zur Component</param>
        /// <param name="required">ist die Component ein Pflichtfeld</param>
        /// <param name="defaultValue">Standardwert</param>
        /// <param name="minValue">Mindestwert</param>
        /// <param name="maxValue">MaximalWert</param>
        public void CreateFloatComponent(int formGroupId, string name, string comment, bool required, float defaultValue, float minValue, float maxValue, int scale)
        {
            var createRequest = new CreateFloatComponentRequest(formGroupId, name, comment, required, defaultValue, minValue, maxValue, scale);

            // Es wird kein Rückgabewert erwartet.
            RestRequest<Object>(createRequest);
        }

        /// <summary>
        /// Erstellt eine Date Component
        /// </summary>
        /// <param name="formGroupId">ID der FormGroup zu der die Component hinzugefügt werden soll</param>
        /// <param name="name">Name</param>
        /// <param name="comment">Ein hilfreiches Kommentar zur Component</param>
        /// <param name="required">ist die Component ein Pflichtfeld</param>
        /// <param name="defaultValue">Standardwert</param>
        public void CreateDateComponent(int formGroupId, string name, string comment, bool required, DateTime defaultValue)
        {
            var createRequest = new CreateDateComponentRequest(formGroupId, name, comment, required, defaultValue);

            // Es wird kein Rückgabewert erwartet.
            RestRequest<Object>(createRequest);
        }

        /// <summary>
        /// Updated die Order der einzelnen Components
        /// </summary>
        /// <param name="components">Liste der neu geordneten Components</param>
        /// <param name="formGroupId">ID der aktuellen Gruppe</param>
        public void UpdateComponentOrder(int formGroupId, List<TaskComponent> components) {

            // components umpacken in passendes Request Objekt
            List<ComponentOrder> componentOrderList = new List<ComponentOrder>();
            foreach (TaskComponent component in components) {
                componentOrderList.Add(new ComponentOrder(component.Id, component.Order));
            }

            var updateRequest = new UpdateComponentOrderRequest(formGroupId, componentOrderList);

            // Es wird kein Rückgabewert erwartet.
            RestRequest<Object>(updateRequest);
        }

        #endregion

        #region Methods

        /// <summary>
        /// Methode zum Verarbeiten aller REST-Requests. Nimmt Request Objekt serialisiert es
        /// und schickt Anfrage an den Server via REST. Die Antwort wird direkt deserialisiert.
        /// </summary>
        /// <typeparam name="T">Generic, da es verschiedene Response Objekte gibt</typeparam>
        /// <param name="requestObj">Das Request-Objekt</param>
        /// <returns>Response Objekt</returns>
        private T RestRequest<T>(RequestBase requestObj) 
        {
            var request = new RestRequest(requestObj.RestUrl, requestObj.Method); 
	        
            // setze Request Format
	        request.RequestFormat = DataFormat.Json; 
	 
	        // Wenn Benutzer angemeldet ist, wird token gesetzt.
            if (ActiveUser != null)
            {
                request.AddHeader("Token", ActiveUser.Token);
            } 

	        //request.AddHeader("Prefer", "status=200"); 
	 
            // Füge Serialisiertes Objekt zum Body hinzu 
            var content = JsonConvert.SerializeObject(requestObj);
            request.AddParameter("application/json", content, ParameterType.RequestBody);
            Debug.Print("Sende REST-Anfrage: {0}:{1} CONTENT: {2}", requestObj.Method, requestObj.RestUrl, content);

            var response = client.Execute(request);
            if (response.ErrorException != null)
            {
                throw response.ErrorException;
            }

            if (String.IsNullOrWhiteSpace(response.Content))
            {
                if (response.StatusCode == HttpStatusCode.OK || response.StatusCode == HttpStatusCode.Created)
                {
                    // Kein Inhalt, aber Request war OK -> z.B. beim aktuallisieren des Jobs.
                    return default(T);
                }

                throw new Exception(string.Format("Ungültige Serveranfrage: {0} (HTTP-CODE: {1})", response.StatusDescription, response.StatusCode));
            }

            if (response.ContentType.Equals("application/json"))
            {
                JObject jsonContent = JObject.Parse(response.Content);
                JToken jsonBody;
                if (response.StatusCode == HttpStatusCode.OK || response.StatusCode == HttpStatusCode.Created)
                {
                    // Dateninhalt deserialisieren.
                    if (jsonContent.TryGetValue("data", out jsonBody))
                    {
                        jsonBody = jsonContent["data"];
                        return JsonConvert.DeserializeObject<T>(jsonBody.ToString());
                    }
                }

                jsonBody = jsonContent["error"];
                var error = JsonConvert.DeserializeObject<Error>(jsonBody.ToString());

                // Prüft ob der token abgeloffen ist und setzt den Benutzer zurück.
                if (error.Code == "token_invalid")
                {
                    ClientSession.Instance.SetUserInvalid();
                }
                else
                {
                    throw new RESTException(error.Code, error.Message);
                }
            }

            throw new Exception(String.Format("Unerwartete Antwort wurde zurückgegeben:{0}{1} ({2}){3}{4}",
                Environment.NewLine,
                response.StatusDescription,
                response.StatusCode,
                Environment.NewLine,
                response.Content));
        }
    }

        #endregion
}
