
package com.opengovtbd.service;

/** Thrown for any authentication or registration failure. */
public class AuthException extends RuntimeException {
    public AuthException(String message) { super(message); }
}
