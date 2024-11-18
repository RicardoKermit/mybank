package org.example.services;

import org.springframework.beans.factory.annotation.Value;
import org.example.dtos.CustomerDTO;
import org.example.exceptions.CustomerServiceUnexistingCustomerException;
import org.example.exceptions.CustomerServiceUnexpectedException;
import org.example.exceptions.MissingDataException;
import org.example.models.EffortRate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class RiskAnalysisService {

    @Value("${endpoints.customers-microservice.baseUrl}")
    private String addressUrl;


    public EffortRate getEffortRate(
            Integer customerId,
            float monthlyAverageIncome,
            float existingCreditsSum
    ) throws MissingDataException,
            CustomerServiceUnexpectedException,
            CustomerServiceUnexistingCustomerException {

        // variables
        CustomerDTO customerDTO;
        EffortRate effortRate;
        float effortRateValue;

        // load customer (RestClient version)
        customerDTO = getCustomer(customerId);

        // familyMembersNumber is required to proceed
        if (customerDTO.getFamilyMembersCount() == null) {
            throw new MissingDataException("familyMembersNumber");
        }

        // Compute effort rate
        effortRateValue = getEffortRate(
                customerDTO.getAge(),
                customerDTO.getFamilyMembersCount(),
                monthlyAverageIncome,
                existingCreditsSum
        );

        effortRate = new EffortRate();
        effortRate.setDateTime(LocalDateTime.now());
        effortRate.setCustomerId(customerId);
        effortRate.setName(customerDTO.getName());
        effortRate.setAddress(customerDTO.getAddress());
        effortRate.setAge(customerDTO.getAge());
        effortRate.setFamilyMembersCount(customerDTO.getFamilyMembersCount());
        effortRate.setMonthlyAverageIncome(monthlyAverageIncome);
        effortRate.setExistingCreditsSum(existingCreditsSum);
        effortRate.setEffortRate(effortRateValue);

        // Return effortRate
        return effortRate;

    }

    private CustomerDTO getCustomer(Integer customerId) throws CustomerServiceUnexistingCustomerException, MissingDataException,CustomerServiceUnexpectedException {
        CustomerDTO customerDTO;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<CustomerDTO> response= restTemplate.getForEntity(addressUrl+"/{customerid}",CustomerDTO.class,customerId);
            return response.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e){
            if (e.getStatusCode()== HttpStatus.BAD_REQUEST){
                throw new CustomerServiceUnexpectedException(e.getResponseBodyAsString());
            }else if (e.getStatusCode()== HttpStatus.NOT_FOUND){
                throw new CustomerServiceUnexistingCustomerException(e.getResponseBodyAsString());
            }else {
                throw new CustomerServiceUnexpectedException(e.getResponseBodyAsString());
            }
        }
    }

    private float getEffortRate(Integer age,
                                Integer familyMembersCount,
                                float monthlyAverageIncome,
                                float existingCreditsSum){
        return 0.15f *familyMembersCount;
    }

}
