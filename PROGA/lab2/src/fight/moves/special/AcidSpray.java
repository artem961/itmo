package fight.moves.special;

import ru.ifmo.se.pokemon.*;

/**
 * Acid Spray наносит урон и снижает Специальную защиту цели на две ступени.
 */
public final class AcidSpray extends SpecialMove {
    public AcidSpray() {
        super(Type.POISON, 40, 100);
    }

    @Override
    protected String describe() {
        return "чихает кислотой в лицо";
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        Effect effect = new Effect();
        effect.stat(Stat.SPECIAL_DEFENSE, -2);
        effect.turns(0);
        effect.chance(1);
        pokemon.addEffect(effect);
    }
}
