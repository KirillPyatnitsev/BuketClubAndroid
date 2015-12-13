package ru.creators.buket.club.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import ru.creators.buket.club.consts.Fields;

/**
 * Created by mifkamaz on 13/12/15.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Order {

    @JsonProperty(Fields.ID)
    private int id;

    @JsonProperty(Fields.USER_ID)
    private int userId;

    @JsonProperty(Fields.PRICE)
    private int price;

    @JsonProperty(Fields.SIZE)
    private String size;

    @JsonProperty(Fields.SIZE_INDEX)
    private int sizeIndex;

    @JsonProperty(Fields.STATUS)
    private String status;

    @JsonProperty(Fields.STATUS_INDEX)
    private int statusIndex;

    @JsonProperty(Fields.RECIPIENT_NAME)
    private String recipientName;

    @JsonProperty(Fields.RECIPIENT_PHONE)
    private String recipientPhone;

    @JsonProperty(Fields.ADDRESS)
    private String address;

    @JsonProperty(Fields.ADDRESS_LAT)
    private float addressLat;

    @JsonProperty(Fields.ADDRESS_LNG)
    private float addressLng;

    @JsonProperty(Fields.TIME_DELIVERY)
    private String timeDelivery;

    @JsonProperty(Fields.COMMENT)
    private String comment;

    @JsonProperty(Fields.BOUQUET_ITEM)
    private Bouquet bouquetItem;

    @JsonProperty(Fields.SHOP)
    private Shop shop;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getSizeIndex() {
        return sizeIndex;
    }

    public void setSizeIndex(int sizeIndex) {
        this.sizeIndex = sizeIndex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusIndex() {
        return statusIndex;
    }

    public void setStatusIndex(int statusIndex) {
        this.statusIndex = statusIndex;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getAddressLat() {
        return addressLat;
    }

    public void setAddressLat(float addressLat) {
        this.addressLat = addressLat;
    }

    public float getAddressLng() {
        return addressLng;
    }

    public void setAddressLng(float addressLng) {
        this.addressLng = addressLng;
    }

    public String getTimeDelivery() {
        return timeDelivery;
    }

    public void setTimeDelivery(String timeDelivery) {
        this.timeDelivery = timeDelivery;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Bouquet getBouquetItem() {
        return bouquetItem;
    }

    public void setBouquetItem(Bouquet bouquetItem) {
        this.bouquetItem = bouquetItem;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
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
