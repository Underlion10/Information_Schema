package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class AccesoDatos {

	public Connection crearConexionMySQL(String dominio, String usr, String clave) {
		Connection connect = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + dominio + "/information_schema";
			connect = DriverManager
			          .getConnection(url, usr, clave);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connect;
	}
	
	public ArrayList<String> mostrarBasesDeDatos(String dominio, String usr, String clave) {
		Connection conn = crearConexionMySQL(dominio, usr, clave);
		ArrayList<String> basesDatos= new ArrayList<String>();
		try {
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("select schema_name from schemata where schema_name not in ('information_schema', 'mysql', 'performance_schema', 'phpmyadmin', 'test')");
			System.out.println("Bases de Datos disponibles");
			int i = 1;
			while(rs.next()) {
				System.out.println("\t" + i + " -- " +rs.getObject(1));
				basesDatos.add((String)rs.getObject(1));
				i++;
			}
			System.out.println("Selecciona una base de datos");
			System.out.println("q -- Atras || x -- Salir");
			rs.close();
			stm.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return basesDatos;
	}
	
	public ArrayList<String> mostrarTablasBaseDeDatos(String dominio, String usr, String clave, String table) {
		Connection conn = crearConexionMySQL(dominio, usr, clave);
		System.out.println("¿Que desea seleccionar de " + table +  " ?");
		ArrayList<String> tablasBD= new ArrayList<String>();
		try {
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("select table_name from tables where table_schema like '" + table + "'" );
			System.out.println("Tablas de " + table);
			int i = 1;
			while(rs.next()) {
				System.out.println("\t" + i + " -- " +rs.getObject(1));
				tablasBD.add((String)rs.getObject(1));
				i++;
			}
			System.out.println("Seleccione la tabla que desee manipular");
			System.out.println("q -- Atras || x -- Salir");
			rs.close();
			stm.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tablasBD;
	}
	
	public void mostrarColumnasTablas(String dominio, String usr, String clave, String bd, String tabla) {
		Connection conn = crearConexionMySQL(dominio, usr, clave);
		try {
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("select * from " + bd + "." + tabla + "");
			ResultSetMetaData rsmt = rs.getMetaData();
			System.out.println("Registros disponibles :" );
			for (int i = 1; i <= rsmt.getColumnCount(); i++) {
				if(i < rsmt.getColumnCount()) {
					System.out.print(i + "--" + rsmt.getColumnName(i) + " || ");
				} else {
					System.out.print(i + "--" + rsmt.getColumnName(i));
				}
			}
			System.out.println("\n¿Que tarea desea realizar?");
			System.out.println("1 -- SELECT || 2 -- UPDATE || 3 -- INSERT || 4 -- DELETE");
			System.out.println("q -- Atras || x -- Salir");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void seleccionarRegistro(String dominio, String usr, String clave,
			String bd, String table,int type) {
		Connection conn = crearConexionMySQL(dominio, usr, clave);
		try {
			Statement stm = conn.createStatement();
			if(type == 1) {
				String selec = acotarSeleccion();
				boolean withWhere = withWhere();
				String condition = "";
				if(withWhere) {
					condition = whereCon();
				}
				ResultSet rs = stm.executeQuery("select " + selec + " from " + bd + "." + table + condition);
				ResultSetMetaData rsmt = rs.getMetaData();
				System.out.println("Registros devueltos de tu consulta");
				for(int j = 1; j <= rsmt.getColumnCount(); j++) {
					if(j < rsmt.getColumnCount()) {
						System.out.print(rsmt.getColumnName(j) + " || ");
					} else {
						System.out.print(rsmt.getColumnName(j) + "\n");
					}
				}
				while(rs.next()) {
					System.out.println();
					for(int j = 1; j <= rsmt.getColumnCount(); j++) {
						if(j < rsmt.getColumnCount()) {
							System.out.print(rs.getObject(j) + " || ");
						} else {
							System.out.print(rs.getObject(j));
						}
					}
				}
				System.out.println();
			} else if(type == 2) {
				
			}
			System.out.println("q -- Atras || x -- Salir");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String whereCon() {
		System.out.println("Introduzca el condicional que desea utilizar ?");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		return line;
	}

	private boolean withWhere() {
		System.out.println("¿Desea introducir una condicion?[S/n]");
		System.out.println("");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		if(line.equalsIgnoreCase("s")) {
			return true;
		} else if(line.equalsIgnoreCase("n")){
			return false;
		}
		return false;
	}

	private String acotarSeleccion() {
		System.out.println("¿Qué desea seleccionar?");
		System.out.println("Para seleccionar todo --- a");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		if(line.equals("a")) {
			return "*";
		} else if(!line.equals("")) {
			return line;
		} else {
			return null;
		}
	}
}
