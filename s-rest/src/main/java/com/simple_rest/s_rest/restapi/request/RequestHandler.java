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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

/** this class provides functionalities to work with restful web services.
 * this class provide methods to make a HTTP request using POJO objects by
   automatically parsing it in JSON format.<br>
 * It also parse & return HTTP response
   to a POJO objects.<br>
 * It uses a 'spring framework' for parsing & handling Http request & response.<br>
 * Note:- It is mandatory to use other threads then main Activity thread to call
   methods of this class as they contains network access permission which is not
   permitted from main Activity thread.<br>
 * It is recommended use 'SimpleRequest' or 'ExtendedRequest' instead as they provides
   higher functionalities & can be used from main activity thread.<br>
 * Created by Neel Patel on 24-07-2017.
 * @author Neel Patel
 * @see SimpleRequest
 * @see ExtendedRequest
 * @version 2.0.0
 */

public class RequestHandler{

    private HttpStatus httpStatus;
    private HttpHeaders requestHeaders, responseHeaders;

    /**
     * this method will make http request using parameters provided.
     * request object will be parsed to json format & Http response will be parsed
       to a POJO.
     * response body must be JSON representation of generic type 'R'.
     * if generic type 'R' is String then content of the response body will
       be returned as it is.
     * @param type class object of expected Http response body.
     * @param url url.
     * @param meth Http request method.
     * @param requestObj body of the Http request.
     * @param header key value pairs of header parameters.
     * @param <T> class type of expected response body.
     * @param <R> class type of HTTP request body.
     * @return return Http response body as object of type 'R'. null otherwise.
     */
    @Nullable
    public <T,R> T getResource(@NonNull Class<T> type,@NonNull String url,@NonNull HttpMethod meth,
                               @NonNull R requestObj,@NonNull Map<String,String> header){
        Log.i("RequestHandler","Request: "+url+", Method: "+meth+", Class: "+type.getName());
        try {
            //parse map to headers
            requestHeaders = new HttpHeaders();
            for(Entry<String,String> e:header.entrySet())
                requestHeaders.add(e.getKey(),e.getValue());

            return getResource(type,url,meth,requestObj,requestHeaders);
        } catch (Exception e) {
            Log.e("RequestHandler", "getResource: "+e.getMessage(), e);
        }
        return null;
    }

    /**
     * this method will make http request without body.
     * response body must be JSON representation of generic type 'R'.
     * if generic type 'R' is String then content of the response body will
       be returned as it is.
     * internally it calls {@code getResource(type, url, meth, null, header);}
     * @param type class object of expected Http response body.
     * @param url url.
     * @param meth Http request method.
     * @param header key value pair of header.
     * @param <T> class type of expected response body.
     * @return return Http response body as object of type 'R'. null otherwise.
     */
    @Nullable
    public <T> T getResource(@NonNull Class<T> type,@NonNull String url,@NonNull HttpMethod meth,
                             Map<String,String> header){
        return getResource(type, url, meth, null, header);
    }

    /**
     * this method will make http request using parameters provided.
     * request object will be parsed to json format & Http response will be parsed
       to a POJO.
     * response body must be JSON representation of generic type 'R'.
     * if generic type 'R' is String then content of the response body will
       be returned as it is.
     * @param type class object of expected Http response body.
     * @param url url.
     * @param meth Http request method.
     * @param requestObj body of the Http request.
     * @param header key value pairs of header parameters.
     * @param <T> class type of expected response body.
     * @param <R> class type of HTTP request body.
     * @return return Http response body as object of type 'R'. null otherwise.
     */
    @Nullable
    public <T,R> T getResource(@NonNull Class<T> type,@NonNull String url,@NonNull HttpMethod meth,
                               R requestObj,@NonNull HttpHeaders headers){
        Log.i("RequestHandler","Request: "+url+", Method: "+meth+", Class: "+type.getName());
        try {
            RestTemplate restTemplate = new RestTemplate();

            //add message converters
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            HttpEntity<R> request;
            HttpHeaders httpHeaders =new HttpHeaders();

            requestHeaders = headers;
            //sending request
            request = new HttpEntity<>(requestObj,httpHeaders);
            ResponseEntity<T> response;
            response = restTemplate.exchange(url,meth,request,type);
            this.httpStatus = response.getStatusCode();
            this.responseHeaders = response.getHeaders();
            return response.hasBody()?response.getBody():null;
        } catch (Exception e) {
            Log.e("RequestHandler", "getResource: "+e.getMessage(), e);
        }
        return null;
    }

    /**
     * this method will make http request without body.
     * response body must be JSON representation of generic type 'R'.
     * if generic type 'R' is String then content of the response body will
       be returned as it is.
     * internally it calls {@code getResource(type, url, meth, null, header);}
     * @param type class object of expected Http response body.
     * @param url url.
     * @param meth Http request method.
     * @param header key value pair of header.
     * @param <T> class type of expected response body.
     * @return return Http response body as object of type 'R'. null otherwise.
     */
    @Nullable
    public <T> T getResource(@NonNull Class<T> type,@NonNull String url,@NonNull HttpMethod meth,
                             @NonNull HttpHeaders header){
        return getResource(type, url, meth, null, header);
    }

    /**
     * this method will make http request without body & Header.
     * response body must be JSON representation of generic type 'R'.
     * if generic type 'R' is String then content of the response body will
       be returned as it is.
     * internally it calls {@code getResource(type, url, meth, emptyMap);}
     * @param type class object of expected Http response body.
     * @param url url.
     * @param meth Http request method.
     * @param <T> class type of expected response body.
     * @return return Http response body as object of type 'R'. null otherwise.
     */
    @Nullable
    public <T> T getResource(@NonNull Class<T> type,@NonNull String url,@NonNull HttpMethod meth){
        return getResource(type, url, meth, Collections.<String, String>emptyMap());
    }

    /**
     * this method will make http get request without body & Header.
     * response body must be JSON representation of generic type 'R'.
     * if generic type 'R' is String then content of the response body will
     be returned as it is.
     * internally it calls {@code getResource(type, url, HttpMethod.GET);}
     * @param type class object of expected Http response body.
     * @param url url.
     * @param <T> class type of expected response body.
     * @return return Http response body as object of type 'R'. null otherwise.
     */
    @Nullable
    public <T> T getResource(@NonNull Class<T> type,@NonNull String url){
        return getResource(type, url, HttpMethod.GET);
    }

    /**
     * @return http status code of last response
     */
    @Nullable
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * @return http headers of last response
     */
    @Nullable
    public HttpHeaders getHttpHeaders() {
        return responseHeaders;
    }

}
