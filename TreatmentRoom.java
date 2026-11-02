public class TreatmentRoom {
    // in this calss the two rooms represents semaphores
    private SemaphoreManager rooms;

    //constructor
    public TreatmentRoom(int numOfRooms){ //the treatment rooms is 2 in the system requirements  but I prefered to generalize the implementation
        rooms = new SemaphoreManager(numOfRooms);
    }

    //doctor try to acquire an available room
    public boolean isRoomAvailable(int time){
        return rooms.acquire(time); //=>in this line acquire function  is different from acquire in WaitingRoom ,
        // this  function  simulates acquiring room in 3 sec and return true if the doctor could acquire the room and false if not.
    }

    //this method should called after a doctor finishs treating a petient
    public void makeRoomAvailable(){
        rooms.signal();
    }

}