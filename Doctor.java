class Doctor implements Runnable{

    private WaitingRoom waitingRoom;
    private TreatmentRoom treatmentRoom;
    private Patient patient;
    private int doctorId;

    public Doctor(WaitingRoom waitingRoom, TreatmentRoom treatmentRoom, int doctorId) {
        this.waitingRoom = waitingRoom;
        this.treatmentRoom = treatmentRoom;
        this.doctorId = doctorId;
    }


    @Override
    public void run(){

        while (true){

            // the doctor waits until a patient is available, then takes that patient.
            try { 
                patient = waitingRoom.leave();
                System.out.println("Doctor D" + doctorId + " is assigned to " + patient + ".");
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // if interrupted while waiting, restore the interrupted status
                break; // the doctor thread exits the loop
            }

            // the doctor tries to acquire a treatment room for up to 3 seconds
            boolean isAcquired;
            
            try { 
                System.out.println("Doctor D" + doctorId + " is trying to acquire a treatment room.");
                isAcquired = treatmentRoom.isRoomAvailable(3000); 
            }
            catch (InterruptedException e) {
                try { 
                    System.out.println("Doctor D" + doctorId + " is sending " + patient + " back to the waiting room.");
                    waitingRoom.sendPatientBack(patient); // if interrupted while acquiring a room, send the patient back
                } 
                catch (InterruptedException ex) { Thread.currentThread().interrupt(); }

                Thread.currentThread().interrupt();
                break;
            }

            if (isAcquired) {

                System.out.println("Doctor D" + doctorId + " acquired a treatment room.");
                System.out.println("Doctor D" + doctorId + " begins treating " + patient + ".");
                
                try { Thread.sleep(5000); } // the doctor thread sleeps to simulate time for the treatment (meaning its thread isn't available now)
                catch (InterruptedException e) {

                    treatmentRoom.makeRoomAvailable(); // if interrupted during treatment, release the room first
                    System.out.println("Doctor D" + doctorId + " released the treatment room.");

                    try { // then return the unfinished patient back
                        System.out.println("Doctor D" + doctorId + " is sending " + patient + " back to the waiting room.");
                        waitingRoom.sendPatientBack(patient); 
                    }
                     
                    catch (InterruptedException ex) { Thread.currentThread().interrupt(); }

                    Thread.currentThread().interrupt();
                    break;
                }

                System.out.println("Doctor D" + doctorId + " finished treating " + patient + ".");
                treatmentRoom.makeRoomAvailable(); // release the room when the treatment finishes normally 

                System.out.println("Doctor D" + doctorId + " released the treatment room.");
                System.out.println("Doctor D" + doctorId + " is now available.");
                System.out.println(patient + " left.");

            } else { 
                // if the doctor couldn't acquire a room within 3 seconds: 
                System.out.println("Doctor D" + doctorId + " could not acquire a treatment room.");
               
                try { // patient returns to the waitingRoom 
                    System.out.println("Doctor D" + doctorId + " is sending " + patient + " back to the waiting room.");
                    waitingRoom.sendPatientBack(patient);
                } 
                catch (InterruptedException e) {  
                    Thread.currentThread().interrupt(); // if sendPatientBack is interrupted, doctor exits the loop, if not it continues the loop and waits for another patient automatically
                    break;
                }

                System.out.println("Doctor D" + doctorId + " is now available.");
            }
        }
    }
}
