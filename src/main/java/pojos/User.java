package pojos;

/**
 * Represents a user entity in the system.
 * Contains basic user information including ID, username, and password.
 */
public class User {
    private int id;
    public String username;
    public String password;

    /**
     * Default constructor for User class.
     */
    public User() {
    }

    /**
     * Constructor for creating a User with all fields.
     *
     * @param id       The unique identifier for the user
     * @param username The username for the user
     * @param password The password for the user
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor for creating a User without an ID.
     *
     * @param username The username for the user
     * @param password The password for the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the user's ID.
     *
     * @return The user's unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user's ID.
     *
     * @param id The unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the username.
     *
     * @return The user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return The user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", password=" + password + ", userId=" + id + '}';
    }
}
