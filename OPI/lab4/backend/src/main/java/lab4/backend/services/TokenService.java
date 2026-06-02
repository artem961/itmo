package lab4.backend.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lab4.backend.configuration.JWTConfig;
import lab4.backend.data.entities.TokenEntity;
import lab4.backend.data.repositories.token.postgres.TokenRepository;
import lab4.backend.data.repositories.user.postgres.UserRepository;
import lab4.backend.dto.TokenDTO;
import lab4.backend.dto.TokenPairDTO;
import lab4.backend.dto.TokenPayloadDTO;
import lab4.backend.dto.UserDTO;
import lab4.backend.services.exceptions.ServiceException;
import lab4.backend.services.utils.annotations.ExceptionMessage;
import lab4.backend.services.utils.annotations.WrapWithServiceException;
import lab4.backend.utils.mapping.TokenMapper;
import lab4.backend.utils.mapping.UserMapper;
import lombok.extern.java.Log;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Stateless
@WrapWithServiceException
@ExceptionMessage
@Log
public class TokenService {
    @Inject
    private JWTConfig jwtConfig;

    @EJB
    private TokenRepository tokenRepository;

    @EJB
    private UserService userService;

    private Key signingKey;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
        this.jwtParser = Jwts.parser()
                .setSigningKey(signingKey)
                .requireIssuer(jwtConfig.getIssuer())
                .build();
    }

    public TokenPairDTO generateTokenPair(UserDTO userDTO) {
        TokenDTO accessToken = generateAccessToken(userDTO);
        TokenDTO refreshToken = generateRefreshToken(userDTO);

        tokenRepository.saveToken(TokenMapper.dtoToEntity(refreshToken));

        return TokenPairDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    public Boolean validateToken(TokenDTO token) {
        try {
            jwtParser.parseClaimsJws(token.getToken());
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public TokenPayloadDTO extractPayloadFromToken(TokenDTO token) {
        try {
            Claims claims = extractClaimsFromToken(token);

            return TokenPayloadDTO.builder()
                    .userId((Integer) claims.get("userId"))
                    .username((String) claims.get("username"))
                    .build();
        } catch (JwtException e) {
            throw new ServiceException("Invalid token: " + e.getMessage());
        }
    }

    public Claims extractClaimsFromToken(TokenDTO token) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token.getToken()).getBody();
            return claims;
        } catch (JwtException e) {
            throw new ServiceException("Invalid token: " + e.getMessage());
        }
    }

    @Transactional
    public TokenPairDTO refreshToken(TokenDTO refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new ServiceException("Invalid token");
        }

        if (!isRefreshToken(refreshToken)) {
            throw new ServiceException("Token is not a refresh token");
        }

        TokenDTO tokenDTO = findByToken(refreshToken.getToken());
        if (!tokenDTO.getRevoked()) {
            revokeToken(tokenDTO);
            UserDTO userDTO = userService.findUserById(tokenDTO.getUser().getId());
            return generateTokenPair(userDTO);
        } else {
            throw new ServiceException("Token is revoked");
        }
    }

    public void revokeToken(TokenDTO refreshToken) {
        TokenEntity entity = TokenMapper.dtoToEntity(findByToken(refreshToken.getToken()));
        entity.setRevoked(true);
        tokenRepository.update(entity);
    }

    @Deprecated
    private TokenDTO generateAccessToken(TokenPayloadDTO payload) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", "access");
        claims.put("userId", payload.getUserId());
        claims.put("username", payload.getUsername());

        Date expires = Date.from(Instant.now().plus(jwtConfig.getAccessTokenExpiration()));

        String token = Jwts.builder()
                .setClaims(claims)
                .issuer(jwtConfig.getIssuer())
                .setSubject(payload.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expires)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();


        return TokenDTO.builder()
                .token(token)
                .maxAge(jwtConfig.getAccessTokenExpiration())
                .build();
    }

    @Deprecated
    private TokenDTO generateRefreshToken(TokenPayloadDTO payload) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", "refresh");
        claims.put("userId", payload.getUserId());
        claims.put("username", payload.getUsername());

        Date expires = Date.from(Instant.now().plus(jwtConfig.getRefreshTokenExpiration()));

        String token = Jwts.builder()
                .setClaims(claims)
                .issuer(jwtConfig.getIssuer())
                .setSubject(payload.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expires)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        return TokenDTO.builder()
                .token(token)
                .maxAge(jwtConfig.getRefreshTokenExpiration())
                .build();
    }

    private TokenDTO generateRefreshToken(UserDTO userDTO) {
        TokenPayloadDTO payload = UserMapper.userDTOToTokenPayloadDTO(userDTO);

        TokenDTO refreshToken = generateRefreshToken(payload);
        refreshToken.setRevoked(false);
        refreshToken.setUser(userDTO);

        return refreshToken;
    }

    private TokenDTO generateAccessToken(UserDTO userDTO) {
        TokenPayloadDTO payload = UserMapper.userDTOToTokenPayloadDTO(userDTO);

        TokenDTO accessToken = generateAccessToken(payload);
        accessToken.setRevoked(false);
        accessToken.setUser(userDTO);

        return accessToken;
    }

    public boolean isRefreshToken(TokenDTO token) {
        try {
            Claims claims = extractClaimsFromToken(token);
            return "refresh".equals(claims.get("token_type"));
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isAccessToken(TokenDTO token) {
        try {
            Claims claims = extractClaimsFromToken(token);
            return "access".equals(claims.get("token_type"));
        } catch (JwtException e) {
            return false;
        }
    }

    public TokenDTO findByToken(String token) {
        TokenEntity tokenEntity = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ServiceException("Token not found"));
        return TokenMapper.entityToDTO(tokenEntity);
    }
}