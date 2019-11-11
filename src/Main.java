import javax.swing.*;

public class Main {

    public static void main(String[] args){

        JFrame logInFrame = new JFrame("Log In to MovieGenerator");
        logInFrame.setContentPane(new LogInForm().getMainPanel());
        logInFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logInFrame.pack();
        logInFrame.setVisible(true);
        System.out.println("Got here");

    }

}
