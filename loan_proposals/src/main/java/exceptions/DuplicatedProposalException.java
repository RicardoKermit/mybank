package exceptions;

public class DuplicatedProposalException extends RuntimeException {

    @Override
    public String getMessage() {
        return "(CC) (LoanProposals Service-Duplicated Proposal): " +
                "There cannot be two pending proposals for the same customer";
    }
}

