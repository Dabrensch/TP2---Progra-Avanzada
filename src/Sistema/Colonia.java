package Sistema;

import java.io.IOException;
import java.util.*;

import Cofres.*;
import General.*;
import Pedido.*;
import Robot.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;

public class Colonia {
	// del archivo de entrada:
	public Set<Cofre> cofres = new HashSet<>();
	public Set<Robot> robots = new HashSet<>();
	public Set<Robopuerto> robopuertos = new HashSet<>();

	// luego de crearRedes():
	Set<Red> redes = new HashSet<>();

	// identificar cofres sin cobertura
	Set<Cofre> cofresNoAsignados;

	public void crearRedes() {

		List<Robopuerto> lista = new ArrayList<>(robopuertos);
		int n = lista.size();
		boolean[][] adyacencia = new boolean[n][n];

		// Armar matriz de adyacencia
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				adyacencia[i][j] = lista.get(i).estaConectado(lista.get(j));
			}
		}

		boolean[] visitado = new boolean[n];
		redes.clear();

		for (int i = 0; i < n; i++) {
			if (!visitado[i]) {
				Set<Robopuerto> redRobopuertos = new HashSet<>();
				Stack<Integer> pila = new Stack<>();
				pila.push(i);

				while (!pila.isEmpty()) {
					int actual = pila.pop();
					if (!visitado[actual]) {
						visitado[actual] = true;
						Robopuerto rp = lista.get(actual);
						redRobopuertos.add(rp);

						for (int j = 0; j < n; j++) {
							if (adyacencia[actual][j] && !visitado[j]) {
								pila.push(j);
							}
						}
					}
				}

				// Crear pedidos
				PriorityQueue<Pedido> ofrecidos = new PriorityQueue<>(
						Comparator.comparingInt(p -> -p.getCofre().getPrioridad()));
				Set<Pedido> solicitados = new HashSet<>();

				// Buscar cofres dentro del alcance de esta red
				Set<Cofre> cofresDeLaRed = new HashSet<>();

				for (Cofre c : cofres) {
					for (Robopuerto rp : redRobopuertos) {
						if (rp.estaEnAlcance(c)) {
							cofresDeLaRed.add(c);

							// Pedidos ofrecidos
							if (c.getOfrece() != null) {

								for (Map.Entry<String, Integer> entry : c.getOfrece().entrySet()) {
									String item = entry.getKey();
									int cantidad = entry.getValue();
									Pedido pedido = new PedidoOfrecido(c, item, cantidad);
									ofrecidos.add(pedido);
								}
							}

							// Pedidos solicitados
							if (c.getSolicita() != null) {
								for (Map.Entry<String, Integer> entry : c.getSolicita().entrySet()) {
									String item = entry.getKey();
									int cantidad = entry.getValue();
									Pedido pedido = new PedidoSolicitado(c, item, cantidad);
									solicitados.add(pedido);
								}
							}

							break;
						}
					}
				}

				// Buscar robots dentro del alcance de esta red
				Set<Robot> robotsDeLaRed = new HashSet<>();

				for (Robot r : robots) {
					if (r.getRobopuertoInicial() != null && redRobopuertos.contains(r.getRobopuertoInicial())) {
						robotsDeLaRed.add(r);
					}
				}

				// Crear la red y agregarla a la colonia
				Red red = new Red(redRobopuertos, cofresDeLaRed, robotsDeLaRed, ofrecidos, solicitados);
				redes.add(red);
			}
		}

		// Manejar cofres que no estén asignados a ninguna red
		cofresNoAsignados = new HashSet<>(cofres);
		for (Red red : redes) {
			cofresNoAsignados.removeAll(red.getCofres());
		}
	}

	public void iniciarSimulacion() {
		for (Red red : redes) {
			System.out.println("\n------ La red " + red.getId() + " va a atender los pedidos ------");
			red.atenderPedidos();
		}

		// ver que hacer cuando hay cofres fuera de cobertura
		if (!cofresNoAsignados.isEmpty()) {
			System.out.println("\nCofres fuera de cobertura: " + cofresNoAsignados);
		}
	}

	public void cargarArchivo() throws Exception {
		try {
			boolean enRobopuerto = false;
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

			// Ya no es necesario registrar un deserializador para Item
			// Leer el archivo YAML y reemplazar los campos de "this"
			Colonia data = mapper.readValue(new File("C:\\Users\\Francisco\\Mi unidad\\Programación Avanzada\\Java\\TP2 codigo\\TP2---Progra-Avanzada\\colonia.yaml"), Colonia.class);
			this.robopuertos = data.robopuertos;
			this.cofres = data.cofres;
			this.robots = data.robots;

			for (Robot robot : robots) {
				enRobopuerto = false;
				for (Robopuerto robopuerto : robopuertos) {
					if (robot.getUbicacion().equals(robopuerto.getUbicacion())) {
						robot.setRobopuertoInicial(robopuerto);
						enRobopuerto = true;
						break;
					}
					// fallar si no esta en un robopuerto
				}
				if(!enRobopuerto)
				{
					throw new Exception("Error de configuración: el robot " + robot.getId() + " con ubicación " + robot.getUbicacion() + " no esta en ningún robopuerto");
				}

				robot.getBateria().recargar();
			}

			System.out.println("Archivo cargado correctamente.");
		} catch (IOException e) {
			System.err.println("Error al cargar el archivo YAML: " + e.getMessage());
			throw new Exception();
		}
	}
	
	public void mostrarAtributos() {
		 System.out.println("\nRobopuertos:"); for (Robopuerto robopuerto :
		 this.robopuertos) { System.out.println("   " + robopuerto); }
		 
		 System.out.println("\nRobots:"); for (Robot robot : this.robots) {
		 System.out.println("   " + robot); }
		 
		 System.out.println("\nCofres:"); for (Cofre cofre : this.cofres) {
		 System.out.println("   " + cofre); }
	}

	public static void main(String[] args) {
		Colonia colonia = new Colonia();

		try{	
			
			colonia.cargarArchivo();
			
		}catch(Exception e) {
			
	        System.exit(1);  // termina el programa con código 1 (error)
		}

		System.out.println("\n\nENTRADA"); //mostrar entradas
		
		for(Cofre cofre: colonia.cofres) {
			if(cofre.getTipo() == "Cofre Activo")
				cofre.guardarItem("itemA", 5);
		}
		
		colonia.mostrarAtributos(); //mostrar entradas

		//colonia.crearRedes();
		/*
		 * // mostarr redes System.out.println("\n\nARMAR REDES");
		 * 
		 * System.out.println("\nRedes:"); for (Red red : colonia.redes) {
		 * System.out.println(red); }
		 * 
		 * System.out.println("\nCofres no asignados:"); for (Cofre cofre :
		 * colonia.cofresNoAsignados) { System.out.println("   " + cofre); }
		 * 
		 * // Iniciar simulacion System.out.println("\n\nINICIAR SIMULACION");
		 */

		//colonia.iniciarSimulacion();

	}

}
