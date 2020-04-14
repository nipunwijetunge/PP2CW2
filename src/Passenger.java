public class Passenger {

    private String firstName; //....................first name of the passenger
    private String sureName; //.....................sure name of the passenger
    private String nic; //..........................nic number of the passenger
    private int seat; //...........................seat number of the passenger
    private int secondsInQueue; //................number of seconds the passenger waited in the queue

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
