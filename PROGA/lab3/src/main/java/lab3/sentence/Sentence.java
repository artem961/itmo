package lab3.sentence;

public record Sentence(StringBuilder content) {
    public void add(String text){
        content.append(text);
    }
    public void add(Delimiters delimiter, String text){
        content.append(delimiter.toString() + " " + text);
    }
}
