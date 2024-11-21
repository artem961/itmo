package fight.pokemons;

import fight.moves.special.AcidSpray;
import fight.moves.special.Acid;
import fight.moves.special.ChargeBeam;
import fight.moves.status.ThunderWave;
import ru.ifmo.se.pokemon.Type;

public final class Eelektross extends Eelektrik {
    public Eelektross(String name, int level) {
        super(name, level);
        this.setType(Type.ELECTRIC);
        this.setStats(85, 115, 80, 105, 80, 50);
        this.setMove(
                new ChargeBeam(),
                new ThunderWave(),
                new AcidSpray(),
                new Acid());
    }
}
