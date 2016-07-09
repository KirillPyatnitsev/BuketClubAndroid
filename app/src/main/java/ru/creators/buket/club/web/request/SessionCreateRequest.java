package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.SessionResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class SessionCreateRequest extends BaseRequest<SessionResponse> {

    private String udid;
    private String password;
    private String deviceToken;
    private String appMode;

    public SessionCreateRequest(String udid, String deviceToken) {
        super(SessionResponse.class);
        this.udid = udid;
        this.deviceToken = deviceToken;
    }

    @Override
    protected Uri getUri() {
        return super.getUri();
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.SESSIONS);
        return uriBuilder;
    }

    @Override
    public SessionResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getPostHttpRequest(null);

        request.getUrl().put(Rest.UUID, udid);
        if (deviceToken!=null) {
            request.getUrl().put(Rest.DEVICE_TOKEN, deviceToken);
        }

//        Log.d(TAG, "deviceToken: "  + String.valueOf(deviceToken));
//        Log.d(TAG, "access_token: " + DataController.getInstance().getSession().getAccessToken());


        request.getUrl().put(Rest.DEVICE_TYPE, Rest.DEVICE_TYPE_ANDROID);

        return (SessionResponse) getResponse(request.execute(), SessionResponse.class);
    }
}
