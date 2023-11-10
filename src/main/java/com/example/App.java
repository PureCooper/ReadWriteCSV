package com.example;
import java.io.Reader;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter; //crap ton of imports 

//NAME: Cooper Brown
//Date: 11/10/2023

public class App 
{
    public static void main( String[] args )
    {
        String path = "src\\main\\people.csv"; //path of our CSV file
        Scanner input = new Scanner(System.in); //input object
        String userInput; 

        while(true) { //infinite loop only exits when program exits
            ClearConsole(); //see clear console method
            System.out.println("What would you like to do?"); //displays options to users
            System.out.println("(1) View Inventory");
            System.out.println("(2) New Inventory Entry");
            System.out.println("(3) Exit");
            DisplayCursor(); //see display cursor method
            userInput = input.nextLine(); //gets input from user 

            if(userInput.equals("1")){ //if they want to view inventory
                ClearConsole();
                DisplayCSV(path, input); //see display csv method
            }
            else if(userInput.equals("2")){ //if they want to add an entry
                ClearConsole();
                WriteCSV(path, input); //see write csv method
            }
            else if(userInput.equals("3")){ //if they want to exit
                ClearConsole();
                input.close(); //closes input so no memory leak
                System.exit(0); // exits the program
            }
            else{ //if user doesn't choose an option
                ClearConsole();
                System.out.println("That is not a valid option!");
                Continue(); //see continue method
                input.nextLine();
            }
        }
    }

    static void WriteCSV(String path, Scanner input) { //takes the input scanner and path as inputs
        try(PrintWriter writer = new PrintWriter(new FileWriter(path, true))){ //checks if file is there, allows user to append
            String userInput; //variables used to add new entry 
            String productName;
            String productID;
            String price; 
            String quantity;
            String manufacturer;

            while(true){ //check for product name 
                DisplayNewInventoryEntry(); //see display new inventory entry method
                System.out.println("Product name: ");
                DisplayCursor();
                userInput = input.nextLine();
                if(Pattern.matches("\\D*", userInput) && userInput != ""){ //if input is only characters and not empty
                    productName = ("\"" + userInput + "\""); //adds quotes 
                    break; //exits loop
                }
                else{
                    InvalidInput(1); //see invalid input 
                    input.nextLine();
                }
            }
            while(true){ //check for product id 
                DisplayNewInventoryEntry();
                System.out.println("Product ID: ");
                DisplayCursor();
                userInput = input.nextLine();
                if(IsNumeric(userInput)){ //if user input is numeric
                    productID = ("\"" + userInput + "\"");
                    break;
                }
                else{
                    InvalidInput(0);
                    input.nextLine();
                }
            }
            while(true){ //check for price
                DisplayNewInventoryEntry();
                System.out.println("Price: ");
                DisplayCursor();
                userInput = input.nextLine();
                if(IsNumeric(userInput)){ //if user input is numeric 
                    price = ("\"" + "$" + userInput + "\"");
                    break;
                }
                else{
                    InvalidInput(0);
                    input.nextLine();
                }
            }
            while(true){ //check for quantity 
                DisplayNewInventoryEntry();
                System.out.println("Quantity: ");
                DisplayCursor();
                userInput = input.nextLine();
                if(IsNumeric(userInput)){ //if user input is numeric 
                    quantity = ("\"" + userInput + "\"");
                    break;
                }
                else{
                    InvalidInput(0);
                    input.nextLine();
                }
            }
            while(true){ //check for manufacturer
                DisplayNewInventoryEntry();
                System.out.println("Manufacturer: ");
                DisplayCursor();
                userInput = input.nextLine();
                if(Pattern.matches("\\D*", userInput) && userInput != ""){ //if user input is only characters and not empty
                    manufacturer = ("\"" + userInput + "\"");
                    break;
                }
                else{
                    InvalidInput(2);
                    input.nextLine();
                }
            }

            writer.println(productName + ", " + productID + ", " + price + ", " + quantity + ", " + manufacturer); //writes to csv file
            ClearConsole();
            System.out.println("New entry added successfully!");
            Continue();
            input.nextLine();
            
        } catch(IOException e) //if exception is thrown
        {
            e.printStackTrace(); //display the exception
        }
        
    }

    static boolean IsNumeric(String num) { //tries to parse the input as a double, if succesful return true, else return false. 
        if (num == null) {
            return false;
        }
        try {
            Double.parseDouble(num);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    static void DisplayCSV(String path, Scanner input) { //takes path and input scanner object
        try(Reader reader = new FileReader(path)) { //checks if file exists
            CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL); //creates new parser object 
            boolean skipHeaders = true; //boolean to skip headers
            int counter = 0; //counter to show what entry is being displayed

            for (CSVRecord record : parser) { //for each record in the parse of the csv file
                if (skipHeaders) { //if skip headers is true
                    skipHeaders = false; 
                    continue; //skips headers 
                }

                counter++; //iterates the counter

                String productName = record.get(0).replace("\"", ""); //pulls the data from the record
                String productID = record.get(1).replace("\"", ""); //puts it in our variables
                String price = record.get(2).replace("\"", "");
                String quantity = record.get(3).replace("\"", "");
                String manufacturer = record.get(4).replace("\"", "");

                System.out.println("~~~~~~~~~Entry " + (counter) + "~~~~~~~~~"); //displays the info to the user
                System.out.println("Product Name: " + productName);
                System.out.println("Product ID:  " + productID);
                System.out.println("Price:       " + price);
                System.out.println("Quantity:    " + quantity);
                System.out.println("Manufacturer:" + manufacturer);

            }
            parser.close(); //closes parser
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~");
            Continue();
            input.nextLine();
        } catch (IOException e) { //if exception thrown
            e.printStackTrace(); //display exception to user
        }
    }

    static void InvalidInput(int key) { // Tells the user if they entered invald input, using a key to differentiate
        ClearConsole();
        if(key == 0){
            System.out.println("That is not valid input, please enter a number.");
        }
        else if(key == 1){
            System.out.println("That is not a valid input, please enter a name.");
        }
        else{
            System.out.println("That is not a valid input, please enter a manufacturer.");
        }
        Continue();
    }

    static void ClearConsole() { //clears console
        System.out.print("\033c"); // found this on stackoverflow as the easiest way to clear the console for
    }

    static void DisplayCursor() { // displays the cursor because it looks nice
        System.out.println();
        System.out.print("> ");
    }

    static void DisplayNewInventoryEntry() { //abstracting this into a method so I dont have to type it many times.
        ClearConsole();
        System.out.println("NEW INVENTORY ENTRY");
        System.out.println("~~~~~~~~~~~~~~~~~~~");
        System.out.println();
    }

    static void Continue() { //abstracting this into a method so I dont have to type it many times.
        System.out.println();
        System.out.println("Press any key to continue.");
        DisplayCursor();
    }

}
