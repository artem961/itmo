package arhr.tech.comp_math_lab2.api

import arhr.tech.comp_math_lab2.api.models.SolveSystemRequest
import arhr.tech.comp_math_lab2.api.models.SolveSystemResponse
import arhr.tech.comp_math_lab2.services.SystemService
import arhr.tech.comp_math_lab2.utils.EquationSystem
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems")
@CrossOrigin(origins = ["*"])
class SystemController(
    private val systemService: SystemService
) {
    @GetMapping("/all")
    fun getSystems(): ResponseEntity<List<EquationSystem>> {
        return ResponseEntity.ok().body(systemService.getSystems())
    }

    @PostMapping("/solve")
    fun solve(
        @Valid @RequestBody solveRequest: SolveSystemRequest
    ): ResponseEntity<SolveSystemResponse> {
        return ResponseEntity.ok(systemService.solveSystem(solveRequest))
    }
}