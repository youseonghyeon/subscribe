package com.example.subscribify.exception;

import java.util.NoSuchElementException;

public class ApplicationNotFoundException extends NoSuchElementException {

    public ApplicationNotFoundException(Long appId) {
        super("Invalid application ID: " + appId);
    }
}
