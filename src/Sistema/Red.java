package Sistema;

import java.util.*;
import Cofres.*;
import General.*;
import Grafo.Adyacente;
import Grafo.EstadoDijkstra;
import Grafo.Ruta;
import Nodo.Nodo;
import Nodo.NodoCofre;
import Nodo.NodoRobopuerto;
import Pedido.*;
import Robopuerto.Robopuerto;
import Robot.*;

public class Red {
	private static int contador = 1;
	private final int id;

	private Set<Cofre> cofres = new HashSet<>();
	private Set<Robot> robots = new HashSet<>();
	private Set<Robopuerto> robopuertos = new HashSet<>();

	private List<Pedido> solicitados;
	private PriorityQueue<Pedido> ofrecidos;

	// CONSTRUCTOR

	public Red(Set<Robopuerto> robopuertos, Set<Cofre> cofres, Set<Robot> robots, PriorityQueue<Pedido> ofrecidos,
			List<Pedido> solicitados) {
		this.id = contador++;

		this.cofres = cofres;
		this.robots = robots;
		this.robopuertos = robopuertos;
		this.solicitados = solicitados;
		this.ofrecidos = ofrecidos;

		construirGrafo();
	}

	private Map<Nodo, List<Adyacente>> grafoDistancias = new HashMap<>();

	private void construirGrafo() {
		Set<Nodo> nodos = new HashSet<>();
		for (Cofre c : cofres)
			nodos.add(new NodoCofre(c));
		for (Robopuerto r : robopuertos)
			nodos.add(new NodoRobopuerto(r));

		for (Nodo u : nodos) {
			List<Adyacente> adyacentes = new ArrayList<>();
			for (Nodo v : nodos) {
				if (!u.equals(v)) {
					double distancia = u.getUbicacion().distanciaA(v.getUbicacion());
					adyacentes.add(new Adyacente(v, distancia));
				}
			}
			grafoDistancias.put(u, adyacentes);
		}
	}

	// ACCIONES

	private Ruta rutaRobot(Cofre origen, Cofre destino, Robot robot) {
		Robopuerto rpInicial = robot.getRobopuerto();
		Robopuerto rpFinal = robopuertoMasCercanoACofre(destino, robopuertos);

		double bateriaMax = robot.getBateria().getCelulasMaximas();

		PriorityQueue<EstadoDijkstra> cola = new PriorityQueue<>(
				Comparator.comparingDouble(e -> e.getCostoAcumulado()));
		Set<String> visitados = new HashSet<>();

		Nodo inicio = new NodoRobopuerto(rpInicial);
		List<Nodo> rutaInicial = new ArrayList<>();
		rutaInicial.add(inicio);

		cola.add(new EstadoDijkstra(inicio, bateriaMax, 0, false, false, rutaInicial));

		while (!cola.isEmpty()) {
			EstadoDijkstra actual = cola.poll();

			// Generar clave única por nodo, batería y estado lógico
			String clave = actual.getNodo().hashCode() + "|" + (int) actual.getBateriaRestante() + "|"
					+ actual.getYaCargo() + "|" + actual.getYaDescargo();
			if (visitados.contains(clave))
				continue;
			visitados.add(clave);

			// Condición de éxito: está en robopuerto, ya pasó por origen y destino
			if (actual.getNodo().equals(new NodoRobopuerto(rpFinal)) && actual.getYaCargo() && actual.getYaDescargo()) {
				List<Nodo> camino = new ArrayList<>(actual.getCamino());
				camino.remove(0);
				return new Ruta(robot, camino, (int) Math.ceil(actual.getCostoAcumulado()));
			}

			List<Adyacente> vecinos = grafoDistancias.get(actual.getNodo());
			if (vecinos == null)
				continue;

			for (Adyacente ady : vecinos) {
				Nodo vecino = ady.getNodo();
				double distancia = ady.getDistancia();
				double consumo = robot.getBateria().celulasNecesarias(distancia);

				// Si alcanza con la batería actual
				if (consumo <= actual.getBateriaRestante()) {
					List<Nodo> nuevoCamino = new ArrayList<>(actual.getCamino());
					nuevoCamino.add(vecino);

					double nuevaBateria = actual.getBateriaRestante() - consumo;
					double nuevoCosto = actual.getCostoAcumulado() + consumo;

					boolean yaCargo = actual.getYaCargo();
					boolean yaDescargo = actual.getYaDescargo();

					// ¿Recarga?
					if (vecino.esRobopuerto()) {
						nuevaBateria = bateriaMax;
						nuevoCosto += 1; // costo de recarga

						EstadoDijkstra nuevoEstado = new EstadoDijkstra(vecino, nuevaBateria, nuevoCosto, yaCargo,
								yaDescargo, nuevoCamino);
						cola.add(nuevoEstado);
					} else {

						// ¿Pasa por cofres clave?

						Cofre c = ((NodoCofre) vecino).getContenido();
						if (c.equals(origen)) {
							yaCargo = true;

							EstadoDijkstra nuevoEstado = new EstadoDijkstra(vecino, nuevaBateria, nuevoCosto, yaCargo,
									yaDescargo, nuevoCamino);
							cola.add(nuevoEstado);
						}

						if (c.equals(destino) && yaCargo) {
							yaDescargo = true; // solo descarga si ya cargó

							EstadoDijkstra nuevoEstado = new EstadoDijkstra(vecino, nuevaBateria, nuevoCosto, yaCargo,
									yaDescargo, nuevoCamino);
							cola.add(nuevoEstado);
						}

					}

				}
			}
		}

		return null; // no hay ruta posible

	}

	private Ruta planearRuta(Cofre origen, Cofre destino) {
		Set<Robot> robots = new HashSet<>(this.robots);

		Robot elegido;

		for (int i = 0; i < robots.size(); i++) {
			elegido = elegirRobotMasCercano(origen, robots);

			Ruta ruta = rutaRobot(origen, destino, elegido);

			if (ruta == null) {
				robots.remove(elegido);
			} else {
				return ruta;
			}
		}

		return null;
	}

	private Robot elegirRobotMasCercano(Cofre cofre, Set<Robot> robots) {
		Robot elegido = null;
		double menorDistancia = Double.MAX_VALUE;

		for (Robot robot : robots) {
			double distancia = robot.getUbicacion().distanciaA(cofre.getUbicacion());

			if (distancia < menorDistancia) {
				menorDistancia = distancia;
				elegido = robot;
			}
		}

		return elegido;
	}

	private Robopuerto robopuertoMasCercanoACofre(Cofre cofre, Set<Robopuerto> robopuertos) {
		Robopuerto masCercano = null;
		double distanciaMinima = Double.MAX_VALUE;

		Coordenada ubicacionCofre = cofre.getUbicacion();

		for (Robopuerto robopuerto : robopuertos) {
			double distancia = ubicacionCofre.distanciaA(robopuerto.getUbicacion());
			if (distancia < distanciaMinima) {
				distanciaMinima = distancia;
				masCercano = robopuerto;
			}
		}

		return masCercano;
	}

	private void atenderPedido(Robot robot, List<Nodo> ruta, Cofre cofreOfrecido, Cofre cofreSolicitado, String item,
			int cantidad) {
		System.out.println("\t\t\tUbicacion actual: " + robot.getUbicacion() + ", Bateria actual: "
				+ robot.getBateria() + ", Capacidad: " + robot.getCapacidad());

		for (Nodo nodo : ruta) {

			if (nodo.esRobopuerto()) {
				Robopuerto robopuertoIntermedio = ((NodoRobopuerto) nodo).getContenido();
				System.out.print("\t\t\tVa a cargarse al robopuerto " + robopuertoIntermedio.getId() + " en "
						+ robopuertoIntermedio.getUbicacion() + ". ");
				robot.moverA(robopuertoIntermedio);
				robot.setRobopuerto(robopuertoIntermedio);
			} else {
				Cofre cofre = ((NodoCofre) nodo).getContenido();
				if (cofre.equals(cofreOfrecido)) {
					robot.moverA(cofreOfrecido);
					robot.cargarItem(cofreOfrecido, item, cantidad);

					System.out.print(
							"\t\t\tAgarro los items del " + cofreOfrecido.getTipo() + " " + cofreOfrecido.getId());
					System.out.println("\t\t\t\tUbicacion actual: " + robot.getUbicacion() + ", Bateria actual: "
							+ robot.getBateria());
				} else {
					robot.moverA(cofreSolicitado);
					robot.descargarItem(cofreSolicitado, item, cantidad);

					System.out.print(
							"\t\t\tDejo los items en el " + cofreSolicitado.getTipo() + " " + cofreSolicitado.getId());
					System.out.println("\t\t\t\tUbicacion actual: " + robot.getUbicacion() + ", Bateria actual: "
							+ robot.getBateria());
				}
			}

		}
	}

	public int atenderPedidos() {
		int costo = 0;

		Set<Pedido> sinOfrecer = new HashSet<Pedido>();

		while (!ofrecidos.isEmpty()) {
			// Sacar de la pila el pedido con mayor prioridad
			Pedido ofrecido = ofrecidos.poll();
			Cofre cofreOfrecido = ofrecido.getCofre();
			String item = ofrecido.getItem();

			System.out.println("\n\nEl " + ofrecido.getCofre().getTipo() + " " + ofrecido.getCofre().getId()
					+ " ofreció " + ofrecido.getCantidad() + " unidades de " + ofrecido.getItem() + ".");

			solicitados.sort(Comparator
					.comparing(pedido -> pedido.getCofre().getUbicacion().distanciaA(cofreOfrecido.getUbicacion())));
			
			// Esta linea busca el cofre solicitud mas cercano para atender la oferta de items del cofreOfrecido.

			for (Pedido solicitado : solicitados) {
				// recorrer las solicitudes en busca de un cofre que solicite el item del pedido
				if (solicitado.getItem().equals(item) == true && solicitado.getCantidad() != 0) {

					System.out.println("\nEl " + solicitado.getCofre().getTipo() + " " + solicitado.getCofre().getId()
							+ " solicitó " + solicitado.getCantidad() + " unidades de " + solicitado.getItem() + ".");

					Cofre cofreSolicitado = solicitado.getCofre();

					int cantidad = 0;

					if (robots.isEmpty()) {
						System.out.println("\t\tNo hay robots. ");
						break;
					}

					while (true) {

						Ruta ruta = planearRuta(cofreOfrecido, cofreSolicitado);

						if (ruta == null) {
							System.out.println(
									"\t\tNingun robot llega, se intentara completar la solcitud cuando otro cofre ofrezca el item solicitado. ");
							break;
						}

						Robot robot = ruta.getRobot();
						List<Nodo> nodos = ruta.getRuta();
						costo += ruta.getCosto();

						cantidad = Math.min(robot.getCapacidad(),
								(Math.min(ofrecido.getCantidad(), solicitado.getCantidad()))); // logica para saber que
																								// cantidad retirar y
																								// entregar
						
						System.out.print("\t\tEl robot " + robot.getId() + " va a realizar el pedido. Llevará  " + cantidad + " unidades");

						atenderPedido(robot, nodos, cofreOfrecido, cofreSolicitado, item, cantidad);

						if (solicitado.getCantidad() != cantidad && ofrecido.getCantidad() != cantidad) {
							System.out.println(
									"\t\tFaltan ofrecidos y solicitados, porque el robot no tenia suficiente capacidad.");

							ofrecido.setCantidad(ofrecido.getCantidad() - cantidad);
							solicitado.setCantidad(solicitado.getCantidad() - cantidad);
						} else
							break;

					}

					// VER QUE FALTA LLEVAR
					// tambien hay que cambiar los vectores de ofrece, solicita y inventario de cada
					// cofre
					if (solicitado.getCantidad() - cantidad == 0) {

						solicitado.setCantidad(0); // ya no hay solicitados

						// comprobar si quedan items para ofrecer
						if (ofrecido.getCantidad() - cantidad == 0) {
							System.out.println("\t\tSe completó todo el pedido.");

							ofrecido.setCantidad(0);
							break; // no hay solicitados ni ofrecidos, se cumplieron ambos pedidos
						} else {
							System.out.println("\t\tSe completó toda la solicitud, pero faltan items para ofrecer.");

							ofrecido.setCantidad(ofrecido.getCantidad() - cantidad); // faltan ofrecidos, se sigue
																						// iterando
						}
					} else if (ofrecido.getCantidad() - cantidad == 0) {
						System.out.println("\t\tSe ofrecieron todos los items, pero faltan items solicitados.");

						ofrecido.setCantidad(0);
						solicitado.setCantidad(solicitado.getCantidad() - cantidad); // se ofrecieron todos los
																						// items
																						// pero faltan solicitados
						break;
					}
				}

			}

			if (ofrecido.getCantidad() != 0) {
				System.out.print("\n\tNo se pudó completar todo el pedido del cofre " + ofrecido.getCofre().getId()
						+ ", quedaron " + ofrecido.getCantidad() + " unidades de " + ofrecido.getItem()
						+ " sin ofrecer. ");

				if (cofreOfrecido.esActivo()) {
					if (robots.isEmpty()) {
						System.out.println("No hay robots para llevar a un cofre de almacenamiento. ");
						break;
					}

					System.out.println("Se va a llevar a un cofre de almacenamiento. ");

					boolean llego = false;
					boolean hayAlmacenamiento = false;
					int cantidad = 0;

					while (true) {

						for (Cofre cofre : cofres) {
							if (cofre.almacena()) {

								hayAlmacenamiento = true;

								Ruta ruta = planearRuta(cofreOfrecido, cofre);

								if (ruta == null) {
									continue;
								}

								Robot robot = ruta.getRobot();
								List<Nodo> nodos = ruta.getRuta();
								costo += ruta.getCosto();


								cantidad = Math.min(robot.getCapacidad(), ofrecido.getCantidad());

								System.out.print("\t\tEl robot " + robot.getId()
										+ " va a llevar el exceso al cofre " + cofre.getTipo() + " " + cofre.getId() + ". ");
								System.out.print("LLevara " + cantidad + " unidades");

								atenderPedido(robot, nodos, cofreOfrecido, cofre, item, cantidad);

								llego = true;
								break;
							}
						}

						if (!hayAlmacenamiento) {
							System.out.println("\t\tNo hay cofres de almacenamiento. ");
							sinOfrecer.add(ofrecido);
							break;
						}

						if (!llego) {
							System.out.println("\t\tNingun robot llega. ");
							sinOfrecer.add(ofrecido);
							break;
						} else {
							if (ofrecido.getCantidad() != cantidad) {
								System.out.println(
										"\t\tQuedan excesos por llevar, porque el robot no tenia suficiente capacidad.");

								ofrecido.setCantidad(ofrecido.getCantidad() - cantidad);
								llego = false;
							} else
								System.out.println("\t\tSe llevo todo el exceso.");
							break;
						}
					}
				} else {
					System.out.println("El cofre va a dejar el exceso en su almacenamiento. ");
					cofreOfrecido.desofrecer(item);
				}

			}

		}

		boolean tieneSolucion = true;
		solicitados.removeIf(pedido -> pedido.getCantidad() == 0);
		
		if(!solicitados.isEmpty()) {
			System.out.println("\nSolicitudes incompletas: ");
			tieneSolucion = false;
		}
		for (Pedido solicitado : solicitados) {
			System.out.println("\t" + solicitado);
			
		}
		if(!sinOfrecer.isEmpty()) {
			System.out.println("\nOfrecimientos incompletos: ");
			tieneSolucion = false;
		}
		for (Pedido pedido : sinOfrecer) {
			System.out.println("\t" + pedido);
		}
		if (!tieneSolucion) {
			return -1;
		} else {
			return costo;
		}
	}

	// SETTERS

	public void setCofres(Set<Cofre> cofres) {
		this.cofres = cofres;
	}

	public void setRobots(Set<Robot> robots) {
		this.robots = robots;
	}

	public void setRobopuertos(Set<Robopuerto> robopuertos) {
		this.robopuertos = robopuertos;
	}

	public void setSolicitados(List<Pedido> solicitados) {
		this.solicitados = solicitados;
	}

	public void setOfrecidos(PriorityQueue<Pedido> ofrecidos) {
		this.ofrecidos = ofrecidos;
	}

	// GETTERS

	public Set<Cofre> getCofres() {
		return cofres;
	}

	public int getId() {
		return id;
	}

	public Set<Robot> getRobots() {
		return robots;
	}

	public Set<Robopuerto> getRobopuertos() {
		return robopuertos;
	}

	public List<Pedido> getSolicitados() {
		return solicitados;
	}

	public PriorityQueue<Pedido> getOfrecidos() {
		return ofrecidos;
	}

	// TO STRING

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Red: " + id + "\n");

		sb.append("  Robopuertos:\n");
		for (Robopuerto r : robopuertos) {
			sb.append("    ").append(r).append("\n");
		}

		sb.append("  Cofres:\n");
		for (Cofre c : cofres) {
			sb.append("    ").append(c).append("\n");
		}

		sb.append("  Robots:\n");
		for (Robot r : robots) {
			sb.append("    ").append(r).append("\n");
		}

		sb.append("  Pedidos solicitados:\n");
		if (solicitados != null) {
			for (Pedido p : solicitados) {
				sb.append("    ").append(p).append("\n");
			}
		} else {
			sb.append("    (vacío)\n");
		}

		sb.append("  Pedidos ofrecidos:\n");
		if (ofrecidos != null) {
			for (Pedido p : ofrecidos) {
				sb.append("    ").append(p).append("\n");
			}
		} else {
			sb.append("    (vacío)\n");
		}

		return sb.toString();
	}

}
