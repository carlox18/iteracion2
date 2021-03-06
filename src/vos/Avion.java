package vos;

public class Avion {
	public static final String CARGA = "Carga";
	public static final String VIAJEROS = "Viajeros";
	private String numSerie;
	private String modelo;
	private String marca;
	private int anioFabrica;
	private String tipo;
	public Avion(String numSerie, String modelo, String marca, int anioFabrica, String tipo) {
		super();
		this.numSerie = numSerie;
		this.modelo = modelo;
		this.marca = marca;
		this.anioFabrica = anioFabrica;
		this.setTipo(tipo);
	}
	public String getNumSerie() {
		return numSerie;
	}
	public void setNumSerie(String numSerie) {
		this.numSerie = numSerie;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public int getAnioFabrica() {
		return anioFabrica;
	}
	public void setAnioFabrica(int anioFabrica) {
		this.anioFabrica = anioFabrica;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
}
