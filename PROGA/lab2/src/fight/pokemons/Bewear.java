package fight.pokemons;


import fight.moves.physical.DoubleEdge;
import fight.moves.physical.RockSlide;
import fight.moves.physical.ShadowClaw;
import fight.moves.status.Rest;
import ru.ifmo.se.pokemon.Type;

public class Bewear extends Stufful{
    public Bewear(String name, int level){
        super(name, level);
        this.setType(Type.NORMAL, Type.FIGHTING);
        this.setStats(120, 125, 80, 55, 60, 60);
        this.setMove(
                new RockSlide(),
                new Rest(),
                new DoubleEdge(),
                new ShadowClaw());

    }
}
