package Cofres;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class CofreActivo extends Cofre {

	// CONSTRUCTOR

	protected void validar() {
		if (solicita != null && !solicita.isEmpty())
			throw new IllegalArgumentException("Un cofre activo no puede solicitar");

		if (almacenamiento != null && !almacenamiento.isEmpty())
			throw new IllegalArgumentException("Un cofre activo no puede almacenar");
	}

	// ACCIONES

	@Override
	public void desofrecer(String item) {
		throw new UnsupportedOperationException("Un cofre activo no puede desofrecer.");
	}

	// TIPO

	@Override
	public boolean esActivo() {
		return true;
	}

	// SETTERS

	@Override
	@JsonProperty("solicita")
	public void setSolicita(Map<String, Integer> solicita) {
		throw new UnsupportedOperationException("Un cofre activo no puede solicitar.");
	}

	// GETTERS

	@Override
	public int getPrioridad() {
		return 3;
	}

	@Override
	public String getTipo() {
		return "CofreActivo";
	}

	// TO STRING

	@Override
	public String toString() {
		return "CofreActivo: " + id + super.toString() + ", ofrece: " + ofrece;
	}

}
