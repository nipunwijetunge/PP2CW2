import javafx.application.Application;
import javafx.stage.Stage;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class TrainStation extends Application {
    Passenger passenger = new Passenger();

    private String[] colomboToBadullaWaitingList = new String[42];
    private String[] badullaToColomboWaitingList = new String[42];

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        File myFile = new File("C:\\Users\\acer\\Desktop\\CUSTOMERS.txt");
        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.println("-------------------------------------------------------------");
            System.out.println("        DENUWARA MENIKE TRAIN SEAT BOOKING PROGRAM");
            System.out.println("-------------------------------------------------------------");
            System.out.println("Enter \"A\" to add a customers to train queue.");
            System.out.println("Enter \"V\" to view waiting room, train queue & train seats.");
            System.out.println("Enter \"D\" to delete passenger from the train queue.");
            System.out.println("Enter \"S\" to store train queue data to a file.");
            System.out.println("Enter \"L\" to load data back from the file into train queue.");
            System.out.println("Enter \"R\" to Run the simulation and produce report.");
            System.out.println();
            System.out.println("-------------------------------------------------------------");

            System.out.print("Choose option from above: ");
            String input = sc.next();
            System.out.println();

            switch (input){
                case"W":
                case"w":
                    checkInToWaitingRoom(myFile, sc);
                case "A":
                case "a":
                    addCustomer();
                case "V":
                case "v":
                    viewTrainQueue();
                case"D":
                case"d":
                    deletePassengerFromQueue();
                case"S":
                case"s":
                    storeTrainQueue();
                case"L":
                case"l":
                    loadDataToTrainQueue();
                case"R":
                case"r":
                    RunSimulationAndReport();
                default:
                    System.out.println("Invalid input..! Please Retry.");
            }
        }
    }

    private void checkInToWaitingRoom(File myFile, Scanner sc) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(myFile));
        String line;
        String[] names = {};
        String[] nic = {};
        String[] date = {};
        String[] trip = {};
        String[] seat = {};
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
            trip = parts[4].split(" : ");

            if (date[1].equals(String.valueOf(today)) && name.equalsIgnoreCase(names[1]) && nicNum.equalsIgnoreCase(nic[1])){
                seat = parts[2].split(" : ");
                int seatNumber = Integer.parseInt(seat[1]);
                String[] fullName = names[1].split(" ");
                String firstName = fullName[0];
                String sureName = fullName[1];

                passenger.setName(firstName, sureName);
                passenger.setNic(nic[1]);

                if (trip[1].equalsIgnoreCase("from colombo to badulla")) {
                    colomboToBadullaWaitingList[seatNumber-1] = passenger.getFirstName()+" "+passenger.getSureName();
                    System.out.println(Arrays.toString(colomboToBadullaWaitingList));
                } else {
                    badullaToColomboWaitingList[seatNumber-1] = passenger.getFirstName()+" "+passenger.getSureName();
                    System.out.println(Arrays.toString(badullaToColomboWaitingList));
                }
            }
        }
    }

    private void RunSimulationAndReport() {

    }

    private void loadDataToTrainQueue() {
    }

    private void storeTrainQueue() {
    }

    private void deletePassengerFromQueue() {
    }

    private void viewTrainQueue() {
    }

    private void addCustomer() {
    }
}
