package dtos;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EffortRateResponseDTO {
    private LocalDateTime dateTime;
    private Integer id;
    private int age;
    private Integer familyMembersCount;
    private float monthlyAverageIncome;
    private float existingCreditsSum;
    private float effortRate;
}