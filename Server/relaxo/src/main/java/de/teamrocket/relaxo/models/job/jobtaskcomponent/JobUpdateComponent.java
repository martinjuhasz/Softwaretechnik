package de.teamrocket.relaxo.models.job.jobtaskcomponent;

/**
 * Eine Bean Klasse zum Speichern der Werte für ein Update eines JobTaskComponents
 */
public class JobUpdateComponent {
	/**
	 * Die Id des zugehörigen TaskComponents
	 */
    private int taskComponentId;
    /**
     * Der neue Wert, mit dem das JobTaskComponent geupdatet werden soll
     */
    private String value;

    public JobUpdateComponent() {

    }

    /**
     * Erzeugt ein JobUpdateComponent mit übergebener taskComponentId und value
     * @param taskComponentId Die Id des zugehörigen TaskComponents
     * @param value Der neue Wert, mit dem das JobTaskComponent geupdatet werden soll
     */
    public JobUpdateComponent(int taskComponentId, String value) {
        this.taskComponentId = taskComponentId;
        this.value = value;
    }

    public int getTaskComponentId() {
        return taskComponentId;
    }

    public void setTaskComponentId(int taskComponentId) {
        this.taskComponentId = taskComponentId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
