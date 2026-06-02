package lab4.backend.utils.mapping;

import lab4.backend.data.entities.TokenEntity;
import lab4.backend.dto.TokenDTO;
import lab4.backend.dto.TokenPairDTO;

public class TokenMapper {
    public static TokenDTO entityToDTO(TokenEntity tokenEntity){
        TokenDTO tokenDTO = TokenDTO.builder()
                .id(tokenEntity.getId())
                .revoked(tokenEntity.getRevoked())
                .token(tokenEntity.getToken())
                .build();

        if (tokenEntity.getUser() != null) {
            tokenDTO.setUser(UserMapper.entityToDTO(tokenEntity.getUser()));
        }

        return tokenDTO;
    }

    public static TokenEntity dtoToEntity(TokenDTO tokenDTO){
        TokenEntity tokenEntity = TokenEntity.builder()
                .id(tokenDTO.getId())
                .revoked(tokenDTO.getRevoked())
                .token(tokenDTO.getToken())
                .build();

        if(tokenDTO.getUser() != null){
            tokenEntity.setUser(UserMapper.dtoToEntity(tokenDTO.getUser()));
        }

        return tokenEntity;
    }
}
