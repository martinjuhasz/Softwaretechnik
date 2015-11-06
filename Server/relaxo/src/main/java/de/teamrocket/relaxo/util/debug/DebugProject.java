package de.teamrocket.relaxo.util.debug;

import de.teamrocket.relaxo.controller.*;
import de.teamrocket.relaxo.controller.exceptions.UserGroupNotFoundException;
import de.teamrocket.relaxo.controller.exceptions.UserNotFoundException;
import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.job.JobTask;
import de.teamrocket.relaxo.models.job.JobWorkflowItem;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobTaskComponent;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.usermanagement.UserGroup;
import de.teamrocket.relaxo.models.workflow.*;
import de.teamrocket.relaxo.util.config.Config;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

/**
 * Debugklasse. Bedarf keines weiteren Kommentars.
 */
// Supress System.Out warnings of sonar - we want syso here
@SuppressWarnings("all")
public class DebugProject {

    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.OTHER);

    // Vars

    protected final WorkflowController workflowController;
    protected final UserManagementController userManagementController;
    protected final JobController jobController;
    protected final FormGroupController formGroupController;
    protected final TaskController taskController;
    protected final WorkflowItemController workflowItemController;

    // Constructor

    public DebugProject(WorkflowController workflowController
            , UserManagementController userManagementController
            , JobController jobController
            , FormGroupController formGroupController
            , TaskController taskController
            , WorkflowItemController workflowItemController) {

        this.workflowController = workflowController;
        this.userManagementController = userManagementController;
        this.jobController = jobController;
        this.formGroupController = formGroupController;
        this.taskController = taskController;
        this.workflowItemController = workflowItemController;

    }

    // Methods

    /**
     * Gibt alle definierten Workflows aus.
     */
    public void printAllWorkflows() {

        System.out.println("Alle definierten Workflows: ");
        for (Workflow workflow : workflowController.getAllWorkflows()) {
            String username = "<keine angaben>";

            if (workflow.getCreatorId() != 0) {
                try {
                    username = userManagementController.getUserById(workflow.getCreatorId()).getUsername();
                } catch (UserNotFoundException e) {
                    LOGGER.warning(e.getMessage(), e);
                }
            }

            System.out.print("- Workflow mit der ID: (" + workflow.getId() + ") | Name: " + workflow.getName() + " | Erstellt von: " + username);
            System.out.println();
        }
    }

    /**
     * Gibt alle Jobs aus.
     */
    public void printAllJobs() {
        System.out.println("Alle Jobs: ");
        for (Job job : jobController.getAllJobs()) {
            String currWorkItmsId = "";


            for (JobWorkflowItem jobWorkflowItem : jobController.getCurrentJobWorkflowItemsForJob(job)) {
                if (jobWorkflowItem instanceof JobTask) {
                    currWorkItmsId += jobWorkflowItem.getId() + ", ";
                } else {
                    currWorkItmsId += "#";
                }
            }
            System.out.println("- Job mit der ID: (" + job.getId() + ") | currWorkItms: (" + jobController.getCurrentJobWorkflowItemsForJob(job).size() + ") bei {" + currWorkItmsId + "}; | vom Workflow: (" + workflowController.getWorkflowById(job.getWorkflowId()).getName() + ")");
        }

    }

    /**
     * Gibt Details zum Workflow aus, welcher die id hat, die übergeben wurde.
     *
     * @param id Die id des auszugebenden Workflows.
     */
    public void printWorkflow(int id) {
        Workflow workflow;
        try {
            workflow = workflowController.getWorkflowById(id);
        } catch (Exception e) {
            LOGGER.warning(e.getMessage(), e);
            return;
        }

        System.out.println("Informationen zum Workflow mit der ID: " + id);

        System.out.println("Name: " + workflow.getName());

        System.out.print("Vorhandene (nicht abgeschlossene) Jobs: ");

        for (Job job : jobController.getAllJobs()) {

            if (job.getWorkflowId() == id && jobController.isJobRunning(job)) {
                System.out.print(job.getId() + "; ");
            }
        }

        System.out.println();

        System.out.println("WorkflowItems:");

        for (WorkflowItem item : workflowItemController.getWorkflowItemsByWorkflowId(workflow.getId())) {
            System.out.print("- " + item.getType() + " (id:" + item.getId() + ") ");

            if (item.getType().equals("START")) {
                Start startItem = (Start) workflowItemController.getWorkflowItemById(item.getId());
                if (startItem.getNextWorkflowItemId() != null) {
                    System.out.println("-> " + startItem.getNextWorkflowItemId() + ": ");
                } else {
                    System.out.println("-> null: ");
                }
            } else if (item.getType().equals("END")) {
                /*void*/
            } else if (item.getType().equals("SCRIPT")) {
                WorkflowScript script = (WorkflowScript) item;
                if (script.getNextWorkflowItemId() != null) {
                    System.out.println("-> " + script.getNextWorkflowItemId() + ": ");
                } else {
                    System.out.println("-> null: ");
                }
                String hellip = "";
                if (script.getScript() != null) {
                    if (script.getScript().length() > 100) {
                        hellip += "...";
                    }
                    System.out.println("Script: " + script.getScript().substring(0, Math.min(script.getScript().length(),100)) + hellip);
                } else {
                    System.out.println("Kein Script nicht definiert");
                }
            } else if (item.getType().equals("TASK")) {
                Task task = taskController.getTask(item.getId());

                WorkflowItem workflowItem = workflowItemController.getWorkflowItemById(task.getNextWorkflowItemId());

                if (workflowItem != null) {
                    System.out.println("-> " + workflowItem.getId() + ": ");
                } else {
                    System.out.println("-> null: ");
                }

                System.out.println("Name: " + task.getName());

                for (TaskComponent comp : task.getTaskComponents()) {
                    System.out.println("   \\ ID: " + comp.getId() + " '" + comp.getName() + "' vom Typ: " + comp.getType());
                }
            } else {
                System.out.println("TYPE " + item.getType() + " nicht im DEBUG definiert.");
            }

            System.out.println();
        }
    }


    /**
     * Gibt Details zum Job aus, welcher die id hat, die übergeben wurde.
     *
     * @param id Die id des auszugebenden Job.
     */
    public void printJob(int id) {
        Job job = jobController.getJob(id);

        System.out.println("Informationen zum Job mit der ID: " + id);

        if (job == null) {
            System.out.println("Dieser Job existiert nicht");
            return;
        }


        System.out.println("# Workflow: " + workflowController.getWorkflowById(job.getWorkflowId()).getName() + " (" + job.getWorkflowId() + ")");
        System.out.println("# JobTasks: ");

        for (JobWorkflowItem jobWorkflowItem : jobController.getCurrentJobWorkflowItemsForJob(job)) {

            String type = String.format("%5s", workflowItemController.getWorkflowItemById(jobWorkflowItem.getWorkflowItemId()).getType());
            String finish = "[finish:" + String.format("%5s", jobWorkflowItem.isDone()) + "]";
            String verweis = "(verweist auf WorkflowItem: " + jobWorkflowItem.getWorkflowItemId() + ")";

            System.out.print("  (" + jobWorkflowItem.getId() + ") " + type + " " + finish + " " + verweis + "");

            if (jobWorkflowItem instanceof JobTask) {
                JobTask jobTask = (JobTask) jobWorkflowItem;
                if (!jobTask.getJobTaskComponents().isEmpty()) {
                    System.out.println(": \"" + taskController.getTask(jobTask.getId()).getName() + "\"");
                    for (JobTaskComponent jobTaskComponent : jobTask.getJobTaskComponents()) {
                        System.out.println("   - (" + jobTaskComponent.getId() + ") " +
                                "bezogen auf (" + jobTaskComponent.getTaskComponentId() + ") " +
                               /* "mit dem Wert: " + jobTaskComponent.getValue()*/"");
                    }
                } else {
                    System.out.println(": \"" + taskController.getTask(jobTask.getId()).getName() + "\"" + " <no-data>");
                }
            } else {
                System.out.println();
            }
        }

        System.out.print("  CurrentJobWorkflowItems");
    }

    /**
     * Gibt alle User aus.
    */
     public void printAllUser(){
         System.out.println("Alle User: ");
         for(User user : userManagementController.getAllUser()){
             System.out.println("User '"+user.getUsername() + "' (" + user.getId() + ") ");

             try{
                 if(!userManagementController.getUserGroupsForUser(user.getId()).isEmpty()){
                     System.out.print("  - in den Gruppen: ");
                     for(UserGroup group : userManagementController.getUserGroupsForUser(user.getId())){
                        System.out.print("'"+group.getName()+"' ("+group.getId()+"); ");
                     }
                     System.out.println();
                 }else {
                    System.out.println("  - in keiner Gruppen.");
                 }

             }catch(UserNotFoundException e) {
                 LOGGER.warning(e.getMessage(), e);
             }
         }
     }

    /**
     * Gibt die Gruppen aus
     */
     public void printAllGroups(){
         System.out.println("Alle Gruppen: ");
         for(UserGroup group : userManagementController.getAllGroups()) {
             System.out.println("Gruppe '"+ group.getName()+"' ("+ group.getId()+"): ");

             try{
                 if(!userManagementController.getUserForUserGroup(group.getId()).isEmpty()){
                     for(User user : userManagementController.getUserForUserGroup(group.getId())) {
                        System.out.print(user.toString()+"; ");
                     }
                     System.out.println();
                 }else{
                        System.out.println("  - Gruppe ist leer.");
                 }

             }catch(UserGroupNotFoundException e) {
                 LOGGER.warning(e.getMessage(), e);
             }

         }
     }
    /**
     * Gibt die Config aus.
     */
    public void printConfig() {
        Config config = Config.getSingleton();

        System.out.println("Alle Config-Parameter: ");
        for (String key : config.getKeyValueMap().keySet()) {
            System.out.println(" " + key + " : " + config.getKeyValueMap().get(key));
        }
    }
}
