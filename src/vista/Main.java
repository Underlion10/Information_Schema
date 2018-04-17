package vista;

import java.util.ArrayList;
import java.util.Scanner;

import modelo.AccesoDatos;

public class Main {

	public static void main(String[] args) {
		AccesoDatos acdc = new AccesoDatos();
		int actualState = 0;
		//int actualQuery = 0;
		String actualDB = "";
		ArrayList<String> bd = acdc.mostrarBasesDeDatos("localhost", "root", "");
		ArrayList<String> tab = null;
		int pos = 0;
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		while (actualState != -1) {
			String input = sc.nextLine();
			if (input.equals("x")) {
				break;
			}
			if (input.equals("q")) {
				actualState--;
				if(actualState == 0) {
					acdc.mostrarBasesDeDatos("localhost", "root", "");
				} else if(actualState == 1) {
					acdc.mostrarColumnasTablas("localhost", "root", "", actualDB, tab.get(pos-1));
				}
				continue;
			}
			
			if (actualState == 0 && !input.equals("")) {
				if (Integer.parseInt(input) < bd.size() + 1) {
					actualDB = bd.get(Integer.parseInt(input) - 1);
					tab = acdc.mostrarTablasBaseDeDatos("localhost", "root", "", actualDB);
					actualState++;
				} else {
					System.out.println("Introduzca un número correcto");
				}
			} else if (actualState == 1 && !input.equals("")) {
				pos = Integer.parseInt(input);
				if (Integer.parseInt(input) < tab.size() + 1) {
					System.out.println("¿Que desea seleccionar?");
					acdc.mostrarColumnasTablas("localhost", "root", "", actualDB, tab.get(pos - 1));
					actualState++;
				} else {
					System.out.println("Introduzca un número correctísimo");
				}
			} else if (actualState == 2 && !input.equals("")) {
				if (Integer.parseInt(input) < tab.size() + 1) {
					System.out.println("¿Que desea seleccionar?");
					acdc.seleccionarRegistro("localhost", "root", "",actualDB, 
							tab.get(pos-1),Integer.parseInt(input));
					actualState++;
				} else {
					System.out.println("Introduzca un valor correcto");
				}
			}

			if (actualState == 0) {
				acdc.mostrarBasesDeDatos("localhost", "root", "");
			}
		}
		System.out.println("Hasta la proxima!");
	}
}
