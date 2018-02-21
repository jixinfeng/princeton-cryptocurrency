import java.util.ArrayList;
import java.util.Collections;

public class MaxFeeTxHandler extends TxHandler{
    /**
     * Create a class MaxFeeTxHandler whose handleTxs() method finds a set of
     * transactions with maximum total transaction fees -- i.e. maximize the
     * sum over all transactions in the set of (sum of input values - sum of
     * output values)).
     *
     * NOTE:
     * This code won't pass coursera grader due to lack of inheritance support
     */
    public MaxFeeTxHandler(UTXOPool utxoPool) {
        super(utxoPool);
    }

    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        ArrayList<Transaction> sortedTxs = new ArrayList<>();
        Collections.addAll(sortedTxs, possibleTxs);

        sortedTxs.sort((tx1, tx2) -> {
            double fee1 = calculateTxFee(tx1);
            double fee2 = calculateTxFee(tx2);
            return Double.compare(fee2, fee1);
        });

        return super.handleTxs((Transaction[]) sortedTxs.toArray());
    }

    /**
     * Calculate transaction fee by totalInput - totalOutput
     * @param tx transaction
     * @return transaction fee, in double
     */
    private double calculateTxFee(Transaction tx) {
        double totalInput = 0;
        double totalOutput = 0;

        for (Transaction.Input in : tx.getInputs()) {
            UTXO utxo = new UTXO(in.prevTxHash, in.outputIndex);
            if (!utxoPool.contains(utxo) || !isValidTx(tx)) { continue; }
            Transaction.Output prevOut = utxoPool.getTxOutput(utxo);
            totalInput += prevOut.value;
        }

        for (Transaction.Output out: tx.getOutputs()) {
            totalOutput += out.value;
        }
        return totalInput - totalOutput;
    }
}
