/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tt
 */
public class TestTaskCondition {

    private ProcessEngine processEngine;

    @Before
    public void setUp() {
        processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        System.out.println("process engine:" + processEngine);
        processEngine.getRepositoryService().createDeployment().addClasspathResource("TaskConditon.bpmn").deploy();
        processEngine.getRuntimeService().startProcessInstanceByKey("myProcess_1");
    }

    @Test
    public void testNew() {
        doProcess("new", "new");
    }

    @Test
    public void testSample() {
        doProcess("sample", "sample");
    }

    @Test
    public void testOr() {
//        doProcess("aa","or");
        doProcess("bb", "or");
    }

    @After
    public void clearTask() {
        List<Task> taskList = processEngine.getTaskService().createTaskQuery().list();
        for (Task task : taskList) {
            deleteTask(task.getProcessInstanceId(), task.getId());
        }
    }

    private void doProcess(Object condition, String target) {
        Map<String, Object> varMap = getVarMap(condition);
        AssertsInterface assertsInterface = (task) -> {
            System.out.println("==============> Assert task name: " + task.getName());
            Assert.assertTrue("Asset First Task Name is " + target + ", current task name is " + task.getName(),
                    target.equals(task.getName()));
        };
        assertCondition(varMap, assertsInterface);
    }

    private void assertCondition(Map<String, Object> varMap, AssertsInterface assertsInterface) {
        Task task = getTask();
        System.out.println("==============> task name: " + task.getName());
        Assert.assertTrue("Asset First Task Name is one", "one".equals(task.getName()));
        //variesMap.put("key","bb"); //test using variables : bb
        completeTask(task.getId(), varMap);
        Task currentTask = getTask();
        assertsInterface.assertTarget(currentTask);
        //finish workflow
        completeTask(currentTask.getId(), null);
    }

    private Map<String, Object> getVarMap(Object targetValue) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", targetValue);
        return map;
    }

    private Task getTask() {
        return processEngine.getTaskService().createTaskQuery().singleResult();
    }

    private void completeTask(String taskId, Map<String, Object> variesMap) {
        processEngine.getTaskService().complete(taskId, variesMap);
    }

    private void deleteTask(String instanceId, String taskId) {
        processEngine.getRuntimeService().deleteProcessInstance(instanceId, "Test Cancel Task");
        processEngine.getTaskService().deleteTask(taskId);
    }

}
