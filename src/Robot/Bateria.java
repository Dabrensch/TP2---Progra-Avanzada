package Robot;

public class Bateria {
	private int celulasMaximas;
	private int celulas;
	private int factorConsumo = 1; // cantidad de celulas por unidad de distancia

	// CONSTRUCTOR

	public Bateria(int celulasMaximas) {
		if (celulasMaximas <= 0)
			throw new IllegalArgumentException("La bateria no puede ser menor o igual a 0");

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

	public void descargar(int celulas) {
		if (this.celulas - celulas < 0)
			throw new IllegalArgumentException("La bateria no puede quedar negativa");

		this.celulas -= celulas;
	}

	public int celulasNecesarias(double distancia) {
		return (int) (Math.ceil(distancia) * factorConsumo);
	}

	// SETTERS

	public void setCelulasMaximas(int celulasMaximas) {
		if (celulasMaximas <= 0)
			throw new IllegalArgumentException("Argumentos invalidos");

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
