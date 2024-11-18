package org.example.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MissingDataException extends Exception {

    private String receivedMessage;

    @Override
    public String getMessage(){
        return String.format("(RS) (RiskAnalysisService-MissingData):"+"Missing Data in customer : %s", receivedMessage);
    }
}
