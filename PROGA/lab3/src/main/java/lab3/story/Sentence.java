package lab3.story;

public record Sentence(String content) {

    public Sentence add(String text){
        return new Sentence(this.content + " " + text);
    }

    public Sentence add(String text, Delimiters delimiter){
        return new Sentence(this.content + " " + delimiter.getText() + " " + text);
    }

    @Override
    public String toString() {
        return this.content + ".";
    }
}
