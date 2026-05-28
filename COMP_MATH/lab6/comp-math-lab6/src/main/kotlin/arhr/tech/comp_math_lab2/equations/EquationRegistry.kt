package arhr.tech.comp_math_lab2.equations

import org.springframework.stereotype.Service


@Service
class EquationRegistry(equationsList: List<OdeEquation>) {
    
    private val equationsMap = equationsList.associateBy { it.id }

    fun getEquation(id: Int): OdeEquation {
        return equationsMap[id] ?: throw IllegalArgumentException("Unknown equation id: $id. Supported: ${equationsMap.keys}")
    }

    fun getAllDescriptions(): Map<Int, String> {
        return equationsMap.mapValues { it.value.stringRepresentation }
    }
}
