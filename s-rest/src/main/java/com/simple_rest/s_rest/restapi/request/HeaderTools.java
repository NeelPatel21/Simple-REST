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

import java.util.Map.Entry;

/**
 * this is factory class. It provides facilities to make & modify Http Headers.
 * Created by Neel Patel on 8/3/2017.
 * @author Neel Patel
 * @version 1.0.0
 */

public class HeaderTools {
    private HeaderTools(){}

    public static final Entry<String,String>
            CONTENT_TYPE_JSON=new EntryImp("Content-Type","application/json"),
            CONTENT_TYPE_XML=new EntryImp("Content-Type","application/xml"),
            ACCEPT_JSON=new EntryImp("Accept","application/json"),
            ACCEPT_XML=new EntryImp("Accept","application/xml"),
            ACCEPT_TEXT=new EntryImp("Accept","text/plain");

    /**
     * make a Authorization header using provided token.
     * @param token token
     * @return Http header.
     */
    public static Entry<String,String> makeAuthorizationHeader(String token){
        return new EntryImp("Authorization",token);
    }

    /**
     * implementation of {@code Entry<String,String>}.
     * @author Neel Patel
     */
    public static class EntryImp implements Entry<String,String>{

        private String key;
        private String value;

        public EntryImp(String key,String value){
            this.key=key;
            this.value=value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) {
            return null;
        }
    }

}
