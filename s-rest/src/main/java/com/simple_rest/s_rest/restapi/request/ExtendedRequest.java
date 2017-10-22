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

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map.Entry;


/**this class provides functionalities to make http request on restful webservice.
 * thia class is advanced version of 'SimpleRequest'.
 * additionally this class can be able to make any http request WITH BODY.
 * <br><br>
 * it provides functionality to parse POJO objects to Json as well as
   Http Json response in POJO objects.
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
 * @see SimpleRequest
 * @see com.simple_rest.s_rest.restapi.request.HeaderTools
 * @see AsyncTask
 * @version 2.0.0
 */

public class ExtendedRequest<T,R> extends AsyncTask<String,Void,T> {

    @NonNull final private Class<T> type;
    @NonNull final private HttpHeaders requestHeaders;
    @NonNull private HttpMethod meth;
    //private Map<String,String> headers=new HashMap<>();
    private R requestObj;
    private HttpStatus httpStatus;
    private HttpHeaders responseHeaders;

    /**
     * initialize object with specified parameters.
     * @param type class object of response type.
     * @param requestObj request object
     * @param meth http request method
     * @param header request headers
     */
    public ExtendedRequest(@NonNull Class<T> type,R requestObj, @NonNull HttpMethod meth,
                           Entry<String,String> ... header) {
        this(type,requestObj,meth,new HttpHeaders());
        for(Entry<String,String> i:header){
            requestHeaders.add(i.getKey(),i.getValue());
        }
    }

    /**
     * initialize object with specified parameters and Http GET method
     * @param type class object of response type.
     * @param requestObj request object
     * @param header request headers
     */
    public ExtendedRequest(@NonNull Class<T> type,R requestObj, Entry<String,String> ... header) {
        this(type, requestObj, HttpMethod.GET, header);
    }


    /**
     * initialize object with specified parameters.
     * @param type class object of response type.
     * @param requestObj request object
     * @param meth http request method
     * @param header request headers
     */
    public ExtendedRequest(@NonNull Class<T> type,R requestObj, @NonNull HttpMethod meth,
                           HttpHeaders header) {
        this.type=type;
        this.meth=meth;
        this.requestObj=requestObj;
        requestHeaders = header;
    }

    /**
     * initialize object with specified parameters and Http GET method
     * @param type class object of response type.
     * @param requestObj request object
     * @param header request headers
     */
    public ExtendedRequest(@NonNull Class<T> type,R requestObj, HttpHeaders header) {
        this(type, requestObj, HttpMethod.GET, header);
    }

    /**
     * initialize object with specified parameters
     * it is set request object with null which can be modified by setter methods.
     * if request object is specified at request execution, it works same as a SimpleRequest.
     * @param type class object of response type.
     * @param meth http request method
     * @param header request headers
     */
    public ExtendedRequest(@NonNull Class<T> type, @NonNull HttpMethod meth,
                           Entry<String,String> ... header) {
        this(type, meth,new HttpHeaders());
        for(Entry<String,String> i:header){
            requestHeaders.add(i.getKey(),i.getValue());
        }
    }

    /**
     * initialize object with specified parameters and Http GET method
     * it is set request object with null which can be modified by setter methods.
     * if request object is specified at request execution, it works same as a SimpleRequest.
     * @param type class object of response type.
     * @param header request headers
     */
    public ExtendedRequest(@NonNull Class<T> type, Entry<String,String> ... header) {
        this(type, HttpMethod.GET, header);
    }

    /**
     * initialize object with specified parameters
     * it is set request object with null which can be modified by setter methods.
     * if request object is specified at request execution, it works same as a SimpleRequest.
     * @param type class object of response type.
     * @param meth http request method
     * @param header request headers
     */
    public ExtendedRequest(@NonNull Class<T> type, @NonNull HttpMethod meth,
                           HttpHeaders header) {
        this(type, null, meth, header);
    }

    /**
     * initialize object with specified parameters and Http GET method
     * it is set request object with null which can be modified by setter methods.
     * if request object is specified at request execution, it works same as a SimpleRequest.
     * @param type class object of response type.
     * @param header request headers
     */
    public ExtendedRequest(@NonNull Class<T> type, HttpHeaders header) {
        this(type, HttpMethod.GET, header);
    }

    @Override
    protected T doInBackground(String... params) {
        try {
            RequestHandler rh = new RequestHandler();
            T obj=rh.getResource(type, params[0], meth, requestObj, requestHeaders);
            this.httpStatus = rh.getHttpStatus();
            this.responseHeaders = rh.getHttpHeaders();
            return obj;
        }catch(Exception ex){
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
     * set Request Object
     * @param requestObj request Object
     */
    public void setRequestObject(@Nullable R requestObj) {
        this.requestObj = requestObj;
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
     * return request object
     * @return request object
     */
    @Nullable
    public R getRequestObject() {
        return requestObj;
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
