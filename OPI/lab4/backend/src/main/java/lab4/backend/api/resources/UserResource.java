package lab4.backend.api.resources;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lab4.backend.api.utils.annotations.ApiExceptionHandler;
import lab4.backend.api.utils.filters.auth.AuthorizationHeader;
import lab4.backend.api.utils.filters.auth.AuthorizedOnly;
import lab4.backend.dto.TokenDTO;
import lab4.backend.dto.UserDTO;
import lab4.backend.services.AuthService;
import lab4.backend.services.UserService;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApiExceptionHandler
@AuthorizedOnly
public class UserResource {
    @EJB
    private AuthService authService;

    @GET
    @Path("/me")
    public Response getUser(@HeaderParam("Authorization") String header){
        UserDTO userDTO = getUserFromHeader(header);
        return Response.ok(userDTO).build();
    }

    private UserDTO getUserFromHeader(String header) {
        AuthorizationHeader authHeader = new AuthorizationHeader(header);
        return authService.getUserByToken(TokenDTO.builder().token(authHeader.getToken()).build());
    }

}
