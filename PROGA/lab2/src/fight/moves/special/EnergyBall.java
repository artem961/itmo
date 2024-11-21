package fight.moves.special;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Stat;

/**
 * Energy Ball наносит урон и с вероятностью 10% снижает специальную защиту цели на одну ступень.
 */
public final class EnergyBall extends SpecialMove{
    public EnergyBall(){
        super(Type.GRASS, 90, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        Effect effect = new Effect();
        effect.stat(Stat.SPECIAL_DEFENSE, -1);
        effect.chance(0.1);
        effect.turns(0);
        pokemon.addEffect(effect);
    }

    @Override
    protected String describe() {
        return "метает энергетический шар";
    }
}
