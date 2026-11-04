public class TreatmentRoom {
    // in this class the two rooms represents semaphores
    private SemaphoreManager rooms;

    //constructor
    public TreatmentRoom(){
        // initially, all treatment rooms are available and hospital has exactly 2 treatment rooms
        rooms = new SemaphoreManager(2);
    }

    //doctor try to acquire an available room
    public boolean isRoomAvailable(int time) throws InterruptedException {
        return rooms.tryAcquire(time); //=>in this line tryAcquire function  is different from acquire in WaitingRoom ,
        // this  function  simulates acquiring room in 3 sec and return true if the doctor could acquire the room and false if not.
    }

    // method used to actually acquire a room 
    public void acquireRoom() throws InterruptedException {
        rooms.acquire();
}

    //this method should called after a doctor finishes treating a patient
    public void makeRoomAvailable(){
        rooms.release();
    }

}
