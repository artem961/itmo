package arhr.tech.comp_math_lab2.services

import arhr.tech.comp_math_lab2.api.models.SolveSystemRequest
import arhr.tech.comp_math_lab2.api.models.SolveSystemResponse
import arhr.tech.comp_math_lab2.data.SystemRepository
import arhr.tech.comp_math_lab2.utils.NewtonSystemSolver
import arhr.tech.comp_math_lab2.utils.EquationSystem
import org.springframework.stereotype.Service

@Service
class SystemService(
    val systemRepository: SystemRepository,
    val newtonSystemSolver: NewtonSystemSolver
) {

    fun getSystems(): List<EquationSystem> {
        return systemRepository.getAll()
    }

    fun solveSystem(request: SolveSystemRequest): SolveSystemResponse {
        val system = systemRepository.getById(request.systemId)
        val x0 = request.x0 ?: throw RuntimeException("Приближение x0 не указано")
        val y0 = request.y0 ?: throw RuntimeException("Приближение y0 не указано")
        val eps = request.eps ?: throw RuntimeException("Точность не указана")

        return newtonSystemSolver.solve(system, x0, y0, eps)
    }
}