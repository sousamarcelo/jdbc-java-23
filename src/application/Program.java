package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;

public class Program {

	public static void main(String[] args) {
		
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = DB.getConnection();
			
			conn.setAutoCommit(false); // configurado para não confirmar as operaçõe automaticamente, deixa que controlamos via codigo, ficara mais flexivel.
			
			st = conn.createStatement();
					
			int rows1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 WHERE DepartmentId = 1");		
			
			/* retirado o erro forçado para ver a todo o processo da transação funcionara
			// forçando um erro no meio do procedo de atualização entre rows1 e rows2
			int x = 1;
			if (x < 2) {
				throw new SQLException("Fake error");
			}
			*/
			
			int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 3090 WHERE DepartmentId = 2");	
			
			conn.commit(); // confirmando termino da transação, garante toda a transação seja executada ou nenhuma seja.
			
			System.out.println("rows1 " + rows1);
			System.out.println("rows2 " + rows2);
			
		} catch (SQLException e) {
			try {
				conn.rollback(); 																		// metodo para voltar o estado inicial do banco caso algumas das operação falhem
				throw new DbException("Transaction rolled back! causad by " + e.getMessage());
			} catch (SQLException e1) { 																//tratando erro do "conn.rollback()"
				throw new DbException("Error trying to rollback! caused by " + e1.getMessage());
			} 
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}

	}

}
