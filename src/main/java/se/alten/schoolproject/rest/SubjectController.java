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
@Path("/subjects")
public class SubjectController {

    @Inject
    private SchoolAccessLocal schoolAccessLocal;

    @GET
    @Produces({"application/JSON"})
    public Response listSubjects() {
            return Response.ok(schoolAccessLocal.listAllSubjects()).build();
    }

    @POST
    @Produces({"application/JSON"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSubject(String subject) {
        try {
            return Response.status(Response.Status.CREATED).entity(schoolAccessLocal.addSubject(subject)).build();
        }
        catch (DuplicateEntityException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
        catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Produces({"application/JSON"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add/student")
    public Response addStudent(String jsonData) {
        try {
            return Response.ok(schoolAccessLocal.addStudentToSubject(jsonData)).build();
        } catch (DuplicateEntityException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/remove/student")
    public Response removeStudent(String jsonData) {
        try {
            schoolAccessLocal.removeStudentFromSubject(jsonData);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeSubject(String jsonData) {
        try {
            schoolAccessLocal.removeSubject(jsonData);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
