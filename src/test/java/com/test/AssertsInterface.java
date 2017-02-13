package com.test;

import org.activiti.engine.task.Task;

/**
 * Created by hehe on 2017/2/13.
 */

@FunctionalInterface
public interface AssertsInterface {
    void assertTarget(Task task);
}
