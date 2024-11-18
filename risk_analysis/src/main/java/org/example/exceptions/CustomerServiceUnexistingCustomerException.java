package org.example.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class CustomerServiceUnexistingCustomerException extends Exception{
    private String receivedMessage;

    @Override
    public String getMessage(){
        return String.format("(RS) (RiskAnalysisService-CustomerService-Unexistingcustomer):"+"Customer service replied: %s", receivedMessage);
    }
}
