package arhr.tech.comp_math_lab2.services

import arhr.tech.comp_math_lab2.api.models.InterpolationRequest
import arhr.tech.comp_math_lab2.api.models.InterpolationResponse
import arhr.tech.comp_math_lab2.dto.InterpolationRequestDto
import arhr.tech.comp_math_lab2.dto.InterpolationResult
import arhr.tech.comp_math_lab2.dto.PointDto
import arhr.tech.comp_math_lab2.services.solvers.InterpolationSolver
import arhr.tech.comp_math_lab2.services.solvers.InterpolationType
import org.springframework.stereotype.Service

@Service
class InterpolationService(
    private val solvers: List<InterpolationSolver>,
) {
    fun solve(request: InterpolationRequest): InterpolationResponse {
        request.points.forEach { point -> requireNotNull(point.x) { "Значение X не определено" }; requireNotNull(point.y) { "Значение Y не определено" } }
        val sortedPoints = request.points.sortedBy { it.x }
        val dto = InterpolationRequestDto(
            sortedPoints.map { it -> PointDto(it.x!!, it.y!!) }.toList(),
            request.x
        )

        var results = mutableListOf<InterpolationResult>()
        for (type in InterpolationType.entries) {
            val solver = findSolver(type)
            try {
                results.add(solver.solve(dto))
            } catch (e: Exception) {
                results.add(
                    InterpolationResult(
                        type = type,
                        message = e.message,
                        x = dto.x,
                        y = null
                    )
                )
            }
        }

        return InterpolationResponse(
            results = results,
            diffTable = InterpolationSolver.calculateFiniteDifferences(dto.points),
            error = null
        )
    }

    fun findSolver(type: InterpolationType): InterpolationSolver {
        solvers.forEach {
            if (it.supports(type)) return it
        }
        throw RuntimeException("Реализация решения для метода с ID ${type.id} отсутствует")
    }


}