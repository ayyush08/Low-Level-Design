import java.util.Arrays;

class BankAccount{
    private final String name;
    private int balance;

    public BankAccount(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public synchronized void deposit(int amount) {
        balance += amount;
    }

    public synchronized void withdraw(int amount) {
        balance -= amount;
    }
}

class TransferTask implements Runnable{
    private final BankAccount fromAccount;
    private final BankAccount toAccount;
    private final int amount;

    public TransferTask(BankAccount fromAccount, BankAccount toAccount, int amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    @Override
    public void run() {
        synchronized (fromAccount) {
            System.out.println(Thread.currentThread().getName() + " locked " + fromAccount.getName());
            try{
                Thread.sleep(100); // Simulate some processing time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (toAccount) {
                System.out.println(Thread.currentThread().getName() + " locked " + toAccount.getName());
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
                System.out.println(Thread.currentThread().getName() + " transferred " + amount + " from " + fromAccount.getName() + " to " + toAccount.getName()); 
            }
        }
    }
}

class DeadlockExample {
    public static void main(String[] args) {
        BankAccount accountA = new BankAccount("Account A", 1000);
        BankAccount accountB = new BankAccount("Account B", 2000);


        //Transfer 100 from Account A to Account B in Thread 1
        Thread thread1 = new Thread(new TransferTask(accountA, accountB, 100), "Thread 1");
        //Transfer 200 from Account B to Account A in Thread 2
        Thread thread2 = new Thread(new TransferTask(accountB, accountA, 200), "Thread 2");

        thread1.start();
        thread2.start();


    }
}


//Lock Ordering

class LockOrderingSimple{
    static class Resource {
        int id;
        int value;

        public Resource(int id, int value) {
            this.id = id;
            this.value = value;
        }
    }


    public static void main(String[] args) {
        Resource r1 = new Resource(1, 100);
        Resource r2 = new Resource(2, 200);

        Runnable task1 = ()-> transfer(r1, r2, 50);
        Runnable task2 = ()-> transfer(r2, r1, 30);

        new Thread(task1, "Thread 1").start();
        new Thread(task2, "Thread 2").start();
    }

    public static void transfer(Resource from, Resource to, int amount) {
        Resource[] locks = new Resource[]{from, to};

        Arrays.sort(locks,(x,y)-> Integer.compare(x.id, y.id));

        synchronized (locks[0]) {
            System.out.println(Thread.currentThread().getName() + " locked Resource " + locks[0].id);
            try {
                Thread.sleep(100); // Simulate some processing time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (locks[1]) {
                System.out.println(Thread.currentThread().getName() + " locked Resource " + locks[1].id);
                from.value -= amount;
                to.value += amount;
                System.out.println(Thread.currentThread().getName() + " transferred " + amount + " from Resource " + from.id + " to Resource " + to.id);
            }
        }
    }
}


