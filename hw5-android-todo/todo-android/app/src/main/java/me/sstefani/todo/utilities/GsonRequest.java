package me.sstefani.todo.utilities;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    private static final String BASE_URL = "http://192.168.0.109:8080";
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Object body;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;


    public GsonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, BASE_URL + url, errorListener);
        Map<String, String> headers = new HashMap<>();
        this.setStandardHeaders(headers);
        this.attachJWT(headers, DataHolder.getInstance().getJwt());
        this.clazz = clazz;
        this.body = null;
        this.headers = headers;
        this.listener = listener;
    }

    public GsonRequest(int method, String url, Object body, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, BASE_URL + url, errorListener);
        Map<String, String> headers = new HashMap<>();
        this.setStandardHeaders(headers);
        this.attachJWT(headers, DataHolder.getInstance().getJwt());
        this.clazz = clazz;
        this.body = body;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body != null ? gson.toJson(body).getBytes() : super.getBody();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            if (response.headers.get("Authorization") != null) {
                JSONObject authenticationResponse = new JSONObject();
                authenticationResponse.put("jwt", response.headers.get("Authorization"));
                json = authenticationResponse.toString();
            }

            return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    private void attachJWT(Map<String, String> headers, String jwt) {
        headers.put("Authorization", jwt);
    }

    private void setStandardHeaders(Map<String, String> headers) {
        headers.put("Accept", "application/json, application/hal+json");
        headers.put("Content-Type", "application/json; charset=utf-8");
    }
}
