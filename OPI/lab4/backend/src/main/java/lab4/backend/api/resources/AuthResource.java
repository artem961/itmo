package lab4.backend.api.resources;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import lab4.backend.api.models.AccessTokenResponseModel;
import lab4.backend.api.utils.annotations.ApiExceptionHandler;
import lab4.backend.dto.TokenDTO;
import lab4.backend.dto.TokenPairDTO;
import lab4.backend.dto.UserDTO;
import lab4.backend.services.AuthService;
import lab4.backend.services.exceptions.ServiceException;
import lab4.backend.utils.mapping.PasswordHasher;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.java.Log;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApiExceptionHandler
@Log
public class AuthResource {
    @EJB
    private AuthService authService;

    @POST
    @Path("/login")
    public Response login(UserDTO userDTO) {
        userDTO = PasswordHasher.hashPassword(userDTO);
        TokenPairDTO tokenPairDTO = authService.authenticate(userDTO);
        return createTokenPairResponse(tokenPairDTO);
    }

    @POST
    @Path("/register")
    public Response register(UserDTO userDTO) {
        userDTO = PasswordHasher.hashPassword(userDTO);
        TokenPairDTO tokenPairDTO = authService.register(userDTO);
        return createTokenPairResponse(tokenPairDTO);
    }

    @POST
    @Path("/refresh")
    public Response refresh(@CookieParam("refresh-token") String refreshToken) {
        if (refreshToken == null) {
            throw new ServiceException("Refresh token not found");
        }
        TokenDTO tokenDTO = TokenDTO.builder().token(refreshToken).build();
        TokenPairDTO tokenPairDTO = authService.refreshToken(tokenDTO);

        return createTokenPairResponse(tokenPairDTO);
    }

    @POST
    @Path("/logout")
    public Response logout(@CookieParam("refresh-token") String refreshToken) {
            if (refreshToken == null) {
                throw new ServiceException("Refresh token not found");
            }

            TokenDTO tokenDTO = TokenDTO.builder()
                    .token(refreshToken)
                    .build();
            authService.logout(tokenDTO);
            return Response.noContent().build();
    }

    private NewCookie createCookie(TokenDTO token) {
        return new NewCookie.Builder("refresh-token")
                .value(token.getToken())
                .maxAge(Long.valueOf(token.getMaxAge().toSeconds()).intValue())
                .path("/")
                .sameSite(NewCookie.SameSite.LAX)
                .httpOnly(true)
                .build();
    }

    private Response createTokenPairResponse(TokenPairDTO tokenPairDTO) {
        NewCookie refreshTokenCookie = createCookie(tokenPairDTO.getRefreshToken());
        return Response
                .ok(AccessTokenResponseModel.fromDTO(tokenPairDTO))
                .cookie(refreshTokenCookie)
                .build();
    }


}
