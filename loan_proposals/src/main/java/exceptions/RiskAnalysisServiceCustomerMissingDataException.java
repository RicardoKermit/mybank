package exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RiskAnalysisServiceCustomerMissingDataException extends RuntimeException {

    private String message;

    @Override
    public String getMessage() {
        return String.format("(CC) (LoanProposals Service-RiskAnalysisService-CustomerMissingData): "+
                "RiskAnalysis service replied: %s", message);
    }
}