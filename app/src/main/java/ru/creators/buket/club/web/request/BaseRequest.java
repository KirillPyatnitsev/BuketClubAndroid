package ru.creators.buket.club.web.request;

import android.net.Uri;

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

import ru.creators.buket.club.consts.ServerConfig;
import ru.creators.buket.club.model.Error;
import ru.creators.buket.club.web.response.DefaultResponse;


/**
 * Created by mifkamaz on 19/11/15.
 */

public abstract class BaseRequest<T> extends GoogleHttpClientSpiceRequest<T> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public BaseRequest(Class<T> clazz) {
        super(clazz);
    }

    protected Uri getUri() {
        Uri.Builder builder = getUriWithServerAddress();
        builder = addSeverConfigToUri(builder);
        builder = addRestAddress(builder);
        return builder.build();
    }

    private Uri.Builder getUriWithServerAddress() {
        return Uri.parse(ServerConfig.SERVER_ADRESS).buildUpon();
    }

    protected Uri.Builder addSeverConfigToUri(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(ServerConfig.SERVER_API_PREFIX);
        uriBuilder.appendPath(ServerConfig.SERVER_API_VERSION);
        uriBuilder.appendPath(ServerConfig.SERVER_API_VERSION_V1);
        return uriBuilder;
    }

    protected abstract Uri.Builder addRestAddress(Uri.Builder uriBuilder);

    protected final HttpRequest getPostHttpRequest(Object object) throws IOException {
        HttpContent content = objectToHttpContent(object);
        return buildRequest(HttpMethods.POST, content);
    }

    protected final HttpRequest getPathHttpRequest(Object object) throws IOException {
        HttpContent content = objectToHttpContent(object);
        HttpRequest request = buildRequest(HttpMethods.POST, content);
        request.getHeaders().set("X-HTTP-Method-Override", "PATCH");
        return request;
    }

    protected final HttpRequest getGetHttpRequest() throws IOException {
        return buildRequest(HttpMethods.GET, null);
    }

    protected final HttpRequest getDeleteHttpRequest() throws IOException {
        return buildRequest(HttpMethods.DELETE, null);
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

    private HttpRequest buildRequest(String method, HttpContent content) throws IOException {
        Uri uri = getUri();
        HttpRequest request = getHttpRequestFactory().buildRequest(method,
                new GenericUrl(URLDecoder.decode(uri.toString(), "ASCII")), content);
        request.getHeaders().setContentType("application/json");
        return request;
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
