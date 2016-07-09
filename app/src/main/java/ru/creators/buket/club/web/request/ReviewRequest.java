package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.model.Review;
import ru.creators.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class ReviewRequest extends BaseRequest<DefaultResponse> {

    private String accessToken;
    private int orderId;
    private String comment;
    private int rating;

    public ReviewRequest(String accessToken, int orderId, String comment, int rating) {
        super(DefaultResponse.class);
        this.accessToken = accessToken;
        this.orderId = orderId;
        this.comment = comment;
        this.rating = rating;
    }


    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.ORDERS);
        uriBuilder.appendPath(Integer.toString(orderId));
        uriBuilder.appendPath(Rest.REVIEWS);
        return uriBuilder;
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getPostHttpRequest(new ReviewContent(new Review(rating, comment)));

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return getResponse(request.execute(), DefaultResponse.class);
    }

    private class ReviewContent {
        @JsonProperty(Fields.REVIEW)
        private Review review;

        public ReviewContent(Review review) {
            this.review = review;
        }
    }
}
