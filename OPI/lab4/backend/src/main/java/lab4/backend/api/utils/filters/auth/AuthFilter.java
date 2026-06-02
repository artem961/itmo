package lab4.backend.api.utils.filters.auth;

import jakarta.annotation.Priority;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lab4.backend.dto.TokenDTO;
import lab4.backend.dto.TokenPayloadDTO;
import lab4.backend.services.AuthService;
import lab4.backend.services.exceptions.ServiceException;
import lombok.Getter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.List;

@Provider
@Priority(Priorities.AUTHENTICATION)
@AuthorizedOnly
@Log
public class AuthFilter implements ContainerRequestFilter {
    @EJB
    private AuthService authService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        try {
            AuthorizationHeader authHeader =
                    new AuthorizationHeader(containerRequestContext.getHeaderString("Authorization"));

            if (authHeader.getTokenType().equalsIgnoreCase("Bearer")) {
                processBearerToken(containerRequestContext, authHeader.getToken());
            } else {
                abortWithUnauthorized(containerRequestContext, "Token type is not supported");
            }
        } catch (Exception e) {
            abortWithUnauthorized(containerRequestContext, e.getMessage());
        }
    }

    private void processBearerToken(ContainerRequestContext containerRequestContext, String bearerToken) {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(bearerToken);
        TokenPayloadDTO payloadDTO = authService.authorize(tokenDTO);
    }

    private void abortWithUnauthorized(ContainerRequestContext containerRequestContext, String message) {
        containerRequestContext.abortWith(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(message)
                .build());
    }
}
