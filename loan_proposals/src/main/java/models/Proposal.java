package models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@Setter
@Getter
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer customerId;
    private LocalDateTime proposalDate;
    private LocalDateTime proposalExpirationDate;
    private Integer customerAge;
    private Integer customerFamilyMembersCount;
    private float customerMonthlyAverageIncome;
    private float customerExistingCreditsSum;
    private float customerCurrentEffortRate;
    private float loanAmountRequested;
    private boolean pending;
    private boolean approved;


}