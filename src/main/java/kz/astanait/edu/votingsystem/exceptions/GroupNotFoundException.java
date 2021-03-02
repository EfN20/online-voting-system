package kz.astanait.edu.votingsystem.exceptions;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException() {
        super("Group was not found!");
    }

    public GroupNotFoundException(String message) {
        super(message);
    }
}
