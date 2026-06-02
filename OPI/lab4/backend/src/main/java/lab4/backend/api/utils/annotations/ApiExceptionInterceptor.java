package lab4.backend.api.utils.annotations;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lab4.backend.services.exceptions.FatalServerException;
import lab4.backend.services.exceptions.ServiceException;
import lab4.backend.services.utils.annotations.ExceptionMessage;
import lombok.extern.java.Log;

@Interceptor
@ApiExceptionHandler
@Log
public class ApiExceptionInterceptor {
    @AroundInvoke
    public Object handleException(InvocationContext ctx) throws Exception {
        try {
            return ctx.proceed();
        } catch (ServiceException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
