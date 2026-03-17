package arhr.tech.comp_math_lab2.api.handlers

import arhr.tech.comp_math_lab2.api.models.SolveEquationRequest
import arhr.tech.comp_math_lab2.api.models.SolveEquationResponse
import jakarta.validation.ValidationException
import org.springframework.beans.factory.parsing.Problem
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<SolveEquationResponse> {
        val errors = e.bindingResult.fieldErrors.map { it.defaultMessage }
        val message = errors.joinToString("\n")

        return ResponseEntity(
            SolveEquationResponse(error = message),
            HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException::class)
    fun handleJsonException(e: Exception): ResponseEntity<String> {
        return ResponseEntity(
            "Некорректный формат данных в запросе",
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<SolveEquationResponse> {
        return ResponseEntity(
            SolveEquationResponse(error =  e.message),
            HttpStatus.BAD_REQUEST)
    }
}