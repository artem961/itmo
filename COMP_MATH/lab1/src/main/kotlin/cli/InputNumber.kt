package com.example.cli

import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.runtime.Session

private const val MIN_N = 1
private const val MAX_N = 20

fun inputNumber(session: Session): Int {
    var n = 1
    var finishing = false

    session.section {

        if (finishing) return@section

        cyan()
        textLine("  ┌──────────────────────────────────────┐")
        textLine("  │           SELECT MATRIX SIZE         │")
        textLine("  │──────────────────────────────────────│")
        textLine("  │  ↑ ↓     Change value                │")
        textLine("  │  Enter   Confirm                     │")
        textLine("  └──────────────────────────────────────┘")
        white()

        textLine()

        val filled = n
        val empty  = MAX_N - n
        cyan(); text("  [")
        green(); text("█".repeat(filled))
        black(isBright = true); text("░".repeat(empty))
        cyan(); text("]")
        white()
        textLine("  N = $n")
        white()

    }.runUntilSignal {
        onKeyPressed {
            when (key) {
                Keys.UP    -> if (n < MAX_N) n++
                Keys.DOWN  -> if (n > MIN_N) n--
                Keys.ENTER -> { finishing = true; rerender(); signal() }
            }
            rerender()
        }
    }

    return n
}