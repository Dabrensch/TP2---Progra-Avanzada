package Colonia;

import java.util.*;

import Cofres.*;
import General.*;
import Pedido.*;
import Robot.*;

public class Colonia {
	// del archivo de entrada:
	Set<Cofre> cofres = new HashSet<>();
	Set<Robot> robots = new HashSet<>();
	Set<Robopuerto> robopuertos = new HashSet<>();

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
						    for (Map.Entry<Item, Integer> entry : c.getOfrece().entrySet()) {
						        Item item = entry.getKey();
						        int cantidad = entry.getValue();
						        Pedido pedido = new PedidoOfrecido(c, item, cantidad); 
						        ofrecidos.add(pedido);
						    }
						    
						    // Pedidos solicitados
						    for (Map.Entry<Item, Integer> entry : c.getSolicita().entrySet()) {
						        Item item = entry.getKey();
						        int cantidad = entry.getValue();
						        Pedido pedido = new PedidoSolicitado(c, item, cantidad); 
						        solicitados.add(pedido);
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
			red.atenderPedidos();
		}
		
		
		if (!cofresNoAsignados.isEmpty()) {
			System.out.println("Cofres fuera de cobertura: " + cofresNoAsignados);
		}
	}
}
