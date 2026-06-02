package lab4.backend.api.utils.filters.auth;

import lab4.backend.services.exceptions.ServiceException;
import lombok.Getter;

import java.util.List;

@Getter
public class AuthorizationHeader {
    private String tokenType;
    private String token;

    public AuthorizationHeader(String header) {
        try {
            List<String> parsed = List.of(header.split("\\s"));
            this.tokenType = parsed.get(0);
            this.token = parsed.get(1);

            if (parsed.size() != 2 || this.tokenType == null || this.token == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ServiceException("Authorization header is invalid");
        }
    }
}
