import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import Constants.*;

public class MainProgramForm {

    //GUI components
    private JFrame parentFrame;
    private JPanel mainPanel;
    private JButton changeUserButton;
    private JLabel movieCountLable;
    private JButton addMovieButton;
    private JComboBox genreComboBox;
    private JLabel movieTitleLabel;
    private JLabel genresLabel;
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
    private JButton generateMovieByConfigurationButton;
    private JLabel yourConfigurationsLabel;
    private JComboBox configurationsComboBox;
    private JButton generateTotallyRandomMovieButton;
    private JCheckBox rCheckBox;
    private JCheckBox PG13CheckBox;
    private JCheckBox PGCheckBox;
    private JCheckBox gCheckBox;
    private JLabel ratingsLabel;
    private JCheckBox actionAdventureCheckBox;
    private JCheckBox horrorCheckBox;
    private JCheckBox kidsFamilyCheckBox;
    private JCheckBox dramaCheckBox;
    private JCheckBox sciFiFantasyCheckBox;
    private JCheckBox comedyCheckBox;
    private JLabel generatedMovieLabel;
    private JButton watchHaveWatchedButton;
    private JLabel provideANameForLabel;
    private JLabel TestLabel;
    private LogInForm logInForm;

    // global variables
    private final String NEW_CONFIGURATION = "New Configuration";
    private int userId;
    private DatabaseLayer dataLayer;
    private boolean includeWatchedMovies = false;
    private char includeAnyYear = 'N';
    private boolean isAdmin;
    private boolean configurationIsEdited = false;
    private Movie movieGenerated;
    private char[] genresFlags = { 'N', 'N', 'N', 'N', 'N', 'N' };
    private char[] ratingsFlags = { 'N', 'N', 'N', 'N' };

    //Constructor
    public MainProgramForm(DatabaseLayer databaseLayer, JFrame pFrame, int userId, LogInForm form) {
        dataLayer = databaseLayer;
        parentFrame = pFrame;
        this.userId = userId;
        parentFrame.revalidate();
        parentFrame.validate();
        parentFrame.setTitle("Movie Generator");
        logInForm = form;


        refreshMovieCountLabel();
        isAdmin = dataLayer.isAdmin(userId);
        if(isAdmin) addMovieButton.setEnabled(true);

        //initialize components
        saveConfigurationButton.setEnabled(false);
        initializeComboBoxes();
        initializeActionListenters();
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
         movieCountLable.setText(" You've watched " + count + " movie(s)." );
    }


    private void setSaveEnabled(boolean isEnabled){
        configurationIsEdited = isEnabled;
        saveConfigurationButton.setEnabled(isEnabled);
    }

    private void initializeComboBoxes() {


        //releaseYearFromComboBox
        for(int i = YearConstants.EARLIEST_YEAR; i < YearConstants.LATEST_YEAR; i++) {
            releaseYearFromComboBox.addItem(i);
        }

        //releaseYearToComboBox
        for(int i = YearConstants.EARLIEST_YEAR; i < YearConstants.LATEST_YEAR; i++) {
            releaseYearToComboBox.addItem(i);
        }

    }

    private void initializeConfigurationsComboBox() {
        ArrayList<PreferenceConfiguration> configurationArrayList = dataLayer.getUserConfigurations(userId);

        configurationsComboBox.addItem(new PreferenceConfiguration(NEW_CONFIGURATION));
        for (PreferenceConfiguration pc : configurationArrayList) {
            configurationsComboBox.addItem(pc);
        }

    }

    private void displayGeneratedMovie() {
        String generatedMovie = "Generated Movie: ";
        generatedMovieLabel.setText(generatedMovie + movieGenerated);
        watchHaveWatchedButton.setEnabled(true);
    }

    // Show a specified preference configuration in the configuration editor
    private void showPreferenceConfiguration(PreferenceConfiguration configuration) {

       setGenreCheckBoxesFromConfiguration(configuration.getGenres());
       setRatingCheckBoxesFromConfiguration(configuration.getRatings());

       if(configuration.getAnyYearFlag() == 'Y') { // configuration is any year
           anyYearCheckBox.setSelected(true);
       } else { // configuration between certain years
           releaseYearFromComboBox.setSelectedIndex(configuration.getReleaseYearFrom() - YearConstants.EARLIEST_YEAR);
           releaseYearToComboBox.setSelectedIndex(configuration.getReleaseYearTo() - YearConstants.EARLIEST_YEAR);
       }

       directorTextField.setText(configuration.getDirector());

    }

    private void setGenreCheckBoxesFromConfiguration(char[] genres) {
        if(genres[Genres.ACTION_ADVENTURE_INDEX] == 'Y') actionAdventureCheckBox.setSelected(true);
        if(genres[Genres.HORROR_INDEX] == 'Y') horrorCheckBox.setSelected(true);
        if(genres[Genres.KIDS_FAMILY_INDEX] == 'Y') kidsFamilyCheckBox.setSelected(true);
        if(genres[Genres.DRAMA_INDEX] == 'Y') dramaCheckBox.setSelected(true);
        if(genres[Genres.SCIFI_FANTASY_INDEX] == 'Y') sciFiFantasyCheckBox.setSelected(true);
        if(genres[Genres.COMEDY_INDEX] == 'Y') comedyCheckBox.setSelected(true);
    }

    private void setRatingCheckBoxesFromConfiguration(char[] ratings) {
        if(ratings[Ratings.R_INDEX] == 'Y') rCheckBox.setSelected(true);
        if(ratings[Ratings.PG13_INDEX] == 'Y') PG13CheckBox.setSelected(true);
        if(ratings[Ratings.PG_INDEX] == 'Y') PGCheckBox.setSelected(true);
        if(ratings[Ratings.G_INDEX] == 'Y') gCheckBox.setSelected(true);
    }

    private void setGenreCheckBoxesEmpty() {
        actionAdventureCheckBox.setSelected(false);
        horrorCheckBox.setSelected(false);
        dramaCheckBox.setSelected(false);
        kidsFamilyCheckBox.setSelected(false);
        comedyCheckBox.setSelected(false);
        sciFiFantasyCheckBox.setSelected(false);
    }

    private void setRatingCheckBoxesEmpty() {
        rCheckBox.setSelected(false);
        PG13CheckBox.setSelected(false);
        PGCheckBox.setSelected(false);
        gCheckBox.setSelected(false);
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
                    includeAnyYear = 'Y';
                } else { // is deselected
                    releaseYearFromComboBox.setEnabled(true);
                    releaseYearToComboBox.setEnabled(true);
                    includeAnyYear = 'N';
                }
            }
        });

        releaseYearFromComboBox.addItemListener(new ItemListener() {
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
            }
        });
        releaseYearToComboBox.addItemListener(new ItemListener() {
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

                if(saveConfigurationAsTextField.getText().compareTo("") == 0) {
                    provideANameForLabel.setForeground(Color.RED);
                    return;
                }
                setSaveEnabled(false);
                provideANameForLabel.setForeground(Color.BLACK);
                dataLayer.insertConfiguration(userId, new PreferenceConfiguration(-1, saveConfigurationAsTextField.getText(),
                        includeAnyYear, (int) releaseYearFromComboBox.getSelectedItem(), (int) releaseYearToComboBox.getSelectedItem(),
                        directorTextField.getText(), ratingsFlags, genresFlags));
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
                    directorTextField.setText("");
                    anyYearCheckBox.setSelected(false);
                    setGenreCheckBoxesEmpty();
                    setRatingCheckBoxesEmpty();
                    if(configurationIsEdited) {
                        setSaveEnabled(false);
                    }
                } else {// Selected Config
                    showPreferenceConfiguration((PreferenceConfiguration) configurationsComboBox.getSelectedItem());
                    setSaveEnabled(false);
                }
            }
        });

        generateMovieByConfigurationButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(configurationIsEdited){ // if any configuration has been edited, use the contents of the editor
                    movieGenerated = dataLayer.generateMovie(userId, new PreferenceConfiguration(-1, "", includeAnyYear,
                            (int) releaseYearFromComboBox.getSelectedItem(), (int) releaseYearToComboBox.getSelectedItem(),
                            directorTextField.getText(), ratingsFlags,
                            genresFlags), includeWatchedMovies);
                    displayGeneratedMovie();
                } else  {
                    movieGenerated = dataLayer.generateMovie(userId, (PreferenceConfiguration) configurationsComboBox.getSelectedItem(),
                            includeWatchedMovies);
                    displayGeneratedMovie();
                }
            }
        });

        generateTotallyRandomMovieButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                movieGenerated = dataLayer.generateRandomMovie();
                if(movieGenerated != null) displayGeneratedMovie();
                else generatedMovieLabel.setText(Movie.MOVIE_ERROR_STRING);
            }
        });

        /*
         *  GENRE CHECK BOXES
         */
        actionAdventureCheckBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) { // if is selected
                    genresFlags[Genres.ACTION_ADVENTURE_INDEX] = 'Y';
                } else {
                    genresFlags[Genres.ACTION_ADVENTURE_INDEX] = 'N';
                }
                setSaveEnabled(true);
            }
        });

        horrorCheckBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) { // if is selected
                    genresFlags[Genres.HORROR_INDEX] = 'Y';
                } else {
                    genresFlags[Genres.HORROR_INDEX] = 'N';
                }
                setSaveEnabled(true);
            }
        });

        kidsFamilyCheckBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) { // if is selected
                    genresFlags[Genres.KIDS_FAMILY_INDEX] = 'Y';
                } else {
                    genresFlags[Genres.KIDS_FAMILY_INDEX] = 'N';
                }
                setSaveEnabled(true);
            }
        });

        dramaCheckBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) { // if is selected
                    genresFlags[Genres.DRAMA_INDEX] = 'Y';
                } else {
                    genresFlags[Genres.DRAMA_INDEX] = 'N';
                }
                setSaveEnabled(true);
            }
        });

        sciFiFantasyCheckBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) { // if is selected
                    genresFlags[Genres.SCIFI_FANTASY_INDEX] = 'Y';
                } else {
                    genresFlags[Genres.SCIFI_FANTASY_INDEX] = 'N';
                }
                setSaveEnabled(true);
            }
        });

        comedyCheckBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) { // if is selected
                    genresFlags[Genres.COMEDY_INDEX] = 'Y';
                } else {
                    genresFlags[Genres.COMEDY_INDEX] = 'N';
                }
                setSaveEnabled(true);
            }
        });

        /*
         * RATING CHECK BOXES
         */
        rCheckBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) { // if is selected
                    ratingsFlags[Ratings.R_INDEX] = 'Y';
                } else {
                    ratingsFlags[Ratings.R_INDEX] = 'N';
                }
                setSaveEnabled(true);
            }
        });

        PG13CheckBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) { // if is selected
                    ratingsFlags[Ratings.PG13_INDEX] = 'Y';
                } else {
                    ratingsFlags[Ratings.PG13_INDEX] = 'N';
                }
                setSaveEnabled(true);
            }
        });

        PGCheckBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) { // if is selected
                    ratingsFlags[Ratings.PG_INDEX] = 'Y';
                } else {
                    ratingsFlags[Ratings.PG_INDEX] = 'N';
                }
                setSaveEnabled(true);
            }
        });

        gCheckBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) { // if is selected
                    ratingsFlags[Ratings.G_INDEX] = 'Y';
                } else {
                    ratingsFlags[Ratings.G_INDEX] = 'N';
                }
                setSaveEnabled(true);
            }
        });

        watchHaveWatchedButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                watchHaveWatchedButton.setEnabled(false);
                dataLayer.insertUserHasWatched(userId, movieGenerated.getMovieId());
                refreshMovieCountLabel();
            }
        });
    }
}
