package com.scb.match.model;

import java.util.Date;
import java.util.Objects;

/**
 * @author DAO TUAN ANH (DENNIS).
 * @created on 14/7/16.
 */
public class Transaction {

    private String transactionId;
    private String accountId;
    private Date postingDate;
    private double amount;

    public Transaction() {

    }

    public Transaction(String transactionId, String accountId, Date postingDate, double amount) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.postingDate = postingDate;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Transaction))
            return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.getAmount(), getAmount()) == 0 &&
                Objects.equals(getTransactionId(), that.getTransactionId()) &&
                Objects.equals(getAccountId(), that.getAccountId()) &&
                Objects.equals(getPostingDate(), that.getPostingDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTransactionId(), getAccountId(), getPostingDate(), getAmount());
    }
}
