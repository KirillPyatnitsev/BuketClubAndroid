package com.opendev.buket.club.consts;

import com.opendev.buket.club.model.Profile;

/**
 * Created by mifkamaz on 20.11.15.
 */
public class ApplicationMode {
    public static final int COST_FIX = Profile.TYPE_PRICE_FIX;
    public static final int COST_FLEXIBLE = Profile.TYPE_PRICE_FLEXIBLE;

    public static final String COST_FIXED_DESC = "fix_price";
    public static final String COST_FLOATING_DESC = "flexible_price";

    public static String getAppModeDesc(int mode) {
        switch (mode) {
            case COST_FIX:
                return COST_FIXED_DESC;
            case COST_FLEXIBLE:
                return COST_FLOATING_DESC;
            default:
                return null;
        }
    }
}
