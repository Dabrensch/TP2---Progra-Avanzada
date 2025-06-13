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
	
	private Robopuerto robopuertoIntermedioV2(Bateria bateria, Coordenada ubicacion, Cofre cofre) {
		Set<Robopuerto> robopuertos = this.robopuertos;
		int n = robopuertos.size();

		while (!robopuertos.isEmpty()) {
			Robopuerto robopuertoIntermedio = robopuertoMasCercanoACofre(cofre, robopuertos);

			if (bateria.getCelulas() >= bateria
					.celulasNecesarias(ubicacion.distanciaA(robopuertoIntermedio.getUbicacion()))) {
				return robopuertoIntermedio;
			} else {
				robopuertos.remove(robopuertoIntermedio);
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

		// puede haber un loop infinito, solucionarlo

		// ver si llega al origen
		while (bateria.getCelulas() < (bateria.celulasNecesarias(ubicacion.distanciaA(origen.getUbicacion()))
				+ bateria.celulasNecesarias(ubicacion.distanciaA(robopuertoOrigen.getUbicacion())))) {

			// pasar por nodos intermedios

			Robopuerto robopuertoIntermedio = robopuertoIntermedioV2(bateria, ubicacion, origen);
			if(robopuertoIntermedio == null)
				return null;
			
			ubicacion.setXY(robopuertoIntermedio.getUbicacion().getX(), robopuertoIntermedio.getUbicacion().getY());
			bateria.recargar();
			nodos.add(new NodoRobopuerto(robopuertoIntermedio));
		}
		// Ir al origen
		bateria.descargar(bateria.celulasNecesarias(ubicacion.distanciaA(origen.getUbicacion())));
		ubicacion.setXY(origen.getUbicacion().getX(), origen.getUbicacion().getY());
		nodos.add(new NodoCofre(origen));


		// ver si llega al destino
		while (bateria.getCelulas() < (bateria
				.celulasNecesarias(ubicacion.distanciaA(destino.getUbicacion()))
				+ bateria.celulasNecesarias(
						destino.getUbicacion().distanciaA(robopuertoDestino.getUbicacion())))) {

			// pasar por nodos intermedios

			Robopuerto robopuertoIntermedio = robopuertoIntermedioV2(bateria, ubicacion, destino);
			if(robopuertoIntermedio == null)
				return null;
			

			ubicacion.setXY(robopuertoIntermedio.getUbicacion().getX(), robopuertoIntermedio.getUbicacion().getY());
			bateria.recargar();
			nodos.add(new NodoRobopuerto(robopuertoIntermedio));
		}
		// Ir al destino
		nodos.add(new NodoCofre(destino));
		nodos.add(new NodoRobopuerto(robopuertoDestino));

		
		return nodos;
		
	}

	private Robot planearRuta(Cofre origen, Cofre destino, Set<Robot> robots) {
		int n = robots.size();

		Robot elegido;

		for (int i = 0; i < n; i++) {
			elegido = elegirRobotMasCercano(origen, robots);

			Set<Nodo> ruta = rutaRobot(origen, destino, elegido);
			
			if (ruta == null) {
				robots.remove(elegido);
			} else
				return elegido;
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

	private Robopuerto robopuertoIntermedio(Robot robot, Cofre cofre) {
		Set<Robopuerto> robopuertos = this.robopuertos;
		int n = robopuertos.size();

		while (!robopuertos.isEmpty()) {
			Robopuerto robopuertoIntermedio = robopuertoMasCercanoACofre(cofre, robopuertos);

			if (robot.getBateria().getCelulas() >= robot.getBateria()
					.celulasNecesarias(robot.getUbicacion().distanciaA(robopuertoIntermedio.getUbicacion()))) {
				return robopuertoIntermedio;
			} else {
				robopuertos.remove(robopuertoIntermedio);
			}
		}

		return null;
	}

	public void atenderPedidos() {
		// Esto no contempla carga del robot, ni capacidad de carga. hay que agregarlo.

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
						Robot robot = elegirRobotMasCercano(cofreOfrecido, robots); // elige al robot mas cercano al
																					// cofre de origen, hay que agregar
																					// que si este no puede llegar elija
																					// al siguiente mas cercano y asi
																					// sucesivamente hasta que pueda

						// abria que intentar con todos los robots lo que hay ams abajo pero de forma
						// simulada para ver si llegan a algo

						System.out.print("\t\tEl robot " + robot.getId() + " va a realizar el pedido. ");

						cantidad = Math.min(robot.getCapacidad(),
								(Math.min(ofrecido.getCantidad(), solicitado.getCantidad()))); // logica para saber que
																								// cantidad retirar y
																								// entregar

						System.out.print("LLevara " + cantidad + " unidades");

						System.out.println("\t\t\t\tUbicacion actual: " + robot.getUbicacion() + ", Bateria actual: "
								+ robot.getBateria());

						// ------ BATERIA ------

						// comprobar si llega al cofre origen (cofreOfrecido) +
						// distanciaARobopuertoOrigen
						// si llega ir directamente
						// si no llega buscar el robopuerto intermedio mas cercano al cofre que llegue
						// el robot con su bateria
						// asi sucesivamente hasta poder llegar al origen

						// comprobar si llega al cofre destino (cofreSolicitado) +
						// distanciaArobopuertoDestino
						// si llega ir directamente
						// si no llega buscar el robopuerto intermedio mas cercano al cofre que llegue
						// el robot con su bateria
						// asi sucesivamente hasta poder llegar al destino
						// luego ir al robopuerto destino

						Robopuerto robopuertoOrigen = robopuertoMasCercanoACofre(cofreOfrecido, robopuertos);
						Robopuerto robopuertoDestino = robopuertoMasCercanoACofre(cofreSolicitado, robopuertos);

						// puede haber un loop infinito, solucionarlo

						// ver si llega al origen
						while (robot.getBateria().getCelulas() < (robot.getBateria()
								.celulasNecesarias(robot.getUbicacion().distanciaA(cofreOfrecido.getUbicacion()))
								+ robot.getBateria().celulasNecesarias(
										cofreOfrecido.getUbicacion().distanciaA(robopuertoOrigen.getUbicacion())))) {

							// pasar por nodos intermedios

							Robopuerto robopuertoIntermedio = robopuertoIntermedio(robot, cofreOfrecido);
							System.out.print("\t\t\tVa a cargarse al robopuerto " + robopuertoIntermedio.getId()
									+ " en " + robopuertoIntermedio.getUbicacion());
							robot.moverA(robopuertoIntermedio);

						}
						// Ir al origen
						robot.moverA(cofreOfrecido);
						robot.cargarItem(cofreOfrecido, item, cantidad);

						System.out.print("\t\t\tAgarro los items que ofreció el cofre");
						System.out.println("\t\t\t\tUbicacion actual: " + robot.getUbicacion() + ", Bateria actual: "
								+ robot.getBateria());

						// ver si llega al destino
						while (robot.getBateria().getCelulas() < (robot.getBateria()
								.celulasNecesarias(robot.getUbicacion().distanciaA(cofreSolicitado.getUbicacion()))
								+ robot.getBateria().celulasNecesarias(
										cofreSolicitado.getUbicacion().distanciaA(robopuertoDestino.getUbicacion())))) {

							// pasar por nodos intermedios

							Robopuerto robopuertoIntermedio = robopuertoIntermedio(robot, cofreSolicitado);

							System.out.print("\t\t\tVa a cargarse al robopuerto " + robopuertoIntermedio.getId()
									+ " en " + robopuertoIntermedio.getUbicacion());
							robot.moverA(robopuertoIntermedio);

						}
						// Ir al destino
						robot.moverA(cofreSolicitado);
						robot.descargarItem(cofreSolicitado, item, cantidad);

						System.out.print("\t\t\tDejo los items que solicitó el cofre");
						System.out.println("\t\t\t\tUbicacion actual: " + robot.getUbicacion() + ", Bateria actual: "
								+ robot.getBateria());

						// Ir al robopuerto destino
						System.out.print("\t\t\tVa a cargarse al robopuerto " + robopuertoDestino.getId() + " en "
								+ robopuertoDestino.getUbicacion() + ". ");
						robot.moverA(robopuertoDestino); // se recarga automaticamente

						if (solicitado.getCantidad() != cantidad && ofrecido.getCantidad() != cantidad) {
							System.out.println("\t\tFaltan ofrecidos y solicitados.");

							ofrecido.setCantidad(ofrecido.getCantidad() - cantidad);
							solicitado.setCantidad(solicitado.getCantidad() - cantidad);
						} else {
							break;
						}

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
				// hay que llevar lo que quedo a un cofre de inventario, solo si es un cofre
				// activo
				// hay que agregar una logica para saber a que cofre de inventario llevarlo
				// si en esta situacion no hay cofre de inventario entonces el sistema no tiene
				// solucion

				// Robot robot = robots.iterator().next();
				System.out.println("El robot X va a llevar el exceso al cofre de inventario X.");
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
