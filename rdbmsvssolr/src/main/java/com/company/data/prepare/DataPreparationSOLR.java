package com.company.data.prepare;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

public class DataPreparationSOLR {
	static Logger logger = Logger.getLogger("logger");

	public static void main(String args[]) {
		try {

			SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/performance").build();

			for (int i = 0; i < 100000000; i++) {

				Date startDate = new Date();
				Date endDate = new Date(System.currentTimeMillis() + 31556952000L);
				Date endDate2Years = new Date(System.currentTimeMillis() + 31556952000L + 31556952000L);

				long random = ThreadLocalRandom.current().nextLong(startDate.getTime(), endDate.getTime());
				Date date = new Date(random);

				random = ThreadLocalRandom.current().nextLong(endDate.getTime(), endDate2Years.getTime());
				endDate = new Date(random);

				   SolrInputDocument doc = new SolrInputDocument();
				      doc.addField("id", i);
				      doc.addField("startdate", date.getTime());
				      doc.addField("enddate", endDate.getTime());
				      client.add(doc);
				      if(i % 100 == 0){
				      System.out.println("Value for i is " +i + " committing"); 	  
				      client.commit();
				      
				      }
				    
			    } 	    
			    //conn.commit();

		} catch (Exception e) {
			logger.info("error occured while processing" +e);
			e.printStackTrace();
		}
	}
	
}	

