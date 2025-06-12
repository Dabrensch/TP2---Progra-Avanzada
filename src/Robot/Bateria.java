package Robot;

public class Bateria {
	private int celulasMaximas;
	private int celulas;
	private int factorConsumo = 1; // cantidad de celulas por unidad de distancia
	
	public Bateria(int celulasMaximas) {
		this.celulasMaximas = celulasMaximas;
		this.celulas = celulasMaximas;
	}
	
	public Bateria() {
		this.celulasMaximas = 0;
		this.factorConsumo = 0;
		this.celulas = 0;
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
	
	
	@Override
    public String toString() {
        return "Bateria{" +
                "celulas:" + celulas +
                "/" + celulasMaximas +
                '}';
    }
}
