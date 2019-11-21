import javax.swing.*;

public class Driver {
    JFrame mainFrame;
    DatabaseLayer dataLayer;

    public Driver(DatabaseLayer dataLayer) {
        this.dataLayer = dataLayer;

        mainFrame = new JFrame("Log In to MovieGenerator");
        mainFrame.setContentPane(new LogInForm(dataLayer, mainFrame).getMainPanel());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
