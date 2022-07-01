import java.util.*;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    private int roundsLeft;
    private boolean[] followees;
    private Set<Transaction> pendingTransactions;
    private ArrayList<Integer> sent;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this.roundsLeft = numRounds;
        this.sent = new ArrayList<>();
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees;
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public Set<Transaction> sendToFollowers() {
        Set<Transaction> send;

        if (this.roundsLeft-- > 0) {
            for (Transaction t : this.pendingTransactions) {
                sent.add(t.id);
            }
            return this.pendingTransactions;
        } else {
            Set<Transaction> ret = new HashSet<>();

            switch (sent.size()) {
                case 0:
                    break;
                case 1:
                    ret.add(new Transaction(sent.get(0)));
                    break;
                default:
                    int lowestId = sent.get(0);
                    for (Integer i : sent) {
                        if (i < lowestId) {
                            lowestId = i;
                        }
                    }
                    ret.add(new Transaction(lowestId));
                    break;
            }

            return ret;
        }
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        this.pendingTransactions.clear();
        for (Candidate c : candidates) {
            this.pendingTransactions.add(c.tx);
        }
    }
}
