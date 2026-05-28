package arhr.tech.comp_math_lab2.services

import arhr.tech.comp_math_lab2.api.models.*
import arhr.tech.comp_math_lab2.equations.EquationRegistry
import arhr.tech.comp_math_lab2.solver.*
import org.springframework.stereotype.Service
import kotlin.math.abs

@Service
class OdeService(
    private val equationRegistry: EquationRegistry,
    private val solvers: List<ODESolver>
) {

    fun solve(request: OdeRequest): OdeResponse {
        require(request.xn > request.x0) { "xn must be strictly greater than x0" }
        
        val eq = equationRegistry.getEquation(request.equationId)

        val exactPoints = generateSequence(request.x0) { it + request.h }
            .takeWhile { it <= request.xn + 1e-9 }
            .map { x -> Point2D(x, eq.exactSolution(x, request.x0, request.y0)) }
            .toList()

        val seriesList = solvers.map { solver ->
            val sol = solver.solve(eq::f, request.x0, request.y0, request.xn, request.h, request.eps)
            val points = sol.xs.indices.map { Point2D(sol.xs[it], sol.ys[it]) }

            var rungeError: Double? = null
            if (solver.isOneStep) {
                val solHalf = solver.solve(eq::f, request.x0, request.y0, request.xn, request.h / 2.0, request.eps)
                val yH = sol.ys.last()
                val yHalf = solHalf.ys.last()
                rungeError = abs(yH - yHalf) / ((1 shl solver.order) - 1).toDouble()
            }

            val localErrors = sol.xs.indices.map { i ->
                abs(sol.ys[i] - eq.exactSolution(sol.xs[i], request.x0, request.y0))
            }

            val maxExactError = localErrors.maxOrNull() ?: 0.0

            val table = sol.xs.indices.map { i ->
                val x = sol.xs[i]
                val y = sol.ys[i]
                val yExact = try { eq.exactSolution(x, request.x0, request.y0) } catch (e: Exception) { null }
                val delta = yExact?.let { abs(y - it) }
                TableRow(x = x, y = y, yExact = yExact, delta = delta)
            }

            OdeResultSeries(
                methodName = solver.name,
                points = points,
                rungeError = rungeError,
                maxExactError = maxExactError,
                localErrors = localErrors,
                table = table
            )
        }
        return OdeResponse(
            exactPoints = exactPoints,
            series = seriesList
        )
    }
}
