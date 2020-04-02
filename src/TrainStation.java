import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;

public class TrainStation extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.println("-------------------------------------------------------------");
            System.out.println("        DENUWARA MENIKE TRAIN SEAT BOOKING PROGRAM");
            System.out.println("-------------------------------------------------------------");
            System.out.println("Enter \"A\" to add a customer to a seat.");
            System.out.println("Enter \"V\" to view all seats.");
            System.out.println("Enter \"E\" to display empty seats.");
            System.out.println("Enter \"D\" to delete customer from seat.");
            System.out.println("Enter \"F\" to find the seat for a given customer name.");
            System.out.println("Enter \"S\" to store program data into file.");
            System.out.println("Enter \"L\" to load program data from file.");
            System.out.println("Enter \"O\" to view seats ordered alphabetically by name.");
            System.out.println("Enter \"Q\" to quit.");
            System.out.println();
            System.out.println(">>You can only book seats from tomorrow and after.");
            System.out.println(">>You should have an NIC to book seats.");
            System.out.println("-------------------------------------------------------------");

            System.out.print("Choose option from above: ");
            String input = sc.next();
            System.out.println();
        }
    }
}
