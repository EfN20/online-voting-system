package kz.astanait.edu.votingsystem.exceptions;

public class AuthorityNotFoundException extends RuntimeException {

    public AuthorityNotFoundException() {
        super("Authority was not found");
    }

    public AuthorityNotFoundException(String message) {
        super(message);
    }
}
