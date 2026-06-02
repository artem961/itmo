package lab4.backend.api.resources;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.container.TimeoutHandler;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lab4.backend.api.models.ResultResponseModel;
import lab4.backend.api.utils.annotations.ApiExceptionHandler;
import lab4.backend.api.utils.filters.auth.AuthorizationHeader;
import lab4.backend.api.utils.filters.auth.AuthorizedOnly;
import lab4.backend.dto.DotDTO;
import lab4.backend.dto.ResultDTO;
import lab4.backend.dto.TokenDTO;
import lab4.backend.dto.UserDTO;
import lab4.backend.services.AuthService;
import lab4.backend.services.ResultService;
import lab4.backend.services.VersionService;
import lab4.backend.services.utils.ResultsObserver;
import lab4.backend.utils.observer.ChangeListener;
import lab4.backend.utils.observer.Observer;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Path("/dots")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AuthorizedOnly
@ApiExceptionHandler
@Log
public class ResultResource {
    @EJB
    private ResultService resultService;

    @EJB
    private VersionService  versionService;

    @EJB
    private AuthService authService;

    @EJB
    private Observer resultsObserver;

    private List<AsyncResponse> waitingClients;

    {
        waitingClients = Collections.synchronizedList(new ArrayList<>());
    }

    @PostConstruct
    private void init() {
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void onChange() {
                String lastModified = versionService.getCurrentVersion("results");
                List<ResultResponseModel> resultsModels = resultService.getAllResults().stream()
                        .map(ResultResponseModel::fromResultDTO)
                        .toList();

                waitingClients.forEach(asyncResponse -> {
                    asyncResponse.resume(Response
                            .ok(new GenericEntity<List<ResultResponseModel>>(resultsModels) {})
                            .header("ETag", lastModified)
                            .build());
                });
                waitingClients.clear();
            }
        };

        resultsObserver.registerChangeListener(changeListener);
    }


    @POST
    @Path("/check")
    public ResultResponseModel checkHit(@Valid DotDTO dotDTO, @HeaderParam("Authorization") String header) {

        UserDTO userDTO = getUserFromHeader(header);
        ResultDTO resultDTO = resultService.checkHit(dotDTO, userDTO);

        return ResultResponseModel.fromResultDTO(resultDTO);
    }

    @GET
    @Path("/all/me")
    public List<ResultResponseModel> getAllMyResults(@HeaderParam("Authorization") String header) {
        UserDTO userDTO = getUserFromHeader(header);
        List<ResultDTO> results = resultService.getAllResultsForUser(userDTO);
        List<ResultResponseModel> resultsModels = results.stream()
                .map(ResultResponseModel::fromResultDTO)
                .toList();
        return resultsModels;
    }

    @GET
    @Path("/all")
    public Response getAllResults(@HeaderParam("If-None-Match") String ifNoneMatch) {
        String lastModified = versionService.getCurrentVersion("results");

        if (ifNoneMatch == null || !ifNoneMatch.equals(lastModified)) {
            List<ResultDTO> results = resultService.getAllResults();
            List<ResultResponseModel> resultsModels = results.stream()
                    .map(ResultResponseModel::fromResultDTO)
                    .toList();

            return Response
                    .ok(new GenericEntity<List<ResultResponseModel>>(resultsModels) {})
                    .header("ETag", lastModified)
                    .build();
        } else {
            return Response
                    .notModified()
                    .build();
        }
    }

    @GET
    @Path("/all/poll")
    public void pollAllResults(@Suspended AsyncResponse asyncResponse) {
        asyncResponse.setTimeout(15000, TimeUnit.MILLISECONDS);
        asyncResponse.setTimeoutHandler(new TimeoutHandler() {
            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.status(Response.Status.NO_CONTENT).build());
                waitingClients.remove(asyncResponse);
            }
        });
        waitingClients.add(asyncResponse);
    }

    @DELETE
    @Path("/all")
    public Response deleteAllResults(@HeaderParam("Authorization") String header) {
        UserDTO userDTO = getUserFromHeader(header);
        resultService.deleteAllResultsForUser(userDTO);
        return Response
                .noContent()
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteResult(@PathParam("id") Integer id, @HeaderParam("Authorization") String header) {
        UserDTO userDTO = getUserFromHeader(header);
        resultService.deleteResultById(userDTO, id);
        return Response
                .noContent()
                .build();
    }

    private UserDTO getUserFromHeader(String header) {
        AuthorizationHeader authHeader = new AuthorizationHeader(header);
        return authService.getUserByToken(TokenDTO.builder().token(authHeader.getToken()).build());
    }
}
