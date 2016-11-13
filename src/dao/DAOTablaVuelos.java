package dao;


import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vos.Aerolinea;
import vos.Aeropuerto;
import vos.Avion;
import vos.AvionCarga;
import vos.AvionViajeros;
import vos.Carga;
import vos.ConsultaViajes;
import vos.Reserva;
import vos.ReservaCarga;
import vos.ReservaViajeros;
import vos.ReservaViajerosMultiple;
import vos.ReservaViajeros;
import vos.Vuelo;
import vos.VueloCarga;
import vos.VueloViajeros;




public class DAOTablaVuelos {



	private ArrayList<Vuelo> vuelos;

	private ArrayList<Aeropuerto> aeropuertos;

	private ArrayList<Avion> aviones;




	private Connection conn;


	public DAOTablaVuelos()  {
		vuelos = new ArrayList<Vuelo>();
		aeropuertos = new ArrayList<Aeropuerto>();
		aviones = new ArrayList<Avion>();

	}


	public void cerrarVuelos() {
		for(Object ob : vuelos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}


	public void setConn(Connection con){
		this.conn = con;
	}


	public ArrayList<Avion> darAviones() throws SQLException, Exception {
		aviones = new ArrayList<Avion>();

		String sql = "SELECT * FROM ISIS2304A131620.AVION";

		PreparedStatement prepStmt = conn.prepareStatement(sql);

		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String id= rs.getString("NUM_SERIE");
			String modelo = rs.getString("MODELO");
			String marca = rs.getString("MARCA");
			int anioFab = rs.getInt("ANIO_FABRICACION");
			String tipo = rs.getString("TIPO");
			aviones.add(new Avion(id,modelo,marca,anioFab, tipo));
		}
		return aviones;
	}


	public ArrayList<Aeropuerto> darAeropuertos() throws SQLException, Exception {
		aeropuertos = new ArrayList<Aeropuerto>();

		String sql = "SELECT * FROM ISIS2304A131620.AEROPUERTO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);

		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String id= rs.getString("COD_IATA");
			String nombre = rs.getString("NOMBRE");
			String tipo = rs.getString("TIPO");
			String nombreCiudad = rs.getString("NOMBRE_CIUDAD");
			aeropuertos.add(new Aeropuerto(id,nombre,tipo,nombreCiudad));
		}
		return aeropuertos;
	}

	public ArrayList<Vuelo> darVuelos() throws SQLException, Exception {
		ArrayList<Vuelo> vuelos = new ArrayList<Vuelo>();

		String sql = "SELECT * FROM ISIS2304A131620.VUELO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);

		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id= rs.getInt("ID");
			Date horaSalida= rs.getDate("HORA_SALIDA");
			Date horaLlegada=rs.getDate("HORA_LLEGADA");
			String duracion =rs.getString("DURACION");
			double distancia= rs.getDouble("DISTANCIA");
			int frecuencia =rs.getInt("FRECUENCIA");
			String tipoViaje= rs.getString("TIPO_VIAJE");
			String aerolinea = rs.getString("AEROLINEA");
			String avion = rs.getString("AVION");
			String aeropuertoLL= rs.getString("AEROPUERTO_SALIDA");
			String aeropuertoSA= rs.getString("AEROPUERTO_LLEGADA");
			Aeropuerto aeropuertoLLegada = getAeropuerto(aeropuertoLL);
			Aeropuerto aeropuertoSalida= getAeropuerto(aeropuertoSA);
			String realizado = rs.getString("REALIZADO");
			boolean realizadoo = false;
			if (realizado.equals("N"))
			{}
			else if(realizado.equals("S"))
			{
				realizadoo = true;
			}
			String tipoVuelo = rs.getString("TIPO_VUELO");
			vuelos.add(new Vuelo(id, horaSalida, horaLlegada, frecuencia, tipoViaje, aerolinea, avion, aeropuertoSalida, aeropuertoLLegada, distancia, duracion, realizadoo,tipoVuelo));
		}
		return vuelos;
	}


	public ArrayList<VueloViajeros> getVuelosViajeros() throws SQLException, ParseException {
		ArrayList<VueloViajeros> vueloViajeros = new ArrayList<VueloViajeros>();
		String sql="SELECT * FROM ISIS2304A131620.VUELO_VIAJEROS";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		while(rs.next()){
			int id = rs.getInt("ID");
			double costoEjec = rs.getDouble("COSTO_EJECUTIVO");
			double costoEcon = rs.getDouble("COSTO_ECONOMICO");
			Vuelo vueloT = getVuelo(id);

			vueloViajeros.add(new VueloViajeros(vueloT.getId(), vueloT.getHoraSalida(), vueloT.getHoraLlegada(), vueloT.getFrecuencia(), vueloT.getTipoViaje(),vueloT.getAerolinea(), vueloT.getAvion(), vueloT.getAeropuertoSA(), vueloT.getAeropuertoLL(), vueloT.getDistancia(), vueloT.getDuracion(), vueloT.isRealizado(), costoEjec, costoEcon));					
		}
		return vueloViajeros;
	}

	public void updateVuelo(Vuelo vuelo) throws SQLException{
		String sql ="UPDATE ISIS2304A131620.VUELO SET AVION="+vuelo.getAvion()+"WHERE ID="+vuelo.getId();

		PreparedStatement prepStmt=conn.prepareStatement(sql);
		prepStmt.executeQuery();

	}
	public Aeropuerto getAeropuerto(String idAeropuerto) throws SQLException {
		Aeropuerto aeropuerto=null;
		String sql="SELECT * FROM ISIS2304A131620.AEROPUERTO WHERE COD_IATA ='"+idAeropuerto+"'";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		if(rs.next()){
			String id= rs.getString("COD_IATA");
			String nombre = rs.getString("NOMBRE");
			String tipo = rs.getString("TIPO");
			String nombreCiudad = rs.getString("NOMBRE_CIUDAD");
			aeropuerto = new Aeropuerto(id,nombre,tipo,nombreCiudad);

		}
		return aeropuerto;
	}


	public void deleteVuelo(Vuelo vuelo) throws SQLException, Exception {

		String sql = "DELETE FROM ISIS2304A131620.VUELO";
		sql += " WHERE id = " + vuelo.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);

		prepStmt.executeQuery();

	}



	public VueloViajeros deleteVueloViajeros(int vueloViajeros) throws Exception
	{
		VueloViajeros vuelo = getVueloViajeros(vueloViajeros);
		DAOTablaReservas daoReservas = new DAOTablaReservas();
		daoReservas.setConn(conn);
		DAOTablaAeropuertos daoAeropuertos = new DAOTablaAeropuertos();
		daoAeropuertos.setConn(conn);
		ArrayList<ReservaViajeros> reservas =  daoReservas.getReservasViajerosPorVuelo(vuelo.getId());
		ArrayList<VueloViajeros> vuelos = getVuelosViajeros();
		ArrayList<ReservaViajeros> reservasACrear = new ArrayList<>();
		for(int i = 0; i<reservas.size(); i++)
		{
			ReservaViajeros reservaActual = reservas.get(i);
			if((reservaActual.getAeroSal().equals(vuelo.getAeropuertoSA().getCodigo()) && reservaActual.getAeroLleg().equals(vuelo.getAeropuertoLL().getCodigo())))
			{
				daoReservas.cancelarReservaViajeroVuelo(reservaActual.getIdReservaGeneral());
				Date min = new Date(Long.MAX_VALUE);
				SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
				min = format.parse(format.format(min));
				Vuelo ganador = null;
				for(int j =0; j< vuelos.size(); j++)
				{
					VueloViajeros actual = vuelos.get(j);
					if( actual.getAeropuertoSA().getCodigo().equals(vuelo.getAeropuertoSA().getCodigo())&& actual.getAeropuertoLL().getCodigo().equals(vuelo.getAeropuertoLL().getCodigo())&& actual.getHoraSalida().after(vuelo.getHoraSalida())&& actual.getHoraSalida().before(min) )
					{
						min = actual.getHoraSalida();
						ganador = actual;
					}
				}
				if(ganador == null)
				{
					throw new Exception("No se ha podido encontrar otro vuelo para asignar las reservas");
				}

				conn.setSavepoint();


				reservaActual.setIdVuelo(ganador.getId());

				daoReservas.createReservaViajero(reservaActual);
			}
			else
			{
				conn.setSavepoint();
				daoReservas.cancelarReservaViajeroVuelo(reservaActual.getIdReservaGeneral());
				conn.setSavepoint();

				reservasACrear.add(reservaActual);
			}
		}

		String sql = "DELETE ISIS2304A131620.VUELO_VIAJEROS WHERE ID = " + vuelo.getId();


		PreparedStatement prepStmt = conn.prepareStatement(sql);

		prepStmt.executeUpdate();

		conn.setSavepoint();
		String sql2 = "DELETE ISIS2304A131620.VUELO WHERE ID = " + vuelo.getId();
		PreparedStatement prepStmt2 = conn.prepareStatement(sql2);

		prepStmt2.executeUpdate();

		for(int i = 0; i< reservasACrear.size(); i++)
		{
			ReservaViajeros actual = reservasACrear.get(i);
			daoReservas.createReservaVuelosMultiples(actual, this);
		}
		return vuelo;

	}
	public Vuelo getVuelo(int idVuelo) throws SQLException, ParseException {
		Vuelo vuelo=null;
		String sql="SELECT * FROM ISIS2304A131620.VUELO WHERE ID ="+idVuelo;
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		if(rs.next()){
			int id= rs.getInt("ID");
			Date horaSalida = null;
			Date horaLlegada = null;
			Timestamp timestamp = rs.getTimestamp("HORA_SALIDA");
			if (timestamp != null)
				horaSalida = new Date(timestamp.getTime());
			Timestamp timestamp2 = rs.getTimestamp("HORA_LLEGADA");
			if(timestamp2 != null)
			{
				horaLlegada = new Date(timestamp2.getTime());
			}
			SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			horaLlegada = format.parse(format.format(horaLlegada));
			horaSalida = format.parse(format.format(horaSalida));
			String duracion =rs.getString("DURACION");
			double distancia= rs.getDouble("DISTANCIA");
			int frecuencia =rs.getInt("FRECUENCIA");
			String tipoViaje= rs.getString("TIPO_VIAJE");
			String aerolinea = rs.getString("AEROLINEA");
			String avion = rs.getString("AVION");
			String aeropuertoSA= rs.getString("AEROPUERTO_SALIDA");
			String aeropuertoLL= rs.getString("AEROPUERTO_LLEGADA");
			Aeropuerto aeropuertoLLegada = getAeropuerto(aeropuertoLL);
			Aeropuerto aeropuertoSalida= getAeropuerto(aeropuertoSA);
			String realizado = rs.getString("REALIZADO");
			boolean realizadoo = false;
			if (realizado.equals("N"))
			{}
			else if(realizado.equals("S"))
			{
				realizadoo = true;
			}
			String tipoVuelo = rs.getString("TIPO_VUELO");
			vuelo = new Vuelo(id, horaSalida, horaLlegada, frecuencia, tipoViaje, aerolinea, avion, aeropuertoSalida, aeropuertoLLegada, distancia, duracion, realizadoo, tipoVuelo);					
		}
		return vuelo;
	}

	public VueloCarga getVueloCarga(int idVuelo) throws SQLException, ParseException {
		VueloCarga vueloCarga=null;
		String sql="SELECT * FROM ISIS2304A131620.VUELO WHERE ID ="+idVuelo;
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		if(rs.next()){

			Vuelo vueloT = getVuelo(idVuelo);

			double costo = rs.getDouble("COSTO_DENSIDAD");

			vueloCarga = new VueloCarga(idVuelo, vueloT.getHoraSalida(), vueloT.getHoraLlegada(), vueloT.getFrecuencia(), vueloT.getTipoViaje(), vueloT.getAerolinea(), vueloT.getAvion(), vueloT.getAeropuertoSA(), vueloT.getAeropuertoLL(), vueloT.getDistancia(), vueloT.getDuracion(), vueloT.isRealizado(), costo);					
		}
		return vueloCarga;
	}

	public VueloViajeros getVueloViajerosAeropuertos(String aeroSal, String aeroLleg) throws SQLException, ParseException {
		VueloViajeros vuelo=null;
		String sql="SELECT * FROM ISIS2304A131620.VUELO WHERE (AEROPUERTO_SALIDA ='" + aeroSal+ "' AND AEROPUERTO_LLEGADA = '" + aeroLleg+ "')";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		if(rs.next()){
			int id= rs.getInt("ID");


			vuelo = getVueloViajeros(id);
		}
		return vuelo;
	}
	public ResultSet queryVuelosViajerosPorViajero(int idViajero, String tipoIdViajero) throws SQLException, ParseException {
		String sql= "SELECT ID_VUELO FROM ((SELECT * FROM RESERVA_VIAJEROS WHERE ID_VIAJERO = "+idViajero+" AND "
				+ "TIPO_ID_VIAJERO = '"+tipoIdViajero+"')T1 JOIN "
				+ "(SELECT * FROM  VUELO_VIAJEROS)T2 ON T1.ID_VUELO = T2.ID) GROUP BY ID_VUELO";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		return prepStmt.executeQuery();
		
	}
	
	public ArrayList<VueloViajeros> getVuelosViajerosPorViajero(ResultSet rs) throws SQLException, ParseException
	{
		ArrayList<VueloViajeros> vuelos= new ArrayList<VueloViajeros>();
		while(rs.next())
		{
			int id = rs.getInt("ID_VUELO");
			VueloViajeros vuelo = getVueloViajeros(id);
	

				vuelos.add(vuelo);

		}
		return vuelos;
	}

	public ArrayList<ConsultaViajes> consultarViajesViajero(int idViajero, String tipoIdViajero) throws Exception
	{
		DAOTablaAerolina daoAerolineas = new DAOTablaAerolina();
		daoAerolineas.setConn(conn);
		DAOTablaReservas daoReservas = new DAOTablaReservas();
		daoReservas.setConn(conn);
		ResultSet rs = queryVuelosViajerosPorViajero(idViajero, tipoIdViajero);
		ArrayList<VueloViajeros> vuelos = getVuelosViajerosPorViajero(rs);
		
		ArrayList<ConsultaViajes> consultas = new ArrayList<>();

		for(int i = 0; i< vuelos.size(); i++)
		{
			VueloViajeros vuelo = vuelos.get(i);
			String aerolineaa = vuelo.getAerolinea();
			Aerolinea aerolinea = daoAerolineas.getAerolinea(aerolineaa);

			ArrayList<ReservaViajeros> reservas = daoReservas.getReservasViajerosPorVueloYViajero(idViajero,tipoIdViajero,vuelo.getId());

			
			String aeroLleg = vuelo.getAeropuertoLL().getCodigo();
			String aeroSal = vuelo.getAeropuertoSA().getCodigo();
			int idVuelo = vuelo.getId();

			double costo = generarReporte(vuelo.getId(), daoReservas);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String fechaLlegada = format.format(vuelo.getHoraLlegada());
			String fechaSalida =format.format(vuelo.getHoraSalida());
			Double millas = vuelo.getDistancia();

			consultas.add(new ConsultaViajes(reservas, idVuelo, aerolinea, fechaSalida, fechaLlegada, aeroSal, aeroLleg, millas, costo));
		}
		return consultas;
		
	}




	public VueloViajeros getVueloViajeros(int idVuelo) throws SQLException, ParseException {
		VueloViajeros vueloViajeros=null;
		String sql="SELECT * FROM ISIS2304A131620.VUELO_VIAJEROS WHERE ID ="+idVuelo;
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		conn.setSavepoint();
		ResultSet rs=prepStmt.executeQuery();
		if(rs.next()){

			Vuelo vueloT = getVuelo(idVuelo);

			double costoEjec = rs.getDouble("COSTO_EJECUTIVO");
			double costoEcon = rs.getDouble("COSTO_ECONOMICO");
			vueloViajeros = new VueloViajeros(idVuelo, vueloT.getHoraSalida(), vueloT.getHoraLlegada(), vueloT.getFrecuencia(), vueloT.getTipoViaje(), vueloT.getAerolinea(), vueloT.getAvion(), vueloT.getAeropuertoSA(), vueloT.getAeropuertoLL(), vueloT.getDistancia(), vueloT.getDuracion(), vueloT.isRealizado(), costoEjec, costoEcon);	
		}
		return vueloViajeros;
	}



	public ArrayList<VueloViajeros> getVuelosViajerosRealizadosAvion(String numSerieAvion) throws SQLException, ParseException
	{
		ArrayList<VueloViajeros> vuelos = new ArrayList<VueloViajeros>();
		String sql="SELECT * FROM ISIS2304A131620.VUELO_VIAJEROS WHERE AVION = '"+numSerieAvion+"' AND REALIZADO = 'Y'";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		while(rs.next()){

			int idVuelo = rs.getInt("ID");
			Vuelo vueloT = getVuelo(idVuelo);
			double costoEjec = rs.getDouble("COSTO_EJECUTIVO");
			double costoEcon = rs.getDouble("COSTO_ECONOMICO");
			VueloViajeros vuelo = new VueloViajeros(idVuelo, vueloT.getHoraSalida(), vueloT.getHoraLlegada(), vueloT.getFrecuencia(), vueloT.getTipoViaje(), vueloT.getAerolinea(), vueloT.getAvion(), vueloT.getAeropuertoSA(), vueloT.getAeropuertoLL(), vueloT.getDistancia(), vueloT.getDuracion(), vueloT.isRealizado(), costoEjec, costoEcon);			
			vuelos.add(vuelo);
		}
		return vuelos;
	}



	public ArrayList<VueloCarga> getVuelosCargaRealizadosAvion(String numSerieAvion) throws SQLException, ParseException
	{
		ArrayList<VueloCarga> vuelos = new ArrayList<VueloCarga>();
		String sql="SELECT * FROM ISIS2304A131620.VUELO_CARGA WHERE AVION = '"+numSerieAvion+"' AND REALIZADO = 'Y'";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		while(rs.next()){

			int idVuelo = rs.getInt("ID");
			Vuelo vueloT = getVuelo(idVuelo);
			double costo = rs.getDouble("COSTO_DENSIDAD");
			VueloCarga vuelo = new VueloCarga(idVuelo, vueloT.getHoraSalida(), vueloT.getHoraLlegada(), vueloT.getFrecuencia(), vueloT.getTipoViaje(), vueloT.getAerolinea(), vueloT.getAvion(), vueloT.getAeropuertoSA(), vueloT.getAeropuertoLL(), vueloT.getDistancia(), vueloT.getDuracion(), vueloT.isRealizado(), costo);			
			vuelos.add(vuelo);
		}
		return vuelos;
	}

	public ArrayList<Vuelo> getVuelosAeropuerto(String aeropuerto) throws SQLException {
		Vuelo vuelo=null;
		ArrayList<Vuelo> vuelos = new ArrayList<Vuelo>();
		String sql="SELECT * FROM ISIS2304A131620.VUELO WHERE (ISIS2304A131620.VUELO.AEROPUERTO_SALIDA ='" + aeropuerto+ "' OR ISIS2304A131620.VUELO.AEROPUERTO_LLEGADA = '" + aeropuerto+ "')";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		while(rs.next()){
			int id= rs.getInt("ID");
			Date horaSalida= rs.getDate("HORA_SALIDA");
			Date horaLlegada=rs.getDate("HORA_LLEGADA");
			String duracion =rs.getString("DURACION");
			double distancia= rs.getDouble("DISTANCIA");
			int frecuencia =rs.getInt("FRECUENCIA");
			String tipoViaje= rs.getString("TIPO_VIAJE");
			String aerolinea = rs.getString("AEROLINEA");
			String avion = rs.getString("AVION");
			String aeropuertoLL= rs.getString("AEROPUERTO_SALIDA");
			String aeropuertoSA= rs.getString("AEROPUERTO_LLEGADA");
			Aeropuerto aeropuertoLLegada = getAeropuerto(aeropuertoLL);
			Aeropuerto aeropuertoSalida= getAeropuerto(aeropuertoSA);
			String realizado = rs.getString("REALIZADO");
			boolean realizadoo = false;
			if (realizado.equals("N"))
			{}
			else if(realizado.equals("S"))
			{
				realizadoo = true;
			}
			String tipoVuelo = rs.getString("TIPO_VUELO");

			vuelo = new Vuelo(id, horaSalida, horaLlegada, frecuencia, tipoViaje, aerolinea, avion, aeropuertoSalida, aeropuertoLLegada, distancia, duracion, realizadoo,tipoVuelo);					
			vuelos.add(vuelo);
		}
		return vuelos;
	}
	public ArrayList<Vuelo> getVuelosAeropuertoRangoFechas(String aeropuerto, Date fechaMin, Date fechaMax) throws SQLException {
		Vuelo vuelo=null;
		ArrayList<Vuelo> vuelos = new ArrayList<Vuelo>();
		String sql="SELECT * FROM ISIS2304A131620.VUELO WHERE ((ISIS2304A131620.VUELO.AEROPUERTO_SALIDA =" + aeropuerto+ "OR ISIS2304A131620.VUELO.AEROPUERTO_LLEGADA = "+aeropuerto+")AND "+fechaMin+" <= ISIS2304A131620.VUELO.HORA_SALIDA AND "+fechaMax+" > ISIS2304A131620.VUELO.HORA_SALIDA);";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		while(rs.next()){
			int id= rs.getInt("ID");
			Date horaSalida= rs.getDate("HORA_SALIDA");
			Date horaLlegada=rs.getDate("HORA_LLEGADA");
			String duracion =rs.getString("DURACION");
			double distancia= rs.getDouble("DISTANCIA");
			int frecuencia =rs.getInt("FRECUENCIA");
			String tipoViaje= rs.getString("TIPO_VIAJE");
			String aerolinea = rs.getString("AEROLINEA");
			String avion = rs.getString("AVION");
			String aeropuertoLL= rs.getString("AEROPUERTO_SALIDA");
			String aeropuertoSA= rs.getString("AEROPUERTO_LLEGADA");
			Aeropuerto aeropuertoLLegada = getAeropuerto(aeropuertoLL);
			Aeropuerto aeropuertoSalida= getAeropuerto(aeropuertoSA);
			String realizado = rs.getString("REALIZADO");
			boolean realizadoo = false;
			if (realizado.equals("N"))
			{}
			else if(realizado.equals("S"))
			{
				realizadoo = true;
			}
			String tipoVuelo = rs.getString("TIPO_VUELO");

			vuelo = new Vuelo(id, horaSalida, horaLlegada, frecuencia, tipoViaje, aerolinea, avion, aeropuertoSalida, aeropuertoLLegada, distancia, duracion, realizadoo,tipoVuelo);		
			vuelos.add(vuelo);
		}
		return vuelos;
	}

	public ArrayList<Vuelo> getVuelosAeropuertoAerolinea(String aeropuerto, String aerolinea) throws SQLException {
		Vuelo vuelo=null;
		ArrayList<Vuelo> vuelos = new ArrayList<Vuelo>();
		String sql="SELECT * FROM ISIS2304A131620.VUELO WHERE ((ISIS2304A131620.VUELO.AEROPUERTO_SALIDA =" + aeropuerto+ "OR ISIS2304A131620.VUELO.AEROPUERTO_LLEGADA = "+aeropuerto+")AND ISIS2304A131620.VUELO.AEROLINEA = "+aerolinea+");";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		while(rs.next()){
			int id= rs.getInt("ID");
			Date horaSalida= rs.getDate("HORA_SALIDA");
			Date horaLlegada=rs.getDate("HORA_LLEGADA");
			String duracion =rs.getString("DURACION");
			double distancia= rs.getDouble("DISTANCIA");
			int frecuencia =rs.getInt("FRECUENCIA");
			String tipoViaje= rs.getString("TIPO_VIAJE");
			aerolinea = rs.getString("AEROLINEA");
			String avion = rs.getString("AVION");
			String aeropuertoLL= rs.getString("AEROPUERTO_SALIDA");
			String aeropuertoSA= rs.getString("AEROPUERTO_LLEGADA");
			Aeropuerto aeropuertoLLegada = getAeropuerto(aeropuertoLL);
			Aeropuerto aeropuertoSalida= getAeropuerto(aeropuertoSA);
			String realizado = rs.getString("REALIZADO");
			boolean realizadoo = false;
			if (realizado.equals("N"))
			{}
			else if(realizado.equals("S"))
			{
				realizadoo = true;
			}
			String tipoVuelo = rs.getString("TIPO_VUELO");

			vuelo = new Vuelo(id, horaSalida, horaLlegada, frecuencia, tipoViaje, aerolinea, avion, aeropuertoSalida, aeropuertoLLegada, distancia, duracion, realizadoo,tipoVuelo);		
			vuelos.add(vuelo);
		}
		return vuelos;
	}
	public ArrayList<Vuelo> getVuelosAeropuertoHoraSalida(String aeropuerto, Date horaSalida) throws SQLException {
		Vuelo vuelo=null;
		ArrayList<Vuelo> vuelos = new ArrayList<Vuelo>();
		String sql="SELECT * FROM ISIS2304A131620.VUELO WHERE ((ISIS2304A131620.VUELO.AEROPUERTO_SALIDA =" + aeropuerto+ "OR ISIS2304A131620.VUELO.AEROPUERTO_LLEGADA = "+aeropuerto+")AND "+horaSalida+" = ISIS2304A131620.VUELO.HORA_SALIDA);";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		while(rs.next()){
			int id= rs.getInt("ID");
			horaSalida= rs.getDate("HORA_SALIDA");
			Date horaLlegada=rs.getDate("HORA_LLEGADA");
			String duracion =rs.getString("DURACION");
			double distancia= rs.getDouble("DISTANCIA");
			int frecuencia =rs.getInt("FRECUENCIA");
			String tipoViaje= rs.getString("TIPO_VIAJE");
			String aerolinea = rs.getString("AEROLINEA");
			String avion = rs.getString("AVION");
			String aeropuertoLL= rs.getString("AEROPUERTO_SALIDA");
			String aeropuertoSA= rs.getString("AEROPUERTO_LLEGADA");
			Aeropuerto aeropuertoLLegada = getAeropuerto(aeropuertoLL);
			Aeropuerto aeropuertoSalida= getAeropuerto(aeropuertoSA);
			String realizado = rs.getString("REALIZADO");
			boolean realizadoo = false;
			if (realizado.equals("N"))
			{}
			else if(realizado.equals("S"))
			{
				realizadoo = true;
			}
			String tipoVuelo = rs.getString("TIPO_VUELO");

			vuelo = new Vuelo(id, horaSalida, horaLlegada, frecuencia, tipoViaje, aerolinea, avion, aeropuertoSalida, aeropuertoLLegada, distancia, duracion, realizadoo,tipoVuelo);		
			vuelos.add(vuelo);
		}
		return vuelos;
	}

	public ArrayList<Vuelo> getVuelosAeropuertoHoraLlegada(String aeropuerto, Date horaLlegada) throws SQLException {
		Vuelo vuelo=null;
		ArrayList<Vuelo> vuelos = new ArrayList<Vuelo>();
		String sql="SELECT * FROM ISIS2304A131620.VUELO WHERE ((ISIS2304A131620.VUELO.AEROPUERTO_SALIDA =" + aeropuerto+ "OR ISIS2304A131620.VUELO.AEROPUERTO_LLEGADA = "+aeropuerto+")AND "+horaLlegada+" = ISIS2304A131620.VUELO.HORA_LLEGADA);";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		while(rs.next()){
			int id= rs.getInt("ID");
			Date horaSalida= rs.getDate("HORA_SALIDA");
			horaLlegada=rs.getDate("HORA_LLEGADA");
			String duracion =rs.getString("DURACION");
			double distancia= rs.getDouble("DISTANCIA");
			int frecuencia =rs.getInt("FRECUENCIA");
			String tipoViaje= rs.getString("TIPO_VIAJE");
			String aerolinea = rs.getString("AEROLINEA");
			String avion = rs.getString("AVION");
			String aeropuertoLL= rs.getString("AEROPUERTO_SALIDA");
			String aeropuertoSA= rs.getString("AEROPUERTO_LLEGADA");
			Aeropuerto aeropuertoLLegada = getAeropuerto(aeropuertoLL);
			Aeropuerto aeropuertoSalida= getAeropuerto(aeropuertoSA);
			String realizado = rs.getString("REALIZADO");
			boolean realizadoo = false;
			if (realizado.equals("N"))
			{}
			else if(realizado.equals("S"))
			{
				realizadoo = true;
			}
			String tipoVuelo = rs.getString("TIPO_VUELO");

			vuelo = new Vuelo(id, horaSalida, horaLlegada, frecuencia, tipoViaje, aerolinea, avion, aeropuertoSalida, aeropuertoLLegada, distancia, duracion, realizadoo,tipoVuelo);		
			vuelos.add(vuelo);
		}
		return vuelos;
	}



	public ArrayList<Vuelo> getVuelosAeropuertoViajeros(String aeropuerto) throws SQLException {
		Vuelo vuelo=null;
		ArrayList<Vuelo> vuelos = new ArrayList<Vuelo>();
		String sql="SELECT * FROM(SELECT * FROM ISIS2304A131620.VUELO WHERE(ISIS2304A131620.VUELO.AEROPUERTO_SALIDA = "+aeropuerto+" OR ISIS2304A131620.VUELO.AEROPUERTO_LLEGADA = "+aeropuerto+"))c1 INNER JOIN ISIS2304A131620.AVION_VIAJEROS ON (c1.AVION = ISIS2304A131620.AVION_VIAJEROS.NUM_SERIE);";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		while(rs.next()){
			int id= rs.getInt("ID");
			Date horaSalida= rs.getDate("HORA_SALIDA");
			Date horaLlegada=rs.getDate("HORA_LLEGADA");
			String duracion =rs.getString("DURACION");
			double distancia= rs.getDouble("DISTANCIA");
			int frecuencia =rs.getInt("FRECUENCIA");
			String tipoViaje= rs.getString("TIPO_VIAJE");
			String aerolinea = rs.getString("AEROLINEA");
			String avion = rs.getString("AVION");
			String aeropuertoLL= rs.getString("AEROPUERTO_SALIDA");
			String aeropuertoSA= rs.getString("AEROPUERTO_LLEGADA");
			Aeropuerto aeropuertoLLegada = getAeropuerto(aeropuertoLL);
			Aeropuerto aeropuertoSalida= getAeropuerto(aeropuertoSA);
			String realizado = rs.getString("REALIZADO");
			boolean realizadoo = false;
			if (realizado.equals("N"))
			{}
			else if(realizado.equals("S"))
			{
				realizadoo = true;
			}
			String tipoVuelo = rs.getString("TIPO_VUELO");

			vuelo = new Vuelo(id, horaSalida, horaLlegada, frecuencia, tipoViaje, aerolinea, avion,aeropuertoSalida , aeropuertoLLegada, distancia, duracion, realizadoo,tipoVuelo);
			vuelos.add(vuelo);
		}
		return vuelos;
	}

	public ArrayList<VueloCarga> getVuelosAeropuertoCarga(String aeropuerto) throws SQLException {
		VueloCarga vuelo=null;
		ArrayList<VueloCarga> vuelos = new ArrayList<VueloCarga>();
		String sql="SELECT * FROM(SELECT * FROM ISIS2304A131620.VUELO WHERE(ISIS2304A131620.VUELO.AEROPUERTO_SALIDA = "+aeropuerto+" OR ISIS2304A131620.VUELO.AEROPUERTO_LLEGADA = "+aeropuerto+"))c1 INNER JOIN ISIS2304A131620.AVION_VIAJEROS ON (c1.AVION = ISIS2304A131620.AVION_CARGA.NUM_SERIE);";
		PreparedStatement prepStmt= conn.prepareStatement(sql);
		ResultSet rs=prepStmt.executeQuery();
		while(rs.next()){
			int id= rs.getInt("ID");
			Date horaSalida= rs.getDate("HORA_SALIDA");
			Date horaLlegada=rs.getDate("HORA_LLEGADA");
			String duracion =rs.getString("DURACION");
			double distancia= rs.getDouble("DISTANCIA");
			int frecuencia =rs.getInt("FRECUENCIA");
			String tipoViaje= rs.getString("TIPO_VIAJE");
			String aerolinea = rs.getString("AEROLINEA");
			String avion = rs.getString("AVION");
			String aeropuertoLL= rs.getString("AEROPUERTO_SALIDA");
			String aeropuertoSA= rs.getString("AEROPUERTO_LLEGADA");
			Aeropuerto aeropuertoLLegada = getAeropuerto(aeropuertoLL);
			Aeropuerto aeropuertoSalida= getAeropuerto(aeropuertoSA);
			String realizado = rs.getString("REALIZADO");
			boolean realizadoo = false;
			if (realizado.equals("N"))
			{}
			else if(realizado.equals("S"))
			{
				realizadoo = true;
			}
			String tipoVuelo = rs.getString("TIPO_VUELO");

			vuelos.add(vuelo);
		}
		return vuelos;
	}


	public void asociarVuelo(int idVuelo, String idAvion) throws SQLException, Exception
	{
		DAOTablaReservas daoReservas = new DAOTablaReservas();
		DAOTablaAviones daoAviones = new DAOTablaAviones();
		DAOTablaCargas daoCargas = new DAOTablaCargas();
		Vuelo vuelo = vuelos.get(idVuelo);
		Avion avion = daoAviones.darAvion(idAvion);

		if(avion.getTipo().equals(vuelo.getTipoViaje()))
		{
			if(avion.getTipo() == Avion.CARGA)
			{
				ArrayList<ReservaCarga> reservas = daoReservas.getReservasCargaPorVuelo(vuelo.getId());
				AvionCarga avionCarga = daoAviones.darAvionCarga(idAvion);
				int cargaTotal = 0;
				for(int i = 0; i<reservas.size();i++)
				{
					ReservaCarga reserva = reservas.get(i);
					Carga carga = daoCargas.getCarga(reserva.getIdCarga());
					cargaTotal += carga.getPeso();
				}
				if(avionCarga.getCargaMax() >= cargaTotal )
				{
					vuelo.setAvion(avion.getNumSerie());
					updateVuelo(vuelo);
				}
			}
			else if (avion.getTipo() == Avion.VIAJEROS)
			{
				ArrayList<ReservaViajeros> reservas = daoReservas.getReservasViajerosPorVuelo(idVuelo);
				AvionViajeros avionViaje = daoAviones.darAvionViajeros(idAvion);
				int puestosEconomica = 0;
				int puestosEjecutiva = 0;
				for(int i = 0; i<reservas.size();i++)
				{
					ReservaViajeros reserva = reservas.get(i);
					if(reserva.getClase().equals(ReservaViajeros.ECONOMICA))
					{
						puestosEconomica++;
					}
					else if(reserva.getClase().equals(ReservaViajeros.EJECUTIVA))
					{
						puestosEjecutiva++;
					}
				}
				if(puestosEconomica <= avionViaje.getSillasEconomicas() && puestosEjecutiva <= avionViaje.getSillasEjecutivas())
				{
					vuelo.setAvion(avion.getNumSerie());
					updateVuelo(vuelo);
				}
			}
		}
		else
		{
			throw new Exception("El avi�n con el que se quiere vincular la reserva no es del tipo "+ vuelo.getTipoViaje());
		}






	}

	public double generarReporte(int idVuelo, DAOTablaReservas daoReservas) throws SQLException, ParseException
	{
		DAOTablaCargas daoCargas = new DAOTablaCargas();
		daoCargas.setConn(conn);
		Vuelo vuelo = getVuelo(idVuelo);
		double costo = 0;
		if(vuelo.getTipoVuelo().equals(Vuelo.CARGA))
		{
			VueloCarga vueloC = getVueloCarga(idVuelo);
			double densidadTotal = 0;
			ArrayList<ReservaCarga> reservasC = daoReservas.getReservasCargaPorVuelo(idVuelo);
			for(int i = 0; i < reservasC.size();i++)
			{
				ReservaCarga reservaActual = reservasC.get(i);
				Carga carga = daoCargas.getCarga(reservaActual.getIdCarga());
				double densidad = (double)(carga.getPeso()/carga.getVolumen());

				densidadTotal+= densidad;
			}
			costo = vueloC.getCostoDensidad()*densidadTotal;
		}
		else if(vuelo.getTipoVuelo().equals(Vuelo.VIAJEROS))
		{
			VueloViajeros vueloV = getVueloViajeros(idVuelo);
			int pasajerosEconomicos = 0;
			int pasajerosEjecutivos = 0;
			ArrayList<ReservaViajeros> reservasV = daoReservas.getReservasViajerosPorVuelo(idVuelo);
			for(int i = 0; i < reservasV.size();i++)
			{
				ReservaViajeros reservaActual = reservasV.get(i);
				if(reservaActual.getClase().equals(ReservaViajeros.ECONOMICA))
				{
					pasajerosEconomicos++;
				}
				else if(reservaActual.getClase().equals(ReservaViajeros.EJECUTIVA))
				{
					pasajerosEjecutivos++;
				}
			}
			costo= (pasajerosEjecutivos*vueloV.getCostoEjec()) + (pasajerosEconomicos*vueloV.getCostoEcon());
		}
		return costo;
	}
	public void registrarVuelo(int idVuelo) throws SQLException{
		String sql ="UPDATE ISIS2304A131620.VUELO SET REALIZADO= Y WHERE ID="+idVuelo;

		PreparedStatement prepStmt=conn.prepareStatement(sql);
		prepStmt.executeQuery();
	}

	public ArrayList<Vuelo> darVuelosEnFechas(Date d1, Date d2){
		return null;
	}


	public ArrayList<Vuelo> getVuelosMasPopulares() {
		return null;

	}




}