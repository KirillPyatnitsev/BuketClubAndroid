package ru.creators.buket.club.model;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;


/**
 * Created by mifkamaz on 12/12/15.
 */
public class Profile {

    public final static int TYPE_PRICE_FLIXIBLE = 1;
    public final static int TYPE_PRICE_FIX = 0;

    @JsonProperty(Fields.ID)
    private int id;

    @JsonProperty(Fields.UUID)
    private String uuid;

    @JsonProperty(Fields.TYPE_PRICE)
    private String typePrice;

    @JsonProperty(Fields.TYPE_PRICE_INDEX)
    private int typePriceIndex;

    @JsonProperty(Fields.CREATED_AT)
    private String createdAt;

    @JsonProperty(Fields.UPDATED_AT)
    private String upadtedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTypePrice() {
        return typePrice;
    }

    public void setTypePrice(String typePrice) {
        this.typePrice = typePrice;
    }

    public int getTypePriceIndex() {
        return typePriceIndex;
    }

    public void setTypePriceIndex(int typePriceIndex) {
        this.typePriceIndex = typePriceIndex;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpadtedAt() {
        return upadtedAt;
    }

    public void setUpadtedAt(String upadtedAt) {
        this.upadtedAt = upadtedAt;
    }
}
