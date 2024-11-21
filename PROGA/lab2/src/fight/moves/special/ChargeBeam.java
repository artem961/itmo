package fight.moves.special;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Stat;

/**
 * Charge Beam наносит урон и с вероятностью 70% повышает специальную атаку атакующего на одну ступень.
  */
public final class ChargeBeam extends SpecialMove{
    public ChargeBeam(){
        super(Type.ELECTRIC, 50, 90);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        Effect effect = new Effect();
        effect.stat(Stat.SPECIAL_ATTACK, +1);
        effect.chance(0.7);
        effect.turns(0);
        pokemon.addEffect(effect);
    }

    @Override
    protected String describe() {
        return "испепеляет лучами солнца";
    }
}
