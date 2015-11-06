package de.teamrocket.relaxo.models.job.jobtaskcomponent;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.joda.time.format.DateTimeFormatter;

/**
 * Eine Klasse zum Erzeugen neuer JobTaskComponentUpdateVisiors.
 */
@Singleton
public class JobTaskComponentUpdateVisitorFactory {

    private final DateTimeFormatter dateTimeFormatter;

    @Inject
    public JobTaskComponentUpdateVisitorFactory(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    /**
     * Erzeugt einen neuen JobTaskComponentUpdateVisitor mit dem gesetzten DateTimeFormatter
     * @return
     */
    public JobTaskComponentUpdateVisitor create() {
        return new JobTaskComponentUpdateVisitor(dateTimeFormatter);
    }
}
