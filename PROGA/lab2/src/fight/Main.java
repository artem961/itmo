package fight;

import ru.ifmo.se.pokemon.Battle;
import fight.pokemons.*;

public class Main {
    public static void main(String[] args) {
        Battle battle = new Battle();
        battle.addAlly(new Bewear("Мишаня", 54));
        battle.addAlly(new TapuBulu("Стрекоза lvl 2000", 1));
        battle.addAlly(new Stufful("Ugly Creature", 44));
        battle.addFoe(new Eelektrik("Электоугорь", 49));
        battle.addFoe(new Eelektross("Lohness monster", 1));
        battle.addFoe(new Tynamo("Рыбка", 1));
        battle.go();
    }
}
