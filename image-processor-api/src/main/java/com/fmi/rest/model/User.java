package com.fmi.rest.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String email;

    public User() {
    }

    public User(final String username, final String password, final String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(final Integer id, final String username, final String password, final String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @JsonGetter("id")
    public Integer getId() {
        return id;
    }

    @JsonGetter("username")
    public String getUsername() {
        return username;
    }

    @JsonGetter("password")
    public String getPassword() {
        return password;
    }

    @JsonGetter("email")
    public String getEmail() {
        return email;
    }

    @JsonSetter("id")
    public void setId(final Integer id) {
        this.id = id;
    }

    @JsonSetter("username")
    public void setUsername(final String username) {
        this.username = username;
    }

    @JsonSetter("password")
    public void setPassword(final String password) {
        this.password = password;
    }

    @JsonSetter("email")
    public void setEmail(final String email) {
        this.email = email;
    }
}
