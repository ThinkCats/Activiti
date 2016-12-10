/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tt
 */
public class TestActiviti {
        
        private ProcessEngine processEngine;
        
        @Before
        public void setUp(){
                processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
                System.out.println("process engine:" + processEngine);
        }
        
        @Test
        public void testEngine(){
                System.out.println("current engine:"+ processEngine);
        }
        
        @Test
        public void testDeploy(){
                processEngine.getRepositoryService().createDeployment().addClasspathResource("test.bpmn").deploy();
        }
        
        @Test
                public void testStartTask(){
                processEngine.getRuntimeService().startProcessInstanceByKey("_14813400799391");
        }
        
        @Test
        public void testCompleteTask(){
                processEngine.getTaskService().complete("17502"); 
        }
        
        @Test
        public void testDeleteTask(){
                processEngine.getRuntimeService().deleteProcessInstance("22501", "测试取消");
                processEngine.getTaskService().deleteTask("22504");
        }
}
