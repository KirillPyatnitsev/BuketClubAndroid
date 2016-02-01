package ru.creators.buket.club.web;

import android.content.Context;
import android.widget.ImageView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.retry.RetryPolicy;
import com.squareup.picasso.Picasso;

import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.web.request.BaseRequest;
import ru.creators.buket.club.web.request.BouquetsGetRequest;
import ru.creators.buket.club.web.request.DictionaryGetRequest;
import ru.creators.buket.club.web.request.GenerateTypePriceRequest;
import ru.creators.buket.club.web.request.OrderCreateRequest;
import ru.creators.buket.club.web.request.OrderDeleteRequest;
import ru.creators.buket.club.web.request.OrderGetFlexibleAnswersRequest;
import ru.creators.buket.club.web.request.OrderGetRequest;
import ru.creators.buket.club.web.request.OrderPatchRequest;
import ru.creators.buket.club.web.request.OrdersGetRequest;
import ru.creators.buket.club.web.request.PriceRangeGetRequest;
import ru.creators.buket.club.web.request.ProfileGetRequest;
import ru.creators.buket.club.web.request.ReviewRequest;
import ru.creators.buket.club.web.request.SessionCreateRequest;
import ru.creators.buket.club.web.request.SessionUpdateRequest;
import ru.creators.buket.club.web.response.BouquetsResponse;
import ru.creators.buket.club.web.response.DefaultResponse;
import ru.creators.buket.club.web.response.DictionaryResponse;
import ru.creators.buket.club.web.response.ListAnswerFlexResponse;
import ru.creators.buket.club.web.response.OrderResponse;
import ru.creators.buket.club.web.response.OrdersResponse;
import ru.creators.buket.club.web.response.PriceRangeResponse;
import ru.creators.buket.club.web.response.ProfileResponse;
import ru.creators.buket.club.web.response.SessionResponse;

/**
 * Created by mifkamaz on 19/11/15.
 */
public class WebMethods {
    private static WebMethods mWebMethods = new WebMethods();

    public static WebMethods getInstance() {
        return mWebMethods;
    }

    public void setSpiceManager(SpiceManager mSpiceManager) {
        this.mSpiceManager = mSpiceManager;
    }

    public SpiceManager getSpiceManager() {
        return mSpiceManager;
    }

    public WebMethods() {}

    public WebMethods(SpiceManager mSpiceManager) {
        this.mSpiceManager = mSpiceManager;
    }

    private SpiceManager mSpiceManager;

    public void createSession(String uuid, String deviceToken, RequestListener<SessionResponse> listener){
        SessionCreateRequest request = new SessionCreateRequest(uuid, deviceToken);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    public void loadBouquets(String accessToken, int floverTypeId,
                              int floverColorId, int dayEventId,
                              int minPrice, int maxPrice, int page, int perPage,
                              RequestListener<BouquetsResponse> listener){
        BouquetsGetRequest request = new BouquetsGetRequest(accessToken, floverTypeId, floverColorId, dayEventId, minPrice, maxPrice, page, perPage);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    public void loadPriceRange(String accessToken, RequestListener<PriceRangeResponse> listener){
        PriceRangeGetRequest request = new PriceRangeGetRequest(accessToken);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    public void getProfile(String accessToken, RequestListener<ProfileResponse> listener){
        ProfileGetRequest request = new ProfileGetRequest(accessToken);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    private RetryPolicy getRetryPolicy() {
        return new RetryPolicy() {
            @Override
            public int getRetryCount() {
                return 0;
            }

            @Override
            public void retry(SpiceException e) {

            }

            @Override
            public long getDelayBeforeRetry() {
                return 0;
            }
        };
    }

    public static void loadImage(Context context, String url, final ImageView imageView) {
        Picasso.with(context)
                .load(url)
                .into(imageView);
        imageView.requestLayout();
    }

    public void getDictionary(String accessToken, String dictionaryType, RequestListener<DictionaryResponse> listener){
        DictionaryGetRequest request = new DictionaryGetRequest(accessToken, dictionaryType);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    public void sendOrder(String accessToken, Order order, RequestListener<OrderResponse> listener){
        OrderCreateRequest request = new OrderCreateRequest(accessToken, order);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    public void getOrders(String accessToken, RequestListener<OrdersResponse> listener){
        OrdersGetRequest request = new OrdersGetRequest(accessToken);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    public void generateTypePrice(String accessToken, RequestListener<DefaultResponse> listener){
        GenerateTypePriceRequest request = new GenerateTypePriceRequest(accessToken);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    public void getFlexAnswers(String accessToken, int orderId,RequestListener<ListAnswerFlexResponse> listener){
        OrderGetFlexibleAnswersRequest request = new OrderGetFlexibleAnswersRequest(accessToken, orderId);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    public void orderPathRequest(String accessToken, Order order, int orderId, RequestListener<DefaultResponse> listener){
        OrderPatchRequest request = new OrderPatchRequest(accessToken, order, orderId);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    public void sendReviewRequest(String accessToken, int orderId, String comment, int rating, RequestListener<DefaultResponse> listener){
        ReviewRequest request = new ReviewRequest(accessToken, orderId, comment, rating);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    public void orderGetRequest(String accessToken, int orderId, RequestListener<OrderResponse> listener){
        OrderGetRequest request = new OrderGetRequest(accessToken, orderId);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    public void sessionUpdateRequest(String accessToken, String deviceToken, RequestListener<DefaultResponse> listener){
        SessionUpdateRequest request = new SessionUpdateRequest(accessToken, deviceToken);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }

    public void removeOrderRequest(String accessToken, int orderId, RequestListener<DefaultResponse> listener){
        OrderDeleteRequest request = new OrderDeleteRequest(accessToken, orderId);
        request.setRetryPolicy(getRetryPolicy());
        mSpiceManager.execute(request, listener);
    }
}
