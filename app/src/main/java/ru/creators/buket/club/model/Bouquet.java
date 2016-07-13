package ru.creators.buket.club.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import ru.creators.buket.club.consts.Fields;

/**
 * Created by mifkamaz on 07/12/15.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bouquet {
    public static final int SIZE_LITTLE = 0;
    public static final int SIZE_MEDIUM = 1;
    public static final int SIZE_GREAT = 2;

    public static final String SIZE_LITTLE_DESC = "small";
    public static final String SIZE_MEDIUM_DESC = "middle";
    public static final String SIZE_GREAT_DESC = "large";

    @JsonProperty(Fields.ID)
    private int id;

    @JsonProperty(Fields.DESCRIPTION)
    private String description;

    @JsonProperty(Fields.SMALL_SIZE_PRICE)
    private int smallSizePrice;

    @JsonProperty(Fields.MIDDLE_SIZE_PRICE)
    private int middleSizePrice;

    @JsonProperty(Fields.LARGE_SIZE_PRICE)
    private int largeSizePrice;

    @JsonProperty(Fields.SMALL_SIZE_NAME)
    private String smallSizeName;

    @JsonProperty(Fields.MIDDLE_SIZE_NAME)
    private String middleSizeName;

    @JsonProperty(Fields.LARGE_SIZE_NAME)
    private String largeSizeName;

    @JsonProperty(Fields.IMAGE_URL)
    private String imageUrl;

    @JsonProperty(Fields.FLOWER_TYPE)
    private DictionaryItem floverType;

    @JsonProperty(Fields.FLOWER_COLOR)
    private DictionaryItem flowerColor;

    @JsonProperty(Fields.DAY_EVENT)
    private DictionaryItem dayEvent;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSmallSizePrice() {
        return smallSizePrice;
    }

    public void setSmallSizePrice(int smallSizePrice) {
        this.smallSizePrice = smallSizePrice;
    }

    public int getMiddleSizePrice() {
        return middleSizePrice;
    }

    public void setMiddleSizePrice(int middleSizePrice) {
        this.middleSizePrice = middleSizePrice;
    }

    public int getLargeSizePrice() {
        return largeSizePrice;
    }

    public void setLargeSizePrice(int largeSizePrice) {
        this.largeSizePrice = largeSizePrice;
    }

    public String getSmallSizeName() {
        return smallSizeName;
    }

    public void setSmallSizeName(String smallSizeName) {
        this.smallSizeName = smallSizeName;
    }

    public String getMiddleSizeName() {
        return middleSizeName;
    }

    public void setMiddleSizeName(String middleSizeName) {
        this.middleSizeName = middleSizeName;
    }

    public String getLargeSizeName() {
        return largeSizeName;
    }

    public void setLargeSizeName(String largeSizeName) {
        this.largeSizeName = largeSizeName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public DictionaryItem getFloverType() {
        return floverType;
    }

    public void setFloverType(DictionaryItem floverType) {
        this.floverType = floverType;
    }

    public DictionaryItem getFlowerColor() {
        return flowerColor;
    }

    public void setFlowerColor(DictionaryItem flowerColor) {
        this.flowerColor = flowerColor;
    }

    public DictionaryItem getDayEvent() {
        return dayEvent;
    }

    public void setDayEvent(DictionaryItem dayEvent) {
        this.dayEvent = dayEvent;
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

    public int getBouquetPriceBySize(int sizeId) {
        switch (sizeId) {
            case SIZE_LITTLE:
                return getSmallSizePrice();
            case SIZE_MEDIUM:
                return getMiddleSizePrice();
            case SIZE_GREAT:
                return getLargeSizePrice();
            default:
                return 0;
        }
    }

    public String getBouquetNameBySize(int sizeId) {
        switch (sizeId) {
            case SIZE_LITTLE:
                return getSmallSizeName();
            case SIZE_MEDIUM:
                return getMiddleSizeName();
            case SIZE_GREAT:
                return getLargeSizeName();
            default:
                return "";
        }
    }

    public String getBouquetDescriptionBySize(int sizeId) {
        switch (sizeId) {
            case SIZE_LITTLE:
                return getDescription();
            case SIZE_MEDIUM:
                return getDescription();
            case SIZE_GREAT:
                return getDescription();
            default:
                return "";
        }
    }

    public static String getSizeDesc(int sizeId) {
        switch (sizeId) {
            case SIZE_LITTLE:
                return SIZE_LITTLE_DESC;
            case SIZE_MEDIUM:
                return SIZE_MEDIUM_DESC;
            case SIZE_GREAT:
                return SIZE_GREAT_DESC;
            default:
                return "";
        }
    }


}
