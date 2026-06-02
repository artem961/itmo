package lab4.backend.utils.mapping;

import lab4.backend.data.entities.UserEntity;
import lab4.backend.dto.TokenPayloadDTO;
import lab4.backend.dto.UserDTO;

public class UserMapper {
    public static UserEntity dtoToEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();

        userEntity.setId(userDTO.getId());
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(userDTO.getPassword());

        return userEntity;
    }

    public static UserDTO entityToDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
       // userDTO.setPassword(userEntity.getPassword());

        return userDTO;
    }

    public static TokenPayloadDTO userDTOToTokenPayloadDTO(UserDTO userDTO) {
        TokenPayloadDTO tokenPayloadDTO = new TokenPayloadDTO();

        tokenPayloadDTO.setUserId(userDTO.getId());
        tokenPayloadDTO.setUsername(userDTO.getUsername());

        return tokenPayloadDTO;
    }

    public static UserDTO tokenPayloadDTOToUserDTO(TokenPayloadDTO tokenPayloadDTO) {
        return UserDTO.builder()
                .id(tokenPayloadDTO.getUserId())
                .username(tokenPayloadDTO.getUsername())
                .build();
    }
}


