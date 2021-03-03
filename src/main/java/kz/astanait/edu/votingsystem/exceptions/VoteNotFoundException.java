package kz.astanait.edu.votingsystem.exceptions;

public class VoteNotFoundException extends RuntimeException {

    public VoteNotFoundException() {
        super("Vote was not found");
    }

    public VoteNotFoundException(String message) {
        super(message);
    }
}
