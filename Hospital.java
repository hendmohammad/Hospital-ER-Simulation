import java.util.concurrent.CountDownLatch;
import java.util.Scanner;

public class Hospital{
    private int waitingroomcapacity;
    private int num_DR;
    private int num_Patients;

    public Hospital(int wr_capacity,int n_DR,int num_pat){
        this.waitingroomcapacity=wr_capacity;
        this.num_DR=n_DR;
        this.num_Patients=num_pat;
    }

    public void Hos_events(){
        WaitingRoom WR = new WaitingRoom(waitingroomcapacity);
        TreatmentRoom TR = new TreatmentRoom();
        CountDownLatch finished = new CountDownLatch(num_Patients);   // after all patients are treated, the Main Thread will work "it was blocked till N=0" 

        Thread[] DRs = new Thread[num_DR];

        // creation loop of doctors threads
        for(int i=0;i<num_DR;i++){
            Doctor doctor = new Doctor(WR, TR, i+1, finished);
            DRs[i] = new Thread(doctor);
            DRs[i].start();}

            Thread[] patients = new Thread[num_Patients];

            // for matching the output sample
            System.out.print("\nPatients arriving:\n"); 
            for (int i = 1; i <= num_Patients; i++) System.out.print("P" + i + " ");
            System.out.println();
            System.out.println();

            // creation loop of patients threads 
            for(int i=0;i<num_Patients;i++){
                Patient patient = new Patient(i+1, WR);
                patients[i] = new Thread(patient);
                patients[i].start();

                // small pause so patients arrive one after another "important for arranging the displayed messages"
                try { Thread.sleep(50); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }

            try { finished.await(); } // it is used to wait until all patients have left (N=0)
            catch (InterruptedException e) { Thread.currentThread().interrupt(); } //".await() can throw exception"

            for (Thread d : DRs) {d.interrupt(); }  // stop idle doctors when all patients finish treatment

            System.out.println("All patients have been treated.");    
            System.out.println("Simulation completed.");              
        }


    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        System.out.print("Enter Waiting Room Capacity: ");
        int capacity = input.nextInt();

        System.out.print("Enter The Number of Doctors: ");
        int Dr = input.nextInt();

        System.out.print("Enter The Number of Patients: ");
        int Patients = input.nextInt();

        if(capacity<=0 || Dr<=0 || Patients<=0){
            System.out.println("The Values must be only positive");
            input.close();
            return;
        }

        Hospital hospital=new Hospital(capacity,Dr,Patients);

        hospital.Hos_events();

        input.close();
    }
}
