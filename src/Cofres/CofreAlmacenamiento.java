package Cofres;

public class CofreAlmacenamiento extends Cofre {
	@Override
    public String toString() {
        return "CofreAlmacenamiento: "+ id + super.toString() + ", almacenamiento: " + almacenamiento;
    }
}
