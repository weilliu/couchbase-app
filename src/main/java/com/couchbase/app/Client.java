package com.couchbase.app;

import java.util.Iterator;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.Document;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.Query;
import com.couchbase.client.java.query.ViewQuery;
import com.couchbase.client.java.query.ViewResult;


public class Client
{
    @SuppressWarnings("deprecation")
	public static void main( String[] args )throws Exception
    {
    	//Connect to the cluster and bucket
    	Cluster cluster = new CouchbaseCluster("172.23.107.174");
    	Bucket bucket = cluster.openBucket("Client","").toBlockingObservable().single(); //no password
	
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
    	
    	
    	//insert single line json to the doc id 
    	Document doc = JsonDocument.create("user_first_name", JsonObject.empty().put("firstname", "John"));
    	bucket.upsert(doc).toBlockingObservable().single();


    	//java 8 async method to create document
    	Document doc_java_8 = JsonDocument.create("hello", JsonObject.empty().put("Hello", "Java8"));
    	bucket
    	    .upsert(doc_java_8)
    	    .flatMap(document -> bucket.get("hello"))
    	    .doOnNext(System.out::println)
    	    .flatMap(document -> cluster.disconnect());
    	//    .subscribe();
    	
    	//View document id with java 8 
    	bucket.query(ViewQuery.from("users", "by_firstname")).subscribe(viewResult ->
        System.out.println("java8 "+viewResult.toString()));

    	
    	
    	//View document id
    	Iterator<ViewResult> iterator = bucket.query(ViewQuery.from("users", "by_firstname")).toBlockingObservable().getIterator();
    	while(iterator.hasNext()) {
    	    ViewResult result = iterator.next();
    	    System.out.println("java7 "+result.id());
    	}
    	
    	
    	//close the connection
    	cluster.disconnect();
    	
/*
    	//Use this code after I install N1QL Server
    	System.setProperty("com.couchbase.client.queryEnabled", "true");
    	
    	Cluster cluster_n1ql = new CouchbaseCluster();
    	Bucket bucket_n1ql = cluster_n1ql.openBucket().toBlockingObservable().single();
    	
    	Thread.sleep(2000); 
*/    	
    	/*
    	bucket_n1ql.query(Query.raw("select * from Client")).subscribe(result -> {
    	        System.out.println(result);
    	});
    	*/
    
    	
    	//cluster_n1ql.disconnect();

    	System.out.println(System.getProperty("java.version"));
    }
}
