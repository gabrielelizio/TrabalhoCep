package BuscaCep;

import static com.mongodb.client.model.Filters.eq;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.bson.Document;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import conexao.Conexao;
import conexao.ConexaoMongoDB;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;


import java.awt.Color;

@SuppressWarnings("serial")
public class Buscacep extends JFrame {

	private JPanel contentPane;
	private JTextField textCep;
	private JTextField textDesc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Buscacep frame = new Buscacep();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Buscacep() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setToolTipText("BuscaCep");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblDigiteOCep = new JLabel("Digite o CEP:");
		lblDigiteOCep.setBounds(10, 23, 101, 23);
		contentPane.add(lblDigiteOCep);

		JLabel lblNewLabel = new JLabel("Digite o logradouro completo:");
		lblNewLabel.setBounds(10, 78, 180, 23);
		contentPane.add(lblNewLabel);

		textCep = new JTextField();
		textCep.setBounds(65, 45, 128, 22);
		contentPane.add(textCep);
		textCep.setColumns(10);

		textDesc = new JTextField();
		textDesc.setBounds(65, 106, 204, 20);
		contentPane.add(textDesc);
		textDesc.setColumns(10);

		JButton btnBuscar = new JButton("Buscar no MySQL?");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Connection con = Conexao.faz_conexao();
					String sql = "SELECT * FROM info_cep where CEP=? || LOGRADOURO=?";
					PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);
					stmt.setString(1, textCep.getText());
					stmt.setString(2, textDesc.getText());
					ResultSet rs = stmt.executeQuery();

					if (rs.next()) {
						String cep = rs.getString(2);
						String logradouro = rs.getString(3);
						String cod_estado = rs.getString(4);
						String bairro = rs.getString(5);

						InsertMongo(cep, logradouro, bairro, cod_estado);

						JOptionPane.showMessageDialog(null,
								"Dados do endereço pesquisado: \n\n" + "CEP: " + cep + "\nLogradouro: " + logradouro
										+ "\nCodigo Estado: " + cod_estado + "\nBairro: " + bairro);

						// exportData(con);

					} else {
						JOptionPane.showMessageDialog(null,
								"Não foi encontrado nenhuma informação referente aos dados digitados.");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		btnBuscar.setBounds(65, 151, 148, 23);
		contentPane.add(btnBuscar);

		JLabel lblParaExportarOs = new JLabel("Para exportar os ceps em um arquivo csv, clique no botao abaixo:");
		lblParaExportarOs.setBounds(10, 185, 414, 14);
		contentPane.add(lblParaExportarOs);

		JButton btnExportar = new JButton("Exportar");
		btnExportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Connection con;
				try {
					con = Conexao.faz_conexao();
					exportData(con);
					JOptionPane.showMessageDialog(null, "Exportado com sucesso.");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		btnExportar.setBounds(158, 210, 89, 23);
		contentPane.add(btnExportar);

		JButton btnBuscarNoMongo = new JButton("Buscar no MongoDB ?");
		btnBuscarNoMongo.addActionListener(new ActionListener() {
			@SuppressWarnings("resource")
			public void actionPerformed(ActionEvent arg0) {
				String cep = textCep.getText();
			    String Logradouro= "";
			    String Bairro= ""; 
			    String Cod_Estado="";
			
			    MongoClient mongoClient;
				MongoDatabase database;
				MongoCollection<Document> collection;
				
			    mongoClient = new MongoClient("localhost", 27017);
				database = mongoClient.getDatabase("banco_aula_sauer");
				collection = database.getCollection("cep");


				MongoCursor<Document> cursor = collection.find(eq("CEP", cep)).iterator();

				try {
					if (cursor.hasNext()) {
						Document atual = cursor.next();
						
						 cep = (String) atual.get("CEP"); 
						 Logradouro = (String) atual.get("Logradouro"); 
						 Bairro = (String) atual.get("Bairro"); 
						 Cod_Estado =(String) atual.get("Cod_Estado");
						 
						 JOptionPane.showMessageDialog(null, "Dados do endereço pesquisado: \n\n" +
						 "CEP: " +cep + "\nLogradouro: " + Logradouro +
						 "\nCodigo Estado: " + Cod_Estado + "\nBairro: " + Bairro);
					
			    }
					else {
						JOptionPane.showMessageDialog(null,
								"Não foi encontrado nenhuma informação referente aos dados digitados.");
					}
				}
				finally {
					cursor.close();
				}		
			}
		});
		btnBuscarNoMongo.setBounds(238, 151, 155, 23);
		contentPane.add(btnBuscarNoMongo);
	}

	
	// Function para exportar a base de dados do Mysql para o Excel.
	public void exportData(Connection con) {
		Statement stmt;
		String query;
		try {
			stmt = (Statement) con.createStatement();

			// For comma separated file
			query = "SELECT * into OUTFILE  'C:/Users/gabri/Documents/eclipse-workspace/TrabalhandoComArquivos/export.csv' FIELDS TERMINATED BY ';' FROM info_cep ";
			stmt.executeQuery(query);
			System.out.println("Arquivo foi exportado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			stmt = null;
			System.out.println("Arquivo já existe.");
		}
	}

	// Função para inserir os dados pesquisados no Mysql para dentro do MongoDB.
	public static void InsertMongo(String cep, String logradouro, String bairro, String cod_estado) {

		ConexaoMongoDB c = new ConexaoMongoDB();
		c.insereCep(cep, logradouro, bairro, cod_estado);

	}



};
