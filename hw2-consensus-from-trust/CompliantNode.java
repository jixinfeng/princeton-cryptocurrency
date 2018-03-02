import java.util.Set;
import java.util.HashSet;

import static java.util.stream.Collectors.toSet;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    private double p_graph, p_malicious, p_txDistribution;
    private int numRounds;

    private boolean[] followees, blackList;

    private Set<Transaction> pendingTransactions;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this.p_graph = p_graph;
        this.p_malicious = p_malicious;
        this.p_txDistribution = p_txDistribution;
        this.numRounds = numRounds;
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees;
        this.blackList = new boolean[followees.length];
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public Set<Transaction> sendToFollowers() {
        Set<Transaction> txs = new HashSet<>(pendingTransactions);
        pendingTransactions.clear();
        return txs;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        Set<Integer> senders = candidates.stream().map(c -> c.sender).collect(toSet());

        for (int i = 0; i < followees.length; i++) {
            if (followees[i] && !senders.contains(i)) {
                blackList[i] = true;
            }
        }

        for (Candidate c: candidates) {
            if (!blackList[c.sender]) {
                pendingTransactions.add(c.tx);
            }
        }
    }
}
