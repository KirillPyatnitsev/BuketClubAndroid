package ru.creators.buket.club.consts;

/**
 * Created by mifkamaz on 20.11.15.
 */
public class ApplicationMode {
    public static final int COST_FIXED = 1;
    public static final int COST_FLOATING = 0;

    public static final String COST_FIXED_DESC = "fix_price";
    public static final String COST_FLOATING_DESC = "flexible_price";

    public static String getAppModeDesc(int mode){
        switch (mode){
            case COST_FIXED:
                return COST_FIXED_DESC;
            case COST_FLOATING:
                return COST_FLOATING_DESC;
            default:
                return null;
        }
    }
}
