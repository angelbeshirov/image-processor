package com.fmi.rest.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fmi.rest.serializers.ExtensionSerializer;

import javax.persistence.*;

/**
 * @author angel.beshirov
 */
@Entity
@Table(name = "extensions")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = ExtensionSerializer.class)
public class Extension {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String value;

    public Extension() {
    }

    public Extension(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Extension(Integer id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    @JsonGetter("id")
    public Integer getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter("value")
    public String getValue() {
        return value;
    }

    @JsonSetter("value")
    public void setValue(String value) {
        this.value = value;
    }
}
