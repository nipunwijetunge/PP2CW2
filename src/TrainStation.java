import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.security.auth.login.FailedLoginException;
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
        File myFile = new File("C:\\Users\\acer\\IdeaProjects\\PPCW\\Booked_Seats.txt");
        File trainQueueFile = new File("Train_Queue.txt");
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
                        viewTrainQueue(trip);
                        break;
                    case "D":
                    case "d":
                        deletePassengerFromQueue(sc);
                        break;
                    case "S":
                    case "s":
                        storeTrainQueue(trainQueueFile, sc, today, trip);
                        break;
                    case "L":
                    case "l":
                        loadDataToTrainQueue(trainQueueFile, sc, today);
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
            if (trainQueue.queueArray[i] != null && sumOfTime <= trainQueue.getMaxStayInQueue()) {
                count++;
            }
        }
        Random random = new Random();
        wrapper:
        while (true) {
            if (sumOfTime <= trainQueue.getMaxStayInQueue()) {
                for (Passenger o : trainQueue.queueArray) {
                    if (o != null) {
                        double timeDie1 = random.nextInt(6) + 1;
                        double timeDie2 = random.nextInt(6) + 1;
                        double timeDie3 = random.nextInt(6) + 1;
                        sumOfTime += (timeDie1 + timeDie2 + timeDie3);
                        trainQueue.setMaxStayInQueue((int) sumOfTime);
                        if (minTime > sumOfTime) {
                            minTime = sumOfTime;
                        }
                        boardedToTrain[o.getSeat() - 1] = o;
                        boardedToTrain[o.getSeat() - 1].setSecondsInQueue((int) sumOfTime);
                        /*TimeUnit.SECONDS.sleep((int) timeDie1);*/
                        System.out.println(o.getFirstName() + " boarded to train to seat #" + o.getSeat());
                        for (int i = 0; i <= 19; i++) {
                            trainQueue.queueArray[i] = trainQueue.queueArray[i + 1];
                        }
                        trainQueue.queueArray[20] = null;
                        trainQueue.i--;
                        break;
                    } else {
                        break wrapper;
                    }
                }
            } else {
                System.out.println("Sorry. Train is about to depart, Boarding process stopped.");
                break;
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
                    writer.write("\nMaximum length queue attained : "+trainQueue.getMaxLength());
                    writer.write("\nMaximum waiting time          : "+trainQueue.getMaxStayInQueue());
                    writer.write("\nMinimum waiting time          : "+Math.round(minTime));
                    writer.write("\nAverage Waiting time          : "+f.format(averageWaitingTime));

                    reportGui(trainQueue.getMaxLength(),trainQueue.getMaxStayInQueue(),Math.round(minTime),averageWaitingTime);

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

    private void loadDataToTrainQueue(File trainQueueFile, Scanner sc, LocalDate today) {
        Passenger temp = null;
        while (true) {
            System.out.print("Are you sure you want to load data? (y/n): ");
            String loadInput = sc.next();
            System.out.println();

            if (loadInput.equalsIgnoreCase("y")) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(trainQueueFile));

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
                                for (int i = 0 ; i <= 20 ; i++) {
                                    if (trainQueue.queueArray[i] != null && trainQueue.queueArray[i].getSeat() == Integer.parseInt(seat[1])) {
                                        break;
                                    } else if (trainQueue.queueArray[i] != null && trainQueue.queueArray[i].getSeat() != Integer.parseInt(seat[1])){
                                        continue;
                                    } else {
                                        trainQueue.queueArray[i] = new Passenger();
                                        trainQueue.queueArray[i].setName(firstName, sureName);
                                        trainQueue.queueArray[i].setNic(nic[1]);
                                        trainQueue.queueArray[i].setSeat(Integer.parseInt(seat[1]));
                                        break;
                                    }
                                }
                                sort(temp);

                            } else if (station[1].equalsIgnoreCase("Badulla") && day[1].equals(String.valueOf(today))) {
                                for (int i = 0 ; i <= 20 ; i++) {
                                    if (trainQueue.queueArray[i] != null && trainQueue.queueArray[i].getSeat() == Integer.parseInt(seat[1])) {
                                        break;
                                    } else if (trainQueue.queueArray[i] != null && trainQueue.queueArray[i].getSeat() != Integer.parseInt(seat[1])){
                                        continue;
                                    } else {
                                        trainQueue.queueArray[i] = new Passenger();
                                        trainQueue.queueArray[i].setName(firstName, sureName);
                                        trainQueue.queueArray[i].setNic(nic[1]);
                                        trainQueue.queueArray[i].setSeat(Integer.parseInt(seat[1]));
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
                    System.out.println("File Not Found!");
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
                    FileWriter queueWriter = new FileWriter(queue, false);
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

    private void viewTrainQueue(int  trip) {
        Stage viewStage = new Stage();
        viewStage.initModality(Modality.APPLICATION_MODAL);
        viewStage.setTitle("View All Passengers");

        GridPane gp = new GridPane();
        gp.setStyle("-fx-background-color: ALICEBLUE; -fx-max-width: Infinity; -fx-max-height: Infinity");
        gp.setPadding(new Insets(0,5,0,5));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        col1.setHalignment(HPos.CENTER);
        ColumnConstraints col4 = new ColumnConstraints(3);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(20);
        col2.setHalignment(HPos.CENTER);
        ColumnConstraints col5 = new ColumnConstraints(3);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(60);
        col3.setHalignment(HPos.CENTER);
        gp.getColumnConstraints().addAll(col1,col4,col2,col5,col3);

        RowConstraints row1 = new RowConstraints(50);
        row1.setValignment(VPos.CENTER);
        RowConstraints row3 = new RowConstraints(550);
        row3.setVgrow(Priority.ALWAYS);
        RowConstraints row5 = new RowConstraints(70);
        row5.setValignment(VPos.CENTER);
        gp.getRowConstraints().addAll(row1,row3,row5);

        Label waitingRoom = new Label("WAITING ROOM");
        waitingRoom.setFont(Font.font("Century",FontWeight.EXTRA_BOLD,20));
        Label queue = new Label("TRAIN QUEUE");
        queue.setFont(Font.font("Century",FontWeight.EXTRA_BOLD,20));
        Label train = new Label("TRAIN");
        train.setFont(Font.font("Century",FontWeight.EXTRA_BOLD,20));

        Separator separator1 = new Separator(Orientation.VERTICAL);
        Separator separator2 = new Separator(Orientation.VERTICAL);

        FlowPane flowPane1 = new FlowPane(Orientation.VERTICAL);
        flowPane1.setPadding(new Insets(10,0,0,0));
        flowPane1.setVgap(1);
        flowPane1.setAlignment(Pos.TOP_CENTER);
        FlowPane flowPane2 = new FlowPane(Orientation.VERTICAL);
        flowPane2.setPadding(new Insets(10,0,0,0));
        flowPane2.setVgap(1);
        flowPane2.setAlignment(Pos.TOP_CENTER);
        FlowPane flowPane3 = new FlowPane(Orientation.VERTICAL);
        flowPane3.setHgap(45);
        flowPane3.setVgap(11);
        flowPane3.setPadding(new Insets(12,10,8,15));

        Pane pane = new Pane();

        Button close = new Button("CLOSE");
        close.setLayoutX(670);
        close.setLayoutY(20);
        close.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: ORANGERED; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");

        close.setOnMouseEntered(event -> {
            close.setCursor(Cursor.HAND); //----------------------------------------Change cursor to hand
            close.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: RED; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
        });
        close.setOnMouseExited(event -> {
            close.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: ORANGERED; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
        });

        close.setOnAction(event -> {
            viewStage.close();
        });

        pane.getChildren().add(close);

        gp.add(waitingRoom,0,0);
        gp.add(queue,2,0);
        gp.add(train,4,0);
        gp.add(flowPane1,0,1);
        gp.add(flowPane2,2,1);
        gp.add(flowPane3,4,1);
        gp.add(separator1,1,1);
        gp.add(separator2,3,1);
        gp.add(pane,4,2);

        if (trip == 1) {
            for (int i = 0; i <= colomboToBadullaWaitingList.length - 1; i++) {
                if (colomboToBadullaWaitingList[i] != null){
                    Label lbl = new Label();
                    lbl.setText(colomboToBadullaWaitingList[i].getFirstName()+" "+colomboToBadullaWaitingList[i].getSureName()+" - "+colomboToBadullaWaitingList[i].getSeat());
                    lbl.setFont(Font.font(null,FontWeight.BOLD,13));
                    flowPane1.getChildren().add(lbl);
                }
            }
        } else {
            for (int i = 0; i <= badullaToColomboWaitingList.length - 1; i++) {
                if (badullaToColomboWaitingList[i] != null){
                    Label lbl = new Label();
                    lbl.setText(badullaToColomboWaitingList[i].getFirstName()+" "+badullaToColomboWaitingList[i].getSureName()+" - "+badullaToColomboWaitingList[i].getSeat());
                    lbl.setFont(Font.font(null,FontWeight.BOLD,13));
                    flowPane1.getChildren().add(lbl);
                }
            }
        }

        for (int i = 0 ; i <= trainQueue.queueArray.length - 1 ; i++){
            if (trainQueue.queueArray[i] != null){
                Label lbl = new Label();
                lbl.setText(trainQueue.queueArray[i].getFirstName()+" "+trainQueue.queueArray[i].getSureName()+" - "+trainQueue.queueArray[i].getSeat());
                lbl.setFont(Font.font(null,FontWeight.BOLD,13));
                flowPane2.getChildren().add(lbl);
            }
        }
        for (int i = 0 ; i <= boardedToTrain.length - 1 ; i++){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setMaxWidth(200);
            hBox.setPrefWidth(210);
            hBox.setAlignment(Pos.CENTER_LEFT);
            Button btn = new Button("Seat "+(i+1));
            btn.setPrefWidth(60);
            Label lbl = new Label();
            hBox.getChildren().addAll(btn,lbl);
            flowPane3.getChildren().add(hBox);
            if (boardedToTrain[i] != null){
                lbl.setText(" - "+boardedToTrain[i].getFirstName()+" "+boardedToTrain[i].getSureName());
                btn.setStyle("-fx-background-radius: 7; -fx-text-fill: CORNFLOWERBLUE; -fx-border-color: CORNFLOWERBLUE; -fx-font-weight: BOLD; -fx-background-color: WHITE; -fx-border-radius: 7");
                lbl.setStyle("-fx-text-fill: CORNFLOWERBLUE; -fx-font-weight: bold");
            } else {
                lbl.setText(" - Empty");
                btn.setStyle("-fx-background-radius: 7; -fx-text-fill: ORANGERED; -fx-border-color: ORANGERED; -fx-font-weight: BOLD; -fx-background-color: WHITE; -fx-border-radius: 7");
                lbl.setStyle("-fx-text-fill: ORANGERED; -fx-font-weight: bold");
            }
        }

        Scene scene = new Scene(gp,1300,670);
        viewStage.setScene(scene);
        viewStage.showAndWait();

    }

    private void addCustomer(int trip) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Waiting Room");

        GridPane gp = new GridPane();
        gp.setStyle("-fx-background-color: ALICEBLUE");
        gp.setPadding(new Insets(0,5,0,5));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100);
        col1.setHgrow(Priority.ALWAYS);
        col1.setHalignment(HPos.CENTER);
        gp.getColumnConstraints().add(col1);

        RowConstraints row1 = new RowConstraints();
        row1.setPrefHeight(47);
        row1.setValignment(VPos.BOTTOM);
        RowConstraints row2 = new RowConstraints();
        row2.setPrefHeight(3);
        RowConstraints row3 = new RowConstraints();
        row3.setPrefHeight(380);
        RowConstraints row4 = new RowConstraints();
        row4.setPrefHeight(3);
        RowConstraints row5 = new RowConstraints();
        row5.setPrefHeight(67);
        gp.getRowConstraints().addAll(row1,row2,row3,row4,row5);

        Label title = new Label("W A I T I N G   R O O M");
        title.setFont(Font.font("Century",FontWeight.EXTRA_BOLD,25));

        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        separator1.setMaxWidth(750);
        Separator separator2 = new Separator(Orientation.HORIZONTAL);
        separator2.setMaxWidth(750);

        FlowPane flowPane = new FlowPane(Orientation.VERTICAL);
        flowPane.setPadding(new Insets(21,10,10,20));
        flowPane.setHgap(40);
        flowPane.setVgap(24);

        if (trip == 1){
            for (int i = 0 ; i <= colomboToBadullaWaitingList.length - 1 ; i++){
                HBox hBox = new HBox();
                Button btn = new Button("slot "+(i+1));
                Label lbl = new Label();
                lbl.setMaxWidth(70);
                btn.setPrefWidth(60);
                hBox.getChildren().addAll(btn,lbl);
                hBox.setAlignment(Pos.CENTER);
                hBox.setMaxWidth(150);
                flowPane.getChildren().add(hBox);

                if (colomboToBadullaWaitingList[i] != null){
                    btn.setStyle("-fx-background-radius: 7; -fx-text-fill: CORNFLOWERBLUE; -fx-border-color: CORNFLOWERBLUE; -fx-font-weight: BOLD; -fx-background-color: WHITE; -fx-border-radius: 7");
                    lbl.setText(" - "+colomboToBadullaWaitingList[i].getFirstName());
                    lbl.setStyle("-fx-text-fill: CORNFLOWERBLUE; -fx-font-weight: bold");
                } else {
                    btn.setStyle("-fx-background-radius: 7; -fx-text-fill: ORANGERED; -fx-border-color: ORANGERED; -fx-font-weight: BOLD; -fx-background-color: WHITE; -fx-border-radius: 7");
                    lbl.setText(" - empty");
                    lbl.setStyle("-fx-text-fill: ORANGERED; -fx-font-weight: bold");
                }
            }
        } else {
            for (int i = 0 ; i <= badullaToColomboWaitingList.length - 1 ; i++){
                HBox hBox = new HBox();
                Button btn = new Button("slot "+(i+1));
                Label lbl = new Label();
                lbl.setMaxWidth(70);
                btn.setPrefWidth(60);
                hBox.getChildren().addAll(btn,lbl);
                hBox.setAlignment(Pos.CENTER);
                hBox.setMaxWidth(150);
                flowPane.getChildren().add(hBox);

                if (badullaToColomboWaitingList[i] != null){
                    btn.setStyle("-fx-background-radius: 7; -fx-text-fill: CORNFLOWERBLUE; -fx-border-color: CORNFLOWERBLUE; -fx-font-weight: BOLD; -fx-background-color: WHITE; -fx-border-radius: 7");
                    lbl.setText(" - "+badullaToColomboWaitingList[i].getFirstName());
                    lbl.setStyle("-fx-text-fill: CORNFLOWERBLUE; -fx-font-weight: bold");
                } else {
                    btn.setStyle("-fx-background-radius: 7; -fx-text-fill: ORANGERED; -fx-border-color: ORANGERED; -fx-font-weight: BOLD; -fx-background-color: WHITE; -fx-border-radius: 7");
                    lbl.setText(" - empty");
                    lbl.setStyle("-fx-text-fill: ORANGERED; -fx-font-weight: bold");
                }
            }
        }

        Pane btns = new Pane();

        Button add = new Button("ADD");
        add.setLayoutX(355);
        add.setLayoutY(15);
        add.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: CORNFLOWERBLUE; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
        Button close = new Button("CLOSE");
        close.setLayoutX(465);
        close.setLayoutY(15);
        close.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: ORANGERED; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
        btns.getChildren().addAll(add,close);

        gp.add(title,0,0);
        gp.add(separator1,0,1);
        gp.add(flowPane,0,2);
        gp.add(separator2,0,3);
        gp.add(btns,0,4);

        close.setOnMouseEntered(event -> {
            close.setCursor(Cursor.HAND); //----------------------------------------Change cursor to hand
            close.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: RED; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
        });
        close.setOnMouseExited(event -> {
            close.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: ORANGERED; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
        });

        close.setOnAction(event -> {
            stage.close();
        });

        add.setOnMouseEntered(event -> {
            add.setCursor(Cursor.HAND); //----------------------------------------Change cursor to hand
            add.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: DARKSLATEBLUE; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
        });
        add.setOnMouseExited(event -> {
            add.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: CORNFLOWERBLUE; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
        });

        add.setOnAction(event -> {
            if (trip == 1) {
                try {
                    if (trainQueue.isFull()) {
                        System.out.println("The queue is full right now. Board some passengers to the train.");
                    } else {
                        Method m = PassengerQueue.class.getDeclaredMethod("add", Passenger[].class);
                        m.setAccessible(true);
                        m.invoke(trainQueue, (Object) colomboToBadullaWaitingList);
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    if (trainQueue.isFull()) {
                        System.out.println("The queue is full right now. Board some passengers to the train.");
                    } else {
                        Method m = PassengerQueue.class.getDeclaredMethod("add", Passenger[].class);
                        m.setAccessible(true);
                        m.invoke(trainQueue, (Object) badullaToColomboWaitingList);
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            Stage queueStage = new Stage();
            queueStage.initModality(Modality.APPLICATION_MODAL);
            queueStage.setTitle("Train Queue");

            GridPane gp1 = new GridPane();
            gp1.setStyle("-fx-background-color: ALICEBLUE");
            gp1.setPadding(new Insets(0,5,0,5));

            ColumnConstraints qcol1 = new ColumnConstraints();
            qcol1.setPercentWidth(27);
            qcol1.setHalignment(HPos.CENTER);
            ColumnConstraints qcol2 = new ColumnConstraints(3);
            ColumnConstraints qcol3 = new ColumnConstraints();
            qcol3.setPercentWidth(45);
            qcol3.setHalignment(HPos.CENTER);
            ColumnConstraints qcol4 = new ColumnConstraints(3);
            ColumnConstraints qcol5 = new ColumnConstraints();
            qcol5.setPercentWidth(27);
            qcol5.setHalignment(HPos.CENTER);
            gp1.getColumnConstraints().addAll(qcol1,qcol2,qcol3,qcol4,qcol5);

            RowConstraints qrow1 = new RowConstraints();
            qrow1.setPrefHeight(47);
            qrow1.setVgrow(Priority.ALWAYS);
            RowConstraints qrow2 = new RowConstraints();
            qrow2.setPrefHeight(3);
            qrow2.setVgrow(Priority.ALWAYS);
            RowConstraints qrow3 = new RowConstraints();
            qrow3.setPrefHeight(30);
            qrow3.setVgrow(Priority.ALWAYS);
            RowConstraints qrow6 = new RowConstraints();
            qrow6.setPrefHeight(3);
            qrow6.setVgrow(Priority.ALWAYS);
            RowConstraints qrow4 = new RowConstraints();
            qrow4.setPrefHeight(467);
            qrow4.setVgrow(Priority.ALWAYS);
            RowConstraints qrow7 = new RowConstraints();
            qrow7.setPrefHeight(3);
            qrow7.setVgrow(Priority.ALWAYS);
            RowConstraints qrow5 = new RowConstraints();
            qrow5.setPrefHeight(47);
            qrow5.setVgrow(Priority.ALWAYS);
            gp1.getRowConstraints().addAll(qrow1,qrow2,qrow3,qrow6,qrow4,qrow7,qrow5);

            Label qTitle = new Label("T R A I N   Q U E U E");
            qTitle.setFont(Font.font("Century",FontWeight.EXTRA_BOLD,20));

            Label no = new Label("No.");
            no.setFont(Font.font(null,FontWeight.BOLD,15));
            Label name = new Label("Name");
            name.setFont(Font.font(null,FontWeight.BOLD,15));
            Label seatNum = new Label("Seat No.");
            seatNum.setFont(Font.font(null,FontWeight.BOLD,15));

            FlowPane qFlowPane1 = new FlowPane(Orientation.VERTICAL);
            qFlowPane1.setVgap(1);
            qFlowPane1.setAlignment(Pos.TOP_CENTER);
            qFlowPane1.setPadding(new Insets(10,0,0,0));
            FlowPane qFlowPane2 = new FlowPane(Orientation.VERTICAL);
            qFlowPane2.setVgap(1);
            qFlowPane2.setAlignment(Pos.TOP_CENTER);
            qFlowPane2.setPadding(new Insets(10,0,0,0));
            FlowPane qFlowPane3 = new FlowPane(Orientation.VERTICAL);
            qFlowPane3.setVgap(1);
            qFlowPane3.setAlignment(Pos.TOP_CENTER);
            qFlowPane3.setPadding(new Insets(10,0,0,0));

            Separator hSeparator1 = new Separator(Orientation.HORIZONTAL);
            hSeparator1.setMaxWidth(450);
            Separator vSeparator1 = new Separator(Orientation.VERTICAL);
            vSeparator1.setMinHeight(400);
            Separator vSeparator2 = new Separator(Orientation.VERTICAL);
            vSeparator2.setMinHeight(400);

            Button qClose = new Button("CLOSE");
            qClose.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: ORANGERED; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
            qClose.setOnAction(event1 -> {
                queueStage.close();
                stage.close();
            });
            qClose.setOnMouseEntered(event1 -> {
                qClose.setCursor(Cursor.HAND); //----------------------------------------Change cursor to hand
                qClose.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: RED; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
            });
            qClose.setOnMouseExited(event1 -> {
                qClose.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: ORANGERED; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
            });

            gp1.add(qTitle,0,0,5,1);
            gp1.add(hSeparator1,0,1,5,1);
            gp1.add(no,0,2);
            gp1.add(name,2,2);
            gp1.add(seatNum,4,2);
            gp1.add(qFlowPane1,0,4);
            gp1.add(vSeparator1,1,4);
            gp1.add(vSeparator2,3,4);
            gp1.add(qFlowPane2,2,4);
            gp1.add(qFlowPane3,4,4);
            gp1.add(qClose,2,6);

            for (int i = 0 ; i < trainQueue.queueArray.length - 1 ; i++){
                if (trainQueue.queueArray[i] != null){
                    Label lblNo = new Label(String.valueOf(i+1));
                    Label lblName = new Label(trainQueue.queueArray[i].getFirstName()+" "+trainQueue.queueArray[i].getSureName());
                    Label lblSeat = new Label(String.valueOf(trainQueue.queueArray[i].getSeat()));
                    lblNo.setFont(Font.font(null, FontWeight.BOLD, 13));
                    lblName.setFont(Font.font(null, FontWeight.BOLD, 13));
                    lblSeat.setFont(Font.font(null, FontWeight.BOLD, 13));
                    qFlowPane1.getChildren().add(lblNo);
                    qFlowPane2.getChildren().add(lblName);
                    qFlowPane3.getChildren().add(lblSeat);
                }
            }

            Scene scene1 = new Scene(gp1,500,610);
            queueStage.setScene(scene1);
            queueStage.showAndWait();
        });

        Scene scene = new Scene(gp,930,500);
        stage.setScene(scene);
        stage.showAndWait();

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

    private void reportGui(int maxLength, double maxWaiting, double minWaiting, double aveWaiting){
        Stage stage = new Stage();
        stage.setTitle("Report");
        stage.initModality(Modality.APPLICATION_MODAL);

        GridPane gp = new GridPane();
        gp.setStyle("-fx-background-color: ALICEBLUE");
        gp.setPadding(new Insets(0,5,0,5));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(35);
        ColumnConstraints col2 = new ColumnConstraints(3);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(20);
        ColumnConstraints col4 = new ColumnConstraints(3);
        ColumnConstraints col5 = new ColumnConstraints();
        col5.setPercentWidth(10);
        ColumnConstraints col6 = new ColumnConstraints(3);
        ColumnConstraints col7 = new ColumnConstraints();
        col7.setPercentWidth(15);
        ColumnConstraints col8 = new ColumnConstraints(3);
        ColumnConstraints col9 = new ColumnConstraints();
        col9.setPercentWidth(20);
        gp.getColumnConstraints().addAll(col1,col2,col3,col4,col5,col6,col7,col8,col9);

        RowConstraints row1 = new RowConstraints(50);
        row1.setVgrow(Priority.ALWAYS);
        RowConstraints row2 = new RowConstraints(3);
        row2.setVgrow(Priority.ALWAYS);
        RowConstraints row7 = new RowConstraints(47);
        row7.setVgrow(Priority.ALWAYS);
        RowConstraints row3 = new RowConstraints(400);
        row3.setVgrow(Priority.ALWAYS);
        RowConstraints row4 = new RowConstraints(4);
        row4.setVgrow(Priority.ALWAYS);
        RowConstraints row5 = new RowConstraints(96);
        row5.setVgrow(Priority.ALWAYS);
        RowConstraints row6 = new RowConstraints(50);
        row6.setVgrow(Priority.ALWAYS);
        gp.getRowConstraints().addAll(row1,row2,row7,row3,row4,row5,row6);

        Label title = new Label("R E P O R T");
        title.setFont(Font.font("Century",FontWeight.EXTRA_BOLD,25));

        Label name = new Label("Name");
        Label nic = new Label("NIC No.");
        Label seat = new Label("Seat No.");
        Label date = new Label("Date");
        Label station = new Label("Station");

        Separator hSep1 = new Separator(Orientation.HORIZONTAL);
        Separator hSep2 = new Separator(Orientation.HORIZONTAL);
        Separator vSep1 = new Separator(Orientation.VERTICAL);
        Separator vSep2 = new Separator(Orientation.VERTICAL);
        Separator vSep3 = new Separator(Orientation.VERTICAL);
        Separator vSep4 = new Separator(Orientation.VERTICAL);

        FlowPane flowPane1 = new FlowPane(Orientation.VERTICAL);
        FlowPane flowPane2 = new FlowPane(Orientation.VERTICAL);
        FlowPane flowPane3 = new FlowPane(Orientation.VERTICAL);
        FlowPane flowPane4 = new FlowPane(Orientation.VERTICAL);
        FlowPane flowPane5 = new FlowPane(Orientation.VERTICAL);
        FlowPane flowPane6 = new FlowPane(Orientation.HORIZONTAL);

        Label maxLengthLbl = new Label("Maximum Length Queue Attained : "+maxLength);
        Label maxWaitingLbl = new Label("Maximum Waiting Time         : "+maxWaiting);
        Label minWaitingLbl = new Label("Minimum Waiting Time         : "+minWaiting);
        Label aveWaitingLbl = new Label("Average Waiting TIme         : "+aveWaiting);

        flowPane6.getChildren().addAll(maxLengthLbl,maxWaitingLbl,minWaitingLbl,aveWaitingLbl);

        Button close = new Button("CLOSE");
        close.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: ORANGERED; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");

        close.setOnMouseEntered(event -> {
            close.setCursor(Cursor.HAND); //----------------------------------------Change cursor to hand
            close.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: RED; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
        });
        close.setOnMouseExited(event -> {
            close.setStyle("-fx-pref-width: 80; -fx-pref-height: 30; -fx-background-color: ORANGERED; -fx-background-radius: 10; -fx-font-weight: BOLD; -fx-text-fill: WHITE");
        });

        close.setOnAction(event -> {
            stage.close();
        });

        gp.add(title,0,0,9,1);
        gp.add(hSep1,0,1,9,1);
        gp.add(name,0,2);
        gp.add(nic,2,2);
        gp.add(seat,4,2);
        gp.add(date,6,2);
        gp.add(station,8,2);
        gp.add(vSep1,1,2,3,4);
        gp.add(vSep2,3,2,4,4);
        gp.add(vSep3,5,2,6,4);
        gp.add(vSep4,7,2,8,4);
        gp.add(flowPane1,0,3);
        gp.add(flowPane2,2,3);
        gp.add(flowPane3,4,3);
        gp.add(flowPane4,6,3);
        gp.add(flowPane5,8,3);
        gp.add(hSep2,0,4,9,4);
        gp.add(flowPane6,0,5,9,6);
        gp.add(close,0,6,9,6);

        Scene scene = new Scene(gp, 812,650);
        stage.setScene(scene);
        stage.showAndWait();

    }
}
