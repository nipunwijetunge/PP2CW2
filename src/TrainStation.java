import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TrainStation extends Application {

    private Passenger[] colomboToBadullaWaitingList = new Passenger[42];
    private Passenger[] badullaToColomboWaitingList = new Passenger[42];

    PassengerQueue trainQueue = new PassengerQueue();

    private Passenger[] boardedToTrain = new Passenger[42];

    double minTime = 18;
    double sumOfTime = 0;
    int count = 0;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        File myFile = new File("C:\\Users\\acer\\Desktop\\CUSTOMERS.txt");
        File cw2file = new File("Train_Queue.txt");
        File report = new File("REPORT.txt");
        Scanner sc = new Scanner(System.in);
        LocalDate today = LocalDate.now();
        int trip;
        ArrayList<String> nicListColomboToBadulla = new ArrayList<>();
        ArrayList<String> nicListBadullToColombo = new ArrayList<>();

        while (true) {
            try {
                System.out.println("-------------------------------------------------------------");
                System.out.println("           DENUWARA MENIKE TRAIN BOARDING PROGRAM");
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
            } catch (InputMismatchException e) {
                System.out.println("Invalid input!");
                System.out.println();
                sc.next();
                continue;
            }

            menu:
            while (true) {
                if (trip == 1) {
                    System.out.println("-------------------------------------------------------------");
                    System.out.println("            WELCOME TO COLOMBO RAILWAY STATION");
                    System.out.println("-------------------------------------------------------------");
                } else {
                    System.out.println("-------------------------------------------------------------");
                    System.out.println("            WELCOME TO BADULLA RAILWAY STATION");
                    System.out.println("-------------------------------------------------------------");
                }
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

                System.out.print("Choose an option from above: ");
                String input = sc.next();
                System.out.println();

                switch (input) {
                    case "W":
                    case "w":
                        checkInToWaitingRoom(today, myFile, sc, trip, nicListColomboToBadulla, nicListBadullToColombo);
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
                        deletePassengerFromQueue(sc);
                        break;
                    case "S":
                    case "s":
                        storeTrainQueue(cw2file, sc, today, trip);
                        break;
                    case "L":
                    case "l":
                        loadDataToTrainQueue(cw2file, sc, today);
                        break;
                    case "R":
                    case "r":
                        RunSimulationAndReport(trip, report);
                        break;
                    case "Q":
                    case "q":
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

    private void checkInToWaitingRoom(LocalDate today, File myFile, Scanner sc, int trip, ArrayList<String> nicListColomboToBadulla, ArrayList<String> nicListBadullToColombo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(myFile));
        String line;
        String[] names;
        String[] nic;
        String[] date;
        String[] journey;
        String[] seat;

        System.out.print("Enter your Full name : ");
        String name = sc.nextLine();
        name += sc.nextLine();
        System.out.print("Enter your NIC number : ");
        String nicNum = sc.next();
        System.out.println();
        if (nicListColomboToBadulla.contains(nicNum) || nicListBadullToColombo.contains(nicNum)) {
            System.out.println("Already checked in.");
        } else {
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" {2}-- {2}", 5);

                date = parts[3].split(" : ");
                names = parts[0].split(" : ");
                nic = parts[1].split(" : ");
                journey = parts[4].split(" : ");

                if (date[1].equals(String.valueOf(today)) && name.equalsIgnoreCase(names[1]) && nicNum.equalsIgnoreCase(nic[1])) {
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
                            System.out.println(firstName+" checked in to he waiting room for seat #" + seatNumber);
                            if (!nicListColomboToBadulla.contains(nicNum)) {
                                nicListColomboToBadulla.add(nicNum);
                            }
                        } else {
                            System.out.println();
                            System.out.println("Sorry " + firstName + " you have not booked a seat.");
                        }
                    } else {
                        if (journey[1].equalsIgnoreCase("from badulla to colombo")) {
                            badullaToColomboWaitingList[seatNumber - 1] = new Passenger();
                            badullaToColomboWaitingList[seatNumber - 1].setName(firstName, sureName);
                            badullaToColomboWaitingList[seatNumber - 1].setNic(nicNum);
                            badullaToColomboWaitingList[seatNumber - 1].setSeat(seatNumber);
                            System.out.println(firstName+" checked in to he waiting room for seat #" + seatNumber);
                            if (!nicListBadullToColombo.contains(nicNum)) {
                                nicListBadullToColombo.add(nicNum);
                            }
                        } else {
                            System.out.println();
                            System.out.println("Sorry " + firstName + " you have not booked a seat.");
                        }
                    }
                }
            }
        }
    }

    private void RunSimulationAndReport(int trip, File report) throws InterruptedException {
        for (int i = 0; i <= trainQueue.queueArray.length - 1; i++) {
            if (trainQueue.queueArray[i] != null) {
                count++;
            }
        }
        Random random = new Random();
        wrapper:
        while (true) {
            for (Passenger o : trainQueue.queueArray) {
                if (o != null) {
                    double timeDie1 = random.nextInt(6) + 1;
                    double timeDie2 = random.nextInt(6) + 1;
                    double timeDie3 = random.nextInt(6) + 1;

                    if (minTime > timeDie1 + timeDie2 + timeDie3){
                        minTime = timeDie1 + timeDie2 + timeDie3;
                    }

                    sumOfTime += (timeDie1 + timeDie2 + timeDie3);
                    boardedToTrain[o.getSeat() - 1] = o;
                    boardedToTrain[o.getSeat() - 1].setSecondsInQueue((int) sumOfTime);
                    TimeUnit.SECONDS.sleep((int)timeDie1);
                    System.out.println(o.getFirstName() + " boarded to train to seat #" + o.getSeat());
                    for (int i = 0 ; i <= 19 ; i++){
                        trainQueue.queueArray[i] = trainQueue.queueArray[i+1];
                    }
                    trainQueue.queueArray[20] = null;
                    break;
                } else {
                    break wrapper;
                }
            }
        }

        boolean isEmpty;
        if (trip == 1) {
            isEmpty = trainQueue.isEmpty(colomboToBadullaWaitingList);
        } else {
            isEmpty = trainQueue.isEmpty(badullaToColomboWaitingList);
        }

        if (isEmpty) {
            while (true) {
                try {
                    FileWriter fw = new FileWriter(report, false);
                    BufferedWriter writer = new BufferedWriter(fw);
                    for (Passenger p : boardedToTrain) {
                        if (p != null) {
                            if (trip == 1) {
                                writer.write("Full Name : " + p.getFirstName() + " " + p.getSureName() +
                                        " | NIC : " + p.getNic() +
                                        " | Seat No. : " + p.getSeat() +
                                        " | Seconds in Queue : " + p.getSecondsInQueue() +
                                        " | Station : Colombo");
                                writer.newLine();
                            } else {
                                writer.write("Full Name : " + p.getFirstName() + " " + p.getSureName() +
                                        " | NIC : " + p.getNic() +
                                        " | Seat No. : " + p.getSeat() +
                                        " | Seconds in Queue : " + p.getSecondsInQueue() +
                                        " | Station : Badulla");
                                writer.newLine();
                            }
                        }
                    }

                    DecimalFormat f = new DecimalFormat("##.00");
                    double averageWaitingTime = sumOfTime / count;
                    writer.write("\nMaximum length queue attained : "+trainQueue.maxLengthAttained);
                    writer.write("\nMaximum waiting time          : "+Math.round(sumOfTime));
                    writer.write("\nMinimum waiting time          : "+Math.round(minTime));
                    writer.write("\nAverage Waiting time          : "+f.format(averageWaitingTime));

                    writer.flush();
                    writer.close();
                    break;

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("File not found!");
                    System.out.println();
                }
            }
        }
    }

    private void loadDataToTrainQueue(File cw2File, Scanner sc, LocalDate today) {
        Passenger temp = null;
        while (true) {
            System.out.print("Are you sure you want to load data? (y/n): ");
            String loadInput = sc.next();
            System.out.println();

            if (loadInput.equalsIgnoreCase("y")) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(cw2File));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(" {2}-- {2}", 5);

                        if (parts.length >= 5) {
                            String[] name = parts[0].split(" : "); //...............add name part to the array called name
                            String[] nic = parts[1].split(" : "); //................add NIC number part to the array called nic
                            String[] seat = parts[2].split(" : "); //...............add seat number part to the array called seat
                            String[] day = parts[3].split(" : "); //................add date part to the array called day
                            String[] station = parts[4].split(" : "); //...............add trip part to the array called trip

                            String[] fullName = name[1].split(" ");
                            String firstName = fullName[0];
                            String sureName = fullName[1];

                            if (station[1].equalsIgnoreCase("Colombo") && day[1].equals(String.valueOf(today))) {
                                for (int i = 0 ; i <= trainQueue.queueArray.length - 1 ; i++) {
                                    if (trainQueue.queueArray[i] == null) {
                                        trainQueue.queueArray[i] = new Passenger();
                                        trainQueue.queueArray[i].setName(firstName, sureName);
                                        trainQueue.queueArray[i].setNic(nic[1]);
                                        trainQueue.queueArray[i].setSeat(Integer.parseInt(seat[1]));
                                        break;
                                    }
                                }
                                sort(temp);

                            } else if (station[1].equalsIgnoreCase("Badulla") && day[1].equals(String.valueOf(today))) {
                                for (int i = 0 ; i <= trainQueue.queueArray.length - 1 ; i++) {
                                    if (trainQueue.queueArray[i] == null) {
                                        trainQueue.queueArray[i] = new Passenger();
                                        trainQueue.queueArray[i].setName(firstName, sureName);
                                        trainQueue.queueArray[i].setNic(nic[1]);
                                        trainQueue.queueArray[i].setSeat(Integer.parseInt(seat[1]));
                                        break;
                                    }
                                }
                                sort(temp);
                            }
                        }
                    }

                    reader.close();
                    System.out.println("Successfully Loaded!");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            } else if (loadInput.equalsIgnoreCase("n")) {
                break;

            } else {
                System.out.println("Invalid Input!! Please retry."); // ......if a different input is detected other than 'y' or 'n', it will iterate util
                System.out.println();       // ...............................the correct input is entered
            }
        }
    }

    private void storeTrainQueue(File queue, Scanner sc, LocalDate today, int trip) {
        while (true) {
            System.out.print("Are you sure you want to store data? (y/n): ");
            String storeInput = sc.next();
            System.out.println();

            if (storeInput.equalsIgnoreCase("y")) {
                try {
                    FileWriter queueWriter = new FileWriter(queue, true);
                    BufferedWriter writer = new BufferedWriter(queueWriter);
                    if (isEmpty()) {
                        System.out.println("There is nothing to be stored!");
                        break;
                    }
                    for (Passenger p : trainQueue.queueArray) {
                        if (p != null) {
                            if (trip == 1) {
                                writer.write("Full Name : " + p.getFirstName() + " " + p.getSureName() +
                                        "  --  NIC Number : " + p.getNic() +
                                        "  --  Seat No. : " + p.getSeat() +
                                        "  --  Date : " + today +
                                        "  --  Station : Colombo");
                                writer.newLine();
                            } else {
                                writer.write("Full Name : " + p.getFirstName() +
                                        "  --  NIC Number : " + p.getNic() +
                                        "  --  Seat No. : " + p.getSeat() +
                                        "  --  Date : " + today +
                                        "  --  Station : Badulla");
                                writer.newLine();
                            }
                        }
                    }
                    writer.flush();
                    writer.close();

                    System.out.println("Successfully stored!");
                    System.out.println();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("File not found!");
                    System.out.println();
                }
            } else if (storeInput.equalsIgnoreCase("n")) {
                break;
            } else {
                System.out.println("Invalid input!! Please reenter."); //................iterates util the correct input is entered
                System.out.println();
            }
        }
    }

    private void deletePassengerFromQueue(Scanner sc) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (isEmpty()) {
            System.out.println("Queue is Empty.");
        } else {
            System.out.print("Enter first name : ");
            String fName = sc.next();
            System.out.print("Enter sure name : ");
            String sName = sc.next();
            System.out.print("Enter NIC number : ");
            String nic = sc.next();
            while (true) {
                if (nic.length() < 10) {
                    System.out.println("\nInvalid NIC number! try again.\n");
                    System.out.print("Enter NIC number : ");
                    nic = sc.next();
                } else {
                    break;
                }
            }

            Method m = PassengerQueue.class.getDeclaredMethod("remove", String.class, String.class, String.class, Scanner.class);
            m.setAccessible(true);
            m.invoke(trainQueue, fName, sName, nic, sc);
        }
    }

    private void viewTrainQueue() {
    }

    private void addCustomer(int trip) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (trip == 1) {
            if (trainQueue.isFull()) {
                System.out.println("The queue is full right now. Board some passengers to the train.");
            } else {
                Method m = PassengerQueue.class.getDeclaredMethod("add", Passenger[].class);
                m.setAccessible(true);
                m.invoke(trainQueue, (Object) colomboToBadullaWaitingList);
            }

        } else {
            if (trainQueue.isFull()) {
                System.out.println("The queue is full right now. Board some passengers to the train.");
            } else {
                Method m = PassengerQueue.class.getDeclaredMethod("add", Passenger[].class);
                m.setAccessible(true);
                m.invoke(trainQueue, (Object) badullaToColomboWaitingList);
            }
        }
    }

    private boolean isEmpty() {
        for (int i = 0; i <= trainQueue.queueArray.length - 1; i++) {
            if (trainQueue.queueArray[i] != null) {
                return false;
            }
        }
        return true;
    }

    private void sort(Passenger temp){
        for (int j = 0; j < trainQueue.queueArray.length - 1; j++) {  //.........................get the first two elements of the allNames list and compare them to each other.
            for (int k = j + 1; k < trainQueue.queueArray.length - 1; k++) {
                if (trainQueue.queueArray[k] != null) {
                    if (trainQueue.queueArray[j].getSeat() > trainQueue.queueArray[k].getSeat()) { //.......if second element is greater than first one
                        temp = trainQueue.queueArray[j];        //.......................first element is assigned to a temporary variable
                        trainQueue.queueArray[j] = trainQueue.queueArray[k]; //.......................then second element is assigned to the first index
                        trainQueue.queueArray[k] = temp;   //.............................then first element is assigned to the second index
                    }
                }
            }
        }
    }

    private void waitingAndTrainSaver(File myFile, int trip, LocalDate today) throws IOException {
        try {
            FileWriter queueWriter = new FileWriter(myFile, true);
            BufferedWriter writer = new BufferedWriter(queueWriter);

            for (Passenger p : trainQueue.queueArray) {
                if (p != null) {
                    if (trip == 1) {
                        writer.write("Full Name : " + p.getFirstName() + " " + p.getSureName() +
                                "  --  NIC Number : " + p.getNic() +
                                "  --  Seat No. : " + p.getSeat() +
                                "  --  Date : " + today +
                                "  --  Station : Colombo to Badulla");
                        writer.newLine();
                    } else {
                        writer.write("Full Name : " + p.getFirstName() +
                                "  --  NIC Number : " + p.getNic() +
                                "  --  Seat No. : " + p.getSeat() +
                                "  --  Date : " + today +
                                "  --  Station : Badulla to Colombo");
                        writer.newLine();
                    }
                }
            }
            writer.flush();
            writer.close();

            System.out.println("Successfully stored!");
            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found!");
            System.out.println();
        }
    }
}
