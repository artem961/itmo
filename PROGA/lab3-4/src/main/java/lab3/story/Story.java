package lab3.story;

public class Story {
    private final Sentence[] sentences;

    public Story(Sentence... sentences){
        this.sentences = sentences;
    }

    public void makeStory(){
        for (Sentence sentence: sentences) {
            System.out.println(sentence);
        }
    }
}