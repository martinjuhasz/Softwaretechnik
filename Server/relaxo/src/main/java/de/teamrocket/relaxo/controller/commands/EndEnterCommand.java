package de.teamrocket.relaxo.controller.commands;

import de.teamrocket.relaxo.models.workflow.End;
import de.teamrocket.relaxo.persistence.services.JobService;

/**
 * Das Command, was beim Erreichen eines {@link End}s ausgeführt werden soll
 */
public class EndEnterCommand implements WorkflowItemCommand {

    // Vars

    /**
     * Instanz des JobServices.
     */
    private final JobService jobService;

    /**
     * ID des Jobs.
     */
    private int jobId;

    // Construct

    /**
     * Konstruktor des EndEnterCommand, welches die Job-ID und die Service-Klasse erwartet.
     *
     * @param jobId Die is des Jobs.
     * @param jobService Instanz des JobService.
     */
    public EndEnterCommand(int jobId, JobService jobService) {
        this.jobId = jobId;
        this.jobService = jobService;
    }

    // Methods

    /**
     * Den aktuellen Job abschließen
     */
    @Override
    public void execute() {
        jobService.finishJob(jobId);
    }
}
