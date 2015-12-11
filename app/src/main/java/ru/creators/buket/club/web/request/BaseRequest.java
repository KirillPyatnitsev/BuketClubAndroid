package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.URLDecoder;

import ru.creators.buket.club.consts.ServerConfig;
import ru.creators.buket.club.model.*;
import ru.creators.buket.club.model.Error;
import ru.creators.buket.club.web.response.DefaultResponse;


/**
 * Created by mifkamaz on 19/11/15.
 */

public abstract class BaseRequest<T> extends GoogleHttpClientSpiceRequest<T>{
    private ObjectMapper objectMapper = new ObjectMapper();

    public BaseRequest(Class<T> clazz){
        super(clazz);
    }

    protected Uri getUri() {
        Uri.Builder builder = getUriWithServerAdress();
        builder = addSeverConfigToUri(builder);
        builder = addRestAddress(builder);
        return builder.build();
    }

    protected Uri.Builder getUriWithServerAdress(){
        return Uri.parse(ServerConfig.SERVER_ADRESS).buildUpon();
    }

    protected Uri.Builder addSeverConfigToUri(Uri.Builder uriBuilder){
        uriBuilder.appendPath(ServerConfig.SERVER_API_PREFIX);
        uriBuilder.appendPath(ServerConfig.SERVER_API_VERSION);
        uriBuilder.appendPath(ServerConfig.SERVER_API_VERSION_V1);
        return uriBuilder;
    }

    protected abstract Uri.Builder addRestAddress(Uri.Builder uriBuilder);

    protected HttpContent jsonStringToHttpContent(String jsonString){
        return ByteArrayContent.fromString("application/json", jsonString);
    }

    protected HttpRequest getPostHttpRequest(HttpContent content) throws IOException {
        Uri uri = getUri();
        HttpRequest request = getHttpRequestFactory().buildPostRequest(new GenericUrl(URLDecoder.decode(uri.toString(), "ASCII")), content);
        return fillHeader(request);
    }

    protected HttpRequest getPathHttpRequest(HttpContent content) throws IOException {
        Uri uri = getUri();
        HttpRequest request = getHttpRequestFactory().buildPostRequest(new GenericUrl(URLDecoder.decode(uri.toString(), "ASCII")), content);

        request.getHeaders().set("X-HTTP-Method-Override", "PATCH");

        return fillHeader(request);
    }

    protected HttpRequest getGetHttpRequest() throws IOException {
        Uri uri = getUri();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(URLDecoder.decode(uri.toString(), "ASCII")));
        return fillHeader(request);
    }

    protected HttpRequest getDeleteHttpRequest() throws IOException {
        Uri uri = getUri();
        HttpRequest request = getHttpRequestFactory().buildDeleteRequest(new GenericUrl(URLDecoder.decode(uri.toString(), "ASCII")));
        return fillHeader(request);
    }

    protected HttpRequest getPutHttpRequest(HttpContent content) throws IOException {
        Uri uri = getUri();
        HttpRequest request = getHttpRequestFactory().buildPutRequest(new GenericUrl(URLDecoder.decode(uri.toString(), "ASCII")), content);
        return fillHeader(request);
    }

    private HttpRequest fillHeader(HttpRequest request) {
        request.getHeaders().setContentType("application/json");
//        request.getHeaders().set("Content-Type", "text/html");
        return request;
    }

    protected String toJson(Object object) throws IOException {
        String json = objectMapper.writeValueAsString(object);

        return json;
    }

    protected <T> T toObject(String json, Class<T> clazz) throws IOException {
        T object = objectMapper.readValue(json, clazz);
        return object;
    }

    protected <Z extends DefaultResponse> DefaultResponse getResponse(HttpResponse httpResponse, Class<Z> clazz){
        DefaultResponse response = null;

        Error status =new Error();
        status.setCode(httpResponse.getStatusCode());

        if (status.isStatusDone()){
            try {
                response = toObject(httpResponse.parseAsString(), clazz);
            }catch (IOException ex){
                status.setMessage(ex.toString());
            }
        }

        if (response == null){
            response = new DefaultResponse();
        }

        response.setStatus(status);

        return response;
    }

    protected <Z extends DefaultResponse> DefaultResponse getResponse(HttpResponse httpResponse, Class<Z> clazz, Z response){

        Error status =new Error();
        status.setCode(httpResponse.getStatusCode());

        if (status.isStatusDone()){
            try {
                response = toObject(httpResponse.parseAsString(), clazz);
            }catch (IOException ex){
                status.setMessage(ex.toString());
            }
        }

        response.setStatus(status);

        return response;
    }

    protected HttpContent getHttpContentFromJsonString(String json){
        return ByteArrayContent.fromString("application/json", json);
    }
}
