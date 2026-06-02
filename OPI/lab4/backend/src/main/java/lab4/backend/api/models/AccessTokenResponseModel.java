package lab4.backend.api.models;

import lab4.backend.dto.TokenPairDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessTokenResponseModel {
    private String accessToken;
    private String tokenType;

    public static AccessTokenResponseModel fromDTO(TokenPairDTO tokenPairDTO) {
        return AccessTokenResponseModel.builder()
                .accessToken(tokenPairDTO.getAccessToken().getToken())
                .tokenType(tokenPairDTO.getTokenType())
                .build();
    }
}
