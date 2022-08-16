package org.example;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String excDate;

    private int USD;

    private int EUR;

    private final int UAH = 1;

    @OneToMany(mappedBy = "exchange")
    private Set<Transaction> transactionSet = new HashSet<>();

    public Exchange() {
    }

    public Exchange(String excDate, int USD, int EUR) {
        this.excDate = excDate;
        this.USD = USD;
        this.EUR = EUR;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExcDate() {
        return excDate;
    }

    public void setExcDate(String excDate) {
        this.excDate = excDate;
    }

    public int getUSD() {
        return USD;
    }

    public void setUSD(int USD) {
        this.USD = USD;
    }

    public int getEUR() {
        return EUR;
    }

    public void setEUR(int EUR) {
        this.EUR = EUR;
    }

    public int getUAH() { return UAH; }

    public Set<Transaction> getTransactionSet() {
        return transactionSet;
    }

    public void setTransactionSet(Set<Transaction> transactionSet) {
        this.transactionSet = transactionSet;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "id=" + id +
                ", excDate=" + excDate +
                ", USD=" + USD +
                ", EUR=" + EUR +
                '}';
    }
}
