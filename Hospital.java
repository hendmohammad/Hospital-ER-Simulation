import java.util.Scanner;

public class Hospital{
    private int waitingroomcapacity;
    private int num_DR;
    private int num_Patients;

    public Hospital(int wr_capacity,int n_DR,int num_pat ){
        this.waitingroomcapacity=wr_capacity;
        this.num_DR=n_DR;
        this.num_Patients=num_pat;
    }

    public void Hos_events(){
        WaitingRoom WR=new WaitingRoom(waitingroomcapacity);
        TreatmentRoom TR=new TreatmentRoom();
         Thread[] DRs=new Thread[num_DR];
         for(int i=0;i<num_DR;i++){
             Doctor doctor=new Doctor(
                     WR,TR,i+1
             );
             DRs[i]=new Thread(doctor);
             DRs[i].start();}

             Thread[] patients=new Thread[num_Patients];
             for(int i=0;i<num_Patients;i++){

                 Patient patient=new Patient();
                 patients[i]=new Thread(patient);
                 patients[i].start();

             }



    }

    public static void main(String[] args){
        Scanner input=new Scanner(System.in);

        System.out.println("Enter Waiting Room Capacity: ");
        int capacity=input.nextInt();

        System.out.print(" Enter The Number of Doctors: ");
        int Dr= input.nextInt();

        System.out.println("Enter The Number of Patients: ");
        int Patients= input.nextInt();

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

