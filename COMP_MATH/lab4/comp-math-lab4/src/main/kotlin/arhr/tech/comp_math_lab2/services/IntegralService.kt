package arhr.tech.comp_math_lab2.services

import arhr.tech.comp_math_lab2.api.models.SolveIntegralRequest
import arhr.tech.comp_math_lab2.api.models.SolveIntegralResponse
import arhr.tech.comp_math_lab2.data.EquationRepository
import arhr.tech.comp_math_lab2.dto.SolveIntegralRequestDto
import arhr.tech.comp_math_lab2.services.solvers.IntegralSolver
import arhr.tech.comp_math_lab2.services.solvers.SolveType
import arhr.tech.comp_math_lab2.utils.Equation
import org.springframework.stereotype.Service

@Service
class IntegralService(
    private val solvers: List<IntegralSolver>,
    private val equationRepository: EquationRepository
) {
    fun solve(request: SolveIntegralRequest): SolveIntegralResponse {
        val type: SolveType = SolveType.fromId(request.methodId)
        val solver = findSolver(type)
        val equation = equationRepository.getById(request.equationId)

        val dto = SolveIntegralRequestDto(equation = equation,
            a = request.a!!,
            b = request.b!!,
            eps = request.eps!!,
        )

        return solver.solve(dto)
    }

    fun findSolver(type: SolveType): IntegralSolver {
        solvers.forEach {
            if (it.supports(type)) return it
        }
        throw RuntimeException("Реализация решения для метода с ID ${type.id} отсутствует")
    }

    fun getEquations(): List<Equation> {
        return equationRepository.getAll();
    }
}