package General;

import java.util.Set;

import Nodo.Nodo;
import Robot.Robot;

public class Ruta {
	private Robot robot;
	private Set<Nodo> ruta;
	private int costo;

	public Ruta(Robot robot, Set<Nodo> ruta, int costo) {
		this.robot = robot;
		this.ruta = ruta;
		this.costo = costo;
	}

	public Robot getRobot() {
		return robot;
	}

	public Set<Nodo> getRuta() {
		return ruta;
	}

	public int getCosto() {
		return costo;
	}

}
