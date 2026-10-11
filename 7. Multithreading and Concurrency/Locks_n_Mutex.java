import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


//ReentrantLock example
class TicketBooking{
    private int availableSeats = 1;
    private final ReentrantLock lock = new ReentrantLock();

    public void bookTicket(String user){
        System.out.println(user + " is trying to book a ticket.");
        lock.lock(); //blocks if already locked by another thread
        try{
            System.out.println(user + " has acquired the lock.");
            if(availableSeats > 0){
                System.out.println(user + " has booked a ticket.");
                availableSeats--;
            } else {
                System.out.println("No tickets available for " + user);
            }
        }
        finally{
            System.out.println(user + " is releasing the lock.");
            lock.unlock();
        }
    }
}

//ReentrantLock with expiry example
class ExpiringReentrantLock{
    private final ReentrantLock lock = new ReentrantLock();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private volatile boolean isLocked = false;

    public boolean tryLockWithExpiry(long timeoutMillis){
        boolean acquired = lock.tryLock();

        if(acquired){
            isLocked = true;

            //Schedule unlock after timeoutMillis
            scheduler.schedule(() -> {
                if(lock.isHeldByCurrentThread() || isLocked){
                    System.out.println("Lock expired, releasing lock.");
                    unlockSafely();    
                    isLocked = false;
                }
            }, timeoutMillis, TimeUnit.MILLISECONDS);
        }

        //logics for user

        return acquired;
    }

    public void unlockSafely(){
        if(lock.isHeldByCurrentThread() || isLocked){
            isLocked = false;
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
                System.out.println("Lock released by thread.");
            }
        }
    }
}


//trylock with timeout
class TicketBookingTryLock{
    private int availableSeats = 1;
    private final ReentrantLock lock = new ReentrantLock();

    public void  bookTicket(String user){
        System.out.println(user + " is trying to book a ticket.");
        boolean lockAcquired = false;

        try{
            //Wait 2 seconds to acquire the lock
            lockAcquired = lock.tryLock(2,TimeUnit.SECONDS);

            if(lockAcquired){
                System.out.println(user + " has acquired the lock.");
                if(availableSeats > 0){
                    System.out.println(user + " has booked a ticket.");
                    availableSeats--;
                } else {
                    System.out.println("No tickets available for " + user);
                }
            } else {
                System.out.println(user + " could not acquire the lock, try again later.");
            }
        }
        catch(InterruptedException e){
            System.out.println(user + " was interrupted while trying to acquire the lock.");
        }
        finally{
            if(lockAcquired){
                System.out.println(user + " is releasing the lock.");
                lock.unlock();
            }
        }
    }
}


//ReadWriteLock example

class StockData{
    private double price = 100.0;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void updatePrice(double newPrice){
        rwLock.writeLock().lock(); //nobody else can read or write while this thread is updating the price
        try{
            System.out.println(Thread.currentThread().getName() + " is updating the price to " + newPrice);
            price = newPrice;
        }
        finally{
            rwLock.writeLock().unlock();
        }
    }

    public void readPrice(){
        rwLock.readLock().lock(); //writer locks are blocked but not other readers
        try{
            System.out.println(Thread.currentThread().getName() + " is reading the price: " + price);
        }
        finally{
            rwLock.readLock().unlock();
        }
    }
}


class TUFPlusAccount {
    private final Semaphore deviceSlots;
 
    // constructor sets permit count
    public TUFPlusAccount(int maxDevices) {
        this.deviceSlots = new Semaphore(maxDevices);
    }
 
    // user attempts to log in
    public boolean login(String user) throws InterruptedException {
        System.out.println(user + " trying to log in...");
 
        // try to grab a permit immediately (non-blocking)
        if (deviceSlots.tryAcquire()) {
            System.out.println(user + " successfully logged in.");
            return true;
        } else {
            System.out.println(user + " denied login - too many devices.");
            return false;
        }
    }
 
    // user logs out and returns the permit
    public void logout(String user) {
        System.out.println(user + " logging out.");
        deviceSlots.release(); 
    }
}