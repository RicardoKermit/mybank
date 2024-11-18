package controllers;

import dtos.ProposalRequestDTO;
import models.Proposal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import services.ProposalsService;

@RestController
@RequestMapping("/loan-proposals")
public class ProposalsController {

    @Autowired
    private ProposalsService proposalsService;

    private ModelMapper modelMapper;

    public ProposalsController() {
        modelMapper = new ModelMapper();
    }

    @PostMapping("/loan-approval-request")
    public Mono<Proposal> loanApprovalRequest(@RequestBody ProposalRequestDTO proposalRequestDTO) {
        Proposal proposal = modelMapper.map(proposalRequestDTO, Proposal.class);
        return Mono.fromSupplier(() -> proposalsService.add(proposal));
    }

    @GetMapping("/landing")
    public Mono<String> landing() {
        return Mono.just("Loan proposals service is working fine...");
    }

    @GetMapping("/get-proposal/{id}")
    public Mono<Proposal> getProposal(@PathVariable Integer id) {
        return Mono.fromSupplier(() -> proposalsService.get(id));
    }

    @GetMapping("/get-all-proposals")
    public Flux<Proposal> getAllProposal() {
        return Flux.fromIterable(proposalsService.get());
    }
}