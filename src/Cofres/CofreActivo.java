package Cofres;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import General.Coordenada;

public class CofreActivo extends Cofre {
	protected void validar() {
        if (solicita != null && !solicita.isEmpty())
            throw new IllegalArgumentException("Un cofre activo no puede solicitar");

        if (almacenamiento != null && !almacenamiento.isEmpty())
            throw new IllegalArgumentException("Un cofre activo no puede almacenar");
    }
	
	@Override
	@JsonProperty("solicita")	
	public void setSolicita(Map<String, Integer> solicita) {
		throw new UnsupportedOperationException("Un cofre activo no puede solicitar.");
	}
	
	public String getTipo() {
		return "Cofre Activo";
	}

    public String toString() {
        return "CofreActivo: " + id + super.toString() + ", ofrece: " + ofrece;
    }
}
