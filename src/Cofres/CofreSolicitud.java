package Cofres;

public class CofreSolicitud extends Cofre {
	protected void validar() {
        if (ofrece != null && !ofrece.isEmpty())
            throw new IllegalArgumentException("Un cofre de solicitud no puede ofrecer");

        if (almacenamiento != null && !almacenamiento.isEmpty())
            throw new IllegalArgumentException("Un cofre de solicitud no puede almacenar");
    }
	
	@Override
    public String toString() {
        return "CofreSolicitud: " + id + super.toString() + ", solicita: " + solicita;
    }
}
