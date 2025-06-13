package Cofres;

import java.util.Map;

import General.Coordenada;

public class CofreActivo extends Cofre {
	protected void validar() {
        if (solicita != null && !solicita.isEmpty())
            throw new IllegalArgumentException("Un cofre activo no puede solicitar");

        if (almacenamiento != null && !almacenamiento.isEmpty())
            throw new IllegalArgumentException("Un cofre activo no puede almacenar");
    }
	
	
	@Override
    public String toString() {
        return "CofreActivo: " + id + super.toString() + ", ofrece: " + ofrece;
    }
}
