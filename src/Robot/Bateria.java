package Robot;

public class Bateria {
	private int celulasMaximas;
	private int celulas;
	private int factorConsumo = 1; // cantidad de celulas por unidad de distancia

	// CONSTRUCTOR

	public Bateria(int celulasMaximas) {
		if (celulasMaximas <= 0)
			throw new IllegalArgumentException("La batería debe tener al menos una célula");

		this.celulasMaximas = celulasMaximas;
		this.celulas = celulasMaximas;
	}

	public Bateria() {
		this.celulasMaximas = 1;
		this.celulas = 1;
	}

	// ACCIONES

	public void recargar() {
		celulas = celulasMaximas;
	}

	public void descargar(int cantidad) {
		if (cantidad < 0)
			throw new IllegalArgumentException("No se puede descargar una cantidad negativa");

		if (cantidad > celulas)
			throw new IllegalArgumentException("La batería no tiene suficientes células para descargar");

		celulas -= cantidad;
	}

	public int celulasNecesarias(double distancia) {
		if (distancia < 0)
			throw new IllegalArgumentException("La distancia no puede ser negativa");
		
		return (int) (Math.ceil(distancia) * factorConsumo);
	}

	// SETTERS

	public void setCelulasMaximas(int celulasMaximas) {
		if (celulasMaximas <= 0)
			throw new IllegalArgumentException("La batería debe tener al menos una célula");

		this.celulasMaximas = celulasMaximas;
	}

	// GETTERS

	public int getCelulasMaximas() {
		return celulasMaximas;
	}

	public int getCelulas() {
		return celulas;
	}

	// TO STRING

	@Override
	public String toString() {
		return celulas + "/" + celulasMaximas;
	}
}
