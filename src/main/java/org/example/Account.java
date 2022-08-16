package org.example;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "currency", discriminatorType = DiscriminatorType.STRING)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer accNumber;

    private TypeCurrency typeCurrency;
    private double amount;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "accountFrom")
    private Set<Transaction> transactionSet = new HashSet<>();

    public Account() {
    }

    public Account(Integer accNumber, TypeCurrency typeCurrency, User user) {
        this.accNumber = accNumber;
        this.typeCurrency = typeCurrency;
        this.user = user;
    }

    public Account(Integer accNumber, TypeCurrency typeCurrency, int amount, User user) {
        this.accNumber = accNumber;
        this.typeCurrency = typeCurrency;
        this.amount = amount;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(Integer accNumber) {
        this.accNumber = accNumber;
    }

    public TypeCurrency getTypeCurrency() { return typeCurrency; }

    public void setTypeCurrency(TypeCurrency typeCurrency) { this.typeCurrency = typeCurrency; }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Transaction> getTransactionSet() {
        return transactionSet;
    }

    public void setTransactionSet(Set<Transaction> transactionSet) {
        this.transactionSet = transactionSet;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accNumber='" + accNumber + '\'' +
                ", type=" + typeCurrency +
                ", amount=" + amount +
                ", user=" + user.getFirstName() + " " + user.getLastName() +
                '}';
    }
}
