package fight.moves.special;

import ru.ifmo.se.pokemon.*;

/**
 * Acid наносит урон и с вероятностью 10% снижает Специальную защиту цели на одну ступень.
 */
public final class Acid extends SpecialMove {
    public Acid() {
        super(Type.POISON, 40, 100);
    }

    @Override
    protected String describe() {
        return "аккуратно капает кислотой";
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        Effect effect = new Effect();
        effect.stat(Stat.SPECIAL_DEFENSE, -1);
        effect.turns(0);
        effect.chance(0.1);
        pokemon.addEffect(effect);
    }
}
