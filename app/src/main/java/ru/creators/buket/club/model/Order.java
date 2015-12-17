package ru.creators.buket.club.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import ru.creators.buket.club.R;
import ru.creators.buket.club.consts.Fields;

/**
 * Created by mifkamaz on 13/12/15.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    public static final int STATUS_FILLING_SHOP_INDEX = 0;
    public static final int STATUS_IN_PROCESS_INDEX = 1;
    public static final int STATUS_DELIVERED_INDEX = 2;
    public static final int STATUS_DONE_INDEX = 3;


    private static final String STATUS_FILLING_SHOP = "finding_shop";
    private static final String STATUS_IN_PROCESS = "in_progress";
    private static final String STATUS_DELIVERED = "delivered";
    private static final String STATUS_DONE = "done";

    private static final int STATUS_FILLING_SHOP_DESC = R.string.text_finding_shop;
    private static final int STATUS_IN_PROCESS_DESC =  R.string.text_in_process;
    private static final int STATUS_DELIVERED_DESC =  R.string.text_delivered;
    private static final int STATUS_DONE_DESC =  R.string.text_order_done;

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
    private double addressLat;

    @JsonProperty(Fields.ADDRESS_LNG)
    private double addressLng;

    @JsonProperty(Fields.TIME_DELIVERY)
    private String timeDelivery;

    @JsonProperty(Fields.COMMENT)
    private String comment;

    @JsonProperty(Fields.BOUQUET_ITEM)
    private Bouquet bouquetItem;

    @JsonProperty(Fields.BOUQUET_ITEM_ID)
    private int bouquetItemId;

    @JsonProperty(Fields.SHOP)
    private Shop shop;

    @JsonProperty(Fields.CREATED_AT)
    private String createdAt;

    @JsonProperty(Fields.UPDATED_AT)
    private String upadtedAt;

    @JsonProperty(Fields.USER)
    private Profile user;

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

    public double getAddressLat() {
        return addressLat;
    }

    public void setAddressLat(double addressLat) {
        this.addressLat = addressLat;
    }

    public double getAddressLng() {
        return addressLng;
    }

    public void setAddressLng(double addressLng) {
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

    public int getBouquetItemId() {
        return bouquetItemId;
    }

    public void setBouquetItemId(int bouquetItemId) {
        this.bouquetItemId = bouquetItemId;
    }

    public int getStatusDescRes(){
        switch (statusIndex){
            case STATUS_FILLING_SHOP_INDEX:
                return STATUS_FILLING_SHOP_DESC;
            case STATUS_IN_PROCESS_INDEX:
                return STATUS_IN_PROCESS_DESC;
            case STATUS_DELIVERED_INDEX:
                return STATUS_DELIVERED_DESC;
            case STATUS_DONE_INDEX:
                return STATUS_DONE_DESC;
            default:
                return STATUS_DONE_DESC;
        }
    }

    public Profile getUser() {
        return user;
    }

    public void setUser(Profile user) {
        this.user = user;
    }
}
