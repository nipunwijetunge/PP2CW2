import com.sun.deploy.util.ArrayUtil;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.Random;

public class PassengerQueue implements Serializable {
    Passenger[] queueArray = new Passenger[42];
    private int first;
    private int last;
    private int maxStayInQueue;
    private int maxLength;
    int sum = 0;
    int i = 0;

    private void add(Passenger[] waitingList){
        i =+ i;
        Random random = new Random();
        int queueDice = random.nextInt(7-1)+1;
        sum = sum+queueDice;
        System.out.println(queueDice);
        System.out.println(sum);
        for (Passenger p : waitingList){
            if (p != null && i < sum){
              queueArray[i] = p;
              for (int j = 0; j <= 41 ; j++){
                  if (waitingList[j] == p){
                      waitingList[j] = null;
                  }
              }
              i++;
            }
        }
    }


    private void remove(){

    }

    public boolean isEmpty(){
        return true;
    }

    public boolean isFull(){
        return true;
    }

    public void display(){
    }

    public int getMaxLength(){
        return maxLength;
    }

    public int getMaxStayInQueue() {
        return maxStayInQueue;
    }
}