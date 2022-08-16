package org.example;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @Temporal(TemporalType.DATE)
    private String transactionDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountFrom_id")
    private Account accountFrom;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountTo_id")
    private Account accountTo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exchange_id")
    private Exchange exchange;

    public Transaction() {
    }

    public Transaction(String transactionDate, User user, Account accountTo) {
        this.transactionDate = transactionDate;
        this.user = user;
        this.accountTo = accountTo;
    }

    public Transaction(String transactionDate, User user, Account accountFrom, Account accountTo) {
        this.transactionDate = transactionDate;
        this.user = user;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
    }

    public Transaction(String transactionDate, User user, Account accountFrom, Account accountTo, Exchange exchange) {
        this.transactionDate = transactionDate;
        this.user = user;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.exchange = exchange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account account) {
        this.accountFrom = account;
    }

    public Account getAccountTo() { return accountTo; }

    public void setAccountTo(Account accountTo) { this.accountTo = accountTo; }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public String toString() {
        String ans;
        if (accountFrom == null && exchange == null) {
            ans = "Transaction{" +
                    "id=" + id +
                    ", transactionDate=" + transactionDate +
                    ", user=" + user +
                    ", to account=" + accountTo.getAccNumber() +
                    '}';
            return ans;
        }
        if (exchange == null) {
                ans = "Transaction{" +
                        "id=" + id +
                        ", transactionDate=" + transactionDate +
                        ", user=" + user +
                        ", from account=" + accountFrom.getAccNumber() +
                        ", to account=" + accountTo.getAccNumber() +
                        '}';
                return ans;
            }
        ans = "Transaction{" +
                "id=" + id +
                ", transactionDate=" + transactionDate +
                ", user=" + user +
                ", from account=" + accountFrom.getAccNumber() +
                ", to account=" + accountTo.getAccNumber() +
                ", exchange=" + exchange +
                '}';
        return ans;
    }
}
