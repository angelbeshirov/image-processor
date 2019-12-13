package com.fmi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fmi.rest.deserializers.TaskDeserializer;

/**
 * @author angel.beshirov
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = TaskDeserializer.class)
public class Task {
    private Action action;
    private String fileName;

    public Task() {
    }

    public Task(Action action, String fileName) {
        this.action = action;
        this.fileName = fileName;
    }

    public Action getAction() {
        return action;
    }

    public String getFileName() {
        return fileName;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
