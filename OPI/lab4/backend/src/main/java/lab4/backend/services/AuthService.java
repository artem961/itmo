package lab4.backend.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import lab4.backend.dto.TokenDTO;
import lab4.backend.dto.TokenPairDTO;
import lab4.backend.dto.TokenPayloadDTO;
import lab4.backend.dto.UserDTO;
import lab4.backend.services.exceptions.ServiceException;
import lab4.backend.services.utils.annotations.ExceptionMessage;
import lab4.backend.services.utils.annotations.WrapWithServiceException;
import lab4.backend.utils.mapping.UserMapper;

@Stateless
@WrapWithServiceException
@ExceptionMessage
public class AuthService {
    @EJB
    private UserService userService;
    @EJB
    private TokenService tokenService;

    @Transactional
    @ExceptionMessage("Failed to authenticate user")
    public TokenPairDTO authenticate(UserDTO userDTO) {
        UserDTO bdUser = userService.getUserIfPasswordValid(userDTO);
        return tokenService.generateTokenPair(bdUser);
    }

    @Transactional
    public TokenPayloadDTO authorize(TokenDTO accessToken) {
        tokenService.validateToken(accessToken);
        if (tokenService.isAccessToken(accessToken)) {
            return tokenService.extractPayloadFromToken(accessToken);
        } else {
            throw new ServiceException("Token is not an access token");
        }
    }

    @Transactional
    public TokenPairDTO register(UserDTO userDTO) {
        userDTO = userService.createUser(userDTO);
        return tokenService.generateTokenPair(userDTO);
    }

    public TokenPairDTO refreshToken(TokenDTO refreshToken) {
        return tokenService.refreshToken(refreshToken);
    }

    public void logout(TokenDTO refreshToken) {
        tokenService.revokeToken(refreshToken);
    }

    @Transactional
    public UserDTO getUserByToken(TokenDTO token) {
        TokenPayloadDTO payload = tokenService.extractPayloadFromToken(token);
        return userService.findUserById(payload.getUserId());
    }
}
