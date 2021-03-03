package kz.astanait.edu.votingsystem.exceptions;

public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException() {
        super("Question was not found");
    }

    public QuestionNotFoundException(String message) {
        super(message);
    }
}
