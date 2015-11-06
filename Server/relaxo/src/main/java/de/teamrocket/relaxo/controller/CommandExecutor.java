package de.teamrocket.relaxo.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.teamrocket.relaxo.controller.commands.WorkflowItemCommand;
//http://stackoverflow.com/questions/5378391/closing-a-blocking-queue
/**
 * Der CommandExecutor nimmt {@link WorkflowItemCommand#}s entgegen und führt sie sequentiell aus.
 */
public class CommandExecutor {
	
	private final ExecutorService executor;
	
	@Inject
	public CommandExecutor(@Named("CommandExecutorService")ExecutorService executor) {
		this.executor = executor;
	}
	
	/**
	 * Nimmt ein {@link WorkflowItemCommand} entgegen und speichert es für die spätere Ausführung.
	 * @param workflowItemCommand das {@link WorkflowItemCommand}, dass ausgeführt werden soll
	 * @return ein {@link Future} für den Status der Ausführung
	 */
	public Future<?> submit(final WorkflowItemCommand workflowItemCommand){
		
		return executor.submit(new Runnable() {
			@Override
			public void run() {
				workflowItemCommand.execute();
			}
		});
	}
	
	/**
	 * Beended den CommandExecutor nachdem alle entgegengenommen {@link WorkflowItemCommand}s abgearbeitet wurden.
	 * Es werden keine neuen {@link WorkflowItemCommand}s akzeptiert.
	 */
	public void shutdown() {
		executor.shutdown(); // gracefully shuts down the executor
    }
}
