package ru.creators.buket.club.web;

import android.content.Context;
import android.os.Debug;
import android.widget.ImageView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.retry.RetryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import ru.creators.buket.club.BuildConfig;
import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.Profile;
import ru.creators.buket.club.web.request.AddressGetRequest;
import ru.creators.buket.club.web.request.BouquetsGetRequest;
import ru.creators.buket.club.web.request.DictionaryGetRequest;
import ru.creators.buket.club.web.request.GenerateTypePriceRequest;
import ru.creators.buket.club.web.request.OrderCreateRequest;
import ru.creators.buket.club.web.request.OrderDeleteRequest;
import ru.creators.buket.club.web.request.OrderGetFlexibleAnswersRequest;
import ru.creators.buket.club.web.request.OrderGetRequest;
import ru.creators.buket.club.web.request.OrderPatchRequest;
import ru.creators.buket.club.web.request.OrdersGetRequest;
import ru.creators.buket.club.web.request.PhoneVerificationFinishPostRequest;
import ru.creators.buket.club.web.request.PhoneVerificationStartPostRequest;
import ru.creators.buket.club.web.request.PriceRangeGetRequest;
import ru.creators.buket.club.web.request.ProfileGetRequest;
import ru.creators.buket.club.web.request.ProfilePatchRequest;
import ru.creators.buket.club.web.request.ReviewRequest;
import ru.creators.buket.club.web.request.SessionCreateRequest;
import ru.creators.buket.club.web.request.SessionUpdateRequest;
import ru.creators.buket.club.web.request.ShopsAllGetRequest;
import ru.creators.buket.club.web.response.BouquetsResponse;
import ru.creators.buket.club.web.response.DefaultResponse;
import ru.creators.buket.club.web.response.DictionaryResponse;
import ru.creators.buket.club.web.response.ListAnswerFlexResponse;
import ru.creators.buket.club.web.response.OrderResponse;
import ru.creators.buket.club.web.response.OrdersResponse;
import ru.creators.buket.club.web.response.PhoneCodeResponse;
import ru.creators.buket.club.web.response.PriceRangeResponse;
import ru.creators.buket.club.web.response.ProfileResponse;
import ru.creators.buket.club.web.response.SessionResponse;
import ru.creators.buket.club.web.response.ShopListResponse;

/**
 * Created by mifkamaz on 19/11/15.
 */
public class WebMethods {
    private static WebMethods mWebMethods = new WebMethods();
    private static WebMethods fakeWebMethods = null;

    public static WebMethods getInstance() {
        WebMethods wm;
        if(BuildConfig.DEBUG) {
            if(fakeWebMethods == null) {
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

    public void loadBouquets(String accessToken, int floverTypeId,
                             int floverColorId, int dayEventId,
                             int minPrice, int maxPrice, int page, int perPage,
                             RequestListener<BouquetsResponse> listener) {
        execute(new BouquetsGetRequest(accessToken, floverTypeId, floverColorId, dayEventId, minPrice, maxPrice, page, perPage), listener);
    }

    public void loadPriceRange(String accessToken, RequestListener<PriceRangeResponse> listener) {
        execute(new PriceRangeGetRequest(accessToken), listener);
    }

    public void getProfile(String accessToken, RequestListener<ProfileResponse> listener) {
        execute(new ProfileGetRequest(accessToken), listener);
    }

    public void loadImage(Context context, String url, final ImageView imageView) {
        loadImage(context, url, imageView, true);
    }

    public void loadImage(Context context, String url, final ImageView imageView, boolean downscale) {
        RequestCreator picasso = Picasso.with(context)
                .load(url);
        if(downscale) {
            picasso.resize(600, 750).onlyScaleDown();
        }
        picasso.into(imageView);
        imageView.requestLayout();
    }

    public void getDictionary(String accessToken, String dictionaryType, RequestListener<DictionaryResponse> listener) {
        execute(new DictionaryGetRequest(accessToken, dictionaryType), listener);
    }

    public void sendOrder(String accessToken, Order order, RequestListener<OrderResponse> listener) {
        execute(new OrderCreateRequest(accessToken, order), listener);
    }

    public void getOrders(String accessToken, int page, int perPage, RequestListener<OrdersResponse> listener) {
        execute(new OrdersGetRequest(accessToken, page, perPage), listener);
    }

    public void generateTypePrice(String accessToken, RequestListener<DefaultResponse> listener) {
        execute(new GenerateTypePriceRequest(accessToken), listener);
    }

    public void getFlexAnswers(String accessToken, int orderId, RequestListener<ListAnswerFlexResponse> listener) {
        execute(new OrderGetFlexibleAnswersRequest(accessToken, orderId), listener);
    }

    public void orderPathRequest(String accessToken, Order order, int orderId, RequestListener<DefaultResponse> listener) {
        execute(new OrderPatchRequest(accessToken, order, orderId), listener);
    }

    public void sendReviewRequest(String accessToken, int orderId, String comment, int rating, RequestListener<DefaultResponse> listener) {
        execute(new ReviewRequest(accessToken, orderId, comment, rating), listener);
    }

    public void orderGetRequest(String accessToken, int orderId, RequestListener<OrderResponse> listener) {
        execute(new OrderGetRequest(accessToken, orderId), listener);
    }

    public void sessionUpdateRequest(String accessToken, String deviceToken, RequestListener<DefaultResponse> listener) {
        execute(new SessionUpdateRequest(accessToken, deviceToken), listener);
    }

    public void removeOrderRequest(String accessToken, int orderId, RequestListener<DefaultResponse> listener) {
        execute(new OrderDeleteRequest(accessToken, orderId), listener);
    }

    public void listShopGetRequest(String accessToken, int page, int perPage, RequestListener<ShopListResponse> listener) {
        execute(new ShopsAllGetRequest(accessToken, page, perPage), listener);
    }

    public void addressGetRequest(double latitude, double longitude, Context context, RequestListener<String> listener){
        execute(new AddressGetRequest(latitude, longitude, context), listener);
    }

    public void phoneVerificationStartPostRequest(String accessToken, String phone, RequestListener<PhoneCodeResponse> listener){
        execute(new PhoneVerificationStartPostRequest(accessToken, phone), listener);
    }

    public void phoneVerificationFinishPostRequest(String accessToken, String phone, String code,RequestListener<DefaultResponse> listener){
        execute(new PhoneVerificationFinishPostRequest(accessToken, phone, code), listener);
    }

    public void profilePatchRequest(String accessToken, Profile profile, RequestListener<DefaultResponse> listener){
        execute(new ProfilePatchRequest(accessToken, profile), listener);
    }

    protected <T> void execute(final SpiceRequest<T> request, final RequestListener<T> requestListener) {
        if (DataController.getInstance().getBaseActivity().isOnline()) {
            request.setRetryPolicy(getRetryPolicy());
            mSpiceManager.execute(request, requestListener);
        }else{
            DataController.getInstance().getBaseActivity().showSnackBar(R.string.check_internet_connection);
            DataController.getInstance().getBaseActivity().stopLoading();
        }
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
}
