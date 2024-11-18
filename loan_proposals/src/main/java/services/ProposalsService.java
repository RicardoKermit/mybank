package services;

import data.ProposalsRepository;
import dtos.EffortRateRequestDTO;
import dtos.EffortRateResponseDTO;
import exceptions.*;
import lombok.Setter;
import models.Proposal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
// Service
public class ProposalsService {

    // Autowired
    private ProposalsRepository proposalsRepository;

    @Value("${endpoints.risk-analysis-microservice.baseUrl}")
    private String addressUrl;

    public Proposal add(Proposal proposal) {
        // variables
        EffortRateResponseDTO effortRateResponseDTO;

        // duplicated proposal not allowed
        if (proposalsRepository.existsByCustomerIdAndPending(proposal.getCustomerId(), true)) {
            throw new DuplicatedProposalException();
        }

        // Load EffortRateDTO
        effortRateResponseDTO = getEffortRate(
                proposal.getCustomerId(),
                proposal.getCustomerMonthlyAverageIncome(),
                proposal.getCustomerExistingCreditsSum()
        );

        // fill data
        proposal.setProposalDate(LocalDateTime.now());
        proposal.setProposalExpirationDate(LocalDateTime.now().plusDays(7));
        proposal.setCustomerAge(effortRateResponseDTO.getAge());
        proposal.setCustomerFamilyMembersCount(effortRateResponseDTO.getFamilyMembersCount());
        proposal.setCustomerCurrentEffortRate(effortRateResponseDTO.getEffortRate());
        proposal.setPending(true);
        proposal.setApproved(false);

        // save the proposal
        return proposalsRepository.save(proposal);

    }

    public Proposal get(Integer id) {
        Proposal proposal= proposalsRepository
                .findById(id)
                .orElseThrow(() -> new ProposalNotFoundException());
        return proposal;
    }

    public List<Proposal> get() {
        return proposalsRepository.findAll();
    }


    private EffortRateResponseDTO getEffortRate(Integer customerId, float monthlyAverageIncome, float existingCreditsSum) {
        // variables
        WebClient webClient;
        ClientResponse clientResponse;
        EffortRateRequestDTO effortRateRequestDTO;
        EffortRateResponseDTO effortRateResponseDTO;

        // prepare request
        effortRateRequestDTO = new EffortRateRequestDTO(customerId,monthlyAverageIncome,existingCreditsSum);

        // prepare connection
        webClient = WebClient.builder()
                .baseUrl(addressUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        // post
        Mono<EffortRateResponseDTO> mono = webClient
                .post()
                .uri("/"+customerId)
                .body(BodyInserters.fromValue(effortRateRequestDTO))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> {
                            if (response.statusCode() == HttpStatus.NOT_FOUND) {
                                return response.bodyToMono(String.class).flatMap(body ->{
                                       return Mono.error(new RiskAnalysisServiceUnexistingCustomerException(body));
                                });
                            } else if (response.statusCode() == HttpStatus.PRECONDITION_FAILED) {
                                return response.bodyToMono(String.class).flatMap(body ->{
                                        return Mono.error(new RiskAnalysisServiceCustomerMissingDataException(body));
                                });
                            } else {
                                return response.bodyToMono(String.class).flatMap(body ->{
                                   return Mono.error(new RiskAnalysisServiceConnectionException(response.statusCode(),body));
                                });
                            }
                        })
                .bodyToMono(EffortRateResponseDTO.class);

        // send back EffortRateResponseDTO
        return mono.block();
    }


}