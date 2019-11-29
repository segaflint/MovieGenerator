import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.Arrays;

public class LogInForm {

    /* panel components */
    private JButton createNewUserButton;
    private JButton logInButton;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private JFrame parentFrame;
    private JPanel mainPanel;
    private JLabel passwordLabel;
    private JLabel userNamelabel;
    private JLabel messageLabel;
    private JComboBox contextComboBox;
    private JLabel contextLabel;

    private logInModeEnum mode;
    private DatabaseLayer dataLayer;

    private int userId;

    private enum logInModeEnum {
        EXISTING_USER, CREATE_USER;
    }

    //Constructor
    public LogInForm(DatabaseLayer databaseLayer, JFrame pFrame) {
        mode = logInModeEnum.EXISTING_USER;
        parentFrame = pFrame;
        this.dataLayer = databaseLayer;

        try {
            dataLayer.connect();
            contextLabel.setText("Connected to Seth's Database");
        } catch ( SQLException e ) {
            contextLabel.setText("Unable to connect to Seth's Database");
            contextLabel.setBackground(Color.RED);
            disableLoginComponents();
        }

        contextComboBox.addItem("Seth");
        contextComboBox.addItem("Caleb");

        initializeListeners();

    }

    /*
     * GETTERS
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /*
     * SETTERS
     */

    /*
     *  PUBLIC METHODS
     */
    public void setPasswordField(String password) {
        passwordField.setText(password);
    }

    /*
     * PRIVATE METHODS
     */

    // Disable log in components
    private void disableLoginComponents(){
        usernameField.setEnabled(false);
        passwordField.setEnabled(false);
        createNewUserButton.setEnabled(false);
        logInButton.setEnabled(false);
    }

    // Enable log in panel components
    private void enableLoginComponents() {
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);
        createNewUserButton.setEnabled(true);
        logInButton.setEnabled(true);
    }

    // Show the main program panel
    private void startUpMainPanel() {
        mainPanel.setVisible(false);
        parentFrame.setContentPane(new MainProgramForm(dataLayer, parentFrame, userId, this).getMainPanel());
        parentFrame.pack();
    }

    /*
     *ACTION LISTENERS
     */
    private void initializeListeners() {

        // NEW USER BUTTON
        createNewUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mode == logInModeEnum.EXISTING_USER) { // mode is existing user
                    createNewUserButton.setText("Login Existing User");
                    messageLabel.setText("Please enter a new user name and password");
                    mode = logInModeEnum.CREATE_USER;

                } else { // Mode is create new user
                    createNewUserButton.setText("Create New User");
                    messageLabel.setText("Please enter your user name and password.");
                    mode = logInModeEnum.EXISTING_USER;
                }
                mainPanel.repaint();
                parentFrame.pack(); // resize the panel accordingly
            }
        });

        // LOGIN BUTTON ACTION
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if(mode == logInModeEnum.EXISTING_USER) { // mode is existing user
                    if(userName.compareTo("") == 0 || password.compareTo("") == 0) {
                        messageLabel.setText("Please enter values for username/password"); // empty username or password
                        messageLabel.setForeground(Color.RED);
                    } else if((userId = dataLayer.getUser(userName, password)) == -1) {
                        // no user exists by this username or password
                        messageLabel.setText("Incorrect username or password.");
                        messageLabel.setForeground(Color.RED);
                    } else { // valid user - log in
                        startUpMainPanel();
                    }

                } else { // Mode is create new user
                    if(userName.compareTo("") == 0 ||
                            password.compareTo("") == 0) {
                        messageLabel.setText("Please enter values for username/password"); // empty username or password
                        messageLabel.setForeground(Color.RED);
                    } else if(userName.length() > 20){ // username too long
                        messageLabel.setText("Please limit your username to 20 characters.");
                        messageLabel.setForeground(Color.RED);
                    } else if (password.length() > 20) { // password too long
                        messageLabel.setText("Please limit your password to 20 characters.");
                        messageLabel.setForeground(Color.RED);
                    } else if ((userId = dataLayer.createNewUser(userName,
                            password)) == -1) { // username already exists or error case
                        messageLabel.setText("Please choose a different username.");
                        messageLabel.setForeground(Color.RED);
                    } else {// successful creation.
                        startUpMainPanel();
                    }
                    mainPanel.repaint();
                    parentFrame.pack(); // resize the panel accordingly
                }
            }
        });


        contextComboBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == 1) {
                    if(contextComboBox.getSelectedItem().toString().compareTo("Seth") == 0) {
                        if(dataLayer.setUserMode(1)) {
                            contextLabel.setText("Connected to Seth's Database");
                            contextLabel.setForeground(Color.BLACK);
                            enableLoginComponents();
                        } else {
                            contextLabel.setText("Unable to connect to Seth's Database");
                            contextLabel.setForeground(Color.RED);
                            disableLoginComponents();
                        }
                    } else if (contextComboBox.getSelectedItem().toString().compareTo("Caleb") == 0) {
                        if(dataLayer.setUserMode(0)) {
                            contextLabel.setText("Connected to Caleb's Database");
                            contextLabel.setForeground(Color.BLACK);
                            enableLoginComponents();
                        } else {
                            contextLabel.setText("Unable to connect to Calebs's Database");
                            contextLabel.setForeground(Color.RED);
                            disableLoginComponents();
                        }
                    }
                    mainPanel.repaint();
                    parentFrame.pack();
                }
            }
        });
    }
}
