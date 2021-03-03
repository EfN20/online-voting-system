package kz.astanait.edu.votingsystem.exceptions;

public class OptionNotFoundException extends RuntimeException {
    public OptionNotFoundException() {
        super("Option was not found");
    }

    public OptionNotFoundException(String message) {
        super(message);
    }
}
