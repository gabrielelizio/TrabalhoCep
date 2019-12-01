package conexao;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import org.bson.Document;

public class ConexaoMongoDB {
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Document> collection;

	public ConexaoMongoDB() {
		mongoClient = new MongoClient("localhost", 27017);
		database = mongoClient.getDatabase("banco_aula_sauer");
		collection = database.getCollection("cep");

	}

	public void insereCep(String cep, String logradouro, String bairro, String cod_estado) {
		Document doc = new Document("CEP", cep).append("Logradouro", logradouro).append("Bairro", bairro)
				.append("Cod_Estado", cod_estado);
		collection.insertOne(doc);
		
		
	}

	

}
