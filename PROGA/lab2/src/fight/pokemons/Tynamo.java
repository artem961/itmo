package fight.pokemons;

import fight.moves.special.ChargeBeam;
import fight.moves.status.ThunderWave;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Tynamo extends Pokemon {
    public Tynamo(String name, int level){
        super(name, level);
        this.setType(Type.ELECTRIC);
        this.setStats(35, 55, 40, 45, 40 ,60);
        this.setMove(
                new ChargeBeam(),
                new ThunderWave());
    }
}
