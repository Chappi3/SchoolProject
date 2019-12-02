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
@Path("/teacher")
public class TeacherController {

    @Inject
    private SchoolAccessLocal schoolAccessLocal;

    @GET
    @Produces({"application/JSON"})
    public Response showTeachers() {
        return Response.ok(schoolAccessLocal.listAllTeachers()).build();
    }

    @GET
    @Path("/find")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/JSON"})
    public Response findTeacher(String jsonData) {
        try {
            return Response.ok(schoolAccessLocal.findTeacher(jsonData)).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/JSON"})
    public Response addTeacher(String teacherJson) {
        try {
            return Response.status(Response.Status.CREATED).entity(schoolAccessLocal.addTeacher(teacherJson)).build();
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
    public Response removeTeacher(String jsonEmail) {
        try {
            schoolAccessLocal.removeTeacher(jsonEmail);
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
    public Response updateTeacher(String jsonData) {
        try {
            schoolAccessLocal.updateTeacher(jsonData);
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
