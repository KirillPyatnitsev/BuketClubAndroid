package ru.creators.buket.club.web.request;

import android.net.Uri;
import android.util.Log;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.concurrent.atomic.AtomicInteger;

import ru.creators.buket.club.AppException;
import ru.creators.buket.club.DataController;
import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.consts.ServerConfig;
import ru.creators.buket.club.model.Error;
import ru.creators.buket.club.model.Session;
import ru.creators.buket.club.tools.Stopwatch;
import ru.creators.buket.club.web.response.BouquetsResponse;
import ru.creators.buket.club.web.response.DefaultResponse;


/**
 * Created by mifkamaz on 19/11/15.
 */

public abstract class BaseRequest<T extends DefaultResponse> extends GoogleHttpClientSpiceRequest<T> {

    private static final String TAG = ServerConfig.TAG_PREFIX + "BaseRequest";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String accessToken;

    private static final AtomicInteger nextRequestIndex = new AtomicInteger(1);

    public BaseRequest(Class<T> clazz) {
        super(clazz);
        DataController instance = DataController.getInstance();
        Session session = instance == null? null: instance.getSession();
        this.accessToken = session == null? null: session.getAccessToken();
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

    private HttpRequest buildRequest(String method, Uri.Builder builder, HttpContent content) throws IOException {
        Uri uri = builder.build();
        GenericUrl url = new GenericUrl(URLDecoder.decode(uri.toString(), "ASCII"));
        HttpRequest request = getHttpRequestFactory().buildRequest(method, url, content);
        request.getHeaders().setContentType("application/json");
        return request;
    }

    protected T executeRequest(HttpRequest request) throws IOException {
        Class<T> resultType = this.getResultType();
        return executeRequest(request, resultType);
    }

    private T executeRequest(HttpRequest request, Class<T> clazz) throws IOException {
        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);
        HttpContent content = request.getContent();

        int requestId = nextRequestIndex.getAndIncrement();
        Log.d(TAG, "REQUEST " + requestId + ": " + request.getRequestMethod() + " " + request.getUrl()
                + (content == null? "":  " " + contentToString(content)));

        T z = null;
        Stopwatch stopwatch = new Stopwatch();
        try {
            HttpResponse httpResponse = request.execute();
            String duration = stopwatch.getMillisString();
            Error status = new Error();
            int code = httpResponse.getStatusCode();
            status.setCode(code);

            String str;
            if(code == HttpStatusCodes.STATUS_CODE_NO_CONTENT) {
                str = "{}";
            } else {
                str = httpResponse.parseAsString();
            }
            z = toObject(str, clazz);
            z.setStatus(status);

            Log.d(TAG, "RESPONSE " + requestId + ": " + duration + " " + str);
        } catch(Exception e) {
            String msg = e.toString();
            if(msg.length() > 1000) {
                msg = msg.substring(0, 1000);
            }
            msg = msg.replace('\n', ' ');
            Log.e(TAG, "EXCEPTION " + requestId + ": " + stopwatch + " " + msg);
            throw e;
        }
        return z;
    }

    private String contentToString(HttpContent content) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        content.writeTo(out);
        byte[] data = out.toByteArray();
        String str = new String(data);
        return str;
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
                Log.e(TAG, "JSON encoding exception: " + err);
            }
        }
        return httpContent;
    }
}
