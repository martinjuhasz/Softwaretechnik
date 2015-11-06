package de.teamrocket.relaxo.rest.models.task.response;


import de.teamrocket.relaxo.models.taskcomponent.*;

/**
 * Factory zum erzeugen neuer konkreter TaskComponentResponses
 */
public class TaskComponentResponseFactory {

    /**
     * Erstellt eine neue konkrete TaskComponentResponse je nach uebergebener TaskComponent
     *
     * @param taskComponent TaskComponent, aus der die TaskComponentResponse erstellt werden soll
     * @return die erzeugte konkrete TaskComponentResponse
     */
    public TaskComponentResponse newTaskComponentResponse(TaskComponent taskComponent) {
        if (taskComponent instanceof TaskComponentText) {
            return new TaskComponentTextResponse((TaskComponentText) taskComponent);
        }
        if (taskComponent instanceof TaskComponentInteger) {
            return new TaskComponentIntegerResponse((TaskComponentInteger) taskComponent);
        }
        if (taskComponent instanceof TaskComponentDate) {
            return new TaskComponentDateResponse((TaskComponentDate) taskComponent);
        }
        if (taskComponent instanceof TaskComponentFloat) {
            return new TaskComponentFloatResponse((TaskComponentFloat) taskComponent);
        }
        return null;
    }

}
