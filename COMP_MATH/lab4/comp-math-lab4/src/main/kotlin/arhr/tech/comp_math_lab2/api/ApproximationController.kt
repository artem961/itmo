package arhr.tech.comp_math_lab2.api

import arhr.tech.comp_math_lab2.api.models.ApproximationRequest
import arhr.tech.comp_math_lab2.api.models.ApproximationResponse
import arhr.tech.comp_math_lab2.services.ApproximationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
class ApproximationController(
    val approximationService: ApproximationService
) {


    @PostMapping("/approx")
    fun solve(
        @Valid @RequestBody request: ApproximationRequest
    ): ResponseEntity<ApproximationResponse> {
        return ResponseEntity.ok(approximationService.solve(request))
    }
}