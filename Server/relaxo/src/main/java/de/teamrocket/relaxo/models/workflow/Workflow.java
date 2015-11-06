package de.teamrocket.relaxo.models.workflow;

import java.util.Date;


/**
 * workflow ist das Wurzelobjekt für das Datenmodell.
 * Ein workflow hat einen Verweis auf das erste WorkflowItem, was den Startpunkt des Workflows darstellt.
 * Der User, der den workflow erstellt wird als Ersteller zugewiesen.
 */

public class Workflow {

    /**
     * Repräsentiert die ID des Workflows.
     */
    private int id;

    /**
     * Beinhaltet den Namen des Workflows.
     */
    private String name;

    /**
     * Hält das Start-Item des Workflows.
     */
    private Integer startItemId;

    /**
     * Referenziert den User, der diesen Workflow erstellt hat.
     */
    private int creatorId;

    /**
     * Stellt das Erstelldatum dieses Workflows dar.
     */
    private Date creationDate;

    /**
     * Ist der Workflow aktiv, lauffähig?
     */
    private boolean runnable;

    /**
     * Gibt den Namen des Workflows wieder.
     *
     * @return Name des Workflows
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Workflows.
     *
     * @param name Name des Workflows
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt die ID des Workflows wieder.
     *
     * @return ID des Workflows
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt die ID des Workflows.
     *
     * @param id des Workflows.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gibt das Start-Item des Workflows wieder.
     *
     * @return Startpunkt des Workflows
     */
    public Integer getStartItemId() {
        return startItemId;
    }

    // Setter

    /**
     * Setzt die Start-Item-Id des Workflows.
     *
     * @param startItemId Id von erstem WorkflowItem
     */
    public void setStartItemId(Integer startItemId) {
        this.startItemId = startItemId;
    }

    /**
     * Gibt die Ersteller Id des Workflows wieder.
     *
     * @return Ersteller Id des Workflows
     */
    public int getCreatorId() {
        return creatorId;
    }

    /**
     * Setzt die Ersteller Id dieses Workflows.
     *
     * @param creatorId des Benutzers, der diesen Workflow erstellt hat.
     */
    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * Gibt das Erstelldatum dieses Workflows wieder.
     *
     * @return Erstellungsdatum
     */
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isRunnable() {
        return runnable;
    }

    public void setRunnable(boolean runnable) {
        this.runnable = runnable;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + " - Name: " + this.name + " - Created: " + creationDate;
    }
}