package fight.moves.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

/**
 * Shadow Claw наносит урон и имеет увеличенное соотношение критических попаданий (1/8 вместо 1/24).
 */
public final class ShadowClaw extends PhysicalMove {
    public ShadowClaw(){
        super(Type.GHOST, 70, 100);
    }

    @Override
    protected String describe() {
        return "чешет коготком";
    }

    @Override
    protected double calcCriticalHit(Pokemon pokemon, Pokemon pokemon1) {
        return super.calcCriticalHit(pokemon, pokemon1)*3;
    }
}
