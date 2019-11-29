import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    private JCheckBox includeHasWatchedCheckBox;
    private JCheckBox anyYearCheckBox;
    private JButton generateMovieButton;
    private JLabel yourConfigurationsLabel;
    private JComboBox configurationsComboBox;
    private JLabel TestLabel;
    private LogInForm logInForm;

    // global variables
    private final String NEW_CONFIGURATION = "New Configuration";
    private int userId;
    private DatabaseLayer dataLayer;
    private boolean includeWatchedMovies = false;
    private boolean includeAnyYear = false;
    private boolean isAdmin;
    private boolean configurationIsEdited = false;

    //Constructor
    public MainProgramForm(DatabaseLayer databaseLayer, JFrame pFrame, int userId, LogInForm form) {
        dataLayer = databaseLayer;
        parentFrame = pFrame;
        this.userId = userId;
        parentFrame.revalidate();
        parentFrame.validate();
//        parentFrame.pack();
        parentFrame.setTitle("Movie Generator");
        logInForm = form;


        refreshMovieCountLabel();
        isAdmin = dataLayer.isAdmin(userId);
        if(isAdmin) addMovieButton.setEnabled(true);

        //initialize components
        saveConfigurationButton.setEnabled(false);
        initializeComboBoxes();
        initializeActionListenters();
//        initializeConfigurationsList();
        initializeConfigurationsComboBox();

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


    private void setSaveEnabled(boolean isEnabled){
        configurationIsEdited = isEnabled;
        saveConfigurationButton.setEnabled(isEnabled);
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

    private void initializeConfigurationsComboBox() {
//        ArrayList<PreferenceConfiguration> configurationArrayList = dataLayer.getUserConfigurations(userId);
        ArrayList<PreferenceConfiguration> configurationArrayList = new ArrayList<>();

        configurationArrayList.add(new PreferenceConfiguration(3, "Jeff", 3, 4, "John boy", "R", "action"));
        configurationArrayList.add(new PreferenceConfiguration(3, "Jesad", 4, 6, "Josadhn boy", "R", "action"));

        configurationsComboBox.addItem(new PreferenceConfiguration(NEW_CONFIGURATION));
        for (PreferenceConfiguration pc : configurationArrayList) {
            configurationsComboBox.addItem(pc);
        }

    }

//    private void initializeConfigurationsList(){
//        DefaultListModel<PreferenceConfiguration> model = new DefaultListModel<>();
//        //ArrayList<PreferenceConfiguration> configurationArrayList = dataLayer.getUserConfigurations(userId);
//        ArrayList<PreferenceConfiguration> configurationArrayList = new ArrayList<>();
//        configurationArrayList.add(new PreferenceConfiguration(3, "Jeff", 3, 4, "John boy", "R", "action"));
//        configurationArrayList.add(new PreferenceConfiguration(3, "Jesad", 4, 6, "Josadhn boy", "R", "action"));
//
//        for (PreferenceConfiguration pc : configurationArrayList) {
//            model.addElement(pc);
//        }
//
//        configurationsList = new JList(model);
//        configurationsList.setCellRenderer( new DefaultListCellRenderer() );
//        configurationsList.setVisible(true);
////        configurationsList.setDragEnabled(false);
//        //TODO: make the list work and show stuff; no luck with this stupid poop
//    }

    // Show a specified preference configuration in the configuration editor
    private void showPreferenceConfiguration(PreferenceConfiguration configuration) {
       movieTitleLabel.setText("");
       for( int i = 0; i < genreComboBox.getItemCount(); i++ ) {
           if(configuration.getGenre().compareTo(genreComboBox.getItemAt(i).toString()) == 0){
               genreComboBox.setSelectedIndex(i);
           }
       }
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

        includeHasWatchedCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
              if(e.getStateChange() == ItemEvent.SELECTED) { // if is selected
                includeWatchedMovies = true;
              } else  { // is deselected
                includeWatchedMovies = false;
              }
            }
        });

        anyYearCheckBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                setSaveEnabled(true);
                if(e.getStateChange() == ItemEvent.SELECTED) { // if is selected
                    releaseYearFromComboBox.setEnabled(false);
                    releaseYearToComboBox.setEnabled(false);
                    includeAnyYear = true;
                } else { // is deselected
                    releaseYearFromComboBox.setEnabled(true);
                    releaseYearToComboBox.setEnabled(true);
                    includeAnyYear = false;
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

        saveConfigurationButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                setSaveEnabled(false);
            }
        });

        configurationsComboBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (configurationsComboBox.getSelectedItem().toString().compareTo(NEW_CONFIGURATION) == 0) {// No config
                    initializeComboBoxes();
                    if(configurationIsEdited) {
                        setSaveEnabled(false);
                    }
                } else {// Selected Config
                    showPreferenceConfiguration((PreferenceConfiguration) configurationsComboBox.getSelectedItem());
                }
            }
        });
    }
}
