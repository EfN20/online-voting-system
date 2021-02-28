package kz.astanait.edu.votingsystem.exceptions;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException() {
        super("Role was not found");
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
