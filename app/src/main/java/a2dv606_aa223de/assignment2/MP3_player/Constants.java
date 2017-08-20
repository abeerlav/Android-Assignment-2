package a2dv606_aa223de.assignment2.MP3_player;

/**
 * Created by Abeer on 3/6/2017.
 */

public class Constants {


    public interface ACTION {
        public static String MAIN_ACTION = "aa223de.foregroundservice.action.main";
        public static String INIT_ACTION = "aa223de.foregroundservice.action.init";
        public static String PREV_ACTION = "aa223de.foregroundservice.action.prev";
        public static String PLAY_ACTION = "aa223de.foregroundservice.action.play";
        public static String NEXT_ACTION = "aa223de.foregroundservice.action.next";
        public static String STARTFOREGROUND_ACTION = "aa223de.foregroundservice.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "aa223de.foregroundservice.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}