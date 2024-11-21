package fight.moves.special;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Effect;

/**
 * Extrasensory наносит урон и с вероятностью 10% вызывает flinch у цели.
 */
public final class Extrasensory extends SpecialMove {
    public Extrasensory() {
        super(Type.PSYCHIC, 80, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        if (Math.random() <= 0.1) {
            Effect.flinch(pokemon);
        }
    }

    @Override
    protected String describe() {
        return "телепатит";
    }
}
