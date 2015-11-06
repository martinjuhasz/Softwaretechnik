package de.teamrocket.relaxo.persistence.services;

import com.google.inject.Inject;
import de.teamrocket.relaxo.models.formgroup.TaskComponentOrder;
import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import de.teamrocket.relaxo.persistence.mapper.FormGroupMapper;
import org.mybatis.guice.transactional.Transactional;

import java.util.List;

/**
 * Service-Klasse zum Persistieren von FormGroup Daten
 */

public class FormGroupService {
    /**
     * FormGroupMapper-Interface
     */
    @Inject
    FormGroupMapper formGroupMapper;

    /**
     * Gibt eine FormGroup anhand einer Id zurück
     *
     * @param formGroupId ID der FormGroup, welche zurückgegeben werden soll
     * @return die FormGroup
     */
    @Transactional
    public FormGroup getFormGroupById(int formGroupId) {
        return formGroupMapper.getFormGroupById(formGroupId);
    }

    /**
     * Gibt eine Liste aller FormGroups eines Workflows zurück
     *
     * @param workflowId des Workflows, dessen FormGroups abgefragt werden
     * @return Liste der FormGroups
     */
    @Transactional
    public List<FormGroup> getAllFormGroups(int workflowId) {
        return formGroupMapper.getAllFormGroups(workflowId);
    }

    /**
     * Erstellt eine neue FormGroup
     *
     * @param formGroup Objekt, mit alle Daten zum Erstellen
     */
    @Transactional
    public void createFormGroup(FormGroup formGroup) {
        formGroupMapper.createFormGroup(formGroup);
    }

    /**
     * Loeschen einer FormGroup
     * @param formGroupId ID der FormGroup
     */
    @Transactional
    public void deleteFormGroup(int formGroupId) {
        formGroupMapper.deleteFormGroup(formGroupId);
    }

    /**
     * Gibt eine Liste von FromGroups fuer einen Task zurueck
     * @param taskId ID des uebergebene Task
     * @return Liste der FormGroups fuer den Task
     */
    @Transactional
    public List<FormGroup> getFormGroupsByTask(int taskId) {
        return formGroupMapper.getFormGroupsByTask(taskId);
    }

    /**
     * Gibt eine Liste von TaskComponents selektiert nach Task und FormGroup
     * @param taskId ID des uebergebenen Task
     * @param formGroupId ID der uebergebenen FormGroup
     * @return Liste der TaskComponents fuer den Task und die FormGroup
     */
    @Transactional
    public List<TaskComponent> getTaskComponentsForTaskAndFormGroup(int taskId, int formGroupId) {
        return formGroupMapper.getTaskComponentsForTaskAndFormGroup(taskId, formGroupId);
    }

    /**
     * Update der Reihenfolger der TaskComponents
     * @param components Liste der TaskComponents
     */
    @Transactional
    public void updateComponentsOrder(List<TaskComponentOrder> components) {
        formGroupMapper.updateComponentsOrder(components);
    }
}
