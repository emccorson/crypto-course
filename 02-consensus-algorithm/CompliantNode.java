import java.util.*;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    private int numRounds;
    private int currentRound;
    private boolean[] followees;
    private Set<Transaction> pendingTransactions;
    private Set<Transaction> sent;
    private Set<Candidate> candidates;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this.numRounds = numRounds;
        this.currentRound = 0;
        this.sent = new HashSet<>();
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees;
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public Set<Transaction> sendToFollowers() {
        Set<Transaction> send;

        if (this.currentRound < this.numRounds) {   // sending to followers
            send = this.pendingTransactions;
            sent.addAll(this.pendingTransactions);
        } else {                                    // sending to simulator
            ArrayList<Integer> array = new ArrayList<>();
            for (Transaction tx : this.sent) {
                array.add(tx.id);
            }
            Collections.sort(array);
            send = new HashSet<>();
            send.add(new Transaction(array.get(0)));
        }

        this.currentRound++;

        return send;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        this.pendingTransactions.clear();
        for (Candidate c : candidates) {
            this.pendingTransactions.add(c.tx);
        }
    }
}
