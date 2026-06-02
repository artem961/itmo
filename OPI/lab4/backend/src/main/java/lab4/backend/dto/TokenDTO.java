package lab4.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDTO {
    private Integer id;
    private String token;
    private Boolean revoked;
    private Duration maxAge;
    private UserDTO user;
}
