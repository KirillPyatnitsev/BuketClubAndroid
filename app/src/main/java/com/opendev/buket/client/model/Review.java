package com.opendev.buket.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.consts.Fields;

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
