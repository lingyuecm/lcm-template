package com.lingyuecm.exception;

/**
 * Exception thrown when the project conventions are violated
 */
public class ConventionViolationException extends RuntimeException {
    /**
     * Carries a message telling the developer what conventions were violated
     */
    public ConventionViolationException(String message) {
        super(message);
    }
}
