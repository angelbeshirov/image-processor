package com.fmi.rest.services;


import com.fmi.rest.UserRepository;
import com.fmi.rest.model.User;
import com.sun.research.ws.wadl.Response;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/users")
public class UserResource {

    @Autowired
    private UserRepository employeeRepository;

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public User getEmployee(@PathParam("id") int id) {
        return null;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response addUser(
            User employee, @Context UriInfo uriInfo) {

//        employeeRepository.addEmployee(new Employee(employee.getId(),
//                employee.getFirstName(), employee.getLastName(),
//                employee.getAge()));

        return null;
    }
}
