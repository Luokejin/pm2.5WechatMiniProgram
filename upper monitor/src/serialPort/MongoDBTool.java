package serialPort;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential; 
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

public class MongoDBTool {

        static final String DBName = "webapp";
        static final String ServerAddress = "139.199.157.87"; 
        static final int PORT = 27017;

        public MongoDBTool(){
        }

        //��������
        public static MongoClient getMongoClient( ){
            
//        	//����������
//        	try{   
//        	       // ���ӵ� mongodb ����
//        	         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//        	       
//        	         // ���ӵ����ݿ�
//        	         MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");  
//        	       System.out.println("Connect to database successfully");
//        	        
//        	      }catch(Exception e){
//        	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//        	     }
        	
        	
        	
        	//����������
        	MongoClient mongoClient = null;
            try {
            	
            	
            	
            	//���ӵ�MongoDB���� �����Զ�����ӿ����滻��localhost��Ϊ����������IP��ַ  
                //ServerAddress()���������ֱ�Ϊ ��������ַ �� �˿�  
                ServerAddress serverAddress = new ServerAddress("139.199.157.87",27017);  
                List<ServerAddress> addrs = new ArrayList<ServerAddress>();  
                addrs.add(serverAddress);  
                  
                //MongoCredential.createScramSha1Credential()���������ֱ�Ϊ �û��� ���ݿ����� ����  
                MongoCredential credential = MongoCredential.createCredential("webapp", "webapp", "webapp-dev".toCharArray());  
                List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
                credentials.add(credential);  
                  
                //ͨ��������֤��ȡMongoDB����  
                mongoClient = new MongoClient(addrs,credentials);
                System.out.println("Connect to database successfully");
//                Dataprocessing.jta.append("--Connect to DataBase successfully--\n\r");
//                Dataprocessing.jta.setLineWrap(true);
            	
            	
            	
//                  // ���ӵ� mongodb ����
//                mongoClient = new MongoClient(ServerAddress, PORT); 
//                System.out.println("Connect to mongodb successfully");
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                Dataprocessing.jta.append(e.getClass().getName() + ": " + e.getMessage());
                Dataprocessing.jta.setLineWrap(true);
            }
            return mongoClient;
        }

        
        
        //���ӵ����ݿ�
        public static MongoDatabase getMongoDataBase(MongoClient mongoClient) {  
            MongoDatabase mongoDataBase = null;
            try {  
                if (mongoClient != null) {  
                      // ���ӵ����ݿ�
                    mongoDataBase = mongoClient.getDatabase(DBName);  
                    System.out.println("Connect to DataBase successfully");
//                    Dataprocessing.jta.append("--Connect to DataBase successfully--");
//                    Dataprocessing.jta.setLineWrap(true);
                } else {  
                    throw new RuntimeException("MongoClient���ܹ�Ϊ��");  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }
            return mongoDataBase;
        }  

        
        
        //�ر�����
        public static void closeMongoClient(MongoDatabase mongoDataBase,MongoClient mongoClient ) {  
            if (mongoDataBase != null) {  
                mongoDataBase = null;  
            }  
            if (mongoClient != null) {  
                mongoClient.close();  
            }  
            System.out.println("CloseMongoClient successfully");  

        }
        
        
        //ѡ�񼯺ϲ��ϴ��ĵ�
        public static void choosetable (MongoDatabase mongoDataBase, String time, int pm25){
        	MongoCollection<Document> collection = mongoDataBase.getCollection("pm25");
		    System.out.println("���� pm25 ѡ��ɹ�");
		    
		  //�����ĵ�  
		    /** 
		     * 1. �����ĵ� org.bson.Document ����Ϊkey-value�ĸ�ʽ 
		     * 2. �����ĵ�����List<Document> 
		     * 3. ���ĵ����ϲ������ݿ⼯���� mongoCollection.insertMany(List<Document>) ���뵥���ĵ������� mongoCollection.insertOne(Document) 
		     * */
		    Document document = new Document("title", "MongoDB").  
		    		append("_id", time).  
		    		append("pm25", pm25);
		    				    		  
		    List<Document> documents = new ArrayList<Document>();  
		    documents.add(document);  
		    collection.insertMany(documents);  
		    System.out.println("�ĵ�����ɹ�");
		    Dataprocessing.jta.append("--�ĵ�����ɹ�--");
            Dataprocessing.jta.setLineWrap(true);
        }
        
        //�����ĵ�
        public static void updata (MongoDatabase mongoDataBase,int pm25)
        {
        	MongoCollection<Document> collection = mongoDataBase.getCollection("pm25");
//		    System.out.println("���� pm25 ѡ��ɹ�");
        	collection.updateMany(Filters.eq("title", "MongoDB"), new Document("$set",new Document("pm25",pm25)));  
            //�����鿴���  
            FindIterable<Document> findIterable = collection.find();  
            MongoCursor<Document> mongoCursor = findIterable.iterator();  
            while(mongoCursor.hasNext()){  
               System.out.println(mongoCursor.next());
               
            }  
        }
        
        
}