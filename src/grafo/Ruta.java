package grafo;

import java.util.*;

import nodo.Nodo;
import robot.Robot;

public class Ruta {
	private Robot robot;
	private List<Nodo> ruta;
	private int costo;

	public Ruta(Robot robot, List<Nodo> ruta, int costo) {
		this.robot = robot;
		this.ruta = ruta;
		this.costo = costo;
	}

	public Robot getRobot() {
		return robot;
	}

	public List<Nodo> getRuta() {
		return ruta;
	}

	public int getCosto() {
		return costo;
	}

}
