package socialnetwork.ui;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.exceptions.RepositoryException;
import socialnetwork.service.AdminService;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminUI implements UI {

    private String options = "";
    private String input = null;
    private AdminService service;

    public AdminUI(AdminService service) {
        this.service = service;
        this.options += "Welcome to ToySocialNetwork\n" +
                "To display current users in the network type 'show'\n" +
                "To add an user to the network type 'add'\n" +
                "To remove an user from the network type 'remove'\n" +
                "To add one friendship to the network type 'add friend'\n" +
                "To remove friendship from the network type 'remove friend'\n" +
                "To print graph type 'graph'\n" +
                "To print number of connected compoents type 'components'\n" +
                "To print the strongest connected component type 'strongest'\n" +
                "To exit the program type 'exit'";
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
            case "show":
                showUI();
                break;
            case "add":
                try {
                    addUI();
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "remove":
                try {
                    removeUI();
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "add friend":
                try {
                    addFriendUI();
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "remove friend":
                try {
                    removeFriendUI();
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "graph":
                printGraphUI();
                break;
            case "components":
                connectedComponent();
                break;
            case "strongest":
                strongestComponent();
                break;
            case "menu":
                showMenu();
                break;
            default:
                System.out.println("Invalid command");
                break;
        }
    }

    private void strongestComponent() {
        System.out.println("Most sociable community: ");
        service.getStrongestConnectedComponent().forEach(entry -> {
            System.out.print(entry.toString() + " ");
        });
    }

    private void connectedComponent() {
        System.out.println("Number of connected components: " + service.getNumberOfConnectedComponents());
    }

    private void printGraphUI() {
        service.getUserGraph().getAdjacency().entrySet().forEach(userListEntry -> {
            System.out.print(userListEntry.getKey() + ": ");
            userListEntry.getValue().forEach(uID -> {
                System.out.print(uID + " ");
            });
            System.out.println();
        });
    }

    private void removeFriendUI() {
        System.out.println("Select the IDS of the users you don't want to be friends: ");
        service.getAll().forEach(System.out::println);
        System.out.print("Insert first ID: ");
        Scanner scanner = new Scanner(System.in);
        Long ID1 = scanner.nextLong();
        System.out.print("Insert the second ID: ");
        Long ID2 = scanner.nextLong();
        service.removeFriend(ID1, ID2);
        System.out.println("Friendship removed successfully");
    }


    private void addFriendUI() {
        System.out.println("Select the IDS of the users you want to be friends: ");
        service.getAll().forEach(System.out::println);
        Scanner scanner = new Scanner(System.in);

        Long ID1 = null;
        Long ID2 = null;

        try {
            System.out.print("Insert first ID: ");
            ID1 = scanner.nextLong();
            System.out.print("Insert the second ID: ");
            ID2 = scanner.nextLong();
            if ((ID1.compareTo(0L) < 0 || ID2.compareTo(0L) < 0))
                System.out.println("Please insert an positive integer.");
            else {
                service.addFriend(ID1, ID2);
            }
        } catch (InputMismatchException e) {
            System.out.println("Please don't try to insert symbols");
        }
    }

    private void showUI() {
        System.out.println("The list of all the users in the network: ");
        service.getAll().forEach(System.out::println);
    }

    private void removeUI() {
        System.out.println("Select the ID of the user you want to be deleted from the following list: ");
        service.getAll().forEach(System.out::println);
        System.out.print("Insert user ID: ");
        Scanner scanner = new Scanner(System.in);
        Long ID = scanner.nextLong();
        service.removeUser(ID);
        System.out.println("User removed successfully");
    }

    private void addUI() {
        Long newID = service.getMaxID() + 1;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert user first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Insert user last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Insert user email: ");
        String email = scanner.nextLine();
        User newUser = new User(firstName, lastName);
        newUser.setEmail(email);
        newUser.setId(newID);
        try {
            service.addUser(newUser);
        } catch (RepositoryException e) {
            System.out.println("Email already exists");
            return;
        }
        System.out.println("User added successfully");
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
