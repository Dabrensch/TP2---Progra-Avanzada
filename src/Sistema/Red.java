package Sistema;

import java.util.*;


import Cofres.Cofre;
import Pedido.Pedido;
import Robot.Robot;

public class Red {
	private static int contador = 1;
    private final int id;
    
    private Set<Cofre> cofres = new HashSet<>();
    private Set<Robot> robots = new HashSet<>();
    private Set<Robopuerto> robopuertos = new HashSet<>();
	
    private Set<Pedido> solicitados;
    private PriorityQueue<Pedido> ofrecidos;
	


	public Red(Set<Robopuerto> robopuertos, Set<Cofre> cofres, Set<Robot> robots, 
			PriorityQueue<Pedido> ofrecidos, Set<Pedido> solicitados) {
		this.id = contador++;
		this.cofres = cofres;
		this.robots = robots;
		this.robopuertos = robopuertos;
		this.solicitados = solicitados;
		this.ofrecidos = ofrecidos;
	}



	public void atenderPedidos() {
		// Esto no contempla carga del robot, ni capacidad de carga. hay que agregarlo.
		
		while(!ofrecidos.isEmpty()) {
			// Sacar de la pila el pedido con mayor prioridad
			Pedido ofrecido = ofrecidos.poll();
			Cofre cofreOfrecido = ofrecido.getCofre();
			String item = ofrecido.getItem();
			
			System.out.println("\nEl cofre " + ofrecido.getCofre().getId() + " ofreció " + ofrecido.getCantidad() + " unidades de " + ofrecido.getItem() + ".");

			
			for (Pedido solicitado : solicitados) {
				// recorrer las solicitudes en busca de un cofre que solicite el item del pedido
				if(solicitado.getItem().equals(item) == true && solicitado.getCantidad() != 0) {
					System.out.println("El cofre " + solicitado.getCofre().getId() + " solicitó " + solicitado.getCantidad() + " unidades de " + solicitado.getItem() + ".");

					int cantidad = Math.min(ofrecido.getCantidad(), solicitado.getCantidad()); // logica para saber que cantidad retirar y entregar
					
					Cofre cofreSolicitado = solicitado.getCofre();
					
					// Hay que aplicar algun algoritmo para elegir al robot
					// Que sea el mas cercado al cofre que ofrece y que tenga suficiente bateria para completar el pedido, aunque sea pasando por un lugar de carga
					Robot robot = robots.iterator().next();
					
					// verificar cuantos viajes debe hacer segun su capacidad maxima
					int viajes = 1;
					while(robot.getCapacidad() < cantidad) {
						cantidad -= robot.getCapacidad();
						viajes ++;
					}
					
					
					if(viajes > 1) {
						System.out.println("El robot " + robot.getId() + " va a realizar el pedido. Debe realizar " + viajes + " viajes debido a su capacidad de carga.");
					} else {
						System.out.println("El robot " + robot.getId() + " va a realizar el pedido.");
					}
					
					for (int i = 0; i < viajes; i++) {
						if(viajes > 1) {
							cantidad = Math.min(robot.getCapacidad(), Math.min(ofrecido.getCantidad(), solicitado.getCantidad()));
						} else {
							cantidad = Math.min(ofrecido.getCantidad(), solicitado.getCantidad());
						}
						
						
						// aca se aplica la logica de buscar y entregar, aca hay que agregar la busqueda de la mejor ruta para el robot, teniendo en cuenta los diferentes caminos y sus distancias y la bateria del robot
						robot.moverA(cofreOfrecido);
						robot.cargarItem(cofreOfrecido, item, cantidad);
						robot.moverA(cofreSolicitado);
						robot.descargarItem(cofreSolicitado, item, cantidad);
					}
						
					
					
					
					// tambien hay que cambiar los vectores de ofrece, solicita y inventario de cada cofre
					if(solicitado.getCantidad() - cantidad == 0) {
						
						solicitado.setCantidad(0); // ya no hay solicitados
						
						// comprobar si quedan items para ofrecer
						if(ofrecido.getCantidad() - cantidad == 0) {
							System.out.println("Se completó todo el pedido.");
							
							ofrecido.setCantidad(0);
							break; // no hay solicitados ni ofrecidos, se cumplieron ambos pedidos
						} else {
							System.out.println("Se completó toda la solicitud, pero faltan items para ofrecer.");
							
							ofrecido.setCantidad(ofrecido.getCantidad() - cantidad); // faltan ofrecidos, se sigue iterando
						}
					} else {
						System.out.println("Se ofrecieron todos los items, pero faltan items solicitados.");

						ofrecido.setCantidad(0);
						solicitado.setCantidad(solicitado.getCantidad() - cantidad); // se ofrecieron todos los items pero faltan solicitados
						break;
					}
					
				}
				
			}
			
			if(ofrecido.getCantidad() != 0) {
				System.out.print("No se pudó completar todo el pedido del cofre " + ofrecido.getCofre().getId() + ", quedaron " + ofrecido.getCantidad() + " unidades de " + ofrecido.getItem() + " sin ofrecer por falta de solicitudes. ");
				// hay que llevar lo que quedo a un cofre de inventario, solo si es un cofre activo
				// hay que agregar una logica para saber a que cofre de inventario llevarlo
				// si en esta situacion no hay cofre de inventario entonces el sistema no tiene solucion
				
			//	Robot robot = robots.iterator().next();
				System.out.println("El robot X va a llevar el exceso al cofre de inventario X.");
			}
			
		}
		
		for (Pedido solicitado : solicitados) {
			if(solicitado.getCantidad() != 0) {
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
	    sb.append("Red: "+ id + "\n");
	    
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
