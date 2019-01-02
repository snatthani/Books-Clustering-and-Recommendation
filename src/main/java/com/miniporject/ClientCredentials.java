package com.miniproject;
public class ClientCredentials {
	static final String API_KEY = "AIzaSyBRsh0Zy2XTEsDQRd-sjG8hfsx-60JHlH0";
	static void errorIfNotSpecified() {
		if (API_KEY.startsWith("Enter ")) {
 			System.err.println(API_KEY);
			System.exit(1);
		}
	}
}
