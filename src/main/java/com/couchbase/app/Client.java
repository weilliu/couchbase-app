package com.couchbase.app;

/*

import java.util.Iterator;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import com.couchbase.client.core.message.ResponseStatus;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.Document;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.Query;
import com.couchbase.client.java.view.ViewQuery;
//import com.couchbase.client.java.query.Stale;
//import com.couchbase.client.java.query.ViewQuery;
//import com.couchbase.client.java.query.ViewResult;



public class Client
{
    @SuppressWarnings("deprecation")
	public static void main( String[] args )throws Exception
    {
    	//Connect to the cluster and bucket
    	Cluster cluster = CouchbaseCluster.create();
    	Bucket bucket = cluster.openBucket("Client","").toBlockingObservable().single(); //no password
	    	
    	
    	//java 8 async method to create document
    	Document doc_java_8 = JsonDocument.create("hello", JsonObject.empty().put("Hello", "Java8"));
    	bucket
    	    .upsert(doc_java_8)
    	    .flatMap(document -> bucket.get("hello"))
    	    .doOnNext(System.out::println)
    	    .flatMap(document -> cluster.disconnect()); //.subscribe();
    	
    	
    	//get a document
    	JsonDocument doc_100 = bucket.get("hello").toBlockingObservable().single();
    	System.out.println(doc_100);
    	
    
    	
    	
    	//insert json array to a document id
    	JsonArray json_array = JsonArray.empty();

    	json_array.add(JsonObject.empty().put("firstname", "John"));
    	json_array.add(JsonObject.empty().put("lastname", "Doe"));
    	json_array.add(JsonObject.empty().put("firstname", "Matt"));
    	json_array.add(JsonObject.empty().put("firstname", "Ingenthron"));
 	
    	Document doc_array = JsonDocument.create("user_array", JsonObject.empty().put("array", json_array));
    	bucket.upsert(doc_array).toBlockingObservable().single();
    	
    	
    	
    	//insert multi-lines json object to a document id
    	JsonObject users = JsonObject.empty();
    	users.put("firstname", "Michael");
    	users.put("lastname", "Nits");
    	bucket.upsert(JsonDocument.create("user_full_name", users));
  
    	System.out.println(users.getString("lastname"));
//    	assertEquals(user.getString("firstname"), "Michael");
//    	assertEquals(user.getString("lastname"), "Nits");
    	
    	

    	
    	
    	
//    	//insert single line json to the doc id 
//    	Document doc = JsonDocument.create("user_first_name", JsonObject.empty().put("firstname", "John2"));
//    	bucket.upsert(doc).toBlockingObservable().single();
//
//
//    	
//    	//View document id with java 8 
//    	bucket.query(ViewQuery.from("users", "by_firstname").stale(Stale.FALSE)).subscribe(viewResult ->
//        System.out.println("java8 "+viewResult.toString()));
    	
    	
    	Document doc = JsonDocument.create("user_first_name", JsonObject.empty().put("firstname", "John"));
    	bucket.upsert(doc).toBlockingObservable().single();
    	
    	//insert single line json to the doc id 
    	doc = JsonDocument.create("user_first_name", JsonObject.empty().put("firstname", "John2"));
    	bucket.upsert(doc).toBlockingObservable().single();
    	
    	Observable<Document> test = null;
    	test = bucket.remove(doc);
    	
//    	//look for no change with doc still in cache
////    	bucket.query(ViewQuery.from("users", "by_firstname").stale(Stale.FALSE)).subscribe(viewResult ->
////        System.out.println("java8 "+viewResult.toString()));
//    	Observable<ViewResult> query = bucket.query(ViewQuery.from("users", "by_firstname").stale(Stale.FALSE).limit(10).reduce());
//    	query.subscribe(result -> System.out.println(result.toString()));
//    	
//    	//View document id
//    	Iterator<ViewResult> iterator = bucket.query(ViewQuery.from("users", "by_firstname").stale(Stale.FALSE)).toBlockingObservable().getIterator();
//    	while(iterator.hasNext()) {
//    	    ViewResult result = iterator.next();
//    	    System.out.println("java7 "+"key:"+result.key()+" value: "+result.value());
//    	}
//    	
    	
//    	ViewQuery query = ViewQuery.from("users", "by_firstname");
//    	
//    	System.out.println("Query"+query.toString());
//    	
    	
    	
    	
    	
    	
    	 JsonObject user = JsonObject.empty();
    	 user.put("First Name", "Subhashni");
    	 bucket.upsert(JsonDocument.create("sample", user));
    	 Observable<JsonDocument> docObs = bucket.get("sample");
    	 //System.out.println("Observable sequence" + docObs);
    	 OperationCollector opsColl = new OperationCollector();
    	 GetObserver<JsonDocument> getObs = new GetObserver<JsonDocument>(opsColl);
    	 try {
    	 Thread.sleep(1);
    	 } catch (InterruptedException ex) {
    	 ex.printStackTrace();
    	 }
    	 System.out.println("Ops count " + opsColl.getCount() + "\nErrors count " + opsColl.getErrors());
    	 //getObs.onNext(docObs.subscribe(new Subscriber<Integer>()));
    	 
    	 docObs.subscribe(getObs);


    	 
    	 Observable
    	    .just(1, 2, 3)
    	    .subscribe(new Subscriber<Integer>() {
    	        @Override
    	        public void onCompleted() {
    	            System.out.println("Completed Observable.");
    	        }

    	        @Override
    	        public void onError(Throwable throwable) {
    	            System.err.println("Whoops: " + throwable.getMessage());
    	        }

    	        @Override
    	        public void onNext(Integer integer) {
    	            System.out.println("Got: " + integer);
    	        }
    	    });
    	 
    	

 
    	//close the connection
    	cluster.disconnect();
   	

//    	//Use this code after I install N1QL Server
//    	System.setProperty("com.couchbase.client.queryEnabled", "true");
//    	
//    	Cluster cluster_n1ql = new CouchbaseCluster();
//    	Bucket bucket_n1ql = cluster_n1ql.openBucket().toBlockingObservable().single();
//    	
//    	Thread.sleep(2000); 
    	
    	
//    	bucket_n1ql.query(Query.raw("select * from Client")).subscribe(result -> {
//    	        System.out.println(result);
//    	});
    	
    
    	
    	//cluster_n1ql.disconnect();

    	System.out.println(System.getProperty("java.version"));
    	
    	System.exit(0);
    }
    


}


*/



//1.4 API

//import com.couchbase.client.CouchbaseClient;
//import com.couchbase.client.CouchbaseConnectionFactory;
//import com.couchbase.client.CouchbaseConnectionFactoryBuilder;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.s;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;





//import com.couchbase.client.CouchbaseClient;
//import com.couchbase.client.CouchbaseConnectionFactory;
//import com.couchbase.client.CouchbaseConnectionFactoryBuilder;
//import java.util.logging.Logger;
import com.jcraft.jsch.*;

import rx.Observable;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;



















import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Arrays;

import org.slf4j.*;

import rx.Observable;

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.CouchbaseAsyncCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.PreparedQuery;
import com.couchbase.client.java.query.Query;
import com.couchbase.client.java.query.QueryPlan;
import com.couchbase.client.java.query.QueryResult;
import com.couchbase.client.java.query.QueryRow;
import com.couchbase.client.java.transcoder.JsonStringTranscoder;
import com.couchbase.client.java.transcoder.Transcoder;
import com.couchbase.client.java.view.AsyncViewResult;
import com.couchbase.client.java.view.AsyncViewRow;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.query.Statement;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.*;
import com.couchbase.client.core.message.query.GenericQueryRequest;



public class Client {
	

	// ssl testing	
	/*
	public static void main(String[] args){


        String hostname = "10.3.3.238";
        String username = "root";
        String password2 = "couchbase";
        String copyFrom = "/opt/couchbase/var/lib/couchbase/config/ssl-cert-key.pem-ca";
        String copyTo = System.getProperty("user.dir");
        KeyStore keystore = null;

        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(username, hostname, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password2);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;

            sftpChannel.get(copyFrom, copyTo);
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();  
        } catch (SftpException e) {
            e.printStackTrace();
        }

		
    	//create keystore 
    	try{	
			//load the current keystore content
    		//String client_cacert = System.getProperty("user.home") + File.separator + "cacerts";
			File keystoreFile = new File("cacerts");
			FileInputStream is = new FileInputStream(keystoreFile);
			keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			
			char[] password = "couchbase".toCharArray();
			keystore.load(is, password);
			
			//import the ssl server certificate
			String alias = "ssl_situational";
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream certstream = fullStream ("ssl-cert-key.pem-ca");
			Certificate certs = cf.generateCertificate(certstream);
			// Add the certificate
			keystore.setCertificateEntry(alias, certs);
			// Save the new keystore contents
			FileOutputStream out = new FileOutputStream(keystoreFile);
			keystore.store(out, password);
			out.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
    
		
    	CouchbaseEnvironment env = DefaultCouchbaseEnvironment
    			.builder()
    			.sslEnabled(true)
    			.sslKeystoreFile("cacerts")
    			.sslKeystorePassword("couchbase")
    			.build();
    	Cluster cluster = CouchbaseCluster.create(env,"10.3.3.238", "10.3.3.241", "10.3.5.95", "10.3.121.191");
		//Cluster cluster = CouchbaseCluster.create("10.3.3.238", "10.3.3.241");//, "10.3.5.95", "10.3.121.191");
		Bucket bucket = cluster.openBucket("default");
		bucket.upsert(JsonDocument.create("foo", JsonObject.create().put("foo", "bar")));
		long sleepMillis = 1000;
		//while (true) {
		  try {
          	JsonDocument response = bucket.get("foo");
              System.out.println(response);
              Thread.sleep(sleepMillis);
          } catch (Exception e) {
              e.printStackTrace();
          }
		  finally{
			  try {
				  	System.out.println("delete entry");
					keystore.deleteEntry("ssl_situational");
					
					CouchbaseEnvironment env2 = DefaultCouchbaseEnvironment
			    			.builder()
			    			.sslEnabled(true)
			    			.sslKeystoreFile("cacerts")
			    			.sslKeystorePassword("couchbase")
			    			.build();
					Cluster cluster2 = CouchbaseCluster.create(env,"10.3.3.238", "10.3.3.241", "10.3.5.95", "10.3.121.191");
					//Cluster cluster = CouchbaseCluster.create("10.3.3.238", "10.3.3.241");//, "10.3.5.95", "10.3.121.191");
					Bucket bucket2 = cluster.openBucket("default");
					bucket2.upsert(JsonDocument.create("foo2", JsonObject.create().put("foo", "bar")));
					  try {
				          	JsonDocument response = bucket.get("foo2");
				              System.out.println(response);
				              Thread.sleep(sleepMillis);
				          } catch (Exception e) {
				              e.printStackTrace();
				          }
					
					//make new cacert with expired date
					Process p;
					p = Runtime.getRuntime().exec("echo couchbase |keytool -delete -noprompt -alias ssl_situational -keystore cacerts ");
					if (p.waitFor() == 0){
						System.out.println("cacert alias delete") ;
						CouchbaseEnvironment env3 = DefaultCouchbaseEnvironment
				    			.builder()
				    			.sslEnabled(true)
				    			.sslKeystoreFile("cacerts")
				    			.sslKeystorePassword("couchbase")
				    			.build();
						Cluster cluster3 = CouchbaseCluster.create(env,"10.3.3.238", "10.3.3.241", "10.3.5.95", "10.3.121.191");
						//Cluster cluster = CouchbaseCluster.create("10.3.3.238", "10.3.3.241");//, "10.3.5.95", "10.3.121.191");
						Bucket bucket3 = cluster.openBucket("default");
						bucket3.upsert(JsonDocument.create("foo3", JsonObject.create().put("foo", "bar")));
						  try {
					          	JsonDocument response = bucket.get("foo3");
					              System.out.println(response);
					              Thread.sleep(sleepMillis);
					          } catch (Exception e) {
					              e.printStackTrace();
					          }
					}
					else{
						System.err.println("cacert delete fail");
					}
				} catch (KeyStoreException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		  }
		  
	
		//}
		
	}
	
	public static InputStream fullStream(String fname) throws IOException {
		FileInputStream fis = new FileInputStream(fname);
		DataInputStream dis = new DataInputStream(fis);
		byte[] bytes = new byte[dis.available()];
		dis.readFully(bytes);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		dis.close();
		return bais;
	}
*/

    public static long total = 0;
    public static long idle = 0;
    
    private static void getCpuT()  {
        
        BufferedReader reader;
        String[] result = new String [10];
        long total_temp = 0;
        long idle_temp = 0;

        long val = 0;
        
        try {
            reader = new BufferedReader(new FileReader("/proc/stat"));
            String line = reader.readLine();
           
            while (line!=null) {
                if (line.startsWith("cpu")){
                    result = line.split("\\s+");    
                    break;
                }
            }
            
            
            for (int i =1; i < result.length; i++){
                val = Long.parseLong(result[i]); 
                total_temp += val;
                
                if (i == 4){
                    idle_temp =val;
                }
            }
//            cpuUser = Integer.parseInt(result[1]);
//            cpuSystem = Integer.parseInt(result[3]);
               
//            System.out.println("cpuUser:"+cpuUser+"   "+"cpuSystem:"+cpuSystem);
            
            reader.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            idle = idle_temp;
            total = total_temp;
        }

        
        
      
        //return cpuUser + cpuSystem;
    }
    
    
    private static void getMemT()
    {
        int mb = 1024*1024;
        
        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();
         
        System.out.println("##### Heap utilization statistics [MB] #####");
         
        //Print used memory
        System.out.println("Used Memory:"
            + (runtime.totalMemory() - runtime.freeMemory()) / mb);
 
        //Print free memory
        System.out.println("Free Memory:"
            + runtime.freeMemory() / mb);
         
        //Print total available memory
        System.out.println("Total Memory:" + runtime.totalMemory() / mb);
 
        //Print Maximum available memory
        System.out.println("Max Memory:" + runtime.maxMemory() / mb);
    }
    
    public static void sendstats(){
        long idle0, total0 = 0;
        long idle1, total1 = 0;
        float idleTicks = 0;
        float totalTicks = 0;
        float cpuUsage = 0;
        
        getCpuT();
        
        idle0 = idle;
        total0 = total;
        System.out.println("total:"+total0+"   "+"idle:"+idle0); 
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        getCpuT();
        
        idle1 = idle;
        total1 = total;
        System.out.println("total:"+total1+"   "+"idle:"+idle1); 
        
        idleTicks = idle1 - idle0;
        totalTicks = total1 - total0;
        cpuUsage = (100 * (totalTicks - idleTicks) / totalTicks);
        
        System.out.println(cpuUsage);
        
    }
    

    public static void main(String[] args) throws InterruptedException {
          
        getMemT();
        sendstats();
        
            
        
        
        //n1ql
        /*     
        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                .queryEnabled(true)
                .build();
        Cluster cluster = CouchbaseCluster.create(env, "172.23.107.175");
        Bucket bucket = cluster.openBucket("default", ""); 
  
        JsonObject testDataBlog = JsonObject.create().put("type", "blog").put("data", "blablabla");
        JsonObject testDataComment = JsonObject.create().put("type", "comment").put("data", "ho hey");
        bucket.upsert(JsonDocument.create("testDp4Blog", testDataBlog));
        bucket.upsert(JsonDocument.create("testDp4Comment", testDataComment)); 
        
        Query createIndex = Query.simple("CREATE PRIMARY INDEX ON default");
        QueryResult queryResult = bucket.query(createIndex); 
        
        
        if (!queryResult.finalSuccess()) {
            for (JsonObject error : queryResult.errors()) {
            System.err.println(error);
            }
          } 
        
        Statement withPlaceholders = select("*").from("default").where(x("type").eq(x("$1")));
        //SimpleQuery simple = Query.simple("Select * from default where META(default).id = 'N1QLFillerSeed'");
        SimpleQuery simple = Query.simple("select * from default;");
        
        String select_column = "*";
        String from_bucket = "default";
        String where_column = "META(default).id";
        String eq = "N1QLFillerSeed";
        
        System.out.println(select_column +":"+ from_bucket +":"+where_column+":" + eq);
        
       //ParametrizedQuery query = Query.parametrized(withPlaceholders, JsonArray.create().add("blog"));

      //System.out.println("Query status: " + (result.finalSuccess() ? "OK" : "ERRORS")); 
        
//        QueryPlan byTypePlan = bucket.prepare(select("*").from("default").where(x("META(default).id").eq(x("$1"))));
//        PreparedQuery byType = Query.prepared(byTypePlan, JsonArray.from("N1QLFillerSeed"));
        
        QueryPlan byTypePlan = bucket.prepare(select(select_column).from(from_bucket).where(x(where_column).eq(x("$1"))));
        PreparedQuery byType = Query.prepared(byTypePlan, JsonArray.from(eq));
        
        
        

        
        while (true){
        	
        	//QueryResult query = bucket.query("select * from default where META(default).id = 'ViewFillerSeed'");
            
        	//System.out.println(query.allRows());
        	
        	QueryResult queryResult2 = bucket.query(simple);
            //QueryResult queryResult2 = bucket.query(byType);
            
          System.out.println(queryResult2.allRows());
            for (QueryRow queryRow : queryResult2.allRows()) {
                System.out.println("here:"+queryRow.value());
            } 
            
            Iterator<QueryRow> rows = queryResult2.rows();
            int count =0;
            while(rows.hasNext()) {
                System.out.println(count+""+rows.next());
                count++;
            }
        	Thread.sleep(1000);
        	
        	
        }
        
        
        //        Bucket bucket = cluster.openBucket("beer-sample");
//
//
//        while (true){
//          QueryResult query = bucket.query("select META(beer-sample).id, abv from beer-sample where abv > 99");
//          System.out.println(query.allRows());
//          
//          Thread.sleep(1000);
//        }

 

*/
        
        
        
        
        
//JSON parser        
/*        
        try {
            // read the json file
            FileReader reader = new FileReader("/Users/wei-li/repo/couchbase-app/n1ql.json");

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            // get a String from the JSON object
            String firstName = (String) jsonObject.get("firstname");
            System.out.println("The first name is: " + firstName);

            // get a number from the JSON object
            long id =  (long) jsonObject.get("id");
            System.out.println("The id is: " + id);

            // get an array from the JSON object
            JSONArray lang= (JSONArray) jsonObject.get("query");
            
            // take the elements of the json array
            for(int i=0; i<lang.size(); i++){
                System.out.println("The " + i + " element of the array: "+lang.get(i));
            }
            Iterator i = lang.iterator();

            // take each value from the json array separately
            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                System.out.println("query "+ innerObj.get("SELECT") );
            }
            // handle a structure into the json object
            JSONObject structure = (JSONObject) jsonObject.get("index");
            System.out.println("primary index syntax: " + structure.get("primary"));

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
*/
    
        
    
        
        
        
//      while (true){   
//      System.out.println("=========get the runtime object associated with the current Java application============");
//      Runtime runtime = Runtime.getRuntime();
//      long freeMemory = runtime.freeMemory();
//      System.out.println("Free memory in JVM (bytes): " + freeMemory);
//      long maxMemory = runtime.maxMemory();
//      System.out.println("Max memory in JVM (bytes): " + maxMemory);
//      long totalMemory = runtime.totalMemory();
//      System.out.println("Total memory in JVM (bytes): " + totalMemory);
//      
//      Thread.sleep(1000);
//  }
        
    }
	
	
//restart all nodes testing 
//    public static void main(String[] args) {
//        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
//                .kvTimeout(100)
//                .bootstrapCarrierEnabled(false)
//                .bootstrapHttpEnabled(true)
//                .build();
//        Cluster cluster = CouchbaseCluster.create(env, "204.151.121.16", "204.151.121.157", "204.151.121.158","204.151.121.159");
//        Bucket bucket = cluster.openBucket("default");
//        bucket.upsert(JsonDocument.create("foo", JsonObject.create().put("foo", "bar")));
//        long sleepMillis = 1000;
//        while (true) {
//            try {
//            	JsonDocument response = bucket.get("foo");
//                System.out.println(response);
//                Thread.sleep(sleepMillis);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

   
    
    
    
    
    
    
//        public static void main(String[] args) throws Exception {
//        	ebay();
//                
//        }
//        
//        public static void ebay() throws IOException, URISyntaxException, InterruptedException, ExecutionException{
//        	List<URI> baseList = new ArrayList<URI>();
//        	baseList.add(new URI("http://ec2-54-160-0-253.compute-1.amazonaws.com:8091/pools"));
////       	baseList.add(new URI("http://10.3.5.95:8091/pools"));
////            baseList.add(new URI("http://172.23.107.174:8091/pools"));
////            baseList.add(new URI("http://172.23.107.175:8091/pools"));
////            baseList.add(new URI("http://172.23.107.176:8091/pools"));
////            baseList.add(new URI("http://172.23.107.177:8091/pools"));
//            String bucketName = "default";
//            String password = "";
//            //CouchbaseClient client = new CouchbaseClient(baseList, bucketName, password);
//
//            CouchbaseConnectionFactoryBuilder builder = new CouchbaseConnectionFactoryBuilder();
//            builder.setOpTimeout(50);
//            CouchbaseConnectionFactory cf = builder.buildCouchbaseConnection(baseList, bucketName, password);
//            
//            CouchbaseClient client = new CouchbaseClient(cf);
//            String key = "test";
//            String value = "{\"foo\": \"bar\"}";
//            client.set(key, value).get();
//                            
//            long sleepMillis = 1000;
//            while(true) {
//                    try {
//                            System.out.println(client.get(key));
//                            Thread.sleep(sleepMillis);
//                    } catch (Exception e) {
//                            e.printStackTrace();
//                    }
//            }
//
//            //client.shutdown();
//        }
       
        
}
