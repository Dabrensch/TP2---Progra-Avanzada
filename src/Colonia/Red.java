package Colonia;

import java.util.*;


import Cofres.Cofre;
import Pedido.Pedido;
import Robot.Robot;

public class Red {

	Set<Cofre> cofres = new HashSet<>();
	Set<Robot> robots = new HashSet<>();
	Set<Robopuerto> robopuertos = new HashSet<>();
	
	Set<Pedido> solicitados;
	PriorityQueue<Pedido> ofrecidos;
	


	public Red(Set<Robopuerto> robopuertos, Set<Cofre> cofres, Set<Robot> robots, 
			PriorityQueue<Pedido> ofrecidos, Set<Pedido> solicitados) {
		this.cofres = cofres;
		this.robots = robots;
		this.robopuertos = robopuertos;
		this.solicitados = solicitados;
		this.ofrecidos = ofrecidos;
	}



	public void atenderPedidos() {
		// Esto no contempla carga del robot, ni capacidad de carga. hay que agregarlo.
		
		while(!ofrecidos.isEmpty()) {
			Pedido ofrecido = ofrecidos.poll();
			Cofre cofreOfrecido = ofrecido.getCofre();
			Item item = ofrecido.getItem();
			
			for (Pedido solicitado : solicitados) {
				if(solicitado.getItem() == item) {
					int cantidad = Math.min(ofrecido.getCantidad(), solicitado.getCantidad()); // hay que aplicar alguna logica para saber que cantidad retirar y entregar
					
					Cofre cofreSolicitado = solicitado.getCofre();
					
					// Hay que aplicar algun algoritmo para elegir al robot
					// Que sea el mas cercado al cofre que ofrece y que tenga suficiente bateria para completar el pedido, aunque sea pasando por un lugar de carga
					Robot robot = robots.iterator().next();
					
					// aca se aplica la logica de buscar y entregar, aca hay que agregar la busqueda de la mejor ruta para el robot, teniendo en cuenta los diferentes caminos y sus distancias y la bateria del robot
					robot.moverA(cofreOfrecido);
					robot.cargarItem(cofreOfrecido, item, cantidad);
					robot.moverA(cofreSolicitado);
					robot.descargarItem(cofreSolicitado, item, cantidad);
					
					// tambien hay que cambiar los vectores de ofrece, solicita y inventario de cada robot
					if(solicitado.getCantidad() - cantidad == 0) {
						solicitados.remove(solicitado); // ya no hay solicitados
						
						// comprobar si quedan items para ofrecer
						if(ofrecido.getCantidad() - cantidad == 0) {
							ofrecido.setCantidad(0);
							break; // no hay solicitados ni ofrecidos, se cumplieron ambos pedidos
						} else {
							ofrecido.setCantidad(ofrecido.getCantidad() - cantidad); // faltan ofrecidos, se sigue iterando
						}
					} else {
						ofrecido.setCantidad(0);
						solicitado.setCantidad(solicitado.getCantidad() - cantidad); // se ofrecieron todos los items pero faltan solicitados
						break;
					}
				}
			}
			
			if(ofrecido.getCantidad() != 0) {
				// hay que llevar lo que quedo a un cofre de inventario
			}
			
			
		}
	}



	public Set<Cofre> getCofres() {
		return cofres;
	}



	public void setCofres(Set<Cofre> cofres) {
		this.cofres = cofres;
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
	
	
	
	

}
