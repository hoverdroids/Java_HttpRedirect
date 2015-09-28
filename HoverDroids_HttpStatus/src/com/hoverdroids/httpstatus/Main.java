package com.hoverdroids.httpstatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
 
public class Main {
	
    public static void main(String args[]) throws Exception {
 
        String[] hostList = {
        		"http://crunchify.com",
        		"http://yahoo.com",
                "http://www.ebay.com",
                "http://google.com",
                "http://www.example.co",
                "http://paypal.com",
                "http://bing.com/", "http://techcrunch.com/",
                "http://mashable.com/", "http://thenextweb.com/",
                "http://www.wordpress.com/", "http://wordpress.org/",
                "http://example.com/", "http://sjsu.edu/",
                "http://ebay.co.uk/", "http://google.co.uk/",
                "http://www.wikipedia.org/",
                "http://en.wikipedia.org/wiki/Main_Page",
                "http://api.edmunds.com/api/vehicle/v2/rolls-royce/models?fmt=json&api_key=38a85swhgh57f4smha9vevn8",
                "http://www.edmunds.com/"};
 
        for (int i = 0; i < hostList.length; i++) {
 
            String url = hostList[i];
            String status = getStatus(url);
 
            System.out.println(url + "\t\tStatus:" + status);
        } 
    }
 
    public static String getStatus(String siteUrl) throws IOException {
    	
        String result = "";
        try {
        	//Create a connection to the target url
            URL url = new URL(siteUrl);
        	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        	conn.setReadTimeout(5000);
        	conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
        	conn.addRequestProperty("User-Agent", "Mozilla");
        	conn.addRequestProperty("Referer", "google.com");
        	
        	int status = conn.getResponseCode();
        	if(status == HttpURLConnection.HTTP_OK){
        		return "Green Code:" + status;
        		
        	}else if (!(status == HttpURLConnection.HTTP_MOVED_TEMP
    			|| status == HttpURLConnection.HTTP_MOVED_PERM
    				|| status == HttpURLConnection.HTTP_SEE_OTHER)){
        		// normally, 3xx is redirect; anything else means error - for this simple example
    			return "Red Code:" + status;
    		}
    		
        	//The request is being redirected...
        	result = "Redirect Code:" + String.valueOf(status);
        	
			// get redirect url from "location" header field
			String newUrl = conn.getHeaderField("Location");

			// get the cookie if need, for login
			String cookies = conn.getHeaderField("Set-Cookie");

			// open the new connection again
			conn = (HttpURLConnection) new URL(newUrl).openConnection();
			conn.setRequestProperty("Cookie", cookies);
			conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			conn.addRequestProperty("User-Agent", "Mozilla");
			conn.addRequestProperty("Referer", "google.com");
			
			status = conn.getResponseCode();
			
			result += ",newUrl:" + newUrl 
					+ ",status:" + (status == HttpURLConnection.HTTP_OK ? "Green" : "Red")
					+ ",code:" + status;
    	
        } catch (Exception e) {
            result = "RED";
        }
        return result;
    } 
}