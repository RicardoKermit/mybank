package org.example.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
    private Integer id;
    private String name;
    private String address;
    private int age;
    private Integer familyMembersCount;
}
