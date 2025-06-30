package cofres;

public class CofreBuffer extends Cofre {

	// CONSTRUCTOR

	protected void validar() {
		if (almacenamiento != null && !almacenamiento.isEmpty())
			throw new IllegalArgumentException("Un cofre buffer no puede almacenar");
	}

	// GETTERS

	@Override
	public String getTipo() {
		return "CofreBuffer";
	}

	@Override
	public int getPrioridad() {
		return 2;
	}

	// TO STRING

	@Override
	public String toString() {
		return "CofreBuffer: " + id + super.toString() + ", ofrece: " + ofrece + ", solicita:" + solicita;
	}
}
