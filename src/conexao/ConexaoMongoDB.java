package conexao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.*;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;
import org.bson.types.ObjectId;

public class ConexaoMongoDB {
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Document> collection;

	public ConexaoMongoDB() {
		mongoClient = new MongoClient("localhost", 27017);
		database = mongoClient.getDatabase("banco_aula_sauer");

		/*MongoClientURI uri = new MongoClientURI(
				"mongodb://root:root@cluster0-mbdma.gcp.mongodb.net/banco_aula_sauer?retryWrites=true&w=majority");
		//'mongodb+srv://oministack:oministack@cluster0-fdi0n.mongodb.net/semana09?retryWrites=true&w=majority'

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("banco_aula_sauer");*/
		collection = database.getCollection("cep");

	}

	public void insereCep(String cep, String logradouro, String bairro, String cod_estado) {
		Document doc = new Document("CEP", cep).append("Logradouro", logradouro).append("Bairro", bairro)
				.append("Cod_Estado", cod_estado);
		collection.insertOne(doc);
	}

	public void exibeCeps() {
		MongoCursor<Document> cursor = collection.find().iterator();
		try {

			while (cursor.hasNext()) {
				Document atual = cursor.next();

				System.out.println(atual.get("_id"));
				System.out.println(atual.get("cep"));
				System.out.println(atual.get("logradouro"));
				System.out.println(atual.get("bairro"));
				System.out.println(atual.get("cod_estado"));
			}
		} finally {
			cursor.close();
		}
	}

	public void exibeCep(String cep) {
		MongoCursor<Document> cursor = collection.find(eq("cep", cep)).iterator();
		try {
			while (cursor.hasNext()) {
				Document atual = cursor.next();

				System.out.println(atual.get("_id"));
				System.out.println(atual.get("cep"));
				System.out.println(atual.get("logradouro"));
				System.out.println(atual.get("bairro"));
				System.out.println(atual.get("cod_estado"));
			}
		} finally {
			cursor.close();
		}
	}

}
