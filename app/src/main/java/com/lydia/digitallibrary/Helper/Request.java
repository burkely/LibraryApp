package com.lydia.digitallibrary.Helper;

import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;


public class Request implements Callable<Response> {
    private URL url;

    public Request(URL url) {
        this.url = url;
    }

    @Override
    public Response call() throws Exception {
        Log.d("tagtest", url.toString());
        InputStream iostream = url.openStream();
        Log.d("tagtest1", iostream.toString());
        Response resp = new Response(url.openStream());
        Log.d("tagtest2", resp.toString());
        return resp;
    }
}