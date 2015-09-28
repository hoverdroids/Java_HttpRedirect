package com.hoverdroids.java.httpredirect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
 
public class Main {
	
    public static void main(String args[]) throws Exception {
 
        String url = "http://twitter.com";
        
        System.out.println("Url:" + url);
        getStatus(url);
    }
 
    public static void getStatus(String siteUrl) throws IOException {
    	
        try {
        	//Create a connection to the target url
            URL url = new URL(siteUrl);
        	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        	conn.setReadTimeout(5000);
        	conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
        	conn.addRequestProperty("User-Agent", "Mozilla");
        	conn.addRequestProperty("Referer", "google.com");
        	
        	int status = conn.getResponseCode();
        	System.out.println("Status:" + status);
        	if(status == HttpURLConnection.HTTP_OK){        		
        		getHtml(conn);
        		return;
        	}else if (!(status == HttpURLConnection.HTTP_MOVED_TEMP
    			|| status == HttpURLConnection.HTTP_MOVED_PERM
    				|| status == HttpURLConnection.HTTP_SEE_OTHER)){
        		// normally, 3xx is redirect; anything else means error - for this simple example
    			getHtml(conn);
    			return;
    		}
    		
        	//The request is being redirected...
        	System.out.println("Redirecting ... ");
        	
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
			
			System.out.println("NewUrl:" + newUrl);
			getHtml(conn);
			
        } catch (Exception e) {
            System.out.println("Houston, we had a problem:" + e.getMessage());
        }
    } 
    
    private static void getHtml(HttpURLConnection conn){
    	
    	try {
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(conn.getInputStream()));
			
			String inputLine;
			StringBuffer html = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				html.append(inputLine);
			}
			in.close();
			
			System.out.println("Html:" + html.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}