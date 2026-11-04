import java.util.LinkedList;

public class WaitingRoom {

    private int capacity; // capacity of the room that will be known from the user input

    //semaphore to manage synchronization
    // In (producer_consumer problems / Bounded Buffert)  or bounded buffer we need three semaphores
    private SemaphoreManager empty;  // counting semaphores to track empty chairs
    private SemaphoreManager full; // counting semaphores to track full chairs
    private SemaphoreManager mutex; // binary semaphore to avoid critical section and  race condition -happens when two threads or more can modify a shared resource- .
    // linked list to store patients (threads). why linked list ? LinkedList can insert , remove or access first and last element using its built-in functions
    // I also may change it if needed later
    private LinkedList<Patient> patients;


    // constructor to intialize the variables of the class
    public WaitingRoom(int capacity) {
        if (capacity <= 0) { // invalid input
            throw new IllegalArgumentException("Invalid value! Enter positive value");
        }
        // if value is valid intialize the variables
        this.capacity = capacity;
        patients = new LinkedList<>();
        
        empty = new SemaphoreManager(capacity);  // the room is initially empty / all seats are available
        full = new SemaphoreManager(0);  // initially, no waiting patiants
        mutex = new SemaphoreManager(1);  // one patient can modify or accss the waiting room at a time

    }

    //Producer_Consumer problem : Producer => patient , Consumer => doctor
    public void enter(Patient patient) throws InterruptedException {

        if (!empty.tryAcquire()) {
            System.out.println("The waiting room is full.\n" + patient + " is waiting outside for an available seat.");

            empty.acquire(); // acquire is a method in semaphore class that reserves one available seat for this patient
        }
            
        boolean inWaitingRoom = false; // to check whether the patient can em=nter the waiting room or not

        // handling when interruption while waiting for mutex but already acquired empty permit, we should return that reserved seat back
        try {
            mutex.acquire(); // acquire the lock  , so one thread ca execute at a time
        }
        catch (InterruptedException e) {
            empty.release();
            throw e;
        }
        
        try {
            patients.addLast(patient);
            inWaitingRoom = true;
            System.out.println(patient + " entered the waiting room.");
        }
        finally {
            mutex.release(); // release the lock , so othe r thread can resume execution
        }
        full.release(); // it wakes the doctor up saying that more patient is now waiting
    }
    //end of enter method


    // doctor acts as a consumer
    public Patient leave() throws InterruptedException {
        full.acquire();
        
        try {
            mutex.acquire(); // acquire the lock  , so one thread ca execute at a time
        }
        catch (InterruptedException e) {
            full.release();
            throw e;
        }

        Patient patient;
        boolean isTreated = false;
        
        try {
            patient = patients.removeFirst();
            isTreated = true;  
        }
        finally {
            mutex.release();
        }
        empty.release();

        return patient;
    }
    //end of leave method


    //this method to handle if the doctor can not acquire the treatment room in 3 sec , so the patient will be sent back 
    public void sendPatientBack(Patient patient) throws InterruptedException {
        
        // tryAcquire is a semaphore method, to try to acquire a resource without making the thread wait if the resource isn't available.
        // it returns true, when a seat is successfully taken. it returns false immediately, when no seats are available (reducing blocking until being available)
        if (!empty.tryAcquire()) {
            System.out.println("The waiting room is full.\n" + patient + " is waiting outside for an available seat.");

            empty.acquire();
        }
        
        try {
            mutex.acquire();
        }
        catch (InterruptedException e) {
            empty.release();
            throw e;
        }
        
        try {
            patients.addFirst(patient);
            System.out.println(patient + " entered the waiting room.");
        }
         finally {
            mutex.release();
        }
        full.release();
    }
    //end of sendPatientBack method

}//end of the class
