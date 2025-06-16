package Sistema;

import java.util.*;

import Cofres.*;
import General.*;
import Pedido.*;
import Robot.*;

public class Red {
	private static int contador = 1;
	private final int id;

	private Set<Cofre> cofres = new HashSet<>();
	private Set<Robot> robots = new HashSet<>();
	private Set<Robopuerto> robopuertos = new HashSet<>();

	private Set<Pedido> solicitados;
	private PriorityQueue<Pedido> ofrecidos;

	private Grafo grafo = null;
	private Map<Robot, Grafo> grafosRobot = null;

	public Red(Set<Robopuerto> robopuertos, Set<Cofre> cofres, Set<Robot> robots, PriorityQueue<Pedido> ofrecidos,
			Set<Pedido> solicitados) {
		this.id = contador++;

		this.cofres = cofres;
		this.robots = robots;
		this.robopuertos = robopuertos;
		this.solicitados = solicitados;
		this.ofrecidos = ofrecidos;

		armarGrafo();
	}

	private void armarGrafo() {
		grafo = new Grafo();

		for (Cofre cofre : cofres) {
			Nodo nodo = new NodoCofre(cofre);
			grafo.agregarNodo(nodo);
		}

		for (Robopuerto robopuerto : robopuertos) {
			Nodo nodo = new NodoRobopuerto(robopuerto);
			grafo.agregarNodo(nodo);
		}

		List<Nodo> nodos = grafo.getNodos();

		for (int i = 0; i < nodos.size(); i++) {
			Nodo ni = nodos.get(i);
			for (int j = i + 1; j < nodos.size(); j++) {
				Nodo nj = nodos.get(j);
				double distancia = ni.distanciaA(nj);
				grafo.agregarArista(ni, nj, distancia);
			}
		}
	}

	private Robopuerto robopuertoIntermedio(Bateria bateria, Coordenada ubicacion, Cofre cofre,
			Set<Robopuerto> robopuertos) {
		Set<Robopuerto> robopuertos2 = new HashSet<>(robopuertos);
		int n = robopuertos2.size();

		while (!robopuertos2.isEmpty()) {
			Robopuerto robopuertoIntermedio = robopuertoMasCercanoACofre(cofre, robopuertos2);

			if (bateria.getCelulas() >= bateria
					.celulasNecesarias(ubicacion.distanciaA(robopuertoIntermedio.getUbicacion()))) {
				return robopuertoIntermedio;
			} else {
				robopuertos2.remove(robopuertoIntermedio);
			}
		}

		return null;
	}

	private Set<Nodo> rutaRobot(Cofre origen, Cofre destino, Robot robot) {
		Set<Nodo> nodos = new LinkedHashSet<>();

		Bateria bateria = new Bateria(robot.getBateria().getCelulasMaximas());
		Coordenada ubicacion = new Coordenada(robot.getUbicacion().getX(), robot.getUbicacion().getY());

		Robopuerto robopuertoOrigen = robopuertoMasCercanoACofre(origen, robopuertos);
		Robopuerto robopuertoDestino = robopuertoMasCercanoACofre(destino, robopuertos);

		Set<Robopuerto> robopuertos = new HashSet<>(this.robopuertos);

		// puede haber un loop infinito, solucionarlo

		// ver si llega al origen
		while (bateria.getCelulas() < (bateria.celulasNecesarias(ubicacion.distanciaA(origen.getUbicacion()))
				+ bateria.celulasNecesarias(ubicacion.distanciaA(robopuertoOrigen.getUbicacion())))) {

			// pasar por nodos intermedios

			Robopuerto robopuertoIntermedio = robopuertoIntermedio(bateria, ubicacion, origen, robopuertos);
			if (robopuertoIntermedio == null)
				return null;

			ubicacion.setXY(robopuertoIntermedio.getUbicacion().getX(), robopuertoIntermedio.getUbicacion().getY());
			bateria.recargar();
			nodos.add(new NodoRobopuerto(robopuertoIntermedio));
			robopuertos.remove(robopuertoIntermedio);
		}
		// Ir al origen
		bateria.descargar(bateria.celulasNecesarias(ubicacion.distanciaA(origen.getUbicacion())));
		ubicacion.setXY(origen.getUbicacion().getX(), origen.getUbicacion().getY());
		nodos.add(new NodoCofre(origen));

		// ver si llega al destino
		robopuertos.addAll(this.robopuertos);
		while (bateria.getCelulas() < (bateria.celulasNecesarias(ubicacion.distanciaA(destino.getUbicacion()))
				+ bateria.celulasNecesarias(destino.getUbicacion().distanciaA(robopuertoDestino.getUbicacion())))) {

			// pasar por nodos intermedios

			Robopuerto robopuertoIntermedio = robopuertoIntermedio(bateria, ubicacion, destino, robopuertos);
			if (robopuertoIntermedio == null)
				return null;

			ubicacion.setXY(robopuertoIntermedio.getUbicacion().getX(), robopuertoIntermedio.getUbicacion().getY());
			bateria.recargar();
			nodos.add(new NodoRobopuerto(robopuertoIntermedio));
			robopuertos.remove(robopuertoIntermedio);
		}
		// Ir al destino
		nodos.add(new NodoCofre(destino));
		nodos.add(new NodoRobopuerto(robopuertoDestino));

		return nodos;

	}

	private Map<Robot, Set<Nodo>> planearRuta(Cofre origen, Cofre destino) {
		Set<Robot> robots = new HashSet<>(this.robots);
		int n = robots.size();

		Robot elegido;

		Map<Robot, Set<Nodo>> salida = new HashMap<>();

		for (int i = 0; i < n; i++) {
			elegido = elegirRobotMasCercano(origen, robots);

			Set<Nodo> ruta = rutaRobot(origen, destino, elegido);

			if (ruta == null) {
				robots.remove(elegido);
			} else {
				salida.put(elegido, ruta);
				return salida;
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

	private void atenderPedido(Robot robot, Set<Nodo> ruta, Cofre cofreOfrecido, Cofre cofreSolicitado, String item,
			int cantidad) {
		System.out.println("\t\t\t\tUbicacion actual: " + robot.getUbicacion() + ", Bateria actual: "
				+ robot.getBateria() + ", Capacidad: " + robot.getCapacidad());

		for (Nodo nodo : ruta) {
			if (nodo.esRobopuerto()) {
				Robopuerto robopuertoIntermedio = ((NodoRobopuerto) nodo).getContenido();
				System.out.print("\t\t\tVa a cargarse al robopuerto " + robopuertoIntermedio.getId() + " en "
						+ robopuertoIntermedio.getUbicacion());
				robot.moverA(robopuertoIntermedio);
			} else {
				Cofre cofre = ((NodoCofre) nodo).getContenido();
				if (cofre.equals(cofreOfrecido)) {
					robot.moverA(cofreOfrecido);
					robot.cargarItem(cofreOfrecido, item, cantidad);

					System.out.print("\t\t\tAgarro los items que ofreció el cofre");
					System.out.println("\t\t\t\tUbicacion actual: " + robot.getUbicacion() + ", Bateria actual: "
							+ robot.getBateria());
				} else {
					robot.moverA(cofreSolicitado);
					robot.descargarItem(cofreSolicitado, item, cantidad);

					System.out.print("\t\t\tDejo los items que solicitó el cofre");
					System.out.println("\t\t\t\tUbicacion actual: " + robot.getUbicacion() + ", Bateria actual: "
							+ robot.getBateria());
				}
			}

		}
	}

	public void atenderPedidos() {

		if (robots.isEmpty()) {
			System.out.println("NO HAY ROBOTS");
			return;
		}

		while (!ofrecidos.isEmpty()) {
			// Sacar de la pila el pedido con mayor prioridad
			Pedido ofrecido = ofrecidos.poll();
			Cofre cofreOfrecido = ofrecido.getCofre();
			String item = ofrecido.getItem();

			System.out.println("\n\nEl cofre " + ofrecido.getCofre().getId() + " ofreció " + ofrecido.getCantidad()
					+ " unidades de " + ofrecido.getItem() + ".");

			for (Pedido solicitado : solicitados) {
				// recorrer las solicitudes en busca de un cofre que solicite el item del pedido
				if (solicitado.getItem().equals(item) == true && solicitado.getCantidad() != 0) {

					System.out.println("\n\tEl cofre " + solicitado.getCofre().getId() + " solicitó "
							+ solicitado.getCantidad() + " unidades de " + solicitado.getItem() + ".");

					Cofre cofreSolicitado = solicitado.getCofre();

					int cantidad = 0;

					while (true) {

						Map<Robot, Set<Nodo>> ruta = planearRuta(cofreOfrecido, cofreSolicitado);

						if (ruta == null)
							throw new IllegalArgumentException("NINGUN ROBOT LLEGA");

						Map.Entry<Robot, Set<Nodo>> entrada = ruta.entrySet().iterator().next();
						Robot robot = entrada.getKey();
						Set<Nodo> nodos = entrada.getValue();

						System.out.print("\t\tEl robot " + robot.getId() + " va a realizar el pedido. ");

						cantidad = Math.min(robot.getCapacidad(),
								(Math.min(ofrecido.getCantidad(), solicitado.getCantidad()))); // logica para saber que
																								// cantidad retirar y
																								// entregar

						System.out.print("LLevara " + cantidad + " unidades");

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
				System.out.print("\tNo se pudó completar todo el pedido del cofre " + ofrecido.getCofre().getId()
						+ ", quedaron " + ofrecido.getCantidad() + " unidades de " + ofrecido.getItem()
						+ " sin ofrecer por falta de solicitudes. ");

				if (cofreOfrecido.esActivo()) {
					System.out.println("Se va a llevar a un cofre de almacenamiento. ");
					
					boolean llego = false;
					boolean hayAlmacenamiento = false;
					int cantidad = 0;

					while (true) {

						for (Cofre cofre : cofres) {
							if (cofre.almacena()) {
								hayAlmacenamiento = true;

								Map<Robot, Set<Nodo>> ruta = planearRuta(cofreOfrecido, cofre);

								if (ruta == null)
									continue;

								Map.Entry<Robot, Set<Nodo>> entrada = ruta.entrySet().iterator().next();
								Robot robot = entrada.getKey();
								Set<Nodo> nodos = entrada.getValue();

								cantidad = Math.min(robot.getCapacidad(), ofrecido.getCantidad());

								System.out.print("\t\tEl robot " + robot.getId()
										+ " va a llevar el exceso al cofre de almacenamiento " + cofre.getId() + ". ");
								System.out.print("LLevara " + cantidad + " unidades");

								atenderPedido(robot, nodos, cofreOfrecido, cofre, item, cantidad);

								llego = true;
								break;
							}
						}

						if (!hayAlmacenamiento)
							throw new IllegalArgumentException("NO HAY COFRES DE ALMACENAMIENTO");

						if (!llego)
							throw new IllegalArgumentException("NINGUN ROBOT LLEGA");
						else {
							if (ofrecido.getCantidad() != cantidad) {
								System.out.println(
										"\t\tQuedan excesos por llevar, porque el robot no tenia suficiente capacidad.");

								ofrecido.setCantidad(ofrecido.getCantidad() - cantidad);
								llego = false;
							} else
								break;
						}
					}
				}

			}

		}

		for (Pedido solicitado : solicitados) {
			if (solicitado.getCantidad() != 0) {
				System.out.println("\nSolicitud incompleta: " + solicitado);
				// si queda algo el sistema no tiene solucion
			}
		}
	}

	public Set<Cofre> getCofres() {
		return cofres;
	}

	public void setCofres(Set<Cofre> cofres) {
		this.cofres = cofres;
	}

	public int getId() {
		return id;
	}

	public Set<Robot> getRobots() {
		return robots;
	}

	public void setRobots(Set<Robot> robots) {
		this.robots = robots;
	}

	public Set<Robopuerto> getRobopuertos() {
		return robopuertos;
	}

	public void setRobopuertos(Set<Robopuerto> robopuertos) {
		this.robopuertos = robopuertos;
	}

	public Set<Pedido> getSolicitados() {
		return solicitados;
	}

	public void setSolicitados(Set<Pedido> solicitados) {
		this.solicitados = solicitados;
	}

	public PriorityQueue<Pedido> getOfrecidos() {
		return ofrecidos;
	}

	public void setOfrecidos(PriorityQueue<Pedido> ofrecidos) {
		this.ofrecidos = ofrecidos;
	}

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
