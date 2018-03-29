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

        //建立连接
        public static MongoClient getMongoClient( ){
            
//        	//无密码链接
//        	try{   
//        	       // 连接到 mongodb 服务
//        	         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//        	       
//        	         // 连接到数据库
//        	         MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");  
//        	       System.out.println("Connect to database successfully");
//        	        
//        	      }catch(Exception e){
//        	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//        	     }
        	
        	
        	
        	//有密码链接
        	MongoClient mongoClient = null;
            try {
            	
            	
            	
            	//连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址  
                //ServerAddress()两个参数分别为 服务器地址 和 端口  
                ServerAddress serverAddress = new ServerAddress("139.199.157.87",27017);  
                List<ServerAddress> addrs = new ArrayList<ServerAddress>();  
                addrs.add(serverAddress);  
                  
                //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码  
                MongoCredential credential = MongoCredential.createCredential("webapp", "webapp", "webapp-dev".toCharArray());  
                List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
                credentials.add(credential);  
                  
                //通过连接认证获取MongoDB连接  
                mongoClient = new MongoClient(addrs,credentials);
                System.out.println("Connect to database successfully");
//                Dataprocessing.jta.append("--Connect to DataBase successfully--\n\r");
//                Dataprocessing.jta.setLineWrap(true);
            	
            	
            	
//                  // 连接到 mongodb 服务
//                mongoClient = new MongoClient(ServerAddress, PORT); 
//                System.out.println("Connect to mongodb successfully");
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                Dataprocessing.jta.append(e.getClass().getName() + ": " + e.getMessage());
                Dataprocessing.jta.setLineWrap(true);
            }
            return mongoClient;
        }

        
        
        //链接到数据库
        public static MongoDatabase getMongoDataBase(MongoClient mongoClient) {  
            MongoDatabase mongoDataBase = null;
            try {  
                if (mongoClient != null) {  
                      // 连接到数据库
                    mongoDataBase = mongoClient.getDatabase(DBName);  
                    System.out.println("Connect to DataBase successfully");
//                    Dataprocessing.jta.append("--Connect to DataBase successfully--");
//                    Dataprocessing.jta.setLineWrap(true);
                } else {  
                    throw new RuntimeException("MongoClient不能够为空");  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }
            return mongoDataBase;
        }  

        
        
        //关闭链接
        public static void closeMongoClient(MongoDatabase mongoDataBase,MongoClient mongoClient ) {  
            if (mongoDataBase != null) {  
                mongoDataBase = null;  
            }  
            if (mongoClient != null) {  
                mongoClient.close();  
            }  
            System.out.println("CloseMongoClient successfully");  

        }
        
        
        //选择集合并上传文档
        public static void choosetable (MongoDatabase mongoDataBase, String time, int pm25){
        	MongoCollection<Document> collection = mongoDataBase.getCollection("pm25");
		    System.out.println("集合 pm25 选择成功");
		    
		  //插入文档  
		    /** 
		     * 1. 创建文档 org.bson.Document 参数为key-value的格式 
		     * 2. 创建文档集合List<Document> 
		     * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document) 
		     * */
		    Document document = new Document("title", "MongoDB").  
		    		append("_id", time).  
		    		append("pm25", pm25);
		    				    		  
		    List<Document> documents = new ArrayList<Document>();  
		    documents.add(document);  
		    collection.insertMany(documents);  
		    System.out.println("文档插入成功");
		    Dataprocessing.jta.append("--文档插入成功--");
            Dataprocessing.jta.setLineWrap(true);
        }
        
        //更新文档
        public static void updata (MongoDatabase mongoDataBase,int pm25)
        {
        	MongoCollection<Document> collection = mongoDataBase.getCollection("pm25");
//		    System.out.println("集合 pm25 选择成功");
        	collection.updateMany(Filters.eq("title", "MongoDB"), new Document("$set",new Document("pm25",pm25)));  
            //检索查看结果  
            FindIterable<Document> findIterable = collection.find();  
            MongoCursor<Document> mongoCursor = findIterable.iterator();  
            while(mongoCursor.hasNext()){  
               System.out.println(mongoCursor.next());
               
            }  
        }
        
        
}