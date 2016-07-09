package ru.creators.buket.club.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import ru.creators.buket.club.consts.Fields;


/**
 * Created by mifkamaz on 12/12/15.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile {

    public final static int TYPE_PRICE_FLEXIBLE = 1;
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

    @JsonProperty(Fields.FULL_NAME)
    private String fillName;

    @JsonProperty(Fields.PHONE)
    private String phone;

    @JsonProperty(Fields.CODE)
    private String code;

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

    public String getFillName() {
        return fillName;
    }

    public void setFillName(String fillName) {
        this.fillName = fillName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonIgnore
    public Profile getProfileForPatchRequest() {
        Profile profile = new Profile();

        profile.setFillName(this.getFillName());
        profile.setCode(this.getCode());
        profile.setPhone(this.getPhone());

        return profile;
    }
}
