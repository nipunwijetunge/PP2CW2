
import java.io.Serializable;
import java.util.Random;
import java.util.Scanner;

public class PassengerQueue implements Serializable {
    Passenger[] queueArray = new Passenger[21]; //.......................................................creates a Passenger class object
    private int first;
    private int last;
    private int maxStayInQueue = 0; //................................................................stores the maximum time any passenger stayed in the queue
    private int maxLength = 0; //...................................................................stores the maximum length queue ever happened to get
    int maxLengthOfQueue = 21; //...........................................set the max length of the queue as 21
    int sumOfPassengersAddedToQueue = 0; //................................sum of passengers added to queue every time using die
    int i = 0; //........................................................uses to set keep track on the index numbers in train queue array

    private void add(Passenger[] waitingList){
        Passenger temp; //..............................................temporarily variable to use for the sorting process
        Random random = new Random();
        int queueDie = random.nextInt(6)+1;
        sumOfPassengersAddedToQueue += queueDie; //.......................adds die value to the varibale each time the method fires
        for (Passenger p : waitingList){ //..............................iterate through the passenger objects in waiting room
            if (p != null && i < sumOfPassengersAddedToQueue){
                queueArray[i] = p;
                if (maxLength < 21){ //.................................queue length will be increased util it gets the highest
                    maxLength++;
                }
                System.out.println("Successfully added seat #"+waitingList[p.getSeat()-1].getSeat()+" to the queue");
                for (int j = 0; j <= 41 ; j++){
                    if (waitingList[j] == p){ //...........................when a certain passenger is added to the queue, that passenger will be removed from the waiting room
                        waitingList[j] = null;
                    }
                }
                i++;
                if (i == maxLengthOfQueue){
                    break;
                }
            }
        }
        for (int j = 0; j < queueArray.length - 1; j++) {  //.........................get the first two elements of the allNames list and compare them to each other.
            for (int k = j + 1; k < queueArray.length - 1; k++) {
                if (queueArray[j] != null && queueArray[k] != null) {
                    if (queueArray[j].getSeat() > queueArray[k].getSeat()) { //.......if second element is greater than first one
                        temp = queueArray[j];        //.......................first element is assigned to a temporary variable
                        queueArray[j] = queueArray[k]; //.......................then second element is assigned to the first index
                        queueArray[k] = temp;   //.............................then first element is assigned to the second index
                    }
                }
            }
        }
    }


    private void remove(String fName, String sName, String nic, Scanner sc) {
        boolean contains = false;
        for (int i = 0 ; i <= queueArray.length - 1 ; i++){ //.................boolean contain will be assigned as true if the entered details by user exists, otherwise false
            if (queueArray[i] != null){
                if (queueArray[i].getFirstName().equalsIgnoreCase(fName) && queueArray[i].getSureName().equalsIgnoreCase(sName) && queueArray[i].getNic().equalsIgnoreCase(nic)){
                    contains = true;
                }
            }
        }
        if (!contains){ //..................................................................if entered data does not exist, error massage will be displayed
            System.out.println("\nThis passenger is already deleted or not added to the queue yet.");

        } else {
            System.out.println("\n"+fName + " seat(s) you have booked are,\n");
            for (Passenger p : queueArray) { //..................................................displays all the seats the respective passenger has booked
                if (p != null && p.getFirstName().equalsIgnoreCase(fName) && p.getSureName().equalsIgnoreCase(sName) && p.getNic().equalsIgnoreCase(nic)) {
                    System.out.println("Seat #" + p.getSeat());
                }
            }
            System.out.println(); //.............................................asks for the seat numbers that user wants to delete
            System.out.println("Enter the seat number(s) you want to remove(with \"1 space\" between each) : ");
            String seatNums = sc.nextLine();
            seatNums += sc.nextLine();
            System.out.println();
            String[] seats = seatNums.split(" "); //..................stores entered seat numbers separately in an array

            for (String s : seats) { //............................iterates through the seats in the array
                for (Passenger p : queueArray) { //.................iterates through the passenger objects in the train queue array
                    if (p != null && p.getFirstName().equalsIgnoreCase(fName) && p.getSureName().equalsIgnoreCase(sName) && p.getNic().equalsIgnoreCase(nic)) {
                        if (s.equals(String.valueOf(p.getSeat()))) {
                            for (int j = 0; j <= queueArray.length - 1; j++) {
                                if (queueArray[j] == p) {
                                    queueArray[j] = null; //..............removes relevant passenger from the queue if all the entered details matches
                                    System.out.println("Successfully deleted seat #" + p.getSeat() + " from the queue.");
                                    for (int k = j ; k <= 19 ; k++){
                                        queueArray[k] = queueArray[k + 1]; //..............re-arranges the queue after deleting passengers
                                    }
                                }
                                if (j == 20){
                                    queueArray[20] = null;
                                }
                            }
                            i--; //......................decrements i by 1 to keep the track on the indexes of the queue array
                        }
                    }
                }
            }
        }
    }

    public boolean isEmpty(Passenger[] waitingList){ //...............................this method returns true if both waiting room and train queue are empty, otherwise empty
        for (int i = 0 ; i <= waitingList.length - 1 ; i++){
            if (waitingList[i] != null){
                return false;
            }
        }
        for (int i = 0 ; i <= queueArray.length - 1 ; i++){
            if (waitingList[i] != null){
                return false;
            }
        }
        return true;
    }

    public boolean isFull(){ //...............................................this method returns true if train queue is full, otherwise false
        if (queueArray[maxLengthOfQueue-1] != null){
            return true;
        }
        return false;
    }

    public void display(){
    }

    public void setMaxStayInQueue(int maxStayInQueue) {
        this.maxStayInQueue = maxStayInQueue;
    }

    public int getMaxLength(){
        return maxLength;
    }

    public int getMaxStayInQueue() {
        return maxStayInQueue;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}