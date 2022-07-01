import java.util.*;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    private int roundsLeft;
    private boolean[] followees;
    private Integer lowestId;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this.roundsLeft = numRounds;
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees;
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        for (Transaction t : pendingTransactions) {
            if (lowestId == null || t.id < lowestId) {
                lowestId = t.id;
            }
        }
    }

    public Set<Transaction> sendToFollowers() {
        Set<Transaction> ret = new HashSet<>();
        if (lowestId != null) {
            ret.add(new Transaction(lowestId));
        }
        return ret;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        for (Candidate c : candidates) {
            if (lowestId == null || c.tx.id < lowestId) {
                lowestId = c.tx.id;
            }
        }
    }
}
