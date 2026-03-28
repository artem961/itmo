package arhr.tech.comp_math_lab2.services

import arhr.tech.comp_math_lab2.api.models.SolveEquationRequest
import arhr.tech.comp_math_lab2.api.models.SolveEquationResponse
import arhr.tech.comp_math_lab2.data.EquationRepository
import arhr.tech.comp_math_lab2.services.solvers.EquationSolver
import arhr.tech.comp_math_lab2.services.solvers.SolveType
import arhr.tech.comp_math_lab2.utils.Equation
import org.springframework.stereotype.Service

@Service
class EquationService(
    private val solvers: List<EquationSolver>,
    private val equationRepository: EquationRepository
) {
    fun solve(request: SolveEquationRequest): SolveEquationResponse {
        val type: SolveType = SolveType.fromId(request.methodId)
        val solver = findSolver(type)
        return solver.solve(request)
    }

    fun findSolver(type: SolveType): EquationSolver {
        solvers.forEach {
            if (it.supports(type)) return it
        }
        throw RuntimeException("Реализация решения для метода с ID ${type.id} отсутствует")
    }

    fun getEquations(): List<Equation> {
        return equationRepository.getAll();
    }
}