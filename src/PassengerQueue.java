import java.io.Serializable;

public class PassengerQueue implements Serializable {
    Passenger[] queueArray = new Passenger[42];
    private int fist;
    private int last;
    private int maxStayInQueue;
    private int maxLength;


}