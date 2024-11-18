package org.example.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EffortRateRequestDTO {

    private Integer customerId;
    private float monthlyAverageIncome;
    private float existingCreditsSum;

}
