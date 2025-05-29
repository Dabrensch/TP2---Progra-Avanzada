package Clases;

public class Bateria {
	private int capacidad;
	private int celula;
	private int factorConsumo;

	public Bateria(int capacidad, int celula, int factorConsumo) {
		this.capacidad = capacidad;
		this.celula = celula;
		this.factorConsumo = factorConsumo;
	}

	public void recargar() {
		this.celula = capacidad;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public int getCelula() {
		return celula;
	}

	public int getFactorConsumo() {
		return factorConsumo;
	}

}
