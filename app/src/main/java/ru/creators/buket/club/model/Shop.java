package ru.creators.buket.club.model;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class Shop {
    @JsonProperty(Fields.ID)
    private int id;

    @JsonProperty(Fields.NAME)
    private String name;

    @JsonProperty(Fields.PHONE)
    private String phone;

    @JsonProperty(Fields.ADDRESS_LAT)
    private float addressLat;

    @JsonProperty(Fields.ADDRESS_LNG)
    private float addressLng;

    @JsonProperty(Fields.IMAGE_URL)
    private String imageUrl;

    @JsonProperty(Fields.CACHED_RATING)
    private float cachedRating;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public float getAddressLat() {
        return addressLat;
    }

    public float getAddressLng() {
        return addressLng;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getCachedRating() {
        return cachedRating;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
