import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MainProgramForm {
    private DatabaseLayer dataLayer;
    private JFrame parentFrame;
    private int userId;
    private JPanel mainPanel;
    private JPanel logInPanel;
    private JButton changeUserButton;
    private JLabel TestLabel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public MainProgramForm(DatabaseLayer databaseLayer, JFrame pFrame, int userId, JPanel logIn) {
        dataLayer = databaseLayer;
        parentFrame = pFrame;
        this.userId = userId;
        parentFrame.pack();
        parentFrame.setTitle("Movie Generator");
        logInPanel = logIn;

        TestLabel.setText("" + userId);
        initializeActionListenters();
    }

    private void initializeActionListenters() {

        changeUserButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.setVisible(false);

            }
        });
    }

}
