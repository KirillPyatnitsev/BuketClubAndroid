package ru.creators.buket.club.web.request;

import android.net.Uri;
import android.util.Log;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.URLDecoder;

import ru.creators.buket.club.AppException;
import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.consts.ServerConfig;
import ru.creators.buket.club.model.Error;
import ru.creators.buket.club.web.response.BouquetsResponse;
import ru.creators.buket.club.web.response.DefaultResponse;


/**
 * Created by mifkamaz on 19/11/15.
 */

public abstract class BaseRequest<T> extends GoogleHttpClientSpiceRequest<T> {

    private static final String TAG = "BaseRequest";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String accessToken;

    public BaseRequest(Class<T> clazz, String accessToken) {
        super(clazz);
        this.accessToken = accessToken;
    }

    protected final Uri.Builder buildUri() {
        Uri.Builder builder = getUriWithServerAddress();
        builder = addSeverConfigToUri(builder);
        return builder;
    }

    private final Uri.Builder getUriWithServerAddress() {
        return Uri.parse(ServerConfig.SERVER_ADRESS).buildUpon();
    }

    private final Uri.Builder addSeverConfigToUri(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(ServerConfig.SERVER_API_PREFIX);
        uriBuilder.appendPath(ServerConfig.SERVER_API_VERSION);
        uriBuilder.appendPath(ServerConfig.SERVER_API_VERSION_V1);
        return uriBuilder;
    }

    protected final HttpRequest makePostRequest(Uri.Builder builder, Object object) throws IOException {
        HttpContent content = objectToHttpContent(object);
        return buildRequest(HttpMethods.POST, builder, content);
    }

    protected final HttpRequest makePatchRequest(Uri.Builder uri, Object object) throws IOException {
        HttpContent content = objectToHttpContent(object);
        HttpRequest request = buildRequest(HttpMethods.POST, uri, content);
        request.getHeaders().set("X-HTTP-Method-Override", "PATCH");
        return request;
    }

    protected final HttpRequest makeGetRequest(Uri.Builder uri) throws IOException {
        return buildRequest(HttpMethods.GET, uri, null);
    }

    protected final HttpRequest makeDeleteRequest(Uri.Builder uri) throws IOException {
        return buildRequest(HttpMethods.DELETE, uri, null);
    }

    protected final <Z extends DefaultResponse> DefaultResponse getResponse(HttpResponse httpResponse, Class<Z> clazz) {
        DefaultResponse response = getResponse(httpResponse, clazz, new DefaultResponse());
        return response;
    }

    protected final <Z extends DefaultResponse> DefaultResponse getResponse(HttpResponse httpResponse, Class<Z> clazz, DefaultResponse response) {
        Error status = new Error();
        status.setCode(httpResponse.getStatusCode());

        if (status.isStatusDone()) {
            try {
                String responseString = httpResponse.parseAsString();
                response = toObject(responseString, clazz);
            } catch (IOException ex) {
                status.setMessage(ex.toString());
            }
        }

        response.setStatus(status);
        return response;
    }

    private HttpRequest buildRequest(String method, Uri.Builder builder, HttpContent content) throws IOException {
        Uri uri = builder.build();
        GenericUrl url = new GenericUrl(URLDecoder.decode(uri.toString(), "ASCII"));
        HttpRequest request = getHttpRequestFactory().buildRequest(method, url, content);
        request.getHeaders().setContentType("application/json");
        return request;
    }

    protected <Z extends DefaultResponse> Z executeRequest(HttpRequest request, Class<Z> responseClass) throws IOException {
        Z resp = null;
        try {
            resp = responseClass.newInstance();
        } catch (Exception e) {
            throw new AppException("Failed to create response instance", e);
        }
        return executeRequest(request, responseClass, resp);
    }

    protected <Z extends DefaultResponse> Z executeRequest(HttpRequest request, Class<Z> clazz, Z response) throws IOException {
        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);
        Log.d(TAG, "REQUEST: " + request.getContent());
        Z z = (Z) getResponse(request.execute(), clazz, response);
        Log.d(TAG, "RESPONSE: " + z);
        return z;
    }

    private String toJson(Object object) throws IOException {
        String json = MAPPER.writeValueAsString(object);
        return json;
    }

    private <T> T toObject(String json, Class<T> clazz) throws IOException {
        T object = MAPPER.readValue(json, clazz);
        return object;
    }

    private HttpContent getHttpContentFromJsonString(String json) {
        return ByteArrayContent.fromString("application/json", json);
    }

    private HttpContent objectToHttpContent(Object content) {
        HttpContent httpContent = null;
        if (content instanceof HttpContent) {
            httpContent = (HttpContent) content;
        } else {
            try {
                final String json = toJson(content);
                httpContent = getHttpContentFromJsonString(json);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        return httpContent;
    }
}
