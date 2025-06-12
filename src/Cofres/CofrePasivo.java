package Cofres;

public class CofrePasivo extends Cofre {
	protected void validar() {
        if (solicita != null && !solicita.isEmpty())
            throw new IllegalArgumentException("Un cofre pasivo no puede solicitar");

        if (almacenamiento != null && !almacenamiento.isEmpty())
            throw new IllegalArgumentException("Un cofre pasivo no puede almacenar");
    }
	
	@Override
    public String toString() {
        return "CofrePasivo: " + id + super.toString() + ", ofrece: " + ofrece;
    }
}
