package de.teamrocket.relaxo.models.taskcomponent;

/**
 * Respaesentation der Komponenten die einem Task zugeordnet werden
 */
public abstract class TaskComponent {

    /**
     * Die automatisch vergebene id des TaskComponent.
     */
    private int id;

    /**
     * Gibt die Bezeichnung dieser Komponente.
     */
    private String name;

    /**
     * ein Kommentar zu dieser Komponente
     */
    private String comment;

    private int index;
    /**
     * Gibt an, ob diese Komponente im Formular ausgefüllt werden muss.
     */
    private boolean required;

    private String type;

    private int formGroupId;

    private boolean readOnly;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFormGroupId() {
        return formGroupId;
    }

    public void setFormGroupId(int formGroupId) {
        this.formGroupId = formGroupId;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
