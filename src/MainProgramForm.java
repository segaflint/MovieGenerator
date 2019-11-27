import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import Constants.*;

public class MainProgramForm {

    //GUI components
    private JFrame parentFrame;
    private JPanel mainPanel;
    private JButton changeUserButton;
    private JLabel movieCountLable;
    private JButton addMovieButton;
    private JTextField movieTitleTextField;
    private JComboBox genreComboBox;
    private JLabel movieTitleLabel;
    private JLabel genreLabel;
    private JLabel releaseYearToLabel;
    private JComboBox releaseYearToComboBox;
    private JComboBox releaseYearFromComboBox;
    private JComboBox ratingComboBox;
    private JLabel directorLabel;
    private JTextField directorTextField;
    private JButton saveConfigurationButton;
    private JTextField saveConfigurationAsTextField;
    private JLabel saveConfigurationAsLabel;
    private JCheckBox userHasWatchedCheckBox;
    private JLabel TestLabel;
    private LogInForm logInForm;

    // global variables
    private int userId;
    private boolean isAdmin;
    private DatabaseLayer dataLayer;

    //Constructor
    public MainProgramForm(DatabaseLayer databaseLayer, JFrame pFrame, int userId, LogInForm form) {
        dataLayer = databaseLayer;
        parentFrame = pFrame;
        this.userId = userId;
        parentFrame.revalidate();
        parentFrame.validate();
        parentFrame.pack();
        parentFrame.setTitle("Movie Generator");
        logInForm = form;


        refreshMovieCountLabel();
        isAdmin = dataLayer.isAdmin(userId);
        if(isAdmin) addMovieButton.setEnabled(true);

        //initialize components
        initializeActionListenters();
        initializeComboBoxes();
    }

    /*
     * GETTERS
     */
    public JPanel getMainPanel() { return mainPanel; }

    /*
     * SETTERS
     */

    /*
     * PUBLIC METHODS
     */

    /*
     * PRIVATE METHODS
     */
    private void refreshMovieCountLabel(){
         int count = dataLayer.getMoviesWatchedCount(userId);
         movieCountLable.setText(" You've watched " + count + " movies." );

    }

    private void initializeComboBoxes() {

        //genreComboBox
        for(Genres gen : Genres.values()) {
            genreComboBox.addItem(gen);
        }

        //releaseYearFromComboBox
        for(int i = 1900; i < 2020; i++) {
            releaseYearFromComboBox.addItem(i);
        }

        //releaseYearToComboBox
        for(int i = 1900; i < 2020; i++) {
            releaseYearToComboBox.addItem(i);
        }

        // ratingComboBox
        for(Ratings rating : Ratings.values()) {
            ratingComboBox.addItem(rating);
        }

    }

    // Show a specified preference configuration in the configuration editor
    private void showPreferenceConfiguration(PreferenceConfiguration configuration) {
       // movieTitleLabel.setText(configuration.get);
    }

    /*
     * ACTION LISTENERS
     */
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
                logInForm.getMainPanel().setVisible(true);
                logInForm.setPasswordField("");
                parentFrame.setContentPane(logInForm.getMainPanel());
                parentFrame.pack();
                parentFrame.setTitle("Log In to Movie Generator");
            }

        });

        userHasWatchedCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
              if(e.getStateChange() == ItemEvent.SELECTED) { // if is selected

              } else  { // is deselected

              }
            }
        });

        addMovieButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: New panel or popup new JFRAME that lets you add a movie?
            }
        });
    }

}
