package Grafo;

import Nodo.*;

public class Adyacente {
    private Nodo nodo;
    private double distancia;

    public Adyacente(Nodo nodo, double distancia) {
        this.nodo = nodo;
        this.distancia = distancia;
    }

    public Nodo getNodo() {
        return nodo;
    }

    public double getDistancia() {
        return distancia;
    }
}