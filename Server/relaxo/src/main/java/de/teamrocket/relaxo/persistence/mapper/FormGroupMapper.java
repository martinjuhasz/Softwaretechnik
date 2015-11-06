package de.teamrocket.relaxo.persistence.mapper;

import de.teamrocket.relaxo.models.formgroup.TaskComponentOrder;
import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Interface fuer FormGroup-Statements an die Datenbank
 */
public interface FormGroupMapper {

    public FormGroup getFormGroupById(int formGroupId);

    public List<FormGroup> getAllFormGroups(int workflowId);

    public void createFormGroup(FormGroup formGroup);

    public void deleteFormGroup(int formGroupId);

    public List<FormGroup> getFormGroupsByTask(int taskId);

    public List<TaskComponent> getTaskComponentsForTaskAndFormGroup(@Param("taskId") int taskId, @Param("formGroupId") int formGroupId);

    public void updateComponentsOrder(@Param("components") List<TaskComponentOrder> components);
}
