package de.teamrocket.relaxo.models.job.jobtaskcomponent;


/**
 * Ein Visitor für JobTaskComponents, das (verschieden implementierte) Value Attribut eines JobTaskComponents ausliest
 * und als Object im Value Attribut des Visitors speichert.
 */
public class JobTaskComponentValueVisitor implements JobTaskComponentVisitor {

	/**
	 * Der Wert des Value Attributs des aufrufenden JobTaskComponents als Object
	 */
    private Object value;

    public Object getValue() {
        return value;
    }

    /**
     * Speichert den Wert des Value Attributs des übergebenen JobTaskComponentInteger im Value Attribut des Visitors
     */
    @Override
    public void visit(JobTaskComponentInteger jobTaskComponentInteger) {
        value = jobTaskComponentInteger.getValue();
    }

    /**
     * Speichert den Wert des Value Attributs des übergebenen JobTaskComponentText im Value Attribut des Visitors
     */
    @Override
    public void visit(JobTaskComponentText jobTaskComponentText) {
        value = jobTaskComponentText.getValue();
    }

    /**
     * Speichert den Wert des Value Attributs des übergebenen JobTaskComponentDate im Value Attribut des Visitors
     */
    @Override
    public void visit(JobTaskComponentDate jobTaskComponentDate) {
        value = jobTaskComponentDate.getValue();
    }

    /**
     * Speichert den double Wert des Value Attributs des übergebenen JobTaskComponentFloat im Value Attribut des Visitors
     */
    @Override
    public void visit(JobTaskComponentFloat jobTaskComponentFloat) {
        value = jobTaskComponentFloat.getValue().doubleValue();
    }

}
