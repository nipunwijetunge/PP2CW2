import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class TrainStation extends Application {

    private Passenger[] colomboToBadullaWaitingList = new Passenger[42];
    private Passenger[] badullaToColomboWaitingList = new Passenger[42];

    PassengerQueue trainQueue = new PassengerQueue();

    private Passenger[] boardedToTrain = new Passenger[42];

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        File myFile = new File("C:\\Users\\acer\\Desktop\\CUSTOMERS.txt");
        Scanner sc = new Scanner(System.in);
        int trip;
        int sumOfTime = 0;

        while (true) {
            try {
                System.out.println("-------------------------------------------------------------");
                System.out.println("        DENUWARA MENIKE TRAIN BOARDING PROGRAM");
                System.out.println("-------------------------------------------------------------");
                System.out.println();
                System.out.println("Pick the trip,");
                System.out.println("\"1\" Colombo To Badulla.");
                System.out.println("\"2\" Badulla to Colombo.");
                System.out.println("\"0\" Quit.");
                System.out.println();
                trip = sc.nextInt();
                if (trip == 0) {
                    System.exit(0);
                }
                if (trip != 1 && trip != 2) {
                    continue;
                }
            } catch (InputMismatchException e){
                System.out.println("Invalid input!");
                System.out.println();
                sc.next();
                continue;
            }

            menu:
            while (true) {
                System.out.println();
                System.out.println("Enter \"W\" to check-in to the waiting room.");
                System.out.println("Enter \"A\" to add a customers to train queue.");
                System.out.println("Enter \"V\" to view waiting room, train queue & train seats.");
                System.out.println("Enter \"D\" to delete passenger from the train queue.");
                System.out.println("Enter \"S\" to store train queue data to a file.");
                System.out.println("Enter \"L\" to load data back from the file into train queue.");
                System.out.println("Enter \"R\" to Run the simulation and produce report.");
                System.out.println("Enter \"Q\" to Quit.");
                System.out.println();
                System.out.println("-------------------------------------------------------------");

                System.out.print("Choose option from above: ");
                String input = sc.next();
                System.out.println();

                switch (input) {
                    case "W":
                    case "w":
                        checkInToWaitingRoom(myFile, sc, trip);
                        break;
                    case "A":
                    case "a":
                        addCustomer(trip);
                        break;
                    case "V":
                    case "v":
                        viewTrainQueue();
                        break;
                    case "D":
                    case "d":
                        deletePassengerFromQueue();
                        break;
                    case "S":
                    case "s":
                        storeTrainQueue();
                        break;
                    case "L":
                    case "l":
                        loadDataToTrainQueue();
                        break;
                    case "R":
                    case "r":
                        RunSimulationAndReport(sumOfTime);
                        break;
                    case"Q":
                    case"q":
                        while (true) {
                            System.out.println("Are you sure you want to quit? (y/n): ");
                            String qornot = sc.next();
                            if (qornot.equalsIgnoreCase("y")) {
                                System.exit(0);
                            } else if (qornot.equalsIgnoreCase("n")) {
                                continue menu;
                            } else {
                                System.out.println("Invalid input!");
                                System.out.println();
                            }
                        }
                    default:
                        System.out.println("Invalid input..! Please Retry.");
                }
            }
        }
    }

    private void checkInToWaitingRoom(File myFile, Scanner sc, int trip) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(myFile));
        String line;
        String[] names;
        String[] nic;
        String[] date;
        String[] journey;
        String[] seat;
        LocalDate today = LocalDate.now();

        System.out.print("Enter your Full name : ");
        String name = sc.nextLine();
        name += sc.nextLine();
        System.out.print("Enter your NIC number : ");
        String nicNum = sc.next();

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" {2}-- {2}", 5);

            date = parts[3].split(" : ");
            names = parts[0].split(" : ");
            nic = parts[1].split(" : ");
            journey = parts[4].split(" : ");

            if (date[1].equals(String.valueOf(today)) && name.equalsIgnoreCase(names[1]) && nicNum.equalsIgnoreCase(nic[1])){
                seat = parts[2].split(" : ");
                int seatNumber = Integer.parseInt(seat[1]);
                String[] fullName = names[1].split(" ");
                String firstName = fullName[0];
                String sureName = fullName[1];

                if (trip == 1) {
                    if (journey[1].equalsIgnoreCase("from colombo to badulla")) {
                        colomboToBadullaWaitingList[seatNumber - 1] = new Passenger();
                        colomboToBadullaWaitingList[seatNumber - 1].setName(firstName, sureName);
                        colomboToBadullaWaitingList[seatNumber - 1].setNic(nicNum);
                        colomboToBadullaWaitingList[seatNumber - 1].setSeat(seatNumber);
                        System.out.println(Arrays.toString(colomboToBadullaWaitingList));
                    } else {
                        System.out.println();
                        System.out.println("Sorry "+firstName+" you have not booked a seat.");
                    }
                } else {
                    if (journey[1].equalsIgnoreCase("from badulla to colombo")) {
                        badullaToColomboWaitingList[seatNumber - 1] = new Passenger();
                        badullaToColomboWaitingList[seatNumber - 1].setName(firstName, sureName);
                        badullaToColomboWaitingList[seatNumber - 1].setNic(nicNum);
                        badullaToColomboWaitingList[seatNumber - 1].setSeat(seatNumber);
                        System.out.println(Arrays.toString(badullaToColomboWaitingList));
                    } else {
                        System.out.println();
                        System.out.println("Sorry "+firstName+" you have not booked a seat.");
                    }
                }
            }
        }
    }

    private void RunSimulationAndReport(int sumOfTime) {
        if (trainQueue.queueArray != null){
            Random random = new Random();
            int timeDie1 = random.nextInt(7-1)+1;
            int timeDie2 = random.nextInt(7-1)+1;
            int timeDie3 = random.nextInt(7-1)+1;
            sumOfTime =+ (timeDie1+timeDie2+timeDie3);
            for (Passenger o : trainQueue.queueArray){
                if (o != null) {
                    boardedToTrain[o.getSeat() - 1] = o;
                    boardedToTrain[o.getSeat() - 1].setSecondsInQueue(sumOfTime);
                    for (int i = 0 ; i <= 41 ; i++){
                        if (trainQueue.queueArray[i] == o){
                            trainQueue.queueArray[i] = trainQueue.queueArray[i+1];
                        }
                    }
                }
            }
        } else {

        }
    }

    private void loadDataToTrainQueue() {
    }

    private void storeTrainQueue() {
    }

    private void deletePassengerFromQueue() {

    }

    private void viewTrainQueue() {
    }

    private void addCustomer(int trip) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (trip == 1){
            Method m = PassengerQueue.class.getDeclaredMethod("add", Passenger[].class);
            m.setAccessible(true);
            m.invoke(trainQueue, (Object) colomboToBadullaWaitingList);

        } else {
            Method m = PassengerQueue.class.getDeclaredMethod("add", Passenger[].class);
            m.setAccessible(true);
            m.invoke(trainQueue, (Object) badullaToColomboWaitingList);
        }
    }
}
