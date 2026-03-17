package arhr.tech.comp_math_lab2.api

import arhr.tech.comp_math_lab2.api.models.SolveEquationRequest
import arhr.tech.comp_math_lab2.api.models.SolveEquationResponse
import arhr.tech.comp_math_lab2.services.EquationService
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
import kotlin.enums.EnumEntries

@RestController
@RequestMapping("/equations")
@CrossOrigin(origins = ["*"])
class EquationController(
    val equationService: EquationService
) {


    @PostMapping("/solve")
    fun solve(
        @Valid @RequestBody request: SolveEquationRequest
    ): ResponseEntity<SolveEquationResponse> {
        return ResponseEntity.ok(equationService.solve(request))
    }

    @GetMapping("/all")
    fun getEquations(): ResponseEntity<List<Equation>> {
        return ResponseEntity.ok(equationService.getEquations())
    }

    @GetMapping("/methods")
    fun getMethods(): ResponseEntity<List<SolveType>> {
        return ResponseEntity.ok(SolveType.entries)
    }

}