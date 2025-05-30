package General;

import java.util.Objects;

public class Coordenada {
	private int x;
	private int y;
	
	public Coordenada() {
    }
	
	public Coordenada(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public double distanciaA(Coordenada otra) {
        return Math.sqrt((this.x - otra.x) * (this.x - otra.x) + (this.y - otra.y) * (this.y - otra.y));
	}
	
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    Coordenada that = (Coordenada) obj;
	    return this.x == that.x && this.y == that.y;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(x, y);
	}
	
	
	@Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
