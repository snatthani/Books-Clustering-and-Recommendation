package com.miniproject;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.Books.Volumes.List;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.util.Scanner;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.UIManager;
import javax.swing.JOptionPane;
import java.awt.Dimension;

class Main{	

	public static void main(final String[] args) throws Exception{
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		int loopvarible = 1;
		String[] queryList = new String[]{"Python","Java","Javascript","Ajax","Google","Apple","Microsoft","Appache","Mathematics","Physics","Chemistry","Biology","Quantum","Machine Learning","Artificial Intelligence","Algorithms","Electronics","Networking","Arduino","Software","Hardware","Aptitude","Economics","Metallurgy","History"};
		Volumes volumes = new Volumes();
		int clusterCount = 25;
		String book_name = new String();
		char ch = 'Y';


		final CassandraConnector client = new CassandraConnector();
		// final String ipAddress = args.length > 0 ? args[0] : "localhost";
		final String ipAddress = "localhost";
		final int port = 9042;
		// final int port = args.length > 1 ? Integer.parseInt(args[1]) : 9042;
		System.out.println("Connecting to IP Address " + ipAddress + ":" + port + "...");
		client.connect(ipAddress, port);
		client.createKeyspace("GoogleBooks","SimpleStrategy",1);
		client.useKeyspace("GoogleBooks");
		client.createTable("Books");

		UIManager.put("OptionPane.minimumSize",new Dimension(200,200)); 
		String str1 = JOptionPane.showInputDialog("Enter book title", "Book Name");
		int clusterid = client.getClusterId("Books", str1);
		System.out.println(clusterid);
		ArrayList<String> titlelist = client.getTitles("Books", clusterid);
		String Description1 = client.getDescriptionByTitle("books",str1);

		StringBuilder book_list = new StringBuilder();
		for(String bookName : titlelist)
		{
			book_list.append(bookName);
			book_list.append("\n");
		}
		String bookList = book_list.toString();



		JOptionPane.showMessageDialog( null,"Description : " + Description1+ "\nRecommended books are as follows:\n" + bookList,"Recommended System", JOptionPane.INFORMATION_MESSAGE);

		while(ch=='y' || ch == 'Y')
		{
			System.out.println("Enter your choice\n1. Mine books data from internet\n2. Cluster books using description\n3. Search related books");
			Scanner in = new Scanner(System.in);
			int choice = in.nextInt();
			switch(choice)
			{
				case 1 : 
							BooksSample booksample = new BooksSample();
							try 
							{
								for(String query : queryList)
								{
									try 
									{
										volumes = booksample.queryGoogleBooks(jsonFactory, query);
									} 
									catch (IOException e) 
									{
										System.err.println(e.getMessage());
									}
									for (Volume volume : volumes.getItems()) 
									{
										Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
										String temp_title = volumeInfo.getTitle();
										java.util.List<String> temp_authors = volumeInfo.getAuthors();
										String temp_description = volumeInfo.getDescription();
										if (temp_title !=null && temp_authors != null && temp_description != null)
										{	
											StringBuilder authors = new StringBuilder();
											for(String s : temp_authors)
											{
												authors.append(s);
												authors.append(", ");
											}
											String temp_author = authors.toString();

											String title = temp_title.replaceAll("\'",",").replaceAll("\"",",");
											String author = temp_author.replaceAll("\'",",").replaceAll("\"",",");
											String description = temp_description.replaceAll("\'",",").replaceAll("\"",",");
											client.insertIntoTable("Books",loopvarible,title,author,description);
											// client.insertIntoTable("Books",loopvarible,title,author,description, loopvarible);
											loopvarible++;		
										}
									}
								}
							}
							catch (Throwable t) 
							{
								t.printStackTrace();
							}
							break;

				case 2 :
						ArrayList<String> ids = new ArrayList<String>();
						ids = client.getTitles("Books");
						//System.out.println(ids);
						//System.out.println(client.getDescriptionByTitle("Books","Law, Computer Science, and Artificial Intelligence"));
						try 
						{
							DocumentCluster clusterMaker = new DocumentCluster(ids, client);
							ArrayList<HashSet<String>> result = clusterMaker.cluster(clusterCount, false);
							System.out.println(result);

							int clusterId = 1;
							for(HashSet<String> object : result)
							{
								// System.out.println(object);
								for(String bookName : object)
								{

									client.updateTable("Books", bookName, clusterId);
								}
								++clusterId;

							}
							System.out.println("Successfully clustered");
						}
						catch (Exception e) 
						{

	             			System.out.println("Exception " + e + " caught");
	             			throw e; // Rethrow so improper temination is obvious
	         			}

					break;
				case 3 :	System.out.println("Enter book name");
							Scanner in_string = new Scanner(System.in);
							book_name = in_string.nextLine();

							int clusterId = client.getClusterId("Books", book_name);
							System.out.println(clusterId);
							ArrayList<String> title_list = client.getTitles("Books", clusterId);

							System.out.println("\nRecommended books are as follows:\n");
							for(String bookName : title_list)
							{
								System.out.println(bookName);
							}


					break;

				default :	System.out.println("Sorry ...! Invalid input");

			}
		System.out.println("Do you want to repeat");
		Scanner in_choice = new Scanner(System.in);
		ch = in_choice.next().charAt(0);


	}


		//int noOfDocs = 847;

         client.close();
	}
}



