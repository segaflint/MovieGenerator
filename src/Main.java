import javax.swing.*;
import java.sql.SQLException;

public class Main {


    public static void main(String[] args){
        DatabaseLayer dataLayer = new DatabaseLayer();

        Driver driver = new Driver(dataLayer);

    }

}
