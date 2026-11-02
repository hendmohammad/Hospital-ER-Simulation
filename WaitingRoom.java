import java.util.LinkedList;

public class WaitingRoom {

    private int capacity; // capacity of the room that will be known from the user input

    //semaphore to manage synchronization
    // In (producer_consumer problems / Bounded Buffert)  or bounded buffer we need three semaphores
    private SemaphoreManager empty;  // counting semaphores to track empty chairs
    private SemaphoreMenager full; // counting semaphores to track full chairs
    private SemaphoreMenager mutex; // binary semaphore to avoid critical section and  race condition -happens when two threads or more can modify a shared resource- .
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
        empty = new SemaphoreManager(capacity);  // the room is initially empty / all seats are available
        full = new SemaphoreManager(0);  // initially, no waiting patiants
        mutex = new SemaphoreMAnager(1)  // one patient can modify or accss the waiting room at a time

    }

    //Producer_Consumer problem : Producer => patient , Consumer => doctor
    public void enter(Patient patient) {
        // as the semaphore class is not implemented yet ,so generally the method called wait and may be changed based on semaphore class implementation
        empty.wait(); // wait is a method in semaphore class that let the other patients wait until a seat be empty
        boolean inWaitingRoom = false; // to check whether the patient can em=nter the waiting room or not
        try
        {
            mutex.acquire() // acquire the lock  , so one thread ca execute at a time
            try
            {
                patients.addLast(patient);
                inWaitingRoom = true;
                System.out.println(patient + " entered the waiting room");
            }
            mutex.release(); // release the lock , so othe r thread can resume execution
        }
        if (!inWaitingRoom)
        {
            empty.signal(); // signal is a method that increase the value and wake up the other threads to resume execution
        }
        full.notifyALl(); // notify doctor that a patient is available
    }//end of enter method


    // doctor acts as a consumer
    public Patient leave() {
        full.wait();
        boolean isTreated = false;
        try
        {
            mutex.acquire();
            try
            {
                Patient patient = patients.removeFirst();
                isTreated = true;
                return patient;
            }
            mutex.release();
        }
        if (!isTreated)
        {
            full.signal();

        }
    }//end of leave method

     //this method should called after treatment, so that means => the patient left
    publc void confirmTreatment(){
        empty.signal();
    }//end of confirmTreatment method


    //this method to handle if the doctor can not acquire the treatment room in 3 sec , so the patient will be send back to waiting room
    public void sendPatientBack(Patient patient){
        mutex.acquire();
        try{
            patients.addFirst(patient);
            System.out.println(patient + "entered the waiting room again");
        }
        mutex.release();
        full.signal();
    }//end of sendPatientBack method

}//end of the class