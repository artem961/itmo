package lab4.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DotDTO {
    @NotNull
    private BigDecimal x;

    @NotNull
    private BigDecimal y;

    @NotNull
    @Min(value = 1)
    @Max(value = 6)
    private BigDecimal r;
}
