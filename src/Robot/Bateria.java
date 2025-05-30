package Robot;

public class Bateria {
	private final int celulasMaximas;
	private int celulas;
	private final int factorConsumo; // cantidad de celulas por unidad de distancia
	
	public Bateria(int celulasMaximas, int celulas, int factorConsumo) {
		super();
		this.celulasMaximas = celulasMaximas;
		this.celulas = celulas;
		this.factorConsumo = factorConsumo;
	}

	public int getCelulasMaximas() {
		return celulasMaximas;
	}

	public int getCelulas() {
		return celulas;
	}

	public void recargar() {
		celulas = celulasMaximas;
	}

	public void consumir(int celulas) {
		this.celulas -= celulas;
	}

	public int celulasNecesarias(double distancia) {
		return (int) (Math.ceil(distancia) * factorConsumo);
	}
}
