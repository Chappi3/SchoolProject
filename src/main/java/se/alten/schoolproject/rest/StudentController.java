package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.exceptions.BadRequestException;
import se.alten.schoolproject.exceptions.NotFoundException;
import se.alten.schoolproject.model.StudentModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@NoArgsConstructor
@Path("/student")
public class StudentController {

    @Inject
    private SchoolAccessLocal schoolAccessLocal;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response showStudents() {
        try {
            List students = schoolAccessLocal.listAllStudents();
            return Response.ok(students).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStudent(String studentModel) {
        try {
            StudentModel answer = schoolAccessLocal.addStudent(studentModel);
            return Response.status(Response.Status.CREATED).entity(answer).build();
        }
        catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeStudent(String jsonEmail) {
        try {
            String email = readJsonEmail(jsonEmail);
            schoolAccessLocal.removeStudent(email);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStudent(String jsonData) {
        try {
            String email = readJsonEmail(jsonData);
            String foreName = readJsonForeName(jsonData);
            String lastName = readJsonLastName(jsonData);
            if (foreName.isBlank() && lastName.isBlank()) {
                throw new BadRequestException("No data");
            }
            else {
                schoolAccessLocal.updateStudent(foreName, lastName, email);
                return Response.status(Response.Status.NO_CONTENT).build();
            }

        }
        catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

}