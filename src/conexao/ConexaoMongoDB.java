package conexao;

import com.mongodb.MongoClient;
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
		collection = database.getCollection("cep");
	}


	public void insereAluno(String cep, String logradouro, String bairro, String cod_estado)
	{
		Document doc = new Document("CEP",cep).append("Logradouro",logradouro).append("Bairro",bairro).append("Cod_Estado",cod_estado);
		collection.insertOne(doc);
	}


	public void exibeAlunos()
	{
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
			cursor.close ();
		}
	}


	public void exibeAluno(String cep){
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


	public void exibeAlunosData(String nome)
	{
		MongoCursor<Document> cursor = collection.find(gte("dataInicio", 2011)).iterator();
		try {
			while (cursor.hasNext()) {
				Document atual = cursor.next();
				System.out.println(atual.get("nome"));
				System.out.println(atual.get("curso"));
				System.out.println(atual.get("anoInicio"));
			}
		} finally {
			cursor.close ();
		}
	}	


	public void alteraAluno(String nomeAntigo, String nomeNovo)
	{
		collection.updateOne(eq("nome", nomeAntigo), new Document("$set",new Document("nome", nomeNovo)));
	}



	public void removeAluno(String nome)
	{
		collection.deleteOne(eq("nome", nome));
	}

	
	public void alteraAluno(ObjectId id, String nomeNovo)
	{
		collection.updateOne(eq("_id", id), new Document("$set", new Document("nome", nomeNovo)));
	}

	
	public void removeAluno(ObjectId id)
	{
		collection.deleteOne(eq("_id", id));
	}




}

