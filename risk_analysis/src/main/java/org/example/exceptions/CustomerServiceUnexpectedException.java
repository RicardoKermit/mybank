package org.example.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomerServiceUnexpectedException extends Exception {
    private String receivedMessage;

    @Override
    public String getMessage(){
        return String.format("(RS) (RiskAnalysisService-CustomerService-UnexpectedException):"+"Customer service replied: %s", receivedMessage);
    }
}
