package tr.edu.boun.cmpe.sculture;


import com.ocpsoft.pretty.time.PrettyTime;

import java.util.Date;

public class Utils {

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public static boolean isUserNameValid(String username) {
        //TODO Replace
        return username.length() > 3;
    }


    public static String timespamptToPrettyStrig(long timesmap) {
        Date date = new Date();
        date.setTime(timesmap);

        PrettyTime p = new PrettyTime();

        return p.format(date);

    }
}
