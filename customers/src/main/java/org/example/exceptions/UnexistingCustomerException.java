package org.example.exceptions;

public class UnexistingCustomerException extends Exception{
    @Override
    public String getMessage() {
        return "(RS) Unexisting customer exception";
    }
}
