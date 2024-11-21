package fight.pokemons;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import fight.moves.special.ChargeBeam;
import fight.moves.special.MoonBlast;
import fight.moves.special.EnergyBall;
import fight.moves.special.Extrasensory;

public final class TapuBulu extends Pokemon {
    public TapuBulu(String name, int level) {
        super(name, level);
        this.setType(Type.GRASS, Type.FAIRY);
        this.setStats(70, 130, 115, 85, 95, 75);
        this.setMove(
                new ChargeBeam(),
                new MoonBlast(),
                new EnergyBall(),
                new Extrasensory());
    }
}
