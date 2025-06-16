package Cofres;

public class CofreAlmacenamiento extends Cofre {
	protected void validar() {
        if (solicita != null && !solicita.isEmpty())
            throw new IllegalArgumentException("Un cofre de almacenamiento no puede solicitar");

        if (ofrece != null && !ofrece.isEmpty())
            throw new IllegalArgumentException("Un cofre de almacenamiento no puede ofrecer");
    }
	
	@Override
	public boolean almacena() {
		return true;
	}
	
	@Override
    public String toString() {
        return "CofreAlmacenamiento: "+ id + super.toString() + ", almacenamiento: " + almacenamiento;
    }
}
