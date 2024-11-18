package org.example.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class EffortRate {
    private LocalDateTime dateTime;
    private Integer customerId;
    private String name;
    private String address;
    private int age;
    private Integer familyMembersCount;
    private float monthlyAverageIncome;
    private float existingCreditsSum;
    private float effortRate;
}
