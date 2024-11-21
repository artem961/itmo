package fight.moves.status;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

/**
 * Thunder Wave парализует противника.
 */
public final class ThunderWave extends StatusMove {
    public ThunderWave(){
        super(Type.ELECTRIC, 0, 90);
    }

    @Override
    protected String describe() {
        return "звонит Зевсу, чтоб вызвать удар молнией";
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        Effect.paralyze(pokemon);
    }
}
