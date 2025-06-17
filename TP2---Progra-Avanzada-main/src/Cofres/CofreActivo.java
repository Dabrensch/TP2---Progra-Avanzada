package Cofres;

import java.util.Map;

import com.fasterxml.jackson.annotation.*;

import General.Coordenada;

public class CofreActivo extends Cofre {
	
	
	protected void validar() {
        if (solicita != null && !solicita.isEmpty())
            throw new IllegalArgumentException("Un cofre activo no puede solicitar");

        if (almacenamiento != null && !almacenamiento.isEmpty())
            throw new IllegalArgumentException("Un cofre activo no puede almacenar");
    }
	
	@Override
	public void desofrecer(String item) {
		throw new UnsupportedOperationException("Un cofre activo no puede desofrecer.");
	}
	
	@Override
	@JsonProperty("solicita")	
	public void setSolicita(Map<String, Integer> solicita) {
		throw new UnsupportedOperationException("Un cofre activo no puede solicitar.");
	}
	
	public String getTipo() {
		return "Cofre Activo";
	}
	
	@Override
	public boolean esActivo() {
		return true;
	}
	
	@Override
	public int getPrioridad() {
		return 3;
	}
	
	@Override
    public String toString() {
        return "CofreActivo: " + id + super.toString() + ", ofrece: " + ofrece;
    }
}
