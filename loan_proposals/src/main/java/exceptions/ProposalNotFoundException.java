package exceptions;

public class ProposalNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "(CC) (Loan Proposals Service-Proposal NotFound): Proposal does not exist";
    }
}

