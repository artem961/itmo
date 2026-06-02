package lab4.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenPairDTO {
    public TokenDTO accessToken;
    public TokenDTO refreshToken;
    public String tokenType;

}
