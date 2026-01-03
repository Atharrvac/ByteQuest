package net.codejava.model;

import java.security.NoSuchAlgorithmException;
import net.codejava.helper.SHA256;

public class Block {
    private String[] transaction;
    private String previousBlockHash;
    private String blockHash;

    public Block(String[] transaction, String previousBlockHash) throws NoSuchAlgorithmException {
        this.transaction = transaction;
        this.previousBlockHash = previousBlockHash;
        this.blockHash = SHA256.getSHA(transaction, previousBlockHash);
    }

    public String[] getTransaction() {
        return transaction;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public String getBlockHash() {
        return blockHash;
    }
}
