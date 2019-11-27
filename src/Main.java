import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    // DO NOT TOUCH MAIN
    public static void main(String[] args){
        DatabaseLayer dataLayer = new DatabaseLayer();

        Driver driver = new Driver(dataLayer);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        Date date = new Date();
//        System.out.println(dateFormat.format(date));
    }
}
