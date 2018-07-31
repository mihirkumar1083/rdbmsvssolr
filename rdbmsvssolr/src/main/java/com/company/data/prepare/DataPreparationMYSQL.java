package com.company.data.prepare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class DataPreparationMYSQL {

	static Logger logger = Logger.getLogger("logger");

	public static void main(String args[]) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = null;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sourcedb", "clouduser", "password");
			
			
			// 1 billion
			for (int i = 0; i < 100000000; i++) {
				
				Date startDate = new Date();
				Date endDate = new Date(System.currentTimeMillis() + 31556952000L);
				Date endDate2Years = new Date(System.currentTimeMillis() + 31556952000L + 31556952000L);
				
				long random = ThreadLocalRandom.current().nextLong(startDate.getTime(), endDate.getTime());
			    Date date = new Date(random);
			    
			    random = ThreadLocalRandom.current().nextLong(endDate.getTime(), endDate2Years.getTime());
			    endDate = new Date(random);
			    
				Statement statement = conn.createStatement();
				
				String sql = "insert into performance values ('"+i+"','"+date.getTime()+"','"+endDate.getTime()+"')";
				
				System.out.println(sql);
				
				Random r = new Random();
				//int Low = 10;
				//int High = 100;
				//int result = r.nextInt(High-Low) + Low;
						
			    statement.execute(sql);
			    statement.close();
			    //conn.commit();
			}

		} catch (Exception e) {
			logger.info("error occured while processing" +e);
			e.printStackTrace();
		}
	}

}
