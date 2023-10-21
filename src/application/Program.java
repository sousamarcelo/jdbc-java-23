package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import db.DB;

public class Program {

	public static void main(String[] args) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			//estabelecendo a conexão
			conn = DB.getConnection();
			
			// criando o statment utiliando o placehoder para cada coluna a ser preenchida
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?) "
					, Statement.RETURN_GENERATED_KEYS // retornando o ID gerado.
					);
			
			// cada set abaixo substitui um interrogação por ordem
			st.setString(1, "Carl Purple");
			st.setString(2, "Carl@gmail.com");
			st.setDate(3, new java.sql.Date(sdf.parse("22/04/1985").getTime())); // por se tratar de consulta no banco e para não geração confusão nos importes foi utilizado nome qualificade da biblioteca Date que deve ser do pacote slq
			st.setDouble(4, 3000.0);
			st.setInt(5, 4);
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys(); // esse comando pega o valor retornando no statmente "Statement.RETURN_GENERATED_KEYS" la no script sql, pode retornar mais de uma valor dependendo do tando de dados adicionado no script acima
				
				while (rs.next()) {
					int id = rs.getInt(1); // foi indicado o valor 1 para informar que trata-se da primeira coluna do resultset, ou seja da tabela
					System.out.println("Done! Id = " + id);
				}
				
			} else {
				System.out.println("No rows affetected!");
			}		
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
			DB.getConnection();
		}
		

	}

}
