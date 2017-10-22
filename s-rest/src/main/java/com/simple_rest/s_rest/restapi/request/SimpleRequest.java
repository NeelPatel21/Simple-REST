/*
 * The MIT License
 *
 *  Copyright 2017 Neel Patel.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package com.simple_rest.s_rest.restapi.request;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.simple_rest.s_rest.R;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map.Entry;


/**this class provides functionalities to make http request on restful webservice.
 * this class can be able to make any http request WITHOUT BODY.
 * see 'ExtendedRequest' to make Http request with body.
 * <br><br>
 * it provides functionality to parse the Http Json response in POJO objects.
 * <br><br>
 * if the response is in form of Json array of particular entity then the
 generic type must be Array of the class.
 * if the response is in form of plan text then the generic type 'T' must be String.
 * any class can be used in case of empty response expected.
 * <br><br>
 * 'HeaderTools' class can be used to make headers.
 * <br><br>
 * Created by Neel Patel on 24-07-2017.
 * @author Neel Patel
 * @see ExtendedRequest
 * @see com.simple_rest.s_rest.restapi.request.HeaderTools
 * @see AsyncTask
 * @version 2.0.0
 */

public class SimpleRequest<T> extends AsyncTask<String,Void,T> {

    @NonNull final private Class<T> type;
    @NonNull final private HttpHeaders requestHeaders;
    @NonNull private HttpMethod meth;
    //private Map<String,String> headers=new HashMap<>();
    private HttpStatus httpStatus;
    private HttpHeaders responseHeaders;

    /**make a object with specified parameters.
     * @param type class object of expected return type.
     * @param meth http request method.
     * @param header http request header.
     */
    public SimpleRequest(@NonNull Class<T> type, @NonNull HttpMethod meth,
                         Entry<String,String> ... header) {
        this(type,meth,new HttpHeaders());
        for(Entry<String,String> i:header){
            requestHeaders.add(i.getKey(),i.getValue());
        }
    }

    /**make a class with http get request without body with specified headers.
     * @param type class object of expected return type.
     * @param header http request headers.
     */
    public SimpleRequest(@NonNull Class<T> type, Entry<String,String> ... header) {
        this(type, HttpMethod.GET, header);
    }

    /**make a object with specified parameters.
     * @param type class object of expected return type.
     * @param meth http request method.
     * @param headers http request header.
     */
    public SimpleRequest(@NonNull Class<T> type, @NonNull HttpMethod meth,
                         @NonNull HttpHeaders headers) {
        this.type=type;
        this.meth=meth;
        this.requestHeaders=headers;
    }

    /**make a class with http get request without body with specified headers.
     * @param type class object of expected return type.
     * @param header http request headers.
     */
    public SimpleRequest(@NonNull Class<T> type, @NonNull HttpHeaders headers) {
        this(type, HttpMethod.GET, headers);
    }

    @Override
    protected T doInBackground(String... params) {
        try {
            RequestHandler rh = new RequestHandler();
            T obj = rh.getResource(type, params[0], meth, requestHeaders);
            this.httpStatus=rh.getHttpStatus();
            this.responseHeaders=rh.getHttpHeaders();
            return obj;
        }catch(Exception ex){
//            System.err.println("do in back");
//            ex.printStackTrace();
            Log.e("SimpleRequest", "doInBackground: "+ex.getMessage(),ex);
        }
        return null;
    }

    /**
     * this method simply return object return by 'get' method of this class.
     * additionally it will mask all exceptions & return null if exception
       occurs while calling get method.
     * @return object parsed from http response.
     */
    @Nullable
    public T getObj(){
        try{
            return get();
        }catch(Exception ex){
            Log.e("SimpleRequest","doInBackground: "+ex.getMessage(),ex);
        }
        return null;
    }

    /**
     * add a Header to requestHeaders
     * @param key header name
     * @param values value of the header
     */
    public void addHeader(@NonNull String key,String ... values){
        requestHeaders.put(key, Arrays.asList(values));
    }

    /**
     * set Request method
     * @param meth http method
     */
    public void setHttpMethod(@NonNull HttpMethod meth) {
        this.meth = meth;
    }


    /**
     * return Http Request headers
     * @return request headers
     */
    @NonNull
    public HttpHeaders getRequestHeaders() {
        return requestHeaders;
    }

    /**
     * return request method
     * @return http method
     */
    @NonNull
    public HttpMethod getHttpMethod() {
        return meth;
    }


    /**
     * @return http status code of last request
     */
    @Nullable
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * @return http headers code of last response
     */
    @Nullable
    public HttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

}
