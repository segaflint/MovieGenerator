import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogInForm {
    private JButton createNewUserButton;
    private JButton logInButton;
    private JPasswordField passwordField1;
    private JTextField textField1;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JLabel passwordLabel;
    private JLabel userNamelabel;
    private JLabel messageLabel;

    private logInModeEnum mode;

    //Strings
    String newUser = "Create New User";
    String logInWithUser = "Login Existing User";

    private enum logInModeEnum {
        EXISTING_USER, CREATE_USER;
    }

    public LogInForm() {
        mode = logInModeEnum.EXISTING_USER;

        // NEW USER BUTTON
        createNewUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mode == logInModeEnum.EXISTING_USER) { // mode is existing user
                    createNewUserButton.setText(newUser);
                } else { // Mode is create new user
                    createNewUserButton.setText(logInWithUser);
                }
            }
        });

        // LOGIN BUTTON ACTION
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mode == logInModeEnum.EXISTING_USER) { // mode is existing user

                } else { // Mode is create new user

                }
            }
        });
    }

}
