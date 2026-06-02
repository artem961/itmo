package lab4.backend.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.time.Duration;
import java.util.Optional;

@ApplicationScoped
@Log
@Data
@NoArgsConstructor
public class JWTConfig {
    private String secretKey;
    private String issuer;
    private Duration accessTokenExpiration;
    private Duration refreshTokenExpiration;

    @PostConstruct
    public void init() {
        this.secretKey = getSecretKeyFromEnv();
        this.issuer = "lab4-backend";
        this.accessTokenExpiration = Duration.ofMinutes(15);
        this.refreshTokenExpiration = Duration.ofDays(30);
    }

    private String getSecretKeyFromEnv() {
//        String secretKey = Optional.ofNullable(System.getenv("JWT_SECRET_KEY"))
//                .orElseGet(() -> {
//                    log.warning("JWT_SECRET_KEY not found in env! using default.");
//                    return "nobody-knows-this-very-secret-key";
//                });
        String secretKey = "secretKeykdfaadlsdsakllllllllllllllllllllllldadsoeqwpeopewqpewqoeowq";
        return secretKey;
    }
}
