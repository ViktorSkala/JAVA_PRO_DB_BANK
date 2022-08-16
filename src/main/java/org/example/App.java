package org.example;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static org.example.TypeCurrency.*;

public class App {
    public static final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    public static EntityManagerFactory emf;
    public static EntityManager em;

    public static void main( String[] args ) {
        Scanner sc = new Scanner(System.in);
        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("Bank");
            em = emf.createEntityManager();

            User user = new User("Viktor", "Skala");
            User user1 = new User("Viktor1", "Skala1");
            User user2 = new User("Viktor2", "Skala2");
            Account account = new Account(001, EUR, 100, user);
            Account account1 = new Account(002, USD, 200, user);
            Account account2 = new Account(101, EUR, 100, user1);
            Account account3 = new Account(202, USD, 200, user2);
            Account account4 = new Account(203, UAH, 300, user2);
            Exchange exchange = new Exchange("01.08.2022 09:00", 37, 38);
            Exchange exchange1 = new Exchange("02.08.2022 09:00", 38, 39);
            Exchange exchange2 = new Exchange("03.08.2022 09:00", 39, 40);
            Exchange exchange3 = new Exchange(df.format(new Date()), 37, 38);
            em.getTransaction().begin();
            em.persist(user);
            em.persist(user1);
            em.persist(user2);
            em.persist(account);
            em.persist(account1);
            em.persist(account2);
            em.persist(account3);
            em.persist(account4);
            em.persist(exchange);
            em.persist(exchange1);
            em.persist(exchange2);
            em.persist(exchange3);
            em.getTransaction().commit();

            System.out.println("______________________________________");
            TypeCurrency type = USD;
            String strType = "org.example.TypeCurrency." + type;
            System.out.println(strType);
            Query q = em.createQuery("SELECT a FROM Account a JOIN a.user u WHERE u.lastName='skala' AND a.typeCurrency=" + strType);
            System.out.println(q.getResultList().toString());

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> userRoot = cq.from(User.class);
            cq.multiselect(userRoot.get("firstName"));

            List<User> userList = em.createQuery(cq).getResultList();
            for (User us : userList)
                System.out.println(us.getFirstName());

            try {
                while (true) {
                    System.out.println("1: create client");
                    System.out.println("2: create account");
                    System.out.println("3: set exchange currency");
                    System.out.println("4: create transaction");
                    System.out.println("5: view all accounts");
                    System.out.println("6: view all transactions");
                    System.out.println("7: view all clients");
                    System.out.println("8: view all exchanges");
                    System.out.println("9: view balance in UAH of client");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            createClient(sc);
                            break;
                        case "2":
                            createAccount(sc);
                            break;
                        case "3":
                            createCurrency(sc);
                            break;
                        case "4":
                            createTransaction(sc);
                            break;
                        case "5":
                            viewAllAccounts(sc);
                            break;
                        case "6":
                            viewAllTransactions(sc);
                            break;
                        case "7":
                            viewAllUsers(sc);
                            break;
                        case "8":
                            viewAllExchanges(sc);
                            break;
                        case "9":
                            viewBalanceInUAH(sc);
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void createClient(Scanner sc) {
        System.out.println("Enter name of user");
        String sName = sc.nextLine();
        System.out.print("Enter last name of user: ");
        String sLastName = sc.nextLine();
        em.getTransaction().begin();
        try {
            Query q1 = em.createQuery("SELECT u FROM User u WHERE u.firstName = '" + sName + "' AND u.lastName = '" + sLastName + "'", User.class);
            User u1 = null;
            try {
                q1.getSingleResult();
                q1.getResultList();
                System.out.println("This client already exist");
                return;
            } catch (Exception e) {
                u1 = new User(sName, sLastName);
                em.persist(u1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("problem");
            em.getTransaction().rollback();
            ex.printStackTrace();
        }
    }

    private static void createCurrency(Scanner sc) {
        System.out.println("Enter actually currency for USD today:");
        String sUSD = sc.nextLine();
        int USD = Integer.parseInt(sUSD);
        System.out.println("Enter actually currency for EUR today:");
        String sEUR = sc.nextLine();
        int EUR = Integer.parseInt(sEUR);

        Exchange exchange1 = new Exchange("01.08.2022 09:00", 35, 36);
        Exchange exchange2 = new Exchange("02.08.2022 09:00", 36, 37);
        Exchange exchange3 = new Exchange("03.08.2022 09:00", 37, 38);
        Exchange exchange = new Exchange(df.format(new Date()),USD, EUR);
        em.getTransaction().begin();
        try {
            em.persist(exchange1);
            em.persist(exchange2);
            em.persist(exchange3);
            em.persist(exchange);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    private static void createAccount(Scanner sc) {
        int accNumb = 0;
        System.out.println("Enter name of user");
        String name = sc.nextLine();
        System.out.print("Enter last name of user: ");
        String lastName = sc.nextLine();

        Query q1 = em.createQuery("SELECT u FROM User u WHERE u.firstName = '" + name + "' AND u.lastName = '" + lastName + "'", User.class);
        User u1 = null;
        try {
            u1 = (User) q1.getSingleResult();
        } catch (Exception e) {
            System.out.println("Such client doesn't exist, please create new client or enter correct name and last name.");
            return;
        }
        System.out.print("Enter type of currency this account: UAH, EUR or USD. write your choice: ");
        String sTypeCurrency = sc.nextLine();
        TypeCurrency typeCurrency = null;
        while (true) {
            if (sTypeCurrency.equalsIgnoreCase("EUR")) {
                typeCurrency = EUR;
                break;
            } else if (sTypeCurrency.equalsIgnoreCase("USD")) {
                typeCurrency = USD;
                break;
            } else if (sTypeCurrency.equalsIgnoreCase("UAH")) {
                typeCurrency = UAH;
                break;
            }
        }
        System.out.println("if you want to enter account number manually - press 1, or press 2 to create account number in automatically mode.");
        String e = sc.nextLine();
        switch (e) {
            case "1":
                boolean b = false;
                boolean a = false;
                while (!a) {
                    while (!b) {
                        System.out.println("Enter new account number:");
                        String sAccNumb = sc.nextLine();
                        try {
                            accNumb = Integer.parseInt(sAccNumb);
                            b = true;
                        } catch (NumberFormatException ex) {
                            System.out.println("You enter account number not in number format, pls try again:");
                            b = false;
                        }
                    }
                    Query q2 = em.createQuery("SELECT a FROM Account a WHERE a.accNumber=" + accNumb, Account.class);
                    Account acc1 = null;
                    try {
                        acc1 = (Account) q2.getSingleResult();
                        System.out.println("This account number already exist.");
                        a = false;
                    } catch (Exception e1) {
                        a = true;
                    }
                }
                break;
            case "2":
                try {
                    accNumb = (int) em.createQuery("SELECT max (a.accNumber) FROM Account a").getSingleResult();
                    accNumb = accNumb + 1;
                } catch (Exception e2) {
                    accNumb = 1;
                }
                break;
        }
        Account account = new Account(accNumb, typeCurrency, u1);
        em.getTransaction().begin();
        try {
            em.persist(account);
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("problem");
            em.getTransaction().rollback();
            ex.printStackTrace();
        }
    }

    public static void createTransaction(Scanner sc) {
        System.out.println("If you want to:");
        System.out.println("\t refill you account - press 1");
        System.out.println("\t money transactions to another account - press 2");
        System.out.println("\t convert money in your account - press 3");
        System.out.println("Please, make your choice:");

        String s = sc.nextLine();
        switch (s) {
            case "1":
                refillAcc(sc);
                break;
            case "2":
                moneyTrans(sc);
                break;
            case "3":
                moneyConvert(sc);
                break;
            default:
                return;
        }
    }

    public static void refillAcc(Scanner sc) {
//        System.out.println("Enter type of currency for refill: EUR, USD or UAH");
//        String sType = sc.nextLine();
        System.out.println("Enter number of your account:");
        String sNumb = sc.nextLine();
        int numb = Integer.parseInt(sNumb);
        System.out.println("Enter the amount to refill:");
        String sAmount = sc.nextLine();
        int amount = Integer.parseInt(sAmount);
        Account acc3 = null;
        try {
            Query query = em.createQuery("select a from Account a where a.accNumber = " + numb);
            acc3 = (Account) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("not found such account");;
        }
        Transaction transaction = null;
        if (!(acc3 == null)) {
            try {
                em.getTransaction().begin();
                acc3.setAmount(acc3.getAmount() + amount);
                transaction = new Transaction(df.format(new Date()), acc3.getUser(), acc3);
                em.persist(transaction);
                em.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Transaction failed");
                em.getTransaction().rollback();
            }
        }
    }

    public static void moneyTrans(Scanner sc) {
//        System.out.println("Enter type of currency for refill: EUR, USD or UAH");
//        String sType = sc.nextLine();
        System.out.println("Enter number of your account:");
        String sNumbFrom = sc.nextLine();
        int numbFrom = Integer.parseInt(sNumbFrom);
        System.out.println("Enter number of account for send money:");
        String sNumbTo = sc.nextLine();
        int numbTo = Integer.parseInt(sNumbTo);
        System.out.println("Enter the amount to send:");
        String sAmount = sc.nextLine();
        int amount = Integer.parseInt(sAmount);
        Account accFrom = null;
        Account accTo = null;
        Transaction trans = null;
        try {
            Query query = em.createQuery("select ac from Account ac where ac.accNumber = " + numbFrom);
            accFrom = (Account) query.getSingleResult();
            System.out.println(accFrom.getTypeCurrency());
            Query query2 = em.createQuery("select ac from Account ac where ac.accNumber = " + numbTo);
            accTo = (Account) query2.getSingleResult();
            System.out.println(accTo.getTypeCurrency());
        } catch (Exception e) {
            System.out.println("not found such account");
        }
        if (!(accFrom == null) && !(accTo == null)) {
            if (accFrom.getTypeCurrency().equals(accTo.getTypeCurrency())) {
                if (accFrom.getAmount() > amount) {
                    System.out.println("you are going to send " + amount + accFrom.getTypeCurrency() + " to account number " + accTo.getAccNumber() + ". To confirm this action press Y, to decline press N");
                    String ans = sc.nextLine();
                    switch (ans) {
                        case "N":
                            break;
                        case "Y":
                            try {
                                em.getTransaction().begin();
                                accFrom.setAmount(accFrom.getAmount() - amount);
                                accTo.setAmount(accTo.getAmount() + amount);
                                trans = new Transaction(df.format(new Date()), accFrom.getUser(), accFrom, accTo);
                                em.persist(trans);
                                em.getTransaction().commit();
                            } catch (Exception e) {
                                System.out.println("Transaction failed");
                                em.getTransaction().rollback();
                            }
                    }
                } else {
                    System.out.println("not enough money at your account");
                    return;
                }
            } else {
                System.out.println("you select accounts with different type of currency");
            }
        } else return;
    }

    public static void moneyConvert(Scanner sc) {
        System.out.println("Enter number of your account:");
        String sNumbFrom = sc.nextLine();
        int numbFrom = Integer.parseInt(sNumbFrom);
        System.out.println("Enter type of currency to which you want convert your money: EUR, USD or UAH");
        String sType = sc.nextLine();
        Account accFrom1 = null;
        Account accTo1 = null;
        try {
            Query query = em.createQuery("SELECT a FROM Account a WHERE a.accNumber = " + numbFrom, Account.class);
            accFrom1 = (Account) query.getSingleResult();
            User user = accFrom1.getUser();
            String type = "org.example.TypeCurrency." + sType;
            Query query1 = em.createQuery("SELECT a FROM Account a JOIN a.user u WHERE u.id = " + user.getId() + " AND a.typeCurrency = " + type, Account.class);
            accTo1 = (Account) query1.getSingleResult();
        } catch (Exception e) {
            System.out.println("not found such account");
            return;
        }
        if (accFrom1.getTypeCurrency().equals(accTo1.getTypeCurrency())) {
            System.out.println("Type currency is the same.");
            return;
        }
        boolean b1 = false;
        double amount = 0;
        while (!b1) {
            System.out.println("Enter the amount to convert:");
            String sAmount = sc.nextLine();
            try {
                amount = Double.parseDouble(sAmount);
                b1 = true;
            } catch (NumberFormatException e) {
                System.out.println("enter amount in number format");
            }
        }
        Exchange ex = null;
        if (accFrom1.getAmount() > amount) {
            try {
                Query q4 = em.createQuery("SELECT e FROM Exchange e WHERE e.id=(SELECT MAX (e.id) FROM Exchange e)");
                ex = (Exchange) q4.getSingleResult();
                TypeCurrency typeCurrFrom = accFrom1.getTypeCurrency();
                TypeCurrency typeCurrTo = accTo1.getTypeCurrency();
                Field[] fields = ex.getClass().getDeclaredFields();
                double currFrom = 0;
                double currTo = 0;
                for (Field f : fields) {
                    if (typeCurrFrom.toString().equals(f.getName())) {
                        f.setAccessible(true);
                        currFrom = f.getDouble(ex);
                    }
                    if (typeCurrTo.toString().equals(f.getName())) {
                        f.setAccessible(true);
                        currTo = f.getDouble(ex);
                    }
                }
                em.getTransaction().begin();
                accFrom1.setAmount(accFrom1.getAmount() - amount);
                accTo1.setAmount(accTo1.getAmount() + amount*currFrom/currTo);
                Transaction transaction1 = new Transaction(df.format(new Date()), accFrom1.getUser(), accFrom1, accTo1, ex);
                em.persist(transaction1);
                em.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Transaction failed");
                em.getTransaction().rollback();
                e.printStackTrace();
            }
        } else {
            System.out.println("not enough money at your account");
            return;
        }
    }

    public static void viewBalanceInUAH(Scanner sc) {
        System.out.println("Enter name of client");
        String name = sc.nextLine();
        System.out.print("Enter last name of client: ");
        String lastName = sc.nextLine();

        Query q = em.createQuery("SELECT u FROM User u WHERE u.firstName = '" + name + "' AND u.lastName = '" + lastName + "'", User.class);
        User u1 = null;
        try {
            u1 = (User) q.getSingleResult();
        } catch (Exception e) {
            System.out.println("Such client doesn't exist, please enter correct information.");
            return;
        }
        double totalAmount = 0;
        Query q1 = em.createQuery("SELECT e FROM Exchange e WHERE e.id=(SELECT MAX (e.id) FROM Exchange e)");
        Exchange ex = (Exchange) q1.getSingleResult();
        Query q2 = em.createQuery("SELECT a FROM Account a JOIN a.user u WHERE u.id=" + u1.getId(), Account.class);
        List<Account> accountList = q2.getResultList();
        Field[] fields = ex.getClass().getDeclaredFields();
        for (Account a : accountList) {
            for (Field f : fields) {
                if (f.getName().equals(a.getTypeCurrency().name())) {
                    f.setAccessible(true);
                    try {
                        totalAmount += ((double) f.getInt(ex))*a.getAmount();
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        System.out.println("Total balance in UAH at all accounts of user " + u1.getFirstName() + " " + u1.getLastName() + " is: " + totalAmount + " UAH.");
    }

    public static void viewAllAccounts(Scanner sc) {
        Query query = em.createQuery("SELECT a FROM Account a", Account.class);
        List<Account> acc = (List<Account>) query.getResultList();
        System.out.println("view list of accounts");

        for (Account a : acc)
            System.out.println(a);

    }

    public static void viewAllTransactions(Scanner sc) {
        Query query = em.createQuery("SELECT t FROM Transaction t", Transaction.class);
        List<Transaction> tr = (List<Transaction>) query.getResultList();
        System.out.println("view list of transactions:");

        for (Transaction t : tr)
            System.out.println(t);

    }

    public static void viewAllUsers(Scanner sc) {
        Query query = em.createQuery("SELECT u FROM User u", User.class);
        List<User> us = (List<User>) query.getResultList();
        System.out.println("view list of users");

        for (User u : us)
            System.out.println(u);
    }

    public static void viewAllExchanges(Scanner sc) {
        Query query = em.createQuery("SELECT ex FROM Exchange ex", Exchange.class);
        List<Exchange> ex = (List<Exchange>) query.getResultList();
        System.out.println("view list of exchanges");

        for (Exchange e : ex)
            System.out.println(e);
    }
}