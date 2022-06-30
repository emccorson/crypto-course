import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    private int numRounds;
    private int currentRound;
    private boolean[] followees;
    private Set<Transaction> pendingTransactions;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this.numRounds = numRounds;
        this.currentRound = 0;
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees;
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public Set<Transaction> sendToFollowers() {
        //Set<Transaction> send;

        //if (this.currentRound < this.numRounds) {
        //    // sending to followers
        //    send = new HashSet<>();
        //} else {
        //    // sending to simulator
        //    send = this.seen;
        //}
        //this.currentRound++;

        //return send;

        return this.pendingTransactions;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        for (Candidate c : candidates) {
            this.pendingTransactions.add(c.tx);
        }
    }
}
