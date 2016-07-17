package ru.creators.buket.club;

import ru.creators.buket.club.consts.ServerConfig;
import ru.creators.buket.club.model.Bouquet;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.PriceRange;
import ru.creators.buket.club.model.Profile;
import ru.creators.buket.club.model.Session;
import ru.creators.buket.club.model.lists.ListBouquet;
import ru.creators.buket.club.tools.PreferenceCache;
import ru.creators.buket.club.view.activitys.BaseActivity;

/**
 * Created by mifkamaz on 12/12/15.
 */
public class DataController {

    private static final String TAG = ServerConfig.TAG_PREFIX + "DataController";

    private static final DataController ourInstance = new DataController();

    public static DataController getInstance() {
        return ourInstance;
    }

    private DataController() {
    }

    private Session session;
    private Profile profile;
    private final ListBouquet listBouquet = new ListBouquet();
    private PriceRange priceRange;
    private Bouquet bouquet;
    private Order order;

    private BaseActivity baseActivity;

    public BaseActivity getBaseActivity() {
        return baseActivity;
    }

    public void setBaseActivity(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public void removeBaseActivity(BaseActivity baseActivity) {
    }

    public Session getSession() {
        if (session == null) {
            BaseActivity act = this.getBaseActivity();
            if(act != null) {
                session = PreferenceCache.getObject(
                        act.getApplicationContext(),
                        PreferenceCache.KEY_SESSION,
                        Session.class);
            } else {
                //Log.e(TAG, "Activity is null"); // Will complain in tests (method is not mocked)
            }
        }
        return session;
    }

    public void setSession(Session session) {
        if (session != null) {
            BaseActivity act = this.getBaseActivity();
            PreferenceCache.putObject(
                    act.getApplicationContext(),
                    PreferenceCache.KEY_SESSION,
                    session);
        }
        this.session = session;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ListBouquet getBouquetList() {
        return listBouquet;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
    }

    public Bouquet getBouquet() {
        return bouquet;
    }

    public void setBouquet(Bouquet bouquet) {
        this.bouquet = bouquet;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setAppIsFolded(boolean appIsFolded) {
        //this.appIsFolded = appIsFolded;
    }
}
