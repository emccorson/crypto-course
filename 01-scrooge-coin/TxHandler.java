import java.util.ArrayList;

public class TxHandler {

    private UTXOPool utxoPool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        this.utxoPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool,
     * (2) the signatures on each input of {@code tx} are valid,
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        double totalOutputValue = 0;
        double totalInputValue = 0;

        ArrayList<Transaction.Input> inputs = tx.getInputs();
        ArrayList<UTXO> seen = new ArrayList<>();
        Transaction.Input input;

        for (int i = 0; i < inputs.size(); i++) {
            input = inputs.get(i);

            // check output is unspent
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            if (!utxoPool.contains(utxo)) {
                return false;
            }

            // check output isn't claimed twice in this tx
            if (seen.contains(utxo)) {
              return false;
            }
            seen.add(utxo);

            // check signature is valid
            Transaction.Output output = utxoPool.getTxOutput(utxo);
            if (!Crypto.verifySignature(output.address, tx.getRawDataToSign(i), input.signature)) {
                return false;
            }
            totalInputValue += output.value;
        }

        // check output values are all non-negative
        for (Transaction.Output op : tx.getOutputs()) {
            if (op.value < 0) {
                return false;
            }
            totalOutputValue += op.value;
        }

        // check total out less than total in
        if (totalOutputValue > totalInputValue) {
            return false;
        }

        return true;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        ArrayList<Transaction> chosen = new ArrayList<>();

        Transaction tx;
        byte[] hash;
        ArrayList<Transaction.Input> inputs;
        Transaction.Output op;

        for (int i = 0; i < possibleTxs.length; i++) {
            tx = possibleTxs[i];

            if (isValidTx(tx)) {
                hash = tx.getHash();
                inputs = tx.getInputs();

                for (Transaction.Input ip : tx.getInputs()) {
                    utxoPool.removeUTXO(new UTXO(ip.prevTxHash, ip.outputIndex));
                };

                for (int j = 0; j < tx.numOutputs(); j++) {
                    utxoPool.addUTXO(new UTXO(hash, j), tx.getOutput(j));
                }

                chosen.add(tx);
            }
        }

        Transaction[] ret = new Transaction[chosen.size()];
        for (int k = 0; k < ret.length; k++) {
          ret[k] = chosen.get(k);
        }

        return ret;
    }

}
