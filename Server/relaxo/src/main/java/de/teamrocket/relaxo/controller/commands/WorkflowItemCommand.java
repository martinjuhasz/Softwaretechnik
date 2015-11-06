package de.teamrocket.relaxo.controller.commands;

/**
 * Interface, das ein WorkflowItemCommand implementieren muss, damit es ausgeführt werden kann
 */
public interface WorkflowItemCommand {

    /**
     * Die Command-Methode, die ausgeführt werden soll, sobald das Command abgearbeitet wird.
     */
    public void execute();

}
