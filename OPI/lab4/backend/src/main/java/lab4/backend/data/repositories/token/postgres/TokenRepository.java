package lab4.backend.data.repositories.token.postgres;

import lab4.backend.data.entities.TokenEntity;

import java.util.Optional;

public interface TokenRepository {
    public TokenEntity saveToken(TokenEntity token);
    public Optional<TokenEntity> findByToken(String token);
    public void delete(TokenEntity token);
    void update(TokenEntity token);
}
