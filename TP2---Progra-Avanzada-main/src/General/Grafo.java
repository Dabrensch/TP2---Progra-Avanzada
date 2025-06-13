package General;

import java.util.*;

public class Grafo {
	private List<Nodo> nodos = new ArrayList<>();
	private Map<Nodo, List<Arista>> adyacencias = new HashMap<>();

    public void agregarNodo(Nodo n) {
        nodos.add(n);
        adyacencias.put(n, new ArrayList<>());
    }

    public void agregarArista(Nodo origen, Nodo destino, double peso) {
        adyacencias.get(origen).add(new Arista(destino, peso));
        adyacencias.get(destino).add(new Arista(origen, peso)); // Grafo no dirigido
    }
    
    
    public List<Nodo> getNodos() {
		return nodos;
	}
    
    
    
    
    public void mostrarGrafo() {
        for (Nodo nodo : nodos) {
            System.out.print(nodo  + " -> ");
            List<Arista> adyacentes = adyacencias.get(nodo);
            for (Arista arista : adyacentes) {
                Nodo destino = arista.getDestino();
                System.out.print("(" + destino + ", distancia: " 
                    + String.format("%.2f", arista.getPeso()) + ") ");
            }
            System.out.println();
        }
    }
}
