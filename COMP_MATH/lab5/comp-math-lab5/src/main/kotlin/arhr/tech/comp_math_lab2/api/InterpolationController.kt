package arhr.tech.comp_math_lab2.api

import arhr.tech.comp_math_lab2.api.models.InterpolationRequest
import arhr.tech.comp_math_lab2.api.models.InterpolationResponse
import arhr.tech.comp_math_lab2.services.InterpolationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
class InterpolationController(
    val interpolationService: InterpolationService
) {


    @PostMapping("/interpolate")
    fun solve(
        @Valid @RequestBody request: InterpolationRequest
    ): ResponseEntity<InterpolationResponse> {
        return ResponseEntity.ok(interpolationService.solve(request))
    }
}