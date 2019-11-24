package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
	private static String nome_db = "jdbc:mysql://localhost/banco_aula_sauer?useSSL=false&requireSSL=false";
	private static String user_db = "root";
	private static String senha_db = "root";
	
	public static Connection faz_conexao() throws SQLException{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(nome_db,user_db,senha_db);
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			throw new SQLException(e.getException());
		}
	}
	
}
 