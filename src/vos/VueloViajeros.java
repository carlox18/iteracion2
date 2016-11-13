package vos;

import java.util.Date;


public class VueloViajeros extends Vuelo {
	public final static String TIPO="Viaje";
	private double costoEjec;
	private double costoEcon;
	public VueloViajeros(int id, Date horaSalida, Date horaLlegada, int frecuencia, String tipoViaje, String aerolinea,
			String avion, Aeropuerto aeropuertoSA, Aeropuerto aeropuertoLL, double distancia, String duracion, boolean realizado, double costoEjec, double costoEcon) {
		super(id, horaSalida, horaLlegada, frecuencia, tipoViaje, aerolinea, avion, aeropuertoSA, aeropuertoLL, distancia,
				duracion, realizado,TIPO);
		this.costoEjec = costoEjec;
		this.setCostoEcon(costoEcon);
		// TODO Auto-generated constructor stub
	}
	public double getCostoEjec() {
		return costoEjec;
	}
	public void setCostoEjec(double costoEjec) {
		this.costoEjec = costoEjec;
	}
	public double getCostoEcon() {
		return costoEcon;
	}
	public void setCostoEcon(double costoEcon) {
		this.costoEcon = costoEcon;
	}

}