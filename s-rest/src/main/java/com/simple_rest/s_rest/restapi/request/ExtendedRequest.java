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

import java.util.Map.Entry;


/**this class provides functionalities to make http request on restful webservice.
 * thia class is advanced version of 'SimpleRequest'.
 * additionally this class can be able to make any http request WITH BODY.
 * it provides functionality to parse POJO objects to Json as well as
   Http Json response in POJO objects.<br><br>
 * <br><br>
 * if the response is in form of plan text then the generic type must be String.<br>
 * <br><br>
 * Created by Neel Patel on 24-07-2017.
 * @author Neel Patel
 * @see SimpleRequest
 * @see com.simple_rest.s_rest.restapi.request.HeaderTools
 * @see AsyncTask
 * @version 2.0.0
 */

public class ExtendedRequest<T,R> extends AsyncTask<String,Void,T> {

    private Class<T> type;
    //private Map<String,String> headers=new HashMap<>();
    private HttpMethod meth;
    private R requestObj;
    private HttpStatus httpStatus;
    private HttpHeaders requestHeaders, responseHeaders;

    public ExtendedRequest(@NonNull Class<T> type,@NonNull R requestObj,@NonNull HttpMethod meth,
                           Entry<String,String> ... header) {
        this.type=type;
        this.meth=meth;
        this.requestObj=requestObj;
        requestHeaders = new HttpHeaders();
        for(Entry<String,String> i:header){
            responseHeaders.add(i.getKey(),i.getValue());
        }
    }

    public ExtendedRequest(@NonNull Class<T> type,@NonNull R requestObj, Entry<String,String> ... header) {
        this(type, requestObj, HttpMethod.GET, header);
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
    public HttpHeaders getHttpHeaders() {
        return responseHeaders;
    }

}
