package ru.creators.buket.club.model;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class Review {

    @JsonProperty(Fields.RATING)
    private int rating;

    @JsonProperty(Fields.COMMENT)
    private String comment;

    public Review(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
}
