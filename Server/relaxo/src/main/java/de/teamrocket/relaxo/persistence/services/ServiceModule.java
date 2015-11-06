package de.teamrocket.relaxo.persistence.services;

import org.mybatis.guice.XMLMyBatisModule;

/**
 * Modul zum binden der Service-Klassen
 */
public class ServiceModule extends XMLMyBatisModule {
    @Override
    protected void initialize() {
        setEnvironmentId("development");
        setClassPathResource("mybatis-config.xml");
        bind(UserManagementService.class);
        bind(FormGroupService.class);
        bind(JobService.class);
        bind(WorkflowItemService.class);
        bind(WorkflowService.class);
    }
}
