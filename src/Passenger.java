public class Passenger {

    private String firstName;
    private String sureName;
    private String nic;
    private int seat;
    private int secondsInQueue;

    public String getFirstName() {
        return firstName;
    }

    public String getSureName() {
        return sureName;
    }

    public String getNic() {
        return nic;
    }

    public int getSeat() {
        return seat;
    }

    public int getSecondsInQueue() {
        return secondsInQueue;
    }

    public void setName(String firstName, String sureName) {
        this.firstName = firstName;
        this.sureName = sureName;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public void setSecondsInQueue(int secondsInQueue) {
        this.secondsInQueue = secondsInQueue;
    }
}
