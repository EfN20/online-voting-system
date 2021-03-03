package kz.astanait.edu.votingsystem.exceptions;

public class InterestNotFoundException extends RuntimeException {

    public InterestNotFoundException() {
        super("Interest was not found!");
    }

    public InterestNotFoundException(String message) {
        super(message);
    }
}
