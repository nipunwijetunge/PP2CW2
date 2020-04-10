
import java.io.Serializable;
import java.util.Random;
import java.util.Scanner;

public class PassengerQueue implements Serializable {
    Passenger[] queueArray = new Passenger[21];
    Passenger[] queueArray2 = new Passenger[21];
    private int first;
    private int last;
    private int maxStayInQueue;
    private int maxLength = 0;
    int maxLengthOfQueue = 21;
    int sum = 0;
    int i = 0;

    private void add(Passenger[] waitingList){
        Passenger temp;
        Random random = new Random();
        int queueDice = random.nextInt(6)+1;
        sum += queueDice;
        for (Passenger p : waitingList){
            if (p != null && i < sum){
                queueArray[i] = p;
                if (maxLength == 21){
                    maxLength = 21;
                } else {
                    maxLength++;
                }
                System.out.println("Successfully added seat #"+waitingList[p.getSeat()-1].getSeat()+" to the queue");
                for (int j = 0; j <= 41 ; j++){
                    if (waitingList[j] == p){
                        waitingList[j] = null;
                    }
                }
                i++;
                if (i == maxLengthOfQueue){
                    i = 0;
                    System.out.println("\nThe queue is full right now. Board some passengers to the train.");
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
        for (int i = 0 ; i <= queueArray.length - 1 ; i++){
            if (queueArray[i] != null){
                if (queueArray[i].getFirstName().equalsIgnoreCase(fName) && queueArray[i].getSureName().equalsIgnoreCase(sName) && queueArray[i].getNic().equalsIgnoreCase(nic)){
                    contains = true;
                }
            }
        }
        if (!contains){
            System.out.println("\nThis passenger is already deleted or not added to the queue yet.");

        } else {
            System.out.println("\n"+fName + " seat(s) you have booked are,\n");
            for (Passenger p : queueArray) {
                if (p != null && p.getFirstName().equalsIgnoreCase(fName) && p.getSureName().equalsIgnoreCase(sName) && p.getNic().equalsIgnoreCase(nic)) {
                    System.out.println("Seat #" + p.getSeat());
                }
            }
            System.out.println();
            System.out.println("Enter the seat number(s) you want to remove(with \"1 space\" between each) : ");
            String seatNums = sc.nextLine();
            seatNums += sc.nextLine();
            System.out.println();
            String[] seats = seatNums.split(" ");

            for (String s : seats) {
                for (Passenger p : queueArray) {
                    if (p != null && p.getFirstName().equalsIgnoreCase(fName) && p.getSureName().equalsIgnoreCase(sName) && p.getNic().equalsIgnoreCase(nic)) {
                        if (s.equals(String.valueOf(p.getSeat()))) {
                            for (int j = 0; j <= queueArray.length - 1; j++) {
                                if (queueArray[j] == p) {
                                    queueArray[j] = null;
                                    System.out.println("Successfully deleted seat #" + p.getSeat() + " from the queue.");
                                    for (int k = j ; k <= 19 ; k++){
                                        queueArray[k] = queueArray[k + 1];
                                    }
                                }
                                if (j == 20){
                                    queueArray[20] = null;
                                }
                            }
                            i--;
                        }
                    }
                }
            }
        }
    }

    public boolean isEmpty(Passenger[] waitingList){
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

    public boolean isFull(){
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
}