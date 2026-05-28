package arhr.tech.comp_math_lab2.api

import arhr.tech.comp_math_lab2.api.models.OdeRequest
import arhr.tech.comp_math_lab2.api.models.OdeResponse
import arhr.tech.comp_math_lab2.equations.EquationRegistry
import arhr.tech.comp_math_lab2.services.OdeService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ode")
@CrossOrigin(origins = ["*"])
class OdeController(
    private val odeService: OdeService,
    private val equationRegistry: EquationRegistry
) {

    @GetMapping("/equations")
    fun getEquations(): ResponseEntity<Map<Int, String>> {
        return ResponseEntity.ok(equationRegistry.getAllDescriptions())
    }

    @PostMapping("/solve")
    fun solve(@Valid @RequestBody request: OdeRequest): ResponseEntity<OdeResponse> {
        return ResponseEntity.ok(odeService.solve(request))
    }
}
