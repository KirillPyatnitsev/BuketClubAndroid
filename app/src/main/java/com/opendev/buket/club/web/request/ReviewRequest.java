package com.opendev.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.club.consts.Fields;
import com.opendev.buket.club.consts.Rest;
import com.opendev.buket.club.model.Review;
import com.opendev.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class ReviewRequest extends BaseRequest<DefaultResponse> {

    private int orderId;
    private String comment;
    private int rating;

    public ReviewRequest(int orderId, String comment, int rating) {
        super(DefaultResponse.class);
        this.orderId = orderId;
        this.comment = comment;
        this.rating = rating;
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.ORDERS);
        uriBuilder.appendPath(Integer.toString(orderId));
        uriBuilder.appendPath(Rest.REVIEWS);
        HttpRequest request = makePostRequest(uriBuilder, new ReviewContent(new Review(rating, comment)));
        return executeRequest(request);
    }

    private class ReviewContent {
        @JsonProperty(Fields.REVIEW)
        private Review review;

        public ReviewContent(Review review) {
            this.review = review;
        }
    }
}
