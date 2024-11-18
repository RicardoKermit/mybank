package exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RiskAnalysisServiceUnexistingCustomerException extends RuntimeException {

    private String message;

    @Override
    public String getMessage() {
        return String.format("(CC) (LoanProposals Service-RiskAnalysisService-Unexisting Customer Exception): "+
                "RiskAnalysis service replied: %s", message);
    }
}