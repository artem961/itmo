package lab4.backend.services.utils.annotations;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import lab4.backend.services.exceptions.FatalServerException;
import lab4.backend.services.exceptions.ServiceException;
import lombok.extern.java.Log;

import java.lang.reflect.Method;
import java.util.List;

@Interceptor
@WrapWithServiceException
@Log
public class ServiceExceptionInterceptor {
    @AroundInvoke
    public Object handleException(InvocationContext ctx) {
        try {
            return ctx.proceed();
        } catch (Exception e) {
            ServiceException serviceException = findServiceExceptionInChain(e);
            if (serviceException != null) {
                throw serviceException;
            }

            ExceptionMessage annotation = getExceptionMessageAnnotation(ctx);
            String message = getMessage(annotation, ctx);
            throw new FatalServerException(message + ": " + e.getMessage(), e);
        }
    }

    private ExceptionMessage getExceptionMessageAnnotation(InvocationContext context) {
        ExceptionMessage methodAnnotation = context.getMethod()
                .getAnnotation(ExceptionMessage.class);

        if (methodAnnotation != null) {
            return methodAnnotation;
        }

        return context.getTarget().getClass()
                .getAnnotation(ExceptionMessage.class);
    }

    private String getMessage(ExceptionMessage annotation, InvocationContext context) {
        String message = annotation != null ? annotation.value() : "Service operation failed";

        if (message.equals("")) {
            message = autoGenerateMessage(context.getMethod());
        }
        return message;
    }

    private String autoGenerateMessage(Method method) {
        String name = method.getName();
        List<String> words = List.of(name.split("(?<=[a-z])(?=[A-Z])"));
        return "Failed to " + String.join(" ", words.stream().map(String::toLowerCase).toList());
    }

    private ServiceException findServiceExceptionInChain(Throwable e) {
        Throwable current = e;
        while (current != null) {
            if (current instanceof ServiceException) {
                return (ServiceException) current;
            }
            current = current.getCause();
        }
        return null;
    }
}
