package lab3.exceptions;

public class TooFewArguments extends Exception {
    public TooFewArguments(String message) {
        super(message);
    }

  @Override
  public String getMessage() {
    return "TooFewArguments " + super.getMessage();
  }
}
