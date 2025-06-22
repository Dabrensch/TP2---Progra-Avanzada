package Nodo;

import java.util.Objects;

import General.Coordenada;

public abstract class Nodo {

	public double distanciaA(Nodo otro) {
		return this.getUbicacion().distanciaA(otro.getUbicacion());
	}

	public abstract Coordenada getUbicacion();

	public abstract boolean esRobopuerto();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Nodo nodo = (Nodo) o;
		return this.getUbicacion().equals(nodo.getUbicacion());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUbicacion());
	}
}
