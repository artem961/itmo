package fight.moves.status;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Status;

/**
 * Пользователь спит в течение 2 оборотов, и полностью исцеляется.
 */
public final class Rest extends StatusMove {
    public Rest(){
        super(Type.PSYCHIC, 0, 10000);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        Effect effect = new Effect();
        effect.turns(2);
        effect.condition(Status.SLEEP);
        pokemon.setCondition(effect);
        pokemon.setMod(Stat.HP, -(int)pokemon.getStat(Stat.HP));
    }

    @Override
    protected String describe() {
        return "думает: бой боем, а сон по расписанию!";
    }
}
