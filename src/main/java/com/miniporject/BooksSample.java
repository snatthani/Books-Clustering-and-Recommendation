package com.miniproject;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.Books.Volumes.List;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.text.NumberFormat;
import java.lang.Long;

public class BooksSample {
	private static final String APPLICATION_NAME = "";

	private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
	private static final NumberFormat PERCENT_FORMATTER = NumberFormat.getPercentInstance();

	public static Volumes queryGoogleBooks(JsonFactory jsonFactory, String query) throws Exception {
		ClientCredentials.errorIfNotSpecified();
		Long maxSize = new Long(40);
		final Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
								.setApplicationName(APPLICATION_NAME)
								.setGoogleClientRequestInitializer(new BooksRequestInitializer(ClientCredentials.API_KEY))
								.build();

		System.out.println("Query: [" + query + "]");
		List volumesList = books.volumes().list(query);
		volumesList.setFilter("ebooks");
		volumesList.setMaxResults(maxSize);
		
		Volumes volumes = volumesList.execute();
		if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
			System.out.println("No matches found.");
		}
		return volumes;
	}
}
