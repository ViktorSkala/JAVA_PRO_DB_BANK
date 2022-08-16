package org.example;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REFRESH)
    private Set<Account> accountSet = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Transaction> transactionSet = new HashSet<>();

    public User() {
    }

    public User(String firstName) {
        this.firstName = firstName;
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Account> getAccountSet() { return accountSet; }

    public void setAccountSet(Set<Account> accountSet) { this.accountSet = accountSet; }

    public Set<Transaction> getTransactionSet() { return transactionSet; }

    public void setTransactionSet(Set<Transaction> transactionSet) { this.transactionSet = transactionSet; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accountSet=" + accountSet +
                '}';
    }
}
