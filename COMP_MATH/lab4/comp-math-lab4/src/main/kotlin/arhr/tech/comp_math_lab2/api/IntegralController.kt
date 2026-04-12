package arhr.tech.comp_math_lab2.api

import arhr.tech.comp_math_lab2.api.models.SolveIntegralRequest
import arhr.tech.comp_math_lab2.api.models.SolveIntegralResponse
import arhr.tech.comp_math_lab2.services.IntegralService
import arhr.tech.comp_math_lab2.services.solvers.SolveType
import arhr.tech.comp_math_lab2.utils.Equation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/integrals")
@CrossOrigin(origins = ["*"])
class IntegralController(
    val integralService: IntegralService
) {


    @PostMapping("/solve")
    fun solve(
        @Valid @RequestBody request: SolveIntegralRequest
    ): ResponseEntity<SolveIntegralResponse> {
        return ResponseEntity.ok(integralService.solve(request))
    }

    @GetMapping("/equations")
    fun getEquations(): ResponseEntity<List<Equation>> {
        return ResponseEntity.ok(integralService.getEquations())
    }

    @GetMapping("/methods")
    fun getMethods(): ResponseEntity<List<SolveType>> {
        return ResponseEntity.ok(SolveType.entries)
    }

}