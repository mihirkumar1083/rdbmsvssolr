package com.company.data.compare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class DataPreparationMYSQLVSSOLR {

	static Logger logger = Logger.getLogger("logger");

	public static void main(String args[]) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = null;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sourcedb", "clouduser", "password");

			Statement statement = conn.createStatement();

			ResultSet resultset = statement.executeQuery("select min(startdate) from performance");

			long startdate = 0;

			if (resultset.next()) {
				startdate = resultset.getLong(1);
			}

			resultset.close();
			statement.close();

			statement = conn.createStatement();

			resultset = statement.executeQuery("select max(enddate) from performance");
			
			
			long enddate = 0;;
			
			if(resultset.next()){
			     enddate = resultset.getLong(1);	
			}
			
			resultset.close();
			statement.close();
			
			statement = conn.createStatement();

			String sql = "select * from performance where startdate >= " + startdate + " and enddate <="
					+ enddate + "";

			ResultSet rs = statement.executeQuery(sql);
			long startTimeMYSQLScan = System.currentTimeMillis();

			int x = 0;
			while (rs.next()) {
				//System.out.println("Continue MYSQL Loop");
				x++;
			}

			rs.close();
			statement.close();

			System.out.println("Total time taken for MYSQL retrive " + (System.currentTimeMillis() - startTimeMYSQLScan)
					+ " Total Records " + x);

			SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/performance").build();

			SolrQuery query = new SolrQuery();
			
			query.setQuery(
					"startdate:[" + startdate + " TO *] AND enddate:[* TO " + enddate + "]");
			// query.setFields("id","price","merchant","cat","store");
			// query.setStart(0);
			
			query.setStart(0);
			query.setRows(200000);
		
			// query.set("defType", "edismax");
			long solarScanStartTime = System.currentTimeMillis();
			QueryResponse response = client.query(query);
			x = 0;
			SolrDocumentList results = response.getResults();
			for (int i = 0; i < results.size(); ++i) {
				//System.out.println(results.get(i));
				x++;
			}

			System.out.println("Time taken for solar scan " + (System.currentTimeMillis() - solarScanStartTime)
					+ " Total records " + x);

		} catch (Exception e) {
			logger.info("error occured while processing" + e);
			e.printStackTrace();
		}
	}

}
