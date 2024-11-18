package org.example.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerDTO {

    private Integer id;
    private String name;
    private String address;
    private int age;
    private Integer familyMembersCount;

}
