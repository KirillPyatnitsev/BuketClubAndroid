package com.opendev.buket.club.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.Fields;

/**
 * Created by mifkamaz on 13/12/15.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    public static final String DELIVERY_TYPE_PICKUP = "pickup";
    public static final String DELIVERY_TYPE_ADDRESS = "address";

    public static final int DELIVERY_TYPE_PICKUP_DESK_RES_ID = R.string.pickup;
    public static final int DELIVERY_TYPE_ADDRESS_DESK_RES_ID = R.string.delivery;

    public static final int STATUS_FILLING_SHOP_INDEX = 0;
    public static final int STATUS_IN_PROCESS_INDEX = 1;
    public static final int STATUS_DELIVERED_INDEX = 2;
    public static final int STATUS_DONE_INDEX = 3;

    public static final String TYPE_PAYMENT_CASH = "cash";
    public static final String TYPE_PAYMENT_CARD = "credit_card";
    public static final String TYPE_PAYMENT_INDEX_CASH = "0";
    public static final String TYPE_PAYMENT_INDEX_CARD = "1";

    public static final int TYPE_PAYMENT_INDEX_CARD_DESK = R.string.text_in_credit_card;
    public static final int TYPE_PAYMENT_INDEX_CASH_DESK = R.string.text_in_cash;

    private static final String STATUS_FILLING_SHOP = "finding_shop";
    private static final String STATUS_IN_PROCESS = "in_progress";
    private static final String STATUS_DELIVERED = "delivered";
    private static final String STATUS_DONE = "done";

    private static final int STATUS_FILLING_SHOP_DESC = R.string.text_finding_shop;
    private static final int STATUS_IN_PROCESS_DESC = R.string.text_in_process;
    private static final int STATUS_IN_PROCESS_DESC_PICKUP = R.string.text_in_process_pickup;
    private static final int STATUS_DELIVERED_DESC = R.string.text_delivered;
    private static final int STATUS_DONE_DESC = R.string.text_order_done;

    @JsonProperty(Fields.ID)
    private Integer id;

    /*
    @JsonProperty(Fields.USER_ID)
    private int userId;
    */

    @JsonProperty(Fields.PRICE)
    private int price;

    @JsonProperty(Fields.SIZE)
    private String size;

    @JsonProperty(Fields.SIZE_INDEX)
    private int sizeIndex;

    @JsonProperty(Fields.STATUS)
    private String status;

    @JsonProperty(Fields.STATUS_INDEX)
    private Integer statusIndex;

    @JsonProperty(Fields.RECIPIENT_NAME)
    private String recipientName;

    @JsonProperty(Fields.RECIPIENT_PHONE)
    private String recipientPhone;

    @JsonProperty(Fields.ADDRESS)
    private String address;

    @JsonProperty(Fields.ADDRESS_LAT)
    private Double addressLat;

    @JsonProperty(Fields.ADDRESS_LNG)
    private Double addressLng;

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

    @JsonProperty(Fields.SHOP_ID)
    private String shopId;

    @JsonProperty(Fields.CREATED_AT)
    private String createdAt;

    @JsonProperty(Fields.UPDATED_AT)
    private String updatedAt;

    @JsonProperty(Fields.USER)
    private Profile user;

    @JsonProperty(Fields.TYPE_PAYMENT)
    private String typePayment;

    @JsonProperty(Fields.SHIPPING_TYPE)
    private String shippingType;

    @JsonProperty(Fields.TYPE_PAYMENT_INDEX)
    private String typePaymentIndex;

    @JsonProperty("code")
    private String code;

    public String getShopId() {
        return shopId;
    }

    public String getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(String typePayment) {
        this.typePayment = typePayment;
    }

    public String getTypePaymentIndex() {
        return typePaymentIndex;
    }

    public void setTypePaymentIndex(String typePaymentIndex) {
        this.typePaymentIndex = typePaymentIndex;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    */

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

    public Integer getStatusIndex() {
        return statusIndex;
    }

    public void setStatusIndex(Integer statusIndex) {
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

    public Double getAddressLat() {
        return addressLat;
    }

    public void setAddressLat(Double addressLat) {
        this.addressLat = addressLat;
    }

    public Double getAddressLng() {
        return addressLng;
    }

    public void setAddressLng(Double addressLng) {
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getBouquetItemId() {
        return bouquetItemId;
    }

    public void setBouquetItemId(int bouquetItemId) {
        this.bouquetItemId = bouquetItemId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonIgnore
    public int getStatusDescRes() {
        switch (statusIndex) {
            case STATUS_FILLING_SHOP_INDEX:
                return STATUS_FILLING_SHOP_DESC;
            case STATUS_IN_PROCESS_INDEX:
                if (shippingType.equals(DELIVERY_TYPE_ADDRESS)) {
                    return STATUS_IN_PROCESS_DESC;
                } else {
                    return STATUS_IN_PROCESS_DESC_PICKUP;
                }
            case STATUS_DELIVERED_INDEX:
                return STATUS_DELIVERED_DESC;
            case STATUS_DONE_INDEX:
                return STATUS_DONE_DESC;
            default:
                return 0;
        }
    }

    @JsonIgnore
    public Order getOrderForServer() {
        Order order = new Order();

        order.setPrice(this.getPrice());
        order.setSize(this.getSize());
        order.setSizeIndex(this.getSizeIndex());
        order.setTypePayment(this.getTypePayment());
        order.setTypePaymentIndex(this.getTypePaymentIndex());
        order.setBouquetItemId(this.getBouquetItemId());
        order.setAddress(this.getAddress());
        order.setAddressLat(this.getAddressLat());
        order.setAddressLng(this.getAddressLng());
        order.setRecipientName(this.getRecipientName());
        order.setRecipientPhone(this.getRecipientPhone());
        order.setTimeDelivery(this.getTimeDelivery());
        order.setComment(this.getComment());
        order.setShopId(this.getShopId());
        order.setShippingType(this.getShippingType());
        order.setCode(this.getCode());

        return order;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    @JsonIgnore
    public int getDeliveryTypeResId() {
        return getDeliveryTypeResId(shippingType);
    }

    @JsonIgnore
    public boolean isFillingShop() {
        return this.statusIndex != null && this.statusIndex == Order.STATUS_FILLING_SHOP_INDEX;
    }

    @JsonIgnore
    public static int getDeliveryTypeResId(String deliveryType) {
        if (deliveryType != null && deliveryType.equals(DELIVERY_TYPE_PICKUP)) {
            return DELIVERY_TYPE_PICKUP_DESK_RES_ID;
        } else {
            return DELIVERY_TYPE_ADDRESS_DESK_RES_ID;
        }
    }

    @JsonIgnore
    public int getPaymentTypeDesk() {
        if (typePayment != null && typePayment.equals(TYPE_PAYMENT_CARD)) {
            return TYPE_PAYMENT_INDEX_CARD_DESK;
        } else {
            return TYPE_PAYMENT_INDEX_CASH_DESK;
        }
    }

    /*
    public Profile getUser() {
        return user;
    }

    public void setUser(Profile user) {
        this.user = user;
    }
    */

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                //", userId=" + userId +
                ", price=" + price +
                ", size='" + size + '\'' +
                ", sizeIndex=" + sizeIndex +
                ", status='" + status + '\'' +
                ", statusIndex=" + statusIndex +
                ", recipientName='" + recipientName + '\'' +
                ", recipientPhone='" + recipientPhone + '\'' +
                ", address='" + address + '\'' +
                ", addressLat=" + addressLat +
                ", addressLng=" + addressLng +
                ", timeDelivery='" + timeDelivery + '\'' +
                ", comment='" + comment + '\'' +
                ", bouquetItem=" + bouquetItem +
                ", bouquetItemId=" + bouquetItemId +
                ", shop=" + shop +
                ", shopId='" + shopId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", user=" + user +
                ", typePayment='" + typePayment + '\'' +
                ", shippingType='" + shippingType + '\'' +
                ", typePaymentIndex='" + typePaymentIndex + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    @JsonIgnore
    public final boolean isDelivered() {
        return getStatusIndex() == Order.STATUS_DELIVERED_INDEX;
    }
}
