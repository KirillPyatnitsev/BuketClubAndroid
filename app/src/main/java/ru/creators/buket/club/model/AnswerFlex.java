package ru.creators.buket.club.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Comparator;

import ru.creators.buket.club.consts.Fields;

/**
 * Created by mifkamaz on 21/01/16.
 */
public class AnswerFlex {
    @JsonProperty(Fields.ID)
    private int id;
    @JsonProperty(Fields.PRICE)
    private int price;
    @JsonProperty(Fields.ORDER)
    private Order order;
    @JsonProperty(Fields.SHOP)
    private Shop shop;

    private float distance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public static final Comparator<AnswerFlex> COMPARATOR_SORT_BY_DIST = new Comparator<AnswerFlex>() {
        @Override
        public int compare(AnswerFlex lhs, AnswerFlex rhs) {
            return (int) (lhs.getDistance() - rhs.getDistance());
        }
    };

    public static final Comparator<AnswerFlex> COMPARATOR_SORT_BY_PRICE = new Comparator<AnswerFlex>() {
        @Override
        public int compare(AnswerFlex lhs, AnswerFlex rhs) {
            return lhs.getPrice() - rhs.getPrice();
        }
    };
}
