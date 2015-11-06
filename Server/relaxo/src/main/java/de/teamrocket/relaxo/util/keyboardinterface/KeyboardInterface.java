package de.teamrocket.relaxo.util.keyboardinterface;

import com.google.inject.Inject;
import de.teamrocket.relaxo.controller.*;
import de.teamrocket.relaxo.controller.exceptions.*;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.usermanagement.UserGroup;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;
import de.teamrocket.relaxo.persistence.SQLExecutor;
import de.teamrocket.relaxo.util.debug.DebugProject;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

import java.util.Scanner;
import java.util.logging.Level;

/**
 * Das Userinterface über die Konsole.
 */
// Supress System.Out warnings of sonar - we want syso here
@SuppressWarnings("all")
public class KeyboardInterface {

    // Static

    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.OTHER);

    public static final String SQL_CREATE = "scripts/install_script.sql";
    public static final String SQL_VIEWS = "scripts/views_script.sql";
    public static final String SQL_TESTDATA = "scripts/testdata_script.sql";

    public static final String STOP_1 = "stop";
    public static final String STOP_2 = "exit";
    public static final String STOP_3 = "shutdown";

    public static final String HELP_COMMAND = "help";
    public static final String MANIPULATE_COMMAND = "m";
    public static final String DB_COMMAND = "db";
    public static final String DEBUG_COMMAND = "d";
    public static final String LOGGER_SET_COMMAND = "l";
    public static final String LOGGER_LEVEL_COMMAND = "l-level";

    public static final int WAIT_USER_MS = 250;

    // Vars

    @Inject
    protected WorkflowController workflowController;
    @Inject
    protected UserManagementController userManagementController;
    @Inject
    protected JobController jobController;
    @Inject
    protected FormGroupController formGroupController;
    @Inject
    protected TaskController taskController;
    @Inject
    protected WorkflowItemController workflowItemController;

    private SQLExecutor sqlExecutor;

    // Constructor

    /**
     *
     */
    public KeyboardInterface() {
        this.sqlExecutor = new SQLExecutor();
    }

    // Public Methods

    /**
     * Wartet auf Benutzerinteraktion über die Konsole. Läuft in ner Endlosschleife, bis "stop" aufgerufen wurde.
     */
    public void interactWithUser() {
        Scanner sc = new Scanner(System.in);


        System.out.println("Zum Beenden, bitte stop eingeben.");

        while (true) {
            this.waitMs(WAIT_USER_MS);

            System.out.print("> ");

            String eingabe = sc.nextLine();

            try {
                if (eingabe.equals(STOP_1) || eingabe.equals(STOP_2) || eingabe.equals(STOP_3)) {
                    break; // End of Loop -> Shutdown
                } else if (eingabe.equals(HELP_COMMAND)) {
                    sectionHelp();
                } else if (eingabe.startsWith(MANIPULATE_COMMAND+" ")) {
                    sectionManipulation(eingabe.substring((MANIPULATE_COMMAND + " ").length()));
                } else if (eingabe.startsWith(LOGGER_SET_COMMAND+" ")) {
                    sectionLog(eingabe.substring((LOGGER_SET_COMMAND+" ").length()));
                } else if (eingabe.startsWith(LOGGER_LEVEL_COMMAND+" ")) {
                    sectionLogLevel(eingabe.substring((LOGGER_LEVEL_COMMAND+" ").length()));
                } else if (eingabe.startsWith(DEBUG_COMMAND+" ")) {
                    sectionDebug(eingabe.substring((DEBUG_COMMAND + " ").length()));
                } else if (eingabe.startsWith(DB_COMMAND+" ")) {
                    sectionDB(eingabe.substring((DB_COMMAND + " ").length()));
                } else {
                    sectionRdfm(eingabe);
                }
            } catch (KeyboardInterfaceException e) {
                LOGGER.warning("KeyboardInterfaceException: " + e.getMessage(), e);
            } catch (Exception e) {
                LOGGER.warning(e.getMessage(), e);
            }
        }

        sc.close();
    }

    // Private Methods

    /**
     * Wartet die übergebene Zeit an Millisekunden.
     *
     * @param ms Die zu wartende Zeit in Millisekunden.
     */
    private void waitMs(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            /* void, dann halt nicht warten. */
        }
    }

    /**
     * Section RDFM.
     *
     * @param eingabe die Eingabe des Users.
     */
    private void sectionRdfm(String eingabe) {
        System.out.println("Der Befehl: " + eingabe + " ist ungültig. Bitte geben Sie help ein, wenn Sie nicht wissen, was Sie tuen.");
    }

    /**
     * Section Manipulation.
     *
     * @param eingabe die Eingabe des Users.
     */
    private void sectionManipulation(String eingabe) {
        String[] splitted = eingabe.split(" ");
        String method = splitted[0];
        String ressource = splitted[1];

        switch (ressource) {
            case "workflow":
                subSectionManipulationWorkflow(method, splitted);
                break;
            case "workflowItem":
                subSectionManipulationWorkflowItem(method, splitted);
                break;
            case "user":
                try {
                    subSectionManipulationUser(method, splitted);
                } catch (UserNotFoundException e) {
                    LOGGER.warning(e.getMessage(), e);
                }
                break;
            case "group":
                subSectionManipulationGroup(method, splitted);
                break;
            default:
                LOGGER.warning("Unbekannte Ressource.", null);
        }
    }

    /**
     * Sub_Section ManipulationGroup
     *
     * @param method die Eingegebene Methode
     * @param splitted das payload
     */
    private void subSectionManipulationGroup(String method, String[] splitted) {
        switch (method) {
            case "delete": {
                UserGroup group;
                try {
                    group = userManagementController.getGroupById(Integer.parseInt(splitted[2]));
                } catch (UserGroupNotFoundException e) {
                    LOGGER.warning(e.getMessage(), e);
                    return;
                }
                String name = group.getName();
                int id = group.getId();
                userManagementController.deleteGroup(group);
                LOGGER.info("Gruppe " + "'" + name + "'" + " mit der id: " + id + " wurde entfernt.");
            }
            break;

        }
    }

    /**
     * Sub_Section ManipulationUser
     *
     * @param method die Eingegebene Methode
     * @param splitted das payload
     */
    private void subSectionManipulationUser(String method, String[] splitted) throws UserNotFoundException {
        switch (method) {
            case "create": {
                User user = new User();
                user.setUsername(splitted[2]);
                user.setPassword(splitted[3]);
                if (splitted.length > 4) {
                    for (int i = 4; i < splitted.length; i++) {

                        String key = (splitted[i].split("="))[0];
                        String value = (splitted[i].split("="))[1];

                        if (key.equals("admin")) { //z.b. m create user adminUser 123456 admin=true
                            user.setAdmin(Boolean.parseBoolean(value));
                        }
                        if (key.equals("name")) { //z.b. m create user adminUser 123456 admin=true name=Cengiz
                            user.setName(value);
                        }
                        if (key.equals("prename")) { //z.b. m create user adminUser 123456 prename=Cengiz
                            user.setPrename(value);
                        }
                    }
                }
                try {
                    userManagementController.createUser(user);
                } catch (UserNameTakenException e) {
                    LOGGER.warning(e.getMessage(), e);
                } catch (FieldMissingException e) {
                    LOGGER.warning(e.getMessage(), e);
                }
                System.out.println("User " + "'" + user.getUsername() + "'" + " mit dem passwort: " + user.getPassword() + " der id: " + user.getId() + " erstellt. admin:(" + user.isAdmin() + ")");
            }
            break;
            case "delete": {
                User user = userManagementController.getUserById(Integer.parseInt(splitted[2]));
                String name = user.getUsername();
                int id = user.getId();
                userManagementController.deleteUser(user);
                LOGGER.info("User " + "'" + name + "'" + " mit der id: " + id + " wurde entfernt.");
            }
            break;
        }

    }

    /**
     * Sub_Section ManipulationWorkflow
     *
     * @param method die Eingegebene Methode
     * @param splitted das payload
     */
    private void subSectionManipulationWorkflow(String method, String[] splitted) {
        switch (method) {
            case "create": {
                User user = userManagementController.getAllUser().get(0);
                Workflow workflow = workflowController.createWorkflow(user, "Serverworkflow");
                if (workflow == null) {
                    throw new KeyboardInterfaceException("ging net");
                }
                if (splitted.length > 2) {
                    workflow.setName(splitted[2]);
                }
                LOGGER.info("Workflow " + "'" + workflow.getName() + "'" + " mit der id: " + workflow.getId() + " erstellt.");
            }
            break;
            case "delete": {
                Workflow workflow = workflowController.getWorkflowById(Integer.parseInt(splitted[2]));
                if (workflow != null) {
                    String name = workflow.getName();
                    int id = workflow.getId();
                    workflowController.deleteWorkflow(workflow);
                    LOGGER.info("Workflow " + "'" + name + "'" + " mit der id: " + id + " entfernt.");
                } else {
                    LOGGER.warning("Workflow nicht gefunden", null);
                }
            }
            break;
        }

    }

    /**
     * Sub_Section ManipulationWorkflowItem
     *
     * @param method die Eingegebene Methode
     * @param splitted das payload
     */
    private void subSectionManipulationWorkflowItem(String method, String[] splitted) {
        switch (method) {
            case "create":
                throw new KeyboardInterfaceException("not jet implemented");
            case "delete": {
                WorkflowItem workflowItem = workflowItemController.getWorkflowItemById(Integer.parseInt(splitted[2]));
                String type = workflowItem.getType();
                int id = workflowItem.getId();
                try {
                    workflowItemController.deleteWorkflowItem(workflowItem.getId(), null);
                    LOGGER.info("WorkflowItem des typen " + "'" + type + "'" + " mit der id: " + id + " entfernt.");
                } catch (WorkflowNotEditableException e) {
                    LOGGER.warning("WorkflowItem des typen " + "'" + type + "'" + " mit der id: " + id + " konnte nicht entfernt werden.", null);
                } catch (WorkflowItemNotFoundException e) {
                    LOGGER.warning("WorkflowItem des typen " + "'" + type + "'" + " mit der id: " + id + " konnte nicht entfernt werden.", null);
                }
            }
            break;
        }
    }

    /**
     * Section DB
     *
     * @param eingabe die Eingabe des Users.
     */
    private void sectionDB(String eingabe) {
        //create
        if (eingabe.equals("create")) {
            sqlExecutor.executeSqlScript(SQL_CREATE);
            System.out.println("SQL-Created.");
            sqlExecutor.executeSqlScript(SQL_VIEWS);
            System.out.println("Views added");
        }
        //testdata
        if (eingabe.equals("testdata")) {
            sqlExecutor.executeSqlScript(SQL_TESTDATA);
            System.out.println("Testdata filled");
        }
    }

    /**
     * Section Log
     *
     * @param eingabe die Eingabe des Users.
     */
    private void sectionLog(String eingabe) {

        String[] splitted = eingabe.split(" ");

        String eingabeType = splitted[0];
        String eingabeBoolean = splitted[1];

        boolean found = false;
        for (RelaxoLoggerType rlt : RelaxoLoggerType.values()) {
            if (rlt.toString().equals(eingabeType)) {

                if (eingabeBoolean.equals("1")) {
                    RelaxoLogger.setLoggerActive(rlt);
                    LOGGER.info("Logger für " + rlt.toString() + " ist nun aktiv.");
                } else {
                    RelaxoLogger.setLoggerInactive(rlt);
                    LOGGER.info("Logger für " + rlt.toString() + " ist nun inaktiv.");
                }

                found = true;
                break;
            }
        }
        if (!found) {
            LOGGER.warning("Unbekanntes Log-Commando. Geben Sie help ein.", null);
        }
    }

    /**
     * Section Log-Level
     *
     * @param eingabe die Eingabe des Users.
     */
    private void sectionLogLevel(String eingabe) {
        String[] splitted = eingabe.split(" ");

        String eingabeLevel = splitted[0];

        boolean found = false;
        switch(eingabeLevel){
            case "ALL":
                RelaxoLogger.setLogLevel(Level.ALL);
                found = true;
                break;
            case "FINE":
                RelaxoLogger.setLogLevel(Level.FINE);
                found = true;
                break;
            case "WARNING":
                RelaxoLogger.setLogLevel(Level.WARNING);
                found = true;
                break;
            case "INFO":
                RelaxoLogger.setLogLevel(Level.INFO);
                found = true;
                break;
        }

        if (!found) {
            LOGGER.warning("Unbekanntes Log-Level-Commando. Geben Sie help ein.", null);
        } else {
            LOGGER.info("Logger-level auf " + eingabeLevel + " gesetzt.");
        }
    }

    /**
     * Section Debug-Level
     *
     * @param eingabe die Eingabe des Users.
     */
    private void sectionDebug(String eingabe) {
        DebugProject debugProject = new DebugProject(workflowController, userManagementController, jobController, formGroupController, taskController, workflowItemController);

        if (eingabe.startsWith("job ")) {
            debugProject.printJob(Integer.parseInt(eingabe.split(" ")[1]));
        } else if (eingabe.equals("jobs")) {
            debugProject.printAllJobs();
        } else if (eingabe.equals("groups")) {
            debugProject.printAllGroups();
        } else if (eingabe.equals("config")) {
            debugProject.printConfig();
        } else if (eingabe.equals("users")) {
            debugProject.printAllUser();
        } else if (eingabe.equals("workflows")) {
            debugProject.printAllWorkflows();
        } else if (eingabe.startsWith("workflow ")) {
            debugProject.printWorkflow(Integer.parseInt(eingabe.split(" ")[1]));
        } else {
            LOGGER.warning("Unbekanntes Debug-Commando. Geben Sie help ein.", null);
        }
    }

    /**
     * Section Help
     *
     * @param eingabe die Eingabe des Users.
     */
    private void sectionHelp() {
        System.out.println("= Hilfe =");
        System.out.println();

        System.out.println("== Allgemein ==");
        System.out.println("Server stoppen               stop / exit / shutdown");
        System.out.println("Log-Level setzen            l-level ALL / FINE / WARNING / INFO");
        System.out.println("Datenbank initialisieren    db create");
        System.out.println();

        System.out.println("== Debuggen (uses Controllers) ==");
        System.out.println("Debug von Workflows          d workflows");
        System.out.println("Debug vom Workflow           d workflow <id>");
        System.out.println("Debug von Jobs               d jobs");
        System.out.println("Debug von Job                d job <id>");
        System.out.println("Debug von Usern              d users");
        System.out.println("Debug von Gruppen            d groups");
        System.out.println("Debug der Config-File        d config");
        System.out.println();

        System.out.println("== Logs ==");
        System.out.println("Logger setzen               l <TYPE> 1");
        System.out.print("Mögliche Werte für <TYPE>    ");
        for (RelaxoLoggerType rlt : RelaxoLoggerType.values()){
            System.out.print(rlt.toString() + " ");
        }
        System.out.println("\n");

        System.out.println("== Manipulation (uses Controllers) ==");
        System.out.println("Ein Workflow erstellen       m create workflow [<name:string>]");
        System.out.println("Ein Workflow entfernen       m delete workflow <id:int>");
        System.out.println("Ein WorkflowItem entfernen   m delete workflowItem <id:int>");
        System.out.println("Einen User erstellen         m create user [<name:string> [<password:string> [admin=<boolean>] [name=<string>] [prename=<string>]]]");
        System.out.println("Einen User entfernen         m delete user <int:id>");
        System.out.println("Eine Gruppe erstellen        m create group [<name:string>]");
        System.out.println("Einen Gruppe entfernen       m delete group <int:id>");
        System.out.println();


        System.out.println();

    }
}
