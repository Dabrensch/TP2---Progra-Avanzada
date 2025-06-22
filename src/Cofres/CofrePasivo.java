package Cofres;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CofrePasivo extends Cofre {

	// CONSTRUCTOR

	protected void validar() {
		if (solicita != null && !solicita.isEmpty())
			throw new IllegalArgumentException("Un cofre pasivo no puede solicitar");

		if (almacenamiento != null && !almacenamiento.isEmpty())
			throw new IllegalArgumentException("Un cofre pasivo no puede almacenar");
	}

	// SETTERS

	@Override
	@JsonProperty("solicita")
	public void setSolicita(Map<String, Integer> solicita) {
		throw new UnsupportedOperationException("Un cofre de ofrecimiento pasivo no puede solicitar.");
	}

	// GETTERS

	@Override
	public int getPrioridad() {
		return 1;
	}

	@Override
	public String getTipo() {
		return "CofrePasivo";
	}

	// TO STRING

	@Override
	public String toString() {
		return "CofrePasivo: " + id + super.toString() + ", ofrece: " + ofrece;
	}

}
