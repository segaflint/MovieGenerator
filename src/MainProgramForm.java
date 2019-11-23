import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainProgramForm {
    private DatabaseLayer dataLayer;
    private JFrame parentFrame;
    private int userId;
    private JPanel mainPanel;
    private JButton changeUserButton;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public MainProgramForm(DatabaseLayer databaseLayer, JFrame pFrame, int userId) {
        dataLayer = databaseLayer;
        parentFrame = pFrame;
        this.userId = userId;
        parentFrame.pack();
        parentFrame.setTitle("Movie Generator");
    }
}
