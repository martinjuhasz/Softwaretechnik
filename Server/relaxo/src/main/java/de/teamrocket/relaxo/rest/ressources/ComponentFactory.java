package de.teamrocket.relaxo.rest.ressources;

import com.google.inject.Inject;

import de.teamrocket.relaxo.models.taskcomponent.*;
import de.teamrocket.relaxo.rest.models.formgroup.request.ComponentCreateRequest;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Factory die TaskComponents ensprechend des RequestsObjekts zurückliefert
 */
public class ComponentFactory {

    // Vars

    /**
     * Instanz des DateTimeFormatter
     */
    private final DateTimeFormatter dateTimeFormatter;

    // Construct

    @Inject
    public ComponentFactory(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    // Methods

    /**
     * Erzeugt ein TaskComponent Objekt anhand des Request Payloadsdas
     *
     * @param createRequest das Request Objekt
     * @return ein befülltes TaskComponent Objekt
     */
    public TaskComponent getTaskComponentFromRequest(ComponentCreateRequest createRequest) {
        ComponentType componentType = ComponentType.fromString(createRequest.getType());
        switch (componentType) {
            case TYPE_TEXT:
                return getTaskComponentTextFromRequest(createRequest);
            case TYPE_DATE:
                return getTaskComponentDateFromRequest(createRequest);
            case TYPE_FLOAT:
                return getTaskComponentFloatFromRequest(createRequest);
            case TYPE_INTEGER:
                return getTaskComponentIntegerFromRequest(createRequest);
            default:
                break;
        }
        return null;
    }

    /**
     * Setzt alle Default Felder die jedes TaskComponent Objekt besitzt
     *
     * @param taskComponent die zu befüllende Component
     * @param createRequest das Request Objekt
     * @return den übergebenen, jetzt befüllten, TaskComponent
     */
    private TaskComponent setDefaultValuesFromRequest(TaskComponent taskComponent, ComponentCreateRequest createRequest) {
        taskComponent.setName(createRequest.getName());
        taskComponent.setComment(createRequest.getComment());
        taskComponent.setRequired(createRequest.isRequired());
        taskComponent.setFormGroupId(createRequest.getFormGroupId());

        return taskComponent;
    }

    /**
     * Erstellt und befüllt ein TaskComponentText
     *
     * @param createRequest der Request
     * @return das TaskComponentText Objekt
     */
    private TaskComponentText getTaskComponentTextFromRequest(ComponentCreateRequest createRequest) {
        TaskComponentText taskComponentText = new TaskComponentText();
        setDefaultValuesFromRequest(taskComponentText, createRequest);

        taskComponentText.setDefaultValue(createRequest.getDefaultValue());
        taskComponentText.setDefaultValue(createRequest.getDefaultValue());
        taskComponentText.setRegex(createRequest.getRegex());

        return taskComponentText;
    }

    /**
     * Erstellt und befüllt ein TaskComponentText
     *
     * @param createRequest der Request
     * @return das TaskComponentText Objekt
     */
    private TaskComponentFloat getTaskComponentFloatFromRequest(ComponentCreateRequest createRequest) {
        TaskComponentFloat taskComponentFloat = new TaskComponentFloat();
        setDefaultValuesFromRequest(taskComponentFloat, createRequest);
        BigDecimal defaultValue = new BigDecimal(createRequest.getDefaultValue());
        taskComponentFloat.setDefaultValue(defaultValue);
        BigDecimal minValue = new BigDecimal(createRequest.getMinValue());
        taskComponentFloat.setMinValue(minValue);
        BigDecimal maxValue = new BigDecimal(createRequest.getMaxValue());
        taskComponentFloat.setMaxValue(maxValue);
        taskComponentFloat.setScale(createRequest.getScale());
        return taskComponentFloat;
    }

    /**
     * Erstellt und befüllt ein TaskComponentInteger
     *
     * @param createRequest der Request
     * @return das TaskComponentInteger Objekt
     */
    private TaskComponentInteger getTaskComponentIntegerFromRequest(ComponentCreateRequest createRequest) {
        TaskComponentInteger taskComponentInteger = new TaskComponentInteger();
        setDefaultValuesFromRequest(taskComponentInteger, createRequest);

        int defaultValue = Integer.parseInt(createRequest.getDefaultValue());
        int minValue = Integer.parseInt(createRequest.getMinValue());
        int maxValue = Integer.parseInt(createRequest.getMaxValue());
        taskComponentInteger.setDefaultValue(defaultValue);
        taskComponentInteger.setMinValue(minValue);
        taskComponentInteger.setMaxValue(maxValue);

        return taskComponentInteger;
    }

    /**
     * Erstellt und befüllt ein TaskComponentDate
     *
     * @param createRequest der Request
     * @return das TaskComponentDate Objekt
     */
    private TaskComponentDate getTaskComponentDateFromRequest(ComponentCreateRequest createRequest) {
        TaskComponentDate taskComponentInteger = new TaskComponentDate();
        setDefaultValuesFromRequest(taskComponentInteger, createRequest);

        DateTime dt = dateTimeFormatter.parseDateTime(createRequest.getDefaultValue());
        Date defaultValue = dt.toDate();

        taskComponentInteger.setDefaultValue(defaultValue);

        return taskComponentInteger;
    }
}
