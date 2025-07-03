package grafo;

import java.util.*;

import nodo.*;

public class EstadoDijkstra {
    private Nodo nodo;
    private double bateriaRestante;
    private double costoAcumulado;
    private boolean yaCargo;
    private boolean yaDescargo;
    private List<Nodo> camino;

    public EstadoDijkstra(Nodo nodo, double bateriaRestante, double costoAcumulado,
                          boolean yaCargó, boolean yaDescargó, List<Nodo> camino) {
        this.nodo = nodo;
        this.bateriaRestante = bateriaRestante;
        this.costoAcumulado = costoAcumulado;
        this.yaCargo = yaCargó;
        this.yaDescargo = yaDescargó;
        this.camino = new ArrayList<>(camino);
    }

	public Nodo getNodo() {
		return nodo;
	}

	public void setNodo(Nodo nodo) {
		this.nodo = nodo;
	}

	public double getBateriaRestante() {
		return bateriaRestante;
	}

	public void setBateriaRestante(double bateriaRestante) {
		this.bateriaRestante = bateriaRestante;
	}

	public double getCostoAcumulado() {
		return costoAcumulado;
	}

	public void setCostoAcumulado(double costoAcumulado) {
		this.costoAcumulado = costoAcumulado;
	}

	public boolean getYaCargo() {
		return yaCargo;
	}

	public void setYaCargó(boolean yaCargo) {
		this.yaCargo = yaCargo;
	}

	public boolean getYaDescargo() {
		return yaDescargo;
	}

	public void setYaDescargo(boolean yaDescargo) {
		this.yaDescargo = yaDescargo;
	}

	public List<Nodo> getCamino() {
		return camino;
	}

	public void setCamino(List<Nodo> camino) {
		this.camino = camino;
	}
    
    
    
    
    
    
    
    
    
}
