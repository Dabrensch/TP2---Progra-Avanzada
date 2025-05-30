package General;

public class Coordenada {
	private int x;
	private int y;
	
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
        return Math.sqrt(this.x - otra.x * this.x - otra.x + this.y - otra.y * this.y - otra.y);
	}
}
