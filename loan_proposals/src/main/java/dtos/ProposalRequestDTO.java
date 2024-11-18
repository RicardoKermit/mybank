package dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProposalRequestDTO {
    private Integer customerId;
    private float customerMonthlyAverageIncome;
    private float customerExistingCreditsSum;
    private float loanAmountRequest;
}
