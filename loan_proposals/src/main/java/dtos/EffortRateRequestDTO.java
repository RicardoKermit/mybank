package dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EffortRateRequestDTO {
    private Integer customerId;
    private float monthlyAverageIncome;
    private float existingCreditsSum;

}