package aulas12;
import Thread.*;

import java.awt.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.PreparedStatement;

import conexao.Conexao;
import conexao.ConexaoMongoDB;

public class Aula12 {

	public static void main(String[] args) {
		
		
		
		String file = "planilhacep.csv";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    
		    while ((line = br.readLine()) != null) {
		    	String cep = line.split(";")[0];
		    	String logradouro = line.split(";")[1];
		    	String bairro = line.split(";")[2];
		    	String cod_estado = line.split(";")[4];
		    	
		    	InsertMongo(cep,logradouro,bairro,cod_estado);
		    	Insert(cep,logradouro,bairro,cod_estado);
		    	  
		    	//System.out.println(cep+" "+logradouro+"  "+bairro+" "+cod_estado+" ");
		       	MinhaThreadRunnable thread1 = new MinhaThreadRunnable("#1", 9);
				MinhaThreadRunnable thread2 = new MinhaThreadRunnable("#2", 10);
				MinhaThreadRunnable thread3 = new MinhaThreadRunnable("#3", 11);

				Thread t1 = new Thread(thread1);
				Thread t2 = new Thread(thread2);
				Thread t3 = new Thread(thread3);

				t1.start();
				t2.start();
				t3.start();
				
				t1.setPriority(5);
				t2.setPriority(3);
				t3.setPriority(1);

		//33-40  - Espera a execucao das 03 acabarem p/ só depois continuar - Maneira mais elegante do que is alive
				try {
					t1.join();
					t2.join();
					t3.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
				

				
			}	    
		    System.out.println("Programa finalizado");
		    
		    System.out.println("Dados Inseridos com sucesso");
		}catch (Exception e) {
		System.out.println(e);
		}	
		
	}
	
	
	public static void Insert (String cep, String logradouro, String bairro, String cod_estado) {
		
		Connection con;
		try {
			con = Conexao.faz_conexao();
			String sql = "INSERT INTO `info_cep`(`CEP`, `LOGRADOURO`, `BAIRRO`, `COD_EST`) VALUES (?,?,?,?)";
	    	PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);
	    	stmt.setString(1, cep  );
	    	stmt.setString(2, logradouro);
	    	stmt.setString(3, bairro);
	    	stmt.setString(4, cod_estado);
	    	
	    	int rs = stmt.executeUpdate();	
	    	
	    	con.close();
	    	    	
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	    	
	}
	
	

public static void InsertMongo (String cep, String logradouro, String bairro, String cod_estado) {
	
	ConexaoMongoDB c = new ConexaoMongoDB();
	c.insereAluno(cep, logradouro, bairro,cod_estado);
	
	    	
}


}