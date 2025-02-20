package lab3;

import lab3.enumerations.*;
import lab3.persons.*;
import lab3.items.*;
import lab3.places.*;
import lab3.story.*;

public class Main{
    public static void main(String[] args) {

        //region Persons
        SeniorPomidor seniorPomidor = new SeniorPomidor("Синьор Помидор");
        Chipolino chipolino = new Chipolino("Чиполино");
        AllOtherPrisoners allOtherPrisoners = new AllOtherPrisoners("все остальные пленники");
        CompanyOfLemons companyOfLemons = new CompanyOfLemons("рота Лимончиков");
        Vishenka vishenka = new Vishenka("Вишенка");
        Limonishka limonishka = new Limonishka("Лимонишка");

        limonishka.setProfession("тюремщик");
        //endregion

        //region Places
        Attic attic = new Attic("чердак", RoomType.GROUND);
        Cell cell = new Cell("камера", RoomType.UNDERGROUND);
        Home home = new Home("дом", RoomType.GROUND);
        Prison prison = new Prison("тюрьма", RoomType.GROUND);

        cell.setLuminocity(Luminocity.NOLIGHT);
        //endregion

        //region Items
        Soup soup = new Soup(Ingridients.BREAD, Ingridients.WATER);
        RibbedBowl ribbedBowl = new RibbedBowl("щербатая миска");

        ribbedBowl.setContent(soup);
        //endregion

        // region Sentences
        Sentence sent1 = new Sentence(seniorPomidor.rejoice());
        sent1 = sent1.add(seniorPomidor.capture(chipolino), Delimiters.COMMA);
        sent1 = sent1.add(seniorPomidor.release(allOtherPrisoners, home), Delimiters.COMMA);

        Sentence sent2 = new Sentence(vishenka.lock(attic));

        Sentence sent3 = new Sentence(chipolino.send(prison, companyOfLemons));
        sent3 = sent3.add(chipolino.lock(cell), Delimiters.AND);

        Sentence sent4 = new Sentence(limonishka.bring(ribbedBowl, Period.TWICE_A_DAY));

        Sentence sent5 = new Sentence(chipolino.eat(soup));
        sent5 = sent5.add(chipolino.notSee());
        chipolino.setHungerLevel(HungerLevel.HUNGRY);
        sent5 = sent5.add(chipolino.getHungerLevel().getText(), Delimiters.FIRSTLY_BECAUSE);
        sent5 = sent5.add(cell.getLuminocity().getText(), Delimiters.SECONDLY_BECAUSE);
        //endregion

        Story story = new Story(sent1, sent2, sent3, sent4, sent5);
        story.makeStory();
    }
}