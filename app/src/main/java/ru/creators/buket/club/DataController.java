package ru.creators.buket.club;

/**
 * Created by mifkamaz on 12/12/15.
 */
public class DataController {
    private static DataController ourInstance = new DataController();

    public static DataController getInstance() {
        return ourInstance;
    }

    private DataController() {
    }
}
