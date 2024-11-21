package fight.moves.special;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Stat;

/**
 * MoonBlast наносит урон и с вероятностью 30% снижает специальную атаку цели на одну ступень.
 */
public final class MoonBlast extends SpecialMove{
    public MoonBlast(){
        super(Type.FAIRY, 95, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        Effect effect = new Effect();
        effect.stat(Stat.SPECIAL_ATTACK, -1);
        effect.chance(0.3);
        effect.turns(0);
        pokemon.addEffect(effect);
    }

    @Override
    protected String describe() {
        return "роняет луну";
    }
}
