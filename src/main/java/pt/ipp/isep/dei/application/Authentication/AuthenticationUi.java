package pt.ipp.isep.dei.application.Authentication;
import pt.ipp.isep.dei.domain.user.UserRoleDTO;


import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

public class AuthenticationUi implements Runnable, Serializable {
    private static final long serialVersionUID = 1L;
    private final AuthenticationController controller;
    private Scanner scanner = new Scanner(System.in);


    public AuthenticationUi() {
        controller = new AuthenticationController();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        boolean mainLoop = true;
        while (mainLoop) {
            System.out.print("=== Do you want to log in? [Y/N] ===\n");
            String loginResponse = sc.nextLine().trim().toUpperCase();
            if (!loginResponse.equals("Y")) {
                System.out.println("=== Exiting application ===");
                break;
            }

            boolean success = doLogin();
            if (!success) {
                System.out.println("=== Failed to login after multiple attempts ===");
                continue;
            }

            UserRoleDTO currentRole = this.controller.getCurrentUserRole();
            if (currentRole == null) {
                System.out.println("=== No role assigned to user ===");
                logout();
                continue;
            }

            boolean sessionActive = true;
            while (sessionActive) {
                redirectToRoleUI(currentRole);
                System.out.print("\n=== Do you want to perform another operation? [Y/N] ===\n");
                String continueResponse = sc.nextLine().trim().toUpperCase();
                if (!continueResponse.equals("Y")) {
                    sessionActive = false;
                }
            }

            logout();

            System.out.print("\n=== Do you want to login with another user? [Y/N] ===\n");
            String anotherUser = sc.nextLine().trim().toUpperCase();
            if (!anotherUser.equals("Y")) {
                mainLoop = false;
            }
        }
    }

    private boolean doLogin() {
        System.out.println("\n\n=== LOGIN UI ===\n");

        int maxAttempts = 3;
        boolean success = false;
        do {
            maxAttempts--;
            String email = readEmail();
            String pwd = readPassword();

            success = controller.doLogin(email, pwd);
            if (!success) {
                System.out.println("Invalid UserId and/or Password. \nYou have " + maxAttempts + " more attempt(s).");
            }

        } while (!success && maxAttempts > 0);
        return success;
    }

    private void logout() {
        controller.doLogout();
    }

    private String readEmail() {
        System.out.print("Enter UserId/Email: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim();
    }

    private String readPassword() {
        System.out.print("Enter Password: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim();
    }

    private void addUser() {
        Scanner sc = new Scanner(System.in);
        try {
            List<UserRoleDTO> roles = this.controller.getUserRoles();
            if ((roles == null) || (roles.isEmpty())) {
                System.out.println("No role assigned to user.");
            } else {
                UserRoleDTO role = selectsRole(roles);

                System.out.print("Enter email: ");
                String email = sc.nextLine().trim();

                System.out.print("Enter password (7 characters, 2 digits, 3 uppercase): ");
                String pwd = sc.nextLine().trim();

                String roleId = role.getId();
                String roleDesc = role.getDescription();
                controller.addUser(email, pwd, roleId, roleDesc);
                System.out.println("User created successfully.");
            }

        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private UserRoleDTO selectsRole(List<UserRoleDTO> roles) {
        if (roles.size() == 1) {
            return roles.get(0);
        } else {
            return readRole(roles);
        }
    }

    private UserRoleDTO readRole(List<UserRoleDTO> roles) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Select User Role by choosing an index: ");
        for (int i = 0; i < roles.size(); i++) {
            System.out.println("[" + i + "] " + roles.get(i).getDescription());
        }
        int index = Integer.parseInt(sc.nextLine().trim());
        return roles.get(index);
    }

    private void redirectToRoleUI(UserRoleDTO selectedRole) {
        if (selectedRole == null || selectedRole.getId() == null || selectedRole.getDescription() == null) {
            System.out.println("Invalid role information.");
            return;
        }

        if (selectedRole.getId().equals("PLAYER")) {
            controller.getPlayerMenu();
        }

        if (selectedRole.getId().equals("EDITOR")) {
            controller.getEditorMenu();

        }
        if (selectedRole.getId().equals("ADMIN")) {
            System.out.println("Hello, Admin!");
            Scanner sc = new Scanner(System.in);
            System.out.print("Do you want to add a new user? [Y/N]: ");
            String response = sc.nextLine().trim().toUpperCase();

            if (response.equals("Y")) {
                addUser();
            } else {
                System.out.println("No user will be added.");
            }
        }
    }
}
