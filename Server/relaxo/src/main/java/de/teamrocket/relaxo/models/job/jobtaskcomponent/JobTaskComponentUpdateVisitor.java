package de.teamrocket.relaxo.models.job.jobtaskcomponent;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Ein Visitor für JobTaskComponents, der den String des newValue Attributs für ein konkretes 
 * JobTaskComponent richtig konvertiert und im JobTaskComponent setzt.
 */
public class JobTaskComponentUpdateVisitor implements JobTaskComponentVisitor {
    private final DateTimeFormatter dateTimeFormatter;

    /**
     * Der Wert, der richtig geparst, im JobTaskComponent gesetzt werden soll
     */
    private String newValue;

    /**
     * Erzeugt einen JobTaskComponentUpdateVisitor mit übergebenem DateTimeFormatter (zum Parsen des String values in ein Date).
     * @param dateTimeFormatter der DateTimeFormatter, der zum Parsen verwendet werden soll
     */
    public JobTaskComponentUpdateVisitor(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    /**
     * Parst das newValue Attribut in ein int und setzt es im übergebenen JobTaskComponentInteger
     */
    @Override
    public void visit(JobTaskComponentInteger jobTaskComponentInteger) {
        int newIntValue = Integer.parseInt(newValue);
        jobTaskComponentInteger.setValue(newIntValue);
    }

    /**
     * Setzt das newValue Attribut im übergebenen JobTaskComponentText
     */
    @Override
    public void visit(JobTaskComponentText jobTaskComponentText) {
        jobTaskComponentText.setValue(newValue);
    }

    /**
     * Parst das newValue Attribut in ein Date Objekt und setzt es im übergebenen JobTaskComponentDate
     */
    @Override
    public void visit(JobTaskComponentDate jobTaskComponentDate) {
        DateTime dt = dateTimeFormatter.parseDateTime(newValue);
        Date newDateValue = dt.toDate();
        jobTaskComponentDate.setValue(newDateValue);
    }

    /**
     * Parst das newValue Attribut in ein BigDecimal und setzt es im übergebenen JobTaskComponenFloat
     */
    @Override
    public void visit(JobTaskComponentFloat jobTaskComponentFloat) {
    	BigDecimal newFloatValue = new BigDecimal(newValue);
        jobTaskComponentFloat.setValue(newFloatValue);
    }

}
