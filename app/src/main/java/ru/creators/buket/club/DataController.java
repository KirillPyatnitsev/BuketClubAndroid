package ru.creators.buket.club;

import ru.creators.buket.club.model.Bouquet;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.PriceRange;
import ru.creators.buket.club.model.Profile;
import ru.creators.buket.club.model.Session;
import ru.creators.buket.club.model.lists.ListBouquet;

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

    private Session session;
    private Profile profile;
    private ListBouquet listBouquet;
    private PriceRange priceRange;
    private Bouquet bouquet;
    private Order order;

    private int paymentType;

    public static DataController getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(DataController ourInstance) {
        DataController.ourInstance = ourInstance;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ListBouquet getListBouquet() {
        return listBouquet;
    }

    public void setListBouquet(ListBouquet listBouquet) {
        this.listBouquet = listBouquet;
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
}