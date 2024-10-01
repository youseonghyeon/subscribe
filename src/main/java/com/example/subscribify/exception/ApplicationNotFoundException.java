package com.example.subscribify.exception;

import java.util.NoSuchElementException;

public class ApplicationNotFoundException extends NoSuchElementException {

    public ApplicationNotFoundException() {
        super();
    }

    public ApplicationNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

    public ApplicationNotFoundException(Throwable cause) {
        super(cause);
    }

    public ApplicationNotFoundException(String s) {
        super(s);
    }

    public ApplicationNotFoundException(Long appId) {
        super("Invalid application ID: " + appId);
    }
}
