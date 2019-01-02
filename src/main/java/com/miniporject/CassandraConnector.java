package com.miniproject;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import java.util.ArrayList;

public class CassandraConnector {
 
	private Cluster cluster;
	private Session session;
 
	public void connect(String node, Integer port) {
		this.cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
		final Metadata metadata = cluster.getMetadata();
		System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
		for (final Host host : metadata.getAllHosts())
			System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
		session = cluster.connect();
	}

	public void createKeyspace(String keyspaceName, String replicationStrategy, int replicationFactor) {
		StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
								.append(keyspaceName).append(" WITH replication = {")
								.append("'class':'").append(replicationStrategy)
								.append("','replication_factor':").append(replicationFactor)
								.append("};");

		String query = sb.toString();
		session.execute(query);
	}
	public void useKeyspace(String keyspaceName){
		StringBuilder sb = new StringBuilder("USE ")
								.append(keyspaceName)
								.append(";");
		String query = sb.toString();
		session.execute(query);
	
	}

	public void createTable(String tableName) {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
								.append(tableName)
								.append("(")
								.append("id int, ")
								.append("title text PRIMARY KEY,")
								.append("author text,")
								.append("clusterid int,")
								.append("description text);");
		String query = sb.toString();
		session.execute(query);
	}

	public void dropTable(String tableName) {
		StringBuilder sb = new StringBuilder("DROP TABLE ")
								.append(tableName)
								.append(";");
		String query = sb.toString();
		session.execute(query);
	}

	public void insertIntoTable(String tableName, int id, String title, String author, String description) {
		StringBuilder sb = new StringBuilder("INSERT INTO ")
								.append(tableName)
								.append("(id, title, author, description ) ")
								.append("VALUES (")
								.append(id)
								.append(", '")
								.append(title)
								.append("', '")
								.append(author)
								.append("', '")
								.append(description)
								// .append("', ")
								// .append(clusterid)
								.append(" );");	
		String query = sb.toString();
		session.execute(query);
	}

	public ArrayList<String> getTitles(String tableName) {
		ArrayList<String> ids = new ArrayList<String>();
		StringBuilder sb = new StringBuilder("SELECT title FROM ")
								.append(tableName)
								.append(";");
		String query = sb.toString();
		ResultSet result = session.execute(query);
		for(Row row : result)
			ids.add(String.valueOf(row.getString("title")));
		return ids;
	}

	public String getDescriptionByTitle(String tableName,String title) {
		String description ="";
		StringBuilder sb = new StringBuilder("SELECT description FROM ")
								.append(tableName)
								.append(" where title = \'")
								.append(title)
								.append("\' ;");
		String query = sb.toString();
		ResultSet result = session.execute(query);
		for(Row row : result)
			description =  row.getString("description");
		return description;
	}

	public void updateTable(String tableName, String bookName, int cluseterID)
	{
		StringBuilder sb = new StringBuilder("UPDATE ")
								.append(tableName)
								.append(" set clusterid = ")
								.append(cluseterID)
								.append(" where title = \'")
								.append(bookName)
								.append("\' ;");
		String query = sb.toString();
		session.execute(query);
	}

	public int getClusterId(String tableName, String bookName)
	{
		int clusterId  = 0;
		StringBuilder sb = new StringBuilder("SELECT clusterid FROM ")
								.append(tableName)
								.append(" where title = \'")
								.append(bookName)
								.append("\' ;");
		String query = sb.toString();
		ResultSet result = session.execute(query);
		for(Row row : result)
			clusterId = row.getInt(0);
		return clusterId;
	}

	public ArrayList<String> getTitles(String tableName, int clusterId)
	{
		ArrayList<String> titleList = new ArrayList<String>() ;
		StringBuilder sb = new StringBuilder("SELECT title FROM ")
								.append(tableName)
								.append(" where clusterid = ")
								.append(clusterId)
								.append("  allow filtering;");
		String query = sb.toString();
		ResultSet result = session.execute(query);
		for(Row row : result)
			titleList.add(row.getString("title"));
		return titleList;


	}

	public Session getSession() {
		return this.session;
	}

	public void close() {
		session.close();
		cluster.close();
	}
}
