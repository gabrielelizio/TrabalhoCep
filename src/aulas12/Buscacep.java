package aulas12;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import conexao.Conexao;
import conexao.ConexaoMongoDB;
import aulas12.Aula12;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

import java.io.FileWriter;
import java.io.IOException;


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
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblDigiteOCep = new JLabel("Digite o CEP:");
		lblDigiteOCep.setBounds(10, 11, 101, 23);
		contentPane.add(lblDigiteOCep);
		
		JLabel lblNewLabel = new JLabel("Digite o logradouro completo:");
		lblNewLabel.setBounds(10, 74, 180, 23);
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
			    	stmt.setString(1,textCep.getText());
			    	stmt.setString(2,textDesc.getText());
			    	ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					String cep        = rs.getString(2);
			    	String logradouro =	rs.getString(3);
			    	String bairro	  = rs.getString(5);
			    	String cod_estado = rs.getString(4);
			    			
			    	JOptionPane.showMessageDialog(null, "Dados do endere�o pesquisado: \n\n"+ "CEP: "+cep+"\nLogradouro: "+logradouro+"\nCodigo Estado: "+cod_estado+"\nBairro: "+ bairro);
			    	
			    	exportData(con);
			    	
				}
				else {
					JOptionPane.showMessageDialog(null, "N�o foi encontrado nenhuma informa��o referente aos dados digitados.");
				}
				}
				 catch (SQLException e) {
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
			    	JOptionPane.showMessageDialog(null,"Exportado com sucesso.");
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
			public void actionPerformed(ActionEvent arg0) {
				ConexaoMongoDB c = new ConexaoMongoDB();
				
				
			}
		});
		btnBuscarNoMongo.setBounds(238, 151, 143, 23);
		contentPane.add(btnBuscarNoMongo);
	}
	

	public void exportData(Connection con) {
        Statement stmt;
        String query;
        try {
            stmt = (Statement) con.createStatement();

            //For comma separated file
            query = "SELECT * into OUTFILE  'C:/Users/gabri/Documents/eclipse-workspace/TrabalhandoComArquivos/input.csv' FIELDS TERMINATED BY ';' FROM info_cep ";
            stmt.executeQuery(query);
            System.out.println("Arquivo foi exportado com sucesso.");
        } catch(Exception e) {
            e.printStackTrace();
            stmt = null;
        }
    }
}; 
    

	
