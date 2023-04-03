package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TenmoService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TenmoService tenmoService = new TenmoService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                 viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
                int selection = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
                if(selection == 0) {
                    continue;
                } else {
                    viewTransferByTransferId(selection, currentUser);
                }
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                getUsers();
               int accountTo = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
                if(accountTo == 0){
                    continue;
                } else {
                    BigDecimal amount = consoleService.promptForBigDecimal("Enter amount : ");
                    sendBucks(accountTo, amount);
                }
            } else if (menuSelection == 5) {

                getUsers();
                int accountFrom = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel): ");
                if (accountFrom == 0){
                    continue;
                } else {
                    BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");
                    requestBucks(accountFrom, amount );
                }
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        System.out.println(tenmoService.getBalance(currentUser));
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        Transfer[] allTransfers = tenmoService.viewTransferHistory(currentUser);
        System.out.println("Transfers");
        System.out.println("ID" + "         " + "From/To" + "         " + "Amount");
        for (Transfer cur : allTransfers) {
            System.out.println(cur.getId() + "         " + cur.getAccountTo() + "         " + cur.getAmount());
        }
		
	}
    private void viewTransferByTransferId (int selection, AuthenticatedUser authenticatedUser){
        Transfer toPrint = tenmoService.viewTransferDetails(selection, authenticatedUser);
        // change print to show all data
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        System.out.println("Id: " + toPrint.getId());
        System.out.println("From: " + toPrint.getFromName());
        System.out.println("To: " + toPrint.getToName());
        System.out.println("Type: " + toPrint.getNameOfTransferType());
        System.out.println("Status: " + toPrint.getNameOfTransferStatus());
        System.out.println("Amount: " + toPrint.getAmount());
    }

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks(int sendToUserId, BigDecimal amount) {
        // TODO Auto-generated method stub
        tenmoService.sendBucks(currentUser, amount, sendToUserId);

		
	}

	private void requestBucks(int requestUserId, BigDecimal amount) {
		// TODO Auto-generated method stub
        tenmoService.requestBucks(currentUser, requestUserId, amount);
		
	}

    private void getUsers(){
       User[] allUsers = tenmoService.getUsers(currentUser);
        System.out.println("Users");
        System.out.println("ID"+"                "+  "Name");
        for(User cur : allUsers){

            System.out.println(cur.getId() +"                "+ cur.getUsername());

        }
    }

}
