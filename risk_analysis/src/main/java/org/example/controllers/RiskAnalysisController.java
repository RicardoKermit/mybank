package org.example.controllers;

import org.example.dtos.EffortRateDTO;
import org.example.dtos.EffortRateRequestDTO;
import org.example.exceptions.CustomerServiceUnexistingCustomerException;
import org.example.exceptions.CustomerServiceUnexpectedException;
import org.example.exceptions.MissingDataException;
import org.example.models.EffortRate;
import org.example.services.RiskAnalysisService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RiskAnalysisController {

    @Autowired
    private RiskAnalysisService riskAnalysisService;

    private ModelMapper modelMapper;

    public RiskAnalysisController() {
        this.modelMapper = new ModelMapper();
    }

    @GetMapping("/landing")
    public ResponseEntity<String> landing() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("risk-analysis-landing servie is working fine");
    }


    @PostMapping("/current-effort-rate/{customerId}")
    public ResponseEntity<?> currentEffortRate(@PathVariable("customerId") Integer customerId,
                                               @RequestBody EffortRateRequestDTO effortRateRequestDTO) {
        // HttpStatus (produces)
        // 200 OK Request proceed as expected.
        // 400 BAD REQUEST
        // 404 NOT FOUND
        // 409 CONFLICT
        // Unexpected errors.
        // Customer record not found.
        // The variable id does not match with id in the body.
        // 412 PRECONDITION FAILED Customer has missing data (getFamilyMent)

        // variable
        EffortRate effortRate;
        // id must match object's id
        if (effortRateRequestDTO.getCustomerId() != customerId) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("(CC) (RiskAnalysisService-currentEffortRate): " + "Id inconsistency");
        }

        try {
            // effortRate computation
            effortRate = riskAnalysisService.getEffortRate(
                    customerId,
                    effortRateRequestDTO.getMonthlyAverageIncome(),
                    effortRateRequestDTO.getExistingCreditsSum());
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        } catch (CustomerServiceUnexistingCustomerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (CustomerServiceUnexpectedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        // return effortRate report
        return new ResponseEntity<>(modelMapper.map(effortRate, EffortRateDTO.class), HttpStatus.OK);
    }

}
