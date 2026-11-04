class Patient implements Runnable {

    private final int id;
    private final WaitingRoom waitingRoom;

    public Patient(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
    }

    @Override
    public void run() {
        System.out.println(this + " arrived.");
        try {
            waitingRoom.enter(this);  
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return "P" + id;
    }
}
