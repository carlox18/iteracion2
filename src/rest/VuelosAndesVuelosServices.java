/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: VideoAndes
 * Autor: Juan Felipe García - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package rest;


import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import tm.VuelosAndesMaster;
import vos.ConsultaTraficoAereo;
import vos.ConsultaViajes;
import vos.ListaVuelos;
import vos.Viaje;
import vos.Vuelo;
import vos.ViajeViajeros;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/VuelAndes/rest/videos/...
 */
@Path("vuelos")
public class VuelosAndesVuelosServices {

	// Servicios REST tipo GET:


	/**
	 * Atributo que usa la anotación @Context para tener el ServletContext de la conexión actual.
	 */
	@Context
	private ServletContext context;

	/**
	 * Método que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}


	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}


//	@PUT
//	@Path("vuelo/{idVuelo}/AsociarAvion/{idAvion}")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response asociarAvion(@javax.ws.rs.PathParam("idVuelo") int idVuelo, @javax.ws.rs.PathParam("idAvion") String Avion){
//		VuelosAndesMaster tm=new VuelosAndesMaster(getPath());
//		Vuelo vuelo=null;
//		try{
//			tm.asociarVuelo(idVuelo, Avion);
//			vuelo=tm.darVuelo(idVuelo);
//		}
//		catch (Exception e) {
//			return Response.status(500).entity(doErrorMessage(e)).build();
//		}
//		return Response.status(200).entity(vuelo).build();
//	}



	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response darVuelos(){
		VuelosAndesMaster tm= new VuelosAndesMaster(getPath());
		ListaVuelos vuelos;
		try{
			vuelos=tm.darVuelos();
		}
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(vuelos).build();
	}

	@GET
	@Path("aeropuerto/{idAeropuerto}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response darVuelosAeropuerto(@javax.ws.rs.PathParam("idAeropuerto") String idAeropuerto){
		VuelosAndesMaster tm= new VuelosAndesMaster(getPath());
		ArrayList<Vuelo> vuelos= new ArrayList<>();
		try{
			vuelos=tm.darVuelosAeropuerto(idAeropuerto);
		}
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(vuelos).build();
	}
	
	/** 
	 * RFC3 Consultar viaje
	 * @param idViajero
	 * @return
	 */
	@GET
	@Path("viajero/{idViajero}/{tipoIdViajero}/consultaViaje")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarViajesViajero(@javax.ws.rs.PathParam("idViajero") int idViajero, @javax.ws.rs.PathParam("tipoIdViajero") String tipoIdViajero){
		VuelosAndesMaster tm= new VuelosAndesMaster(getPath());
		ArrayList<ConsultaViajes> consulta= null; 
		try{
			consulta=tm.consultarViajesViajero(idViajero, tipoIdViajero);
		}
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(consulta).build();
	}


	
	@GET
	@Path("vuelosMasPopulares")
	@Produces(MediaType.APPLICATION_JSON)
	public Response vuelosMasPopulares(){
		VuelosAndesMaster tm= new VuelosAndesMaster(getPath());
		ArrayList<Vuelo> vuelos= new ArrayList<>();
		try{
			vuelos=tm.darVueloMasPopulares();
		}
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(vuelos).build();
	}

	@PUT
	@Path("vuelo/{idVuelo}/RegistrarVuelo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarVueloRealizado(@javax.ws.rs.PathParam("idVuelo") String idVuelo)
	{
		Vuelo vuelo = null;
		VuelosAndesMaster tm= new VuelosAndesMaster(getPath());
		try{
			tm.registrarVuelo(idVuelo);
			vuelo = tm.darVuelo(idVuelo);
		}
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(vuelo).build();
	}

	@POST
	@Path("vuelo/{idVuelo}/GenerarReporte")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String generarReporteVuelo(@javax.ws.rs.PathParam("idVuelo") String idVuelo)
	{
		double resp = 0;
		VuelosAndesMaster tm= new VuelosAndesMaster(getPath());
		try{
			resp = tm.generarReporteVuelo(idVuelo, new Date());
		}
		catch (Exception e) {
			return e.getMessage();
		}
		return "El costo total es de: "+resp; 
	}

	/**
	 * RF15. Cancelar viaje
	 * @param idVuelo
	 * @return
	 */
	@DELETE
	@Path("/cancelarVuelo/{idVuelo}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelarVuelo(@javax.ws.rs.PathParam("idVuelo") String idVuelo) {
		VuelosAndesMaster tm = new VuelosAndesMaster(getPath());
		ViajeViajeros viaje = null;
		try {
			viaje = tm.deleteViaje(idVuelo);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(viaje).build();
	}
	
	/**iter 4 RF1*/
	@GET 
	@Path("aeropuerto/{idAeropuerto}/{tipoParametro}/{parametro}/{organizacion}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response darVueloAeropuertoParametroOrganizado(@javax.ws.rs.PathParam("idAeropuerto") String idAeropuerto,@javax.ws.rs.PathParam("tipoParametro") String tipoParam,@javax.ws.rs.PathParam("parametro") String param,@javax.ws.rs.PathParam("organizacion") String organizacion)
	{
		VuelosAndesMaster tm = new VuelosAndesMaster(getPath());
		ArrayList<Viaje> consulta = null;
		try {
			consulta = tm.darVueloAeropuertoParametroOrganizado(idAeropuerto, tipoParam, param, organizacion);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(consulta).build();

	}
	

	/**iter 4 RF2*/
	@GET 
	@Path("aeropuerto/{idAeropuerto}/{dia1}/{mes1}/{anio1}/hasta/{dia2}/{mes2}/{anio2}/aerolinea/{aerolinea}/avion/{avion}/salida/{horaSal}/llegada/{horaLleg}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response darVueloAeropuertoNoParametros(@javax.ws.rs.PathParam("idAeropuerto") String idAeropuerto,@javax.ws.rs.PathParam("dia1") int dia1,@javax.ws.rs.PathParam("mes1") int mes1,@javax.ws.rs.PathParam("anio1")int anio1,
			@javax.ws.rs.PathParam("dia2")int dia2,@javax.ws.rs.PathParam("mes2")int mes2,@javax.ws.rs.PathParam("anio2")int anio2,@javax.ws.rs.PathParam("aerolinea") String aerolinea, 
			@javax.ws.rs.PathParam("avion") String avion, @javax.ws.rs.PathParam("horaSal") String horaSal,@javax.ws.rs.PathParam("horaLleg") String horaLleg)
	{
		VuelosAndesMaster tm = new VuelosAndesMaster(getPath());
		ArrayList<Viaje> consulta = null;
		try {
			consulta = tm.darVuelosAeropuertoNoParametros(idAeropuerto, dia1, mes1, anio1, dia2, mes2, anio2,aerolinea,avion,horaSal,horaLleg);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(consulta).build();

	}
	/**iter 4 RF3*/
	@GET 
	@Path("consultarViajes/clase/{clase}/minMillas/{millas}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultaViajesDeViajeros(@javax.ws.rs.PathParam("clase") String clase,@javax.ws.rs.PathParam("millas") int millas)
	{
		VuelosAndesMaster tm = new VuelosAndesMaster(getPath());
		ArrayList<ConsultaViajes> consulta = null;
		try {
			consulta = tm.consultarViajesViajeroParametros( clase, millas);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(consulta).build();
	}

	
	/**iter 4 RF4*/
	@GET 
	@Path("consultaTrafico/{ciudad1}/{ciudad2}/desde/{dia1}/{mes1}/{anio1}/hasta/{dia2}/{mes2}/{anio2}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultaTraficoEntreCiudades(@javax.ws.rs.PathParam("ciudad1") String ciudad1, @javax.ws.rs.PathParam("ciudad2") String ciudad2,
			@javax.ws.rs.PathParam("dia1") int dia1,@javax.ws.rs.PathParam("mes1") String mes1,@javax.ws.rs.PathParam("anio1")int anio1,
			@javax.ws.rs.PathParam("dia2")int dia2,@javax.ws.rs.PathParam("mes2")String mes2,@javax.ws.rs.PathParam("anio2")int anio2)
	{
		VuelosAndesMaster tm = new VuelosAndesMaster(getPath());
		ConsultaTraficoAereo consulta = null;
		try {
			consulta = tm.consultarTraficoAereoCiudades(ciudad1,ciudad2,dia1,mes1,anio1,dia2,mes2,anio2);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(consulta).build();
	}

}
