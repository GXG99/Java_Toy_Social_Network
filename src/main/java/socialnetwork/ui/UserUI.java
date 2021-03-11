package socialnetwork.ui;

import socialnetwork.repository.exceptions.RepositoryException;
import socialnetwork.service.AdminService;
import socialnetwork.service.UserService;

import java.util.Scanner;
import java.util.SortedMap;

public class UserUI implements UI {
    private String options = "";
    private String input = null;
    private UserService service;

    public UserUI(UserService service) {
        this.service = service;
        options = "Menu: \n" +
                "'friends' - Show all friends \n" +
                "'friends date' - Show all friends in specified date \n" +
                "'send message' - Send a message \n" +
                "'reply message' - Reply to an user\n" +
                "'show conversation' - Shows the conversation with an user\n";
        init();

        welcome();
    }

    private void welcome() {
        if (service.getLoggedUser() != null)
            System.out.print("Welcome: " + service.getLoggedUser().getFirstName() + " "
                    + service.getLoggedUser().getLastName() + "\n");
    }

    private void init() {
        while (service.getLoggedUser() == null) {
            System.out.print("Enter the email of the user you want to be: ");
            Scanner scanner = new Scanner(System.in);
            String email = scanner.nextLine();
            try {
                service.logIn(email);
            } catch (RepositoryException exception) {
                System.out.println("Email does not exist!");
            }
        }
    }

    @Override
    public void runUI() {
        showMenu();
        boolean isRunning = true;
        while (isRunning) {
            readInput();
            if (input.toLowerCase().equals("exit")) {
                isRunning = false;
                System.out.println("Program exit");
            } else {
                System.out.println("You typed: " + input);
                computeCommand();
            }
            System.out.println();
        }
    }

    private void computeCommand() {
        String normalizedInput = input.toLowerCase();
        switch (normalizedInput) {
            case "friends":
                friendsUI();
                break;
            case "friends date":
                friendsDateUI();
                break;
            default:
                System.out.println("Invalid command");
                break;
        }
    }

    private void friendsDateUI() {
        int month = -1;
        while (month < 0 || month > 12) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Month: ");
            month = scanner.nextInt();
            if (month < 0 || month > 12) {
                System.out.println("Please enter a valid month");
            }
        }
        service.getFriendsByDate(month).forEach(System.out::println);
    }

    private void friendsUI() {
        service.getFriends().forEach(System.out::println);
    }

    @Override
    public void readInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Command>> ");
        input = scanner.nextLine();
    }

    @Override
    public void showMenu() {
        System.out.println(options);
    }
}
