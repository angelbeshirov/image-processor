package com.fmi.rest.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String location;
    private LocalDate uploadedOn;
    private Integer uploadedBy;
    private Integer size;
    private Integer extension;

    public Image() {
    }

    public Image(String name,
                 String location,
                 LocalDate uploadedOn,
                 Integer uploadedBy,
                 Integer size,
                 Integer extension) {
        this.name = name;
        this.location = location;
        this.uploadedOn = uploadedOn;
        this.uploadedBy = uploadedBy;
        this.size = size;
        this.extension = extension;
    }

    public Image(Integer id,
                 String name,
                 String location,
                 LocalDate uploadedOn,
                 Integer uploadedBy,
                 Integer size,
                 String email,
                 Integer extension) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.uploadedOn = uploadedOn;
        this.uploadedBy = uploadedBy;
        this.size = size;
        this.extension = extension;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getUploadedOn() {
        return uploadedOn;
    }

    public Integer getUploadedBy() {
        return uploadedBy;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getExtension() {
        return extension;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUploadedOn(LocalDate uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public void setUploadedBy(Integer uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setExtension(Integer extension) {
        this.extension = extension;
    }
}
