package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.exceptions.BadRequestException;
import se.alten.schoolproject.exceptions.DuplicateEntityException;
import se.alten.schoolproject.exceptions.NotFoundException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@NoArgsConstructor
@Path("/student")
public class StudentController {

    @Inject
    private SchoolAccessLocal schoolAccessLocal;

    @GET
    @Produces({"application/JSON"})
    public Response showStudents() {
            return Response.ok(schoolAccessLocal.listAllStudents()).build();
    }

    @GET
    @Path("/find")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/JSON"})
    public Response findStudent(String jsonData) {
        try {
            return Response.ok(schoolAccessLocal.findStudent(jsonData)).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/JSON"})
    public Response addStudent(String studentJson) {
        try {
            return Response.status(Response.Status.CREATED).entity(schoolAccessLocal.addStudent(studentJson)).build();
        }
        catch (DuplicateEntityException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
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
            schoolAccessLocal.removeStudent(jsonEmail);
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
                schoolAccessLocal.updateStudent(jsonData);
                return Response.status(Response.Status.NO_CONTENT).build();
        }
        catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
