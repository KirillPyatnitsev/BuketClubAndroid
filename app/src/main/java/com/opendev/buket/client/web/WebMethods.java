package com.opendev.buket.client.web;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.retry.DefaultRetryPolicy;
import com.octo.android.robospice.retry.RetryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import com.opendev.buket.client.BuildConfig;
import com.opendev.buket.client.DataController;
import com.opendev.buket.client.R;
import com.opendev.buket.client.consts.ServerConfig;
import com.opendev.buket.client.model.Order;
import com.opendev.buket.client.model.Profile;
import com.opendev.buket.client.view.activitys.BaseActivity;
import com.opendev.buket.client.web.request.AddressGetRequest;
import com.opendev.buket.client.web.request.BouquetsGetRequest;
import com.opendev.buket.client.web.request.DictionaryGetRequest;
import com.opendev.buket.client.web.request.GenerateTypePriceRequest;
import com.opendev.buket.client.web.request.OrderCreateRequest;
import com.opendev.buket.client.web.request.OrderDeleteRequest;
import com.opendev.buket.client.web.request.OrderGetFlexibleAnswersRequest;
import com.opendev.buket.client.web.request.OrderGetRequest;
import com.opendev.buket.client.web.request.OrderPatchRequest;
import com.opendev.buket.client.web.request.OrdersGetRequest;
import com.opendev.buket.client.web.request.PhoneVerificationFinishPostRequest;
import com.opendev.buket.client.web.request.PhoneVerificationStartPostRequest;
import com.opendev.buket.client.web.request.PriceRangeGetRequest;
import com.opendev.buket.client.web.request.ProfileGetRequest;
import com.opendev.buket.client.web.request.ProfilePatchRequest;
import com.opendev.buket.client.web.request.ReviewRequest;
import com.opendev.buket.client.web.request.SessionCreateRequest;
import com.opendev.buket.client.web.request.SessionUpdateRequest;
import com.opendev.buket.client.web.request.ShopsAllGetRequest;
import com.opendev.buket.client.web.response.BouquetsResponse;
import com.opendev.buket.client.web.response.DefaultResponse;
import com.opendev.buket.client.web.response.DictionaryResponse;
import com.opendev.buket.client.web.response.ListAnswerFlexResponse;
import com.opendev.buket.client.web.response.OrderResponse;
import com.opendev.buket.client.web.response.OrdersResponse;
import com.opendev.buket.client.web.response.PhoneCodeResponse;
import com.opendev.buket.client.web.response.PriceRangeResponse;
import com.opendev.buket.client.web.response.ProfileResponse;
import com.opendev.buket.client.web.response.SessionResponse;
import com.opendev.buket.client.web.response.ShopListResponse;

/**
 * Created by mifkamaz on 19/11/15.
 */
public class WebMethods {
    private static final String LOG_TAG = ServerConfig.TAG_PREFIX + "WebMethods";

    private static final WebMethods mWebMethods = new WebMethods();
    private static WebMethods fakeWebMethods = null;

    private static final RetryPolicy NO_RETRY = new DefaultRetryPolicy(0, 0, 0);

    public static WebMethods getInstance() {
        WebMethods wm;
        if (ServerConfig.USE_FAKE_DEBUG_DATA && BuildConfig.DEBUG) {
            if (fakeWebMethods == null) {
                fakeWebMethods = new FakeWebMethods();
            }
            wm = fakeWebMethods;
        } else {
            wm = mWebMethods;
        }
        return wm;
    }

    private SpiceManager mSpiceManager;

    protected WebMethods() {
    }

    public void setSpiceManager(SpiceManager mSpiceManager) {
        this.mSpiceManager = mSpiceManager;
    }

    public void createSession(String uuid, String deviceToken, RequestListener<SessionResponse> listener) {
        execute(new SessionCreateRequest(uuid, deviceToken), listener);
    }

    public void loadBouquets(int flowerTypeId,
                             int flowerColorId, int dayEventId,
                             int minPrice, int maxPrice, int page, int perPage,
                             RequestListener<BouquetsResponse> listener) {
        execute(new BouquetsGetRequest(flowerTypeId, flowerColorId, dayEventId, minPrice, maxPrice, page, perPage), listener);
    }

    public void loadPriceRange(RequestListener<PriceRangeResponse> listener) {
        execute(new PriceRangeGetRequest(), listener);
    }

    public void getProfile(RequestListener<ProfileResponse> listener) {
        execute(new ProfileGetRequest(), listener);
    }

    public void getDictionary(String dictionaryType, RequestListener<DictionaryResponse> listener) {
        execute(new DictionaryGetRequest(dictionaryType), listener);
    }

    public void sendOrder(Order order, RequestListener<OrderResponse> listener) {
        execute(new OrderCreateRequest(order), listener);
    }

    public void getOrders(int page, int perPage, RequestListener<OrdersResponse> listener) {
        execute(new OrdersGetRequest(page, perPage), listener);
    }

    public void generateTypePrice(RequestListener<DefaultResponse> listener) {
        execute(new GenerateTypePriceRequest(), listener);
    }

    public void getFlexAnswers(int orderId, RequestListener<ListAnswerFlexResponse> listener) {
        execute(new OrderGetFlexibleAnswersRequest(orderId), listener);
    }

    public void orderPatchRequest(Order order, int orderId, RequestListener<DefaultResponse> listener) {
        execute(new OrderPatchRequest(order, orderId), listener);
    }

    public void sendReviewRequest(int orderId, String comment, int rating, RequestListener<DefaultResponse> listener) {
        execute(new ReviewRequest(orderId, comment, rating), listener);
    }

    public void orderGetRequest(int orderId, RequestListener<OrderResponse> listener) {
        execute(new OrderGetRequest(orderId), listener);
    }

    public void sessionUpdateRequest(String deviceToken, RequestListener<DefaultResponse> listener) {
        execute(new SessionUpdateRequest(deviceToken), listener);
    }

    public void removeOrderRequest(int orderId, RequestListener<DefaultResponse> listener) {
        execute(new OrderDeleteRequest(orderId), listener);
    }

    public void listShopGetRequest(int page, int perPage, RequestListener<ShopListResponse> listener) {
        execute(new ShopsAllGetRequest(page, perPage), listener);
    }

    public void addressGetRequest(double latitude, double longitude, Context context, RequestListener<String> listener) {
        execute(new AddressGetRequest(latitude, longitude, context), listener);
    }

    public void phoneVerificationStartPostRequest(String phone, RequestListener<PhoneCodeResponse> listener) {
        execute(new PhoneVerificationStartPostRequest(phone), listener);
    }

    public void phoneVerificationFinishPostRequest(String phone, String code, RequestListener<DefaultResponse> listener) {
        execute(new PhoneVerificationFinishPostRequest(phone, code), listener);
    }

    public void profilePatchRequest(Profile profile, RequestListener<DefaultResponse> listener) {
        execute(new ProfilePatchRequest(profile), listener);
    }

    protected <T> void execute(final SpiceRequest<T> request, final RequestListener<T> requestListener) {
        BaseActivity activity = DataController.getInstance().getBaseActivity();
        if (activity.isOnline()) {
            request.setRetryPolicy(NO_RETRY);
            Log.d(LOG_TAG, "Request: " + request);
            mSpiceManager.execute(request, new RequestListener<T>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    Log.d(LOG_TAG, "Request failed: " + spiceException);
                    requestListener.onRequestFailure(spiceException);
                }

                @Override
                public void onRequestSuccess(T t) {
                    Log.d(LOG_TAG, "Request success: " + t);
                    requestListener.onRequestSuccess(t);
                }
            });

        } else {
            activity.showSnackBar(R.string.check_internet_connection);
            activity.stopLoading();
        }
    }

}
