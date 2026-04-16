package arhr.tech.comp_math_lab2.services

import arhr.tech.comp_math_lab2.api.models.ApproximationRequest
import arhr.tech.comp_math_lab2.api.models.ApproximationResponse
import arhr.tech.comp_math_lab2.dto.ApproximationRequestDto
import arhr.tech.comp_math_lab2.dto.ApproximationResult
import arhr.tech.comp_math_lab2.dto.PointDto
import arhr.tech.comp_math_lab2.services.solvers.ApproximationSolver
import arhr.tech.comp_math_lab2.services.solvers.ApproximationType
import org.springframework.stereotype.Service

@Service
class ApproximationService(
    private val solvers: List<ApproximationSolver>,
) {
    fun solve(request: ApproximationRequest): ApproximationResponse {
        request.points.forEach { point -> requireNotNull(point.x){"Значение X не определено"}; requireNotNull(point.y){"Значение Y не определено"} }
        val dto = ApproximationRequestDto(
            request.points.map { it -> PointDto(it.x!!, it.y!!) }
                .toList())

        var results = mutableListOf<ApproximationResult>()
        for (type in ApproximationType.entries) {
            val solver = findSolver(type)
            try{
                results.add(solver.solve(dto))
            } catch (e: Exception){}
        }

        val best = (results.sortedBy { it.delta }).firstOrNull();
        return ApproximationResponse(
            results,
            best = best?.type,
            error = null
        )
    }

    fun findSolver(type: ApproximationType): ApproximationSolver {
        solvers.forEach {
            if (it.supports(type)) return it
        }
        throw RuntimeException("Реализация решения для метода с ID ${type.id} отсутствует")
    }
}