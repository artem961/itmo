package fight.moves.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Stat;

/**
 * DoubleEdge наносит урон, но пользователь получает 1/3 от урона, нанесенного им.
 */
public final class DoubleEdge extends PhysicalMove {
    public DoubleEdge() {
        super(Type.NORMAL, 120, 100);
    }

    @Override
    protected void applySelfDamage(Pokemon pokemon, double damage) {
        pokemon.setMod(Stat.HP, (int)(damage / 3));
    }

    @Override
    protected String describe() {
        return "использует клинок ассасина";
    }
}
