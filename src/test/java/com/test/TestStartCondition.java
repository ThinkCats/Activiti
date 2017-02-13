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
 * Created by hehe on 2017/2/13.
 */
public class TestStartCondition {
    private ProcessEngine processEngine;

    @Before
    public void setUp() {
        processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        processEngine.getRepositoryService().createDeployment().addClasspathResource("StartCondition.bpmn").deploy();
    }

    @Test
    public void testStartA(){
        doStart("a","taskA");
    }

    @Test
    public void testStartB(){
//        doStart("b","taskBC");
        doStart("c","taskBC");
    }

    @Test
    public void testStartD(){
        doStart("d","taskD");
    }

    @Test
    public void testProcessTrue(){
        doProcess(true,"taskTrue");
    }

    @Test
    public void testProcessFalse(){
        doProcess(false,"taskFalse");
    }

    @After
    public void clearTask() {
        List<Task> taskList = processEngine.getTaskService().createTaskQuery().list();
        for (Task task : taskList) {
            deleteTask(task.getProcessInstanceId(), task.getId());
        }
    }

    private void doStart(Object condition,String target){
        Map<String, Object> varMap = getVarMap(condition);
        processEngine.getRuntimeService().startProcessInstanceByKey("myProcess_2",varMap);
        AssertsInterface assertsInterface = (task) -> {
            System.out.println("==============> Assert start task name: " + task.getName());
            Assert.assertTrue("Asset Task Name is " + target + ", current task name is " + task.getName(),
                    target.equals(task.getName()));
        };
        Task task = getTask();
        assertsInterface.assertTarget(task);
    }

    private void doProcess(Object condition, String target) {
        doStart("a","taskA"); //start using task taskA
        Task task = getTask();
        completeTask(task.getId(),null);

        AssertsInterface assertsInterface = (t) -> {
            System.out.println("==============> Assert task name: " + t.getName());
            Assert.assertTrue("Asset First Task Name is " + target + ", current task name is " + t.getName(),
                    target.equals(t.getName()));
        };
        Map<String, Object> varMap = getVarMap(condition);
        Task currentTask = getTask();
        completeTask(currentTask.getId(),varMap);

        Task latestTask = getTask();
        assertsInterface.assertTarget(latestTask);
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
