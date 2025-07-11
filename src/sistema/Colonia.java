package sistema;

import java.io.*;
import java.util.*;

import cofres.*;
import general.Printer;
import pedido.*;
import robopuerto.Robopuerto;
import robot.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;


public class Colonia {
	public Set<Cofre> cofres = new HashSet<>();
	public Set<Robot> robots = new HashSet<>();
	public Set<Robopuerto> robopuertos = new HashSet<>();

	Set<Red> redes = new HashSet<>();

	Set<Cofre> cofresNoAsignados = new HashSet<>();
	List<Cofre> cofresCriticos = new ArrayList<>();
	List<Cofre> cofresNoCriticos = new ArrayList<>();
	Set<Red> redesSinSolucion = new HashSet<>();

	public void crearRedes() {
		// SE USA EL ALGORITMO DFS

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

		// Recorrer robopuertos
		for (int i = 0; i < n; i++) {
			// Identificar elementos de la red
			if (!visitado[i]) {
				Set<Robopuerto> redRobopuertos = new HashSet<>();
				Stack<Integer> pila = new Stack<>();
				pila.push(i);

				// Identificar robopuertos de la red
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
				List<Pedido> solicitados = new ArrayList<>();

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

		// Buscar cofres que no estan asignados a ninguna red
		cofresNoAsignados = new HashSet<>(cofres);
		for (Red red : redes) {
			cofresNoAsignados.removeAll(red.getCofres());
		}
		for (Cofre cofre : cofresNoAsignados) {
		    boolean esCritico = false;

		    String tipo = cofre.getTipo();

		    if (tipo.equals("CofreActivo") || tipo.equals("CofreSolicitud") || tipo.equals("CofreBuffer") && cofre.solicita())
		        esCritico = true;

		    if (esCritico) {
		        cofresCriticos.add(cofre);
		    } else {
		        cofresNoCriticos.add(cofre);
		    }
		}
	}

	public void iniciarSimulacion() {
		System.out.println("************************ EMPIEZA LA SIMULACIÓN! ************************\n");
		Printer.mostrarColonia(this);
		for (Red red : redes) {
			System.out.print("\n------------ La red " + red.getId() + " va a atender los pedidos ------------");
			int costo = red.atenderPedidos();
			System.out.println("\n\n------------ La red " + red.getId() + " terminó de atender los pedidos ------------\n\n");
			if (costo == -1) {
				Printer.advertencia("La red " + red.getId() + " no tiene solucion. ");
				redesSinSolucion.add(red);
			}

			else
				Printer.info("La red " + red.getId() + " tiene solucion, tuvo un costo de: " + costo);
			// costo = distancia recorrida + 1 por cada carga + 1 por sacar algo de un cofre
			// + 1 por dejar algo en un cofre
		}
		
		System.out.println("\n\n************************ TERMINO LA SIMULACIÓN! ************************");

		boolean hayCofresNoAsignadosCriticos = !cofresCriticos.isEmpty();
		
		if (hayCofresNoAsignadosCriticos || !redesSinSolucion.isEmpty())
			Printer.advertencia("El sistema no tiene solución");
		else
			Printer.info("El sistema tiene solución! Todas las redes completaron los pedidos y no hay cofres fuera de cobertura.");

		if (hayCofresNoAsignadosCriticos) {
			Printer.advertencia("Hay cofres criticos fuera de cobertura: ");
			for (Cofre cofre : cofresCriticos) {
				System.out.println("\t\t"+ cofre.getTipo() + " "+ cofre.getId());
			}
		}
		if(!cofresNoCriticos.isEmpty()) {
			Printer.info("Hay otros cofres fuera de cobertura: ");
			for(Cofre cofre : cofresNoCriticos) {
				System.out.println("\t\t"+ cofre.getTipo() + " "+ cofre.getId());
			}
		}
		
		
		if (!redesSinSolucion.isEmpty()) {
			Printer.advertencia("Hay redes que no completaron todos los pedidos: ");
			for (Red red : redesSinSolucion) {
				System.out.println("\t\tRed: " + red.getId());
			}
		}
	}

	public void cargarArchivo() throws Exception {
		// SE USA UN ARCHIVO YAML

		System.out.println("Se va a cargar el archivo de entrada para realizar la simulacion...");
		try {
			boolean enRobopuerto = false;
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

			Colonia data = mapper.readValue(
					new File("C://Users//Fran_//Desktop//colonia_variacion_2.yaml"),
					Colonia.class);
			this.robopuertos = data.robopuertos;
			this.cofres = data.cofres;
			this.robots = data.robots;

			for (Robot robot : robots) {
				enRobopuerto = false;
				for (Robopuerto robopuerto : robopuertos) {
					if (robot.getUbicacion().equals(robopuerto.getUbicacion())) {
						robot.setRobopuertoInicial(robopuerto);
						robot.setRobopuerto(robopuerto);
						enRobopuerto = true;
						break;
					}
				}
				if (!enRobopuerto) {
					throw new IllegalArgumentException("Error de configuración: el robot " + robot.getId()
							+ " con ubicación " + robot.getUbicacion() + " no esta en ningún robopuerto");
				}

				robot.getBateria().recargar();
			}

			System.out.println("Archivo cargado correctamente.\n\n");
		} catch (IOException e) {
			System.err.println("Error al cargar el archivo YAML: " + e.getMessage());
			throw new Exception();
		}
	}

	public void mostrarAtributos() {
		System.out.println("\nRobopuertos:");
		for (Robopuerto robopuerto : this.robopuertos) {
			System.out.println("   " + robopuerto);
		}

		System.out.println("\nRobots:");
		for (Robot robot : this.robots) {
			System.out.println("   " + robot);
		}

		System.out.println("\nCofres:");
		for (Cofre cofre : this.cofres) {
			System.out.println("   " + cofre);
		}
	}
	public Set<Red> getRedes() {
	    return this.redes;
	}

}
