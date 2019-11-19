package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.exceptions.BadRequestException;
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
    @Path("{email}")
    public Response deleteUser( @PathParam("email") String email) {
        try {
            schoolAccessLocal.removeStudent(email);
            return Response.ok().build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    public Response updateStudent( @QueryParam("foreName") String foreName, @QueryParam("lastName") String lastName, @QueryParam("email") String email) {
        sal.updateStudent(forename, lastname, email);
            schoolAccessLocal.updateStudent(foreName, lastName, email);

    @PATCH
    public void updatePartialAStudent(String studentModel) {
        sal.updateStudentPartial(studentModel);
    }
}
