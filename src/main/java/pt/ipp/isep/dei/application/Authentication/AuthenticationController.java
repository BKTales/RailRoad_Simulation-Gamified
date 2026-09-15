package pt.ipp.isep.dei.application.Authentication;

import pt.ipp.isep.dei.application.UserMenus.EditorMenu;
import pt.ipp.isep.dei.application.UserMenus.PlayerSimulationMenu;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.user.UserRoleDTO;
import pt.ipp.isep.dei.authenticationrepository.AuthenticationRepository;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import java.io.Serializable;
import java.util.List;

/**
 * Controller class responsible for managing user authentication,
 * user roles, scenarios, maps, and simulation lifecycle.
 */
public class AuthenticationController implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Constant for Editor role ID */
    public static final String ROLE_EDITOR_ID = "EDITOR";

    /** Constant for Player role ID */
    public static final String ROLE_PLAYER_ID = "PLAYER";

    /** Constant for Admin role ID */
    public static final String ROLE_ADMIN_ID = "ADMIN";

    private Simulator simulator = Simulator.getInstance();

    private final AuthenticationRepository authenticationRepository;

    /**
     * Constructs an AuthenticationController instance
     * initializing the AuthenticationRepository.
     */
    public AuthenticationController() {
        this.authenticationRepository = simulator.getAuthenticationRepository();
    }

    /**
     * Attempts to log in a user with the given ID and password.
     *
     * @param id  the user identifier (e.g., email)
     * @param pwd the user's password
     * @return true if login was successful; false otherwise
     */
    public boolean doLogin(String id, String pwd) {
        try {
            return authenticationRepository.doLogin(id, pwd);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Returns the list of roles for the currently logged-in user.
     *
     * @return list of UserRoleDTO for the current user or null if no user is logged in
     */
    public List<UserRoleDTO> getUserRoles() {
        if (authenticationRepository.getCurrentUserSession().isLoggedIn()) {
            return authenticationRepository.getCurrentUserSession().getUserRoles();
        }
        return null;
    }

    /**
     * Returns the primary role of the currently logged-in user.
     *
     * @return the UserRoleDTO of the current user or null if no user is logged in
     */
    public UserRoleDTO getCurrentUserRole() {
        if (authenticationRepository.getCurrentUserSession().isLoggedIn()) {
            return authenticationRepository.getCurrentUserSession().getUser().getUserRole();
        }
        return null;
    }

    /**
     * Adds a new user with the specified email, password, role ID and description.
     * If the role ID is "EDITOR", the user is added with the Editor role,
     * otherwise with the Player role.
     *
     * @param email           the email of the new user
     * @param password        the password for the new user
     * @param roleId          the role ID to assign
     * @param roleDescription the description of the role
     */
    public void addUser(String email, String password, String roleId, String roleDescription) {
        if (roleId.equals(ROLE_EDITOR_ID)) {
            authenticationRepository.addUser(email, password, ROLE_EDITOR_ID, roleDescription);
        } else {
            authenticationRepository.addUser(email, password, ROLE_PLAYER_ID, roleDescription);
        }
    }

    /**
     * Logs out the currently logged-in user.
     */
    public void doLogout() {
        authenticationRepository.doLogout();
    }

    /**
     * Starts a simulation for the given scenario name.
     * Initializes a new Simulation and starts production for all industries.
     * Runs the simulation year by year until the end year,
     * printing production and port statuses and sleeping for 1 second between each year.
     *
     * @param scenarioName the name of the scenario to simulate
     */


    /**
     * Returns the current map.
     *
     * @return the current Map instance
     */
    public Map getCurrentMap() {
        return Simulation.getInstance().getCurrentMap();
    }

    /**
     * Sets the current scenario by name.
     *
     * @param chosenScenario the name of the scenario to set as current
     */
    public void setCurrentScenario(String chosenScenario) {
        simulator.getScenarioRepository().setActiveScenario(chosenScenario);
    }

    public void getPlayerMenu() {
        PlayerSimulationMenu ui = new PlayerSimulationMenu();
        try{
            ui.start(App.getPrimaryStage());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getEditorMenu() {
        EditorMenu ui = new EditorMenu();
        try{
            ui.start(App.getPrimaryStage());
        } catch (Exception e){
            e.printStackTrace();
        }
    }



}

