//package com.fmi.rest.model;
//
//import com.fasterxml.jackson.annotation.JsonGetter;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonSetter;
//
//import javax.persistence.*;
//import java.time.LocalDate;
//
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class OutputImage {
//
//    private Integer id;
//    private String name;
//    private String location;
//    private LocalDate uploadedOn;
//    private Integer uploadedBy;
//    private Long size;
//    private Integer extension;
//
//    public OutputImage() {
//    }
//
//    public OutputImage(final String name,
//                       final String location,
//                       final LocalDate uploadedOn,
//                       final Integer uploadedBy,
//                       final Long size,
//                       final Extension extension) {
//        this.name = name;
//        this.location = location;
//        this.uploadedOn = uploadedOn;
//        this.uploadedBy = uploadedBy;
//        this.size = size;
//        this.extension = extension;
//    }
//
//    public OutputImage(final Integer id,
//                       final String name,
//                       final String location,
//                       final LocalDate uploadedOn,
//                       final Integer uploadedBy,
//                       final Long size,
//                       final String email,
//                       final Integer extension) {
//        this.id = id;
//        this.name = name;
//        this.location = location;
//        this.uploadedOn = uploadedOn;
//        this.uploadedBy = uploadedBy;
//        this.size = size;
//        this.extension = extension;
//    }
//
//    @JsonGetter("id")
//    public Integer getId() {
//        return id;
//    }
//
//    @JsonGetter("name")
//    public String getName() {
//        return name;
//    }
//
//    @JsonGetter("location")
//    public String getLocation() {
//        return location;
//    }
//
//    @JsonGetter("uploadedOn")
//    public LocalDate getUploadedOn() {
//        return uploadedOn;
//    }
//
//    @JsonGetter("uploadedBy")
//    public Integer getUploadedBy() {
//        return uploadedBy;
//    }
//
//    @JsonGetter("size")
//    public Long getSize() {
//        return size;
//    }
//
//    @JsonGetter("extension")
//    public Integer getExtension() {
//        return extension;
//    }
//
//    @JsonSetter("id")
//    public void setId(final Integer id) {
//        this.id = id;
//    }
//
//    @JsonSetter("name")
//    public void setName(final String name) {
//        this.name = name;
//    }
//
//    @JsonSetter("location")
//    public void setLocation(final String location) {
//        this.location = location;
//    }
//
//    @JsonSetter("uploadedOn")
//    public void setUploadedOn(final LocalDate uploadedOn) {
//        this.uploadedOn = uploadedOn;
//    }
//
//    @JsonSetter("uploadedBy")
//    public void setUploadedBy(final Integer uploadedBy) {
//        this.uploadedBy = uploadedBy;
//    }
//
//    @JsonSetter("size")
//    public void setSize(final Long size) {
//        this.size = size;
//    }
//
//    @JsonSetter("extension")
//    public void setExtension(final Integer extension) {
//        this.extension = extension;
//    }
//}
