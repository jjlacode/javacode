package factunomo.modelo;


import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComboBox;

import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.modelo.obj.FiltroVO;
import factunomo.modelo.obj.GastoVO;
import factunomo.modelo.obj.IngresoVO;
import factunomo.modelo.obj.DetalleGastoVO;
import factunomo.modelo.obj.DetalleIngresoVO;
import factunomo.modelo.obj.ProveedorVO;
import factunomo.modelo.obj.ClienteVO;
import factunomo.modelo.obj.ProductoVO;
import factunomo.modelo.obj.FormaPagoVO;
import factunomo.utils.StringUtil;


public class Modelo {

	public static final String CODIGO_PAIS_SPAIN = "ESPAÑA";
	
	
	public static Modelo modelo;

	
	/**
	 * Constructor por defecto.
	 */
	public Modelo(){
		
	}
	
	
	
	public static Modelo getInstance(){
		if(modelo == null){
			modelo = new Modelo();
		}
		return modelo;
	}
	
	// OPERACIONES CON FECHAS
	
	/**
	 * Devuelve la primera fecha de la bbdd
	 * @param String bbdd "INGRESOS" o "GASTOS"
	 */
	public Date primeraFecha(String bbdd){
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		Date fecha=new Date();
		if(conn != null){
			try{
				String query = "SELECT \"Fecha\" FROM ";
				query+= bbdd;
				query+= " ORDER BY \"Fecha\" ASC LIMIT 1";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				if (rs.first()) fecha = rs.getDate("Fecha");
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return fecha;
	}
	
	/**
	 * Devuelve la última fecha de la bbdd
	 * @param String bbdd "INGRESOS" o "GASTOS"
	 */
	public Date ultimaFecha(String bbdd){
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		Date fecha=new Date();
		if(conn != null){
			try{
				String query = "SELECT \"Fecha\" FROM ";
				query+= bbdd;
				query+= " ORDER BY \"Fecha\" DESC LIMIT 1";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				if (rs.first())	fecha = rs.getDate("Fecha");
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return fecha;
	}
	
	/**
	 * Obtiene la fecha de inicio del periodo y ejercicio
	 */
	public Date fechaInicio(int ejercicio, int periodo){
		String cadena="";
		Date fecha=null;
		switch (periodo){
		case 1:
		case 5:
			cadena="01-01-";
			break;
		case 2:
			cadena="01-04-";
			break;
		case 3:
			cadena="01-07-";
			break;
		case 4:
			cadena="01-10-";
		}
		cadena+=ejercicio;
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
		try {
			fecha = formatoFecha.parse(cadena);
			return fecha;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return fecha;
		}
	}

	/**
	 * Obtiene la fecha de final del periodo y ejercicio
	 */
	public Date fechaFinal(int ejercicio, int periodo){
		String cadena="";
		Date fecha=null;
		switch (periodo){
		case 1:
			cadena="31-03-";
			break;
		case 2:
			cadena="30-06-";
			break;
		case 3:
			cadena="30-09-";
			break;
		case 5:
		case 4:
			cadena="31-12-";
		}
		cadena+=ejercicio;
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
		try {
			fecha = formatoFecha.parse(cadena);
			return fecha;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return fecha;
		}
	}
	
	/**
	 * Obtiene el periodo de una determinada fecha
	 * @param Date fecha
	 */
	public int getPeriodo(Date fecha)
	{
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy");
		int anho = Integer.parseInt(formatoFecha.format(fecha));
		for (int i=1;i<5;i++)
		{
			if (!fecha.after(fechaFinal(anho,i))) return i;
		}
		return 0;
	}
	
	
	// IDs
	
	/**
	 * Obtiene el idProveedor para nueva entrada
	 */
	public String nuevoIdProveedor(){
		String nuevaID = "0000";
		String maxID="0000";
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				String query = "SELECT \"ID_proveedor\" FROM PROVEEDORES ORDER BY \"ID_proveedor\" DESC LIMIT 1";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				if (rs.first()) maxID = rs.getString("ID_proveedor");
				Integer siguiente = Integer.parseInt(maxID);
				siguiente++;
				nuevaID = "0000" + siguiente;	
				nuevaID = nuevaID.substring(nuevaID.length()-4,nuevaID.length());
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return nuevaID;
	}
	
	/**
	 * Obtiene el idCliente para nueva entrada
	 */
	public String nuevoIdCliente(){
		String nuevaID = "0000";
		String maxID="0000";
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				String query = "SELECT \"ID_cliente\" FROM CLIENTES ORDER BY \"ID_cliente\" DESC LIMIT 1";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				if (rs.first()) maxID = rs.getString("ID_cliente");
				Integer siguiente = Integer.parseInt(maxID);
				siguiente++;
				nuevaID = "0000" + siguiente;	
				nuevaID = nuevaID.substring(nuevaID.length()-4,nuevaID.length());
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return nuevaID;
	}
	
		
	/**
	 * Obtiene el idProducto para nueva entrada
	 */
	public String nuevoIdProducto(){
		String nuevaID = "000";
		String maxID="000";
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				String query = "SELECT \"ID_producto\" FROM PRODUCTOS ORDER BY \"ID_producto\" DESC LIMIT 1";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				if (rs.first()) maxID = rs.getString("ID_producto");
				Integer siguiente = Integer.parseInt(maxID);
				siguiente++;
				nuevaID = "000" + siguiente;	
				nuevaID = nuevaID.substring(nuevaID.length()-3,nuevaID.length());
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return nuevaID;
	}
	
	/**
	 * Obtiene el idGasto para nueva entrada
	 */
	public String nuevoIdGasto(int ejercicio){
		ResultSet rs = null;
		Statement st = null;
		String nuevaID = "00001";
		String maxID="00G00000";
		String ejeID = "" + ejercicio;
		ejeID = ejeID.substring(2,4);
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				String query = "SELECT \"ID_gasto\" FROM GASTOS ORDER BY \"ID_gasto\" DESC LIMIT 1";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				if (rs.first()) maxID = rs.getString("ID_gasto");
				String prefID = maxID.substring(0,2);
				if (prefID.equals(ejeID)){
					String sufID = maxID.substring(3,8);
					Integer siguiente = Integer.parseInt(sufID);
					siguiente++;
					nuevaID = "00000" + siguiente;
					nuevaID = nuevaID.substring(nuevaID.length()-5,nuevaID.length());
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		nuevaID=ejeID+"G"+nuevaID; 
		return nuevaID;
	}
	
	/**
	 * Obtiene el idIngreso para nueva entrada
	 */
	public String nuevoIdIngreso(int ejercicio){
		ResultSet rs = null;
		Statement st = null;
		String nuevaID = "0001";
		String maxID="00F0000";
		String ejeID = "" + ejercicio;
		ejeID = ejeID.substring(2,4);
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				String query = "SELECT \"ID_ingreso\" FROM INGRESOS ORDER BY \"ID_ingreso\" DESC LIMIT 1";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				if (rs.first())	maxID = rs.getString("ID_ingreso");
				String prefID = maxID.substring(0,2);
				if (prefID.equals(ejeID)){
					String sufID = maxID.substring(3,maxID.length());
					Integer siguiente = Integer.parseInt(sufID);
					siguiente++;
					nuevaID = "0000" + siguiente;
					nuevaID = nuevaID.substring(nuevaID.length()-4,nuevaID.length());
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		nuevaID=ejeID+"F"+nuevaID;
		return nuevaID;
	}
	
	/**
	 * Obtiene el idFormaPago para nueva entrada
	 */
	public String nuevoIdFormaPago(){
		String nuevaID = "00";
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				String query = "SELECT \"ID_formapago\" FROM FORMAS_PAGO ORDER BY \"ID_formapago\" DESC LIMIT 1";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				rs.next();
				String maxID = rs.getString("ID_formapago");
				Integer siguiente = Integer.parseInt(maxID);
				siguiente++;
				nuevaID += siguiente;	
				nuevaID = nuevaID.substring(nuevaID.length()-2,nuevaID.length());
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return nuevaID;
	}
	
	/*
	 * OBTENER DATOS
	 */
		
	/**
	 * Obtiene los datos del proveedor por nombre
	 * 
	 * @return
	 */
	public ProveedorVO getProveedor(String nombre) {
		ProveedorVO proveedor = new ProveedorVO();
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				String cadena = "select * from Proveedores where \"Nombre\" = '" + nombre + "'";
				rs = st.executeQuery(cadena);
				while(rs.next()){
					proveedor.setIdProveedor(rs.getString("ID_proveedor"));
					proveedor.setNombre(rs.getString("nombre"));
					proveedor.setNIF(rs.getString("NIF"));
					proveedor.setDireccion(rs.getString("direccion"));
					proveedor.setCP(rs.getString("CP"));
					proveedor.setCiudad(rs.getString("ciudad"));
					proveedor.setProvincia(rs.getString("provincia"));
					proveedor.setTelefono(rs.getString("telefono"));
					proveedor.setMovil(rs.getString("movil"));
					proveedor.setFax(rs.getString("fax"));
					proveedor.setEmail(rs.getString("email"));
					proveedor.setWeb(rs.getString("web"));
					proveedor.setConIRPF(rs.getBoolean("conIRPF"));
					proveedor.setIdFormaPago(rs.getString("ID_formaPago"));
					proveedor.setNumeroCuenta(rs.getString("numeroCuenta"));
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return proveedor;
	}
	
	/**
	 * Obtiene los datos del cliente por nombre
	 * 
	 * @return
	 */
	public ClienteVO getCliente(String nombre) {
		ClienteVO cliente = new ClienteVO();
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				String cadena = "select * from Clientes where \"Nombre\" = '" + nombre + "'";
				rs = st.executeQuery(cadena);
				while(rs.next()){
					cliente.setIdCliente(rs.getString("ID_cliente"));
					cliente.setNombre(rs.getString("nombre"));
					cliente.setNIF(rs.getString("NIF"));
					cliente.setConIRPF(rs.getBoolean("ConIRPF"));
					cliente.setDireccion(rs.getString("direccion"));
					cliente.setCP(rs.getString("CP"));
					cliente.setCiudad(rs.getString("ciudad"));
					cliente.setProvincia(rs.getString("provincia"));
					cliente.setTelefono(rs.getString("telefono"));
					cliente.setMovil(rs.getString("movil"));
					cliente.setFax(rs.getString("fax"));
					cliente.setEmail(rs.getString("email"));
					cliente.setWeb(rs.getString("web"));
					cliente.setConIRPF(rs.getBoolean("conIRPF"));
					cliente.setIdFormaPago(rs.getString("ID_formaPago"));
					cliente.setNumeroCuenta(rs.getString("numeroCuenta"));
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return cliente;
	}
	
	
	/**
	 * Obtiene los datos de la forma de pago por ID
	 * 
	 * @return
	 */
	public FormaPagoVO getFormaPago(String idformapago) {
		FormaPagoVO formaPago = new FormaPagoVO();
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				String cadena = "select * from Formas_pago where \"ID_formapago\" = '" + idformapago + "'";
				rs = st.executeQuery(cadena);
				while(rs.next()){
					formaPago.setIdFormaPago(rs.getString("ID_formaPago"));
					formaPago.setNombre(rs.getString("nombre"));
					formaPago.setNumeroCuenta(rs.getBoolean("numeroCuenta"));
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}	
		return formaPago;
	}
	
	
	/**
	 * Obtiene los datos de la forma de pago por el nombre
	 * 
	 * @return
	 */
	public FormaPagoVO getNombreFormaPago(String nombre) {
		FormaPagoVO formaPago = new FormaPagoVO();
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				String cadena = "select * from Formas_pago where \"Nombre\" = '" + nombre + "'";
				rs = st.executeQuery(cadena);
				while(rs.next()){
					formaPago.setIdFormaPago(rs.getString("ID_formaPago"));
					formaPago.setNombre(rs.getString("nombre"));
					formaPago.setNumeroCuenta(rs.getBoolean("numeroCuenta"));
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}	
		return formaPago;
	}
	
	/**
	 * Obtiene los datos del producto por código
	 * 
	 * @return
	 */
	public ProductoVO getProducto(String codigo) {
		ProductoVO producto = new ProductoVO();
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				String cadena = "select * from Productos where codigo = '" + codigo + "'";
				rs = st.executeQuery(cadena);
				while(rs.next()){
					producto.setCodigo(rs.getString("codigo"));
					producto.setIdProducto(rs.getString("ID_producto"));
					producto.setNombre(rs.getString("nombre"));
					producto.setPrecio(rs.getFloat("precio"));
					producto.setIVA(rs.getFloat("IVA"));
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return producto;
	}
	
	
	// COMBOS
	
	/**
	 * Devuelve el combo de proveedores
	 * 
	 * @return
	 */
	public JComboBox<String> getProveedores(int tipo) {
		JComboBox<String> combo = new JComboBox<String>();
		ResultSet rs = null;
		Statement st = null;
			Connection conn = this.getConnection();
			if(conn != null){
				switch (tipo){
				case 1:
					combo.addItem("TODOS");
					break;
				case 2:
					combo.addItem("");
					break;
				default:
				}
				try{
					st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs = st.executeQuery("select * from Proveedores order by \"Nombre\"");
					while(rs.next()){
						combo.addItem(rs.getString("nombre"));
					}
					if (!rs.first()) combo.addItem("Ninguno");
				} catch(Exception e){
					e.printStackTrace();
				} finally {
					this.cerrarTodo(conn, st, rs);
				}
			}
		return combo;
	}
	
	/**
	 * Devuelve el combo de clientes
	 * 
	 * @return
	 */
	public JComboBox<String> getClientes(int tipo) {
		JComboBox<String> combo = new JComboBox<String>();
		ResultSet rs = null;
		Statement st = null;
			Connection conn = this.getConnection();
			if(conn != null){
				switch (tipo){
				case 1:
					combo.addItem("TODOS");
					break;
				case 2:
					combo.addItem("");
					break;
				default:
				}
				try{
					st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs = st.executeQuery("select * from Clientes order by \"Nombre\"");
					while(rs.next()){
						combo.addItem(rs.getString("nombre"));
					}
					if (!rs.first()) combo.addItem("Ninguno");
				} catch(Exception e){
					e.printStackTrace();
				} finally {
					this.cerrarTodo(conn, st, rs);
				}
			}
		return combo;
	}
	
	
	/**
	 * Devuelve el combo de formas de pago
	 * 
	 * @return
	 */
	public JComboBox<String> getFormasPago() {
		JComboBox<String> combo = new JComboBox<String>();
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			try{
					st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs = st.executeQuery("select * from Formas_pago");
					while(rs.next()){
						combo.addItem(rs.getString("nombre"));
					}
				} catch(Exception e){
					e.printStackTrace();
				} finally {
					this.cerrarTodo(conn, st, rs);
				}
			}
		return combo;
	}
	
	/**
	 * Devuelve el combo de años
	 * 
	 * @return
	 */
	public JComboBox<String> getAnhos(int tipo) {
		JComboBox<String> combo = new JComboBox<String>();
		ResultSet rs = null;
		Statement st = null;
		Date fechaInicio = new Date();
		Calendar fechaFinal = new GregorianCalendar();
		
		int anhoFinal = fechaFinal.get(Calendar.YEAR);
		int anhoInicio = anhoFinal;
			Connection conn = this.getConnection();
			if(conn != null){
				try{
					// Obtener primer año de gastos
					String query = "SELECT \"Fecha\" FROM GASTOS ORDER BY \"Fecha\" LIMIT 1";
					st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs = st.executeQuery(query);
					if (rs.first()) fechaInicio = rs.getDate("Fecha");
					SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
					int anhoGasto=Integer.parseInt(simpleDateformat.format(fechaInicio));
					anhoInicio=anhoGasto;
					// Obtener primer año de ingresos
					query = "SELECT \"Fecha\" FROM INGRESOS ORDER BY \"Fecha\" LIMIT 1";
					st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs = st.executeQuery(query);
					if (rs.first()) fechaInicio = rs.getDate("Fecha");
					int anhoIngreso=Integer.parseInt(simpleDateformat.format(fechaInicio));
					if (anhoIngreso<anhoGasto) anhoInicio=anhoIngreso;
						
				} catch(Exception e){
					e.printStackTrace();
				} finally {
					this.cerrarTodo(conn, st, rs);
				}
				if (tipo==1) combo.addItem("TODOS");
				for (int i=anhoFinal;i>=anhoInicio;i--){
					String anho=""+i;
					combo.addItem(anho);
				}
			}
		return combo;
	}
	
	
	/*
	 * LISTADOS
	 */
		
		
	/**
	 * Devuelve un listado de gastos filtrando por el parametro.
	 * @param filtro
	 * @return
	 */
	public ArrayList<GastoVO> getGastos(FiltroVO filtro){
		ResultSet rs = null;
		Statement st = null;
		ArrayList<GastoVO> gastos = new ArrayList<GastoVO>();
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				SimpleDateFormat formato=new SimpleDateFormat("yyyy-MM-dd");
				
				String query = "select * from GASTOS WHERE \"Fecha\">='"+
						formato.format(filtro.getFechaInicio())+ "' AND \"Fecha\"<='" +
						formato.format(filtro.getFechaFinal()) + "'";
				if (!filtro.getNombre().equals("TODOS")){
					query += " AND \"Nombre\"='" + filtro.getNombre() + "'";
				}
				query += " ORDER BY \"Fecha\",\"ID_gasto\"";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				while(rs.next()){
					GastoVO p = new GastoVO();
					p.setFecha(rs.getDate("fecha"));
					p.setNombre(rs.getString("nombre"));
					p.setIdGasto(rs.getString("ID_gasto"));
					p.setBaseImponible1(rs.getFloat("baseImponible1"));
					p.setIVA1(rs.getFloat("IVA1"));
					p.setBaseImponible2(rs.getFloat("baseImponible2"));
					p.setIVA2(rs.getFloat("IVA2"));
					p.setBaseImponible3(rs.getFloat("baseImponible3"));
					p.setIVA3(rs.getFloat("IVA3"));
					p.setNIF(StringUtil.nullToString(rs.getString("NIF")));
					p.setDireccion(rs.getString("direccion"));
					p.setCP(rs.getString("CP"));
					p.setCiudad(rs.getString("Ciudad"));
					p.setProvincia(rs.getString("Provincia"));
					p.setTOTAL(rs.getFloat("TOTAL"));
					p.setDescuento(rs.getFloat("descuento"));
					p.setTipoIRPF(rs.getFloat("tipoIRPF"));
					p.setIdFormaPago(rs.getString("ID_formapago"));
					p.setNumeroCuenta(rs.getString("numerocuenta"));
					gastos.add(p);
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return gastos;
	}
	
	/**
	 * Devuelve un listado de ingresos filtrando por el parametro.
	 * @param filtro
	 * @return
	 */
	public ArrayList<IngresoVO> getIngresos(FiltroVO filtro){
		ResultSet rs = null;
		Statement st = null;
		ArrayList<IngresoVO> ingresos = new ArrayList<IngresoVO>();
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				SimpleDateFormat formato=new SimpleDateFormat("yyyy-MM-dd");
				
				String query = "select * from INGRESOS WHERE \"Fecha\">='"+
						formato.format(filtro.getFechaInicio())+ "' AND \"Fecha\"<='" +
						formato.format(filtro.getFechaFinal()) + "'";
				if (!filtro.getNombre().equals("TODOS")){
					query += " AND \"Nombre\"='" + filtro.getNombre() + "'";
				}
				query += " ORDER BY \"Fecha\",\"ID_ingreso\"";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				while(rs.next()){
					IngresoVO p = new IngresoVO();
					p.setFecha(rs.getDate("fecha"));
					p.setNombre(rs.getString("nombre"));
					p.setIdIngreso(rs.getString("ID_ingreso"));
					p.setTipoIRPF(rs.getFloat("TipoIRPF"));
					p.setBaseImponible1(rs.getFloat("baseImponible1"));
					p.setIVA1(rs.getFloat("IVA1"));
					p.setBaseImponible2(rs.getFloat("baseImponible2"));
					p.setIVA2(rs.getFloat("IVA2"));
					p.setBaseImponible3(rs.getFloat("baseImponible3"));
					p.setIVA3(rs.getFloat("IVA3"));
					p.setNIF(StringUtil.nullToString(rs.getString("NIF")));
					p.setDireccion(rs.getString("direccion"));
					p.setCP(rs.getString("CP"));
					p.setCiudad(rs.getString("Ciudad"));
					p.setProvincia(rs.getString("Provincia"));
					p.setTOTAL(rs.getFloat("TOTAL"));
					p.setDescuento(rs.getFloat("descuento"));
					p.setIdFormaPago(rs.getString("ID_formapago"));
					p.setNumeroCuenta(rs.getString("numerocuenta"));
					ingresos.add(p);
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return ingresos;
	}
	
	
	/**
	 * Devuelve un listado de proveedores ordenado por IdProducto
	 * @return
	 */
	public ArrayList<ProveedorVO> getProveedores(){
		ResultSet rs = null;
		Statement st = null;
		ArrayList<ProveedorVO> proveedores = new ArrayList<ProveedorVO>();
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				String query = "select * from PROVEEDORES order by \"ID_proveedor\" ";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				while(rs.next()){
					ProveedorVO p = new ProveedorVO();
					p.setIdProveedor(rs.getString("ID_proveedor"));
					p.setNombre(rs.getString("Nombre"));
					p.setDireccion(rs.getString("Direccion"));
					p.setCP(rs.getString("CP"));
					p.setCiudad(rs.getString("Ciudad"));
					p.setProvincia(rs.getString("Provincia"));
					p.setNIF(rs.getString("NIF"));
					p.setTelefono(rs.getString("Telefono"));
					p.setMovil(rs.getString("Movil"));
					p.setFax(rs.getString("Fax"));
					p.setEmail(rs.getString("Email"));
					p.setWeb(rs.getString("Web"));
					p.setNumeroCuenta(rs.getString("NumeroCuenta"));
					p.setConIRPF(rs.getBoolean("ConIRPF"));
					p.setIdFormaPago(rs.getString("ID_formaPago"));
					proveedores.add(p);
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return proveedores;
	}
	
	/**
	 * Devuelve un listado de clientes ordenado por IdProducto
	 * @return
	 */
	public ArrayList<ClienteVO> getClientes(){
		ResultSet rs = null;
		Statement st = null;
		ArrayList<ClienteVO> clientes = new ArrayList<ClienteVO>();
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				String query = "select * from CLIENTES order by \"ID_cliente\" ";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				while(rs.next()){
					ClienteVO p = new ClienteVO();
					p.setIdCliente(rs.getString("ID_cliente"));
					p.setNombre(rs.getString("Nombre"));
					p.setDireccion(rs.getString("Direccion"));
					p.setCP(rs.getString("CP"));
					p.setCiudad(rs.getString("Ciudad"));
					p.setProvincia(rs.getString("Provincia"));
					p.setNIF(rs.getString("NIF"));
					p.setTelefono(rs.getString("Telefono"));
					p.setMovil(rs.getString("Movil"));
					p.setFax(rs.getString("Fax"));
					p.setEmail(rs.getString("Email"));
					p.setWeb(rs.getString("Web"));
					p.setNumeroCuenta(rs.getString("NumeroCuenta"));
					p.setConIRPF(rs.getBoolean("ConIRPF"));
					p.setIdFormaPago(rs.getString("ID_formaPago"));
					clientes.add(p);
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return clientes;
	}
	
	/**
	 * Devuelve un listado de productos ordenado por IdProducto
	 * @return
	 */
	public ArrayList<ProductoVO> getProductos(){
		ResultSet rs = null;
		Statement st = null;
		ArrayList<ProductoVO> productos = new ArrayList<ProductoVO>();
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				String query = "select * from PRODUCTOS order by \"ID_producto\" ";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				while(rs.next()){
					ProductoVO p = new ProductoVO();
					p.setNombre(rs.getString("Nombre"));
					p.setIdProducto(rs.getString("ID_producto"));
					p.setPrecio(rs.getFloat("Precio"));
					p.setIVA(rs.getFloat("IVA"));
					productos.add(p);
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return productos;
	}
	
	/**
	 * Devuelve un listado de formas de pago
	 * @return
	 */
	public ArrayList<FormaPagoVO> getFormas(){
		ResultSet rs = null;
		Statement st = null;
		ArrayList<FormaPagoVO> formas = new ArrayList<FormaPagoVO>();
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				String query = "select * from FORMAS_PAGO order by \"ID_formapago\" ";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				while(rs.next()){
					FormaPagoVO p = new FormaPagoVO();
					p.setNombre(rs.getString("Nombre"));
					p.setIdFormaPago(rs.getString("ID_formapago"));
					p.setNumeroCuenta(rs.getBoolean("numeroCuenta"));
					formas.add(p);
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return formas;
	}
	
	/**
	 * Devuelve un listado de cuentas del usuario
	 * @return
	 */
	public ArrayList<String> getCuentas(){
		ArrayList<String> cuentas = new ArrayList<String>();
		ConfiguracionVO datosUsuario=getDatosFactunomo();
		if (!datosUsuario.getNumeroCuenta1().equals("")) cuentas.add(datosUsuario.getNumeroCuenta1());
		if (!datosUsuario.getNumeroCuenta2().equals("")) cuentas.add(datosUsuario.getNumeroCuenta2());
		if (!datosUsuario.getNumeroCuenta3().equals("")) cuentas.add(datosUsuario.getNumeroCuenta3());
		return cuentas;
	}

	
	/**
	 * Devuelve un listado de detalles de un gasto por Id
	 * @return
	 */
	public ArrayList<DetalleGastoVO> getDetalleGasto(String ID_gasto){
		ResultSet rs = null;
		Statement st = null;
		ArrayList<DetalleGastoVO> gastos = new ArrayList<DetalleGastoVO>();
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				String query = "select * from Gastos_datos where \"ID_gasto\" = '" + ID_gasto + "'";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				while(rs.next()){
					DetalleGastoVO p = new DetalleGastoVO();
					p.setIdDetalleGasto(rs.getInt("ID_detallegasto"));
					p.setIdProducto(StringUtil.nullToString(rs.getString("ID_producto")));
					p.setDescripcion(rs.getString("descripcion"));
					p.setCantidad(rs.getFloat("cantidad"));
					p.setPrecio(rs.getFloat("precio"));
					p.setIVA(rs.getFloat("IVA"));
					p.setSubTotal(p.getCantidad()*p.getPrecio());
					gastos.add(p);
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return gastos;
	}
	
	/**
	 * Devuelve un listado de detalles de un ingreso por Id
	 * @return
	 */
	public ArrayList<DetalleIngresoVO> getDetalleIngreso(String ID_ingreso){
		ResultSet rs = null;
		Statement st = null;
		ArrayList<DetalleIngresoVO> ingresos = new ArrayList<DetalleIngresoVO>();
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				String query = "select * from Ingresos_datos where \"ID_ingreso\" = '" + ID_ingreso + "'";
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				while(rs.next()){
					DetalleIngresoVO p = new DetalleIngresoVO();
					p.setIdDetalleIngreso(rs.getInt("ID_detalleIngreso"));
					p.setIdProducto(StringUtil.nullToString(rs.getString("ID_producto")));
					p.setDescripcion(rs.getString("descripcion"));
					p.setCantidad(rs.getFloat("cantidad"));
					p.setPrecio(rs.getFloat("precio"));
					p.setIVA(rs.getFloat("IVA"));
					p.setSubTotal(p.getCantidad()*p.getPrecio());
					ingresos.add(p);
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return ingresos;
	}

	
	/**
	 * Funcion para consultar los datos del usuario grabados en BBDD.
	 * @return datos
	 */
	public ConfiguracionVO getDatosFactunomo(){
		ConfiguracionVO datos = new ConfiguracionVO();
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			try{
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery("select * from Configuracion where \"Codigo\" = 1");
				if(rs.next()){
					datos.setNombre(rs.getString("nombre"));
					datos.setDireccion(rs.getString("direccion"));
					datos.setCP(rs.getString("CP"));
					datos.setCiudad(rs.getString("ciudad"));
					datos.setProvincia(rs.getString("provincia"));
					datos.setNIF(StringUtil.nullToString(rs.getString("NIF")));
					datos.setTelefono(rs.getString("telefono"));
					datos.setWeb(rs.getString("web"));
					datos.setEmail(rs.getString("email"));
					datos.setIRPF(rs.getFloat("IRPF"));
					datos.setIVAGeneral(rs.getFloat("IVAGeneral"));
					datos.setIVAReducido(rs.getFloat("IVAReducido"));
					datos.setIVASuperReducido(rs.getFloat("IVASuperReducido"));
					datos.setNumeroCuenta1(StringUtil.nullToString(rs.getString("numeroCuenta1")));
					datos.setNumeroCuenta2(StringUtil.nullToString(rs.getString("numeroCuenta2")));
					datos.setNumeroCuenta3(StringUtil.nullToString(rs.getString("numeroCuenta3")));
					datos.setPeriodo(rs.getInt("periodo"));
					datos.setEjercicio(rs.getInt("ejercicio"));
					
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}	
		return datos;
	}
	
	
	/*
	 * ACTUALIZAR 
	 */
	
	/**
	 * Actualiza los datos del Usuario en la Base de Datos.
	 * @param datos
	 * @throws Exception
	 */
	public void actualizarDatosUsuario(ConfiguracionVO datos) {
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "update CONFIGURACION set \"Nombre\" = ?,\"NIF\" = ?,\"Direccion\" = ?,\"CP\" = ?" +
						",\"Ciudad\" = ?,\"Provincia\" = ?,\"Web\" = ?,\"Email\" = ?" +
						",\"Telefono\" = ?,\"IVAGeneral\" = ?,\"IVAReducido\" = ?,\"IVASuperReducido\" = ?" +
						",\"NumeroCuenta1\" = ?" +
						",\"NumeroCuenta2\" = ?" +
						",\"NumeroCuenta3\" = ?" +
						",\"IRPF\" = ?" +
						",\"Ejercicio\" = ?" +
						",\"Periodo\" = ?" +
						" where \"Codigo\" = 1";
				st = conn.prepareStatement(query);
				int i = 0;
				st.setString(++i, datos.getNombre());
				st.setString(++i, datos.getNIF());
				st.setString(++i, datos.getDireccion());
				st.setString(++i, datos.getCP());
				st.setString(++i, datos.getCiudad());
				st.setString(++i, datos.getProvincia());
				st.setString(++i, datos.getWeb());
				st.setString(++i, datos.getEmail());
				st.setString(++i, datos.getTelefono());
				st.setFloat(++i, datos.getIVAGeneral());
				st.setFloat(++i, datos.getIVAReducido());
				st.setFloat(++i, datos.getIVASuperReducido());
				st.setString(++i, datos.getNumeroCuenta1());
				st.setString(++i, datos.getNumeroCuenta2());
				st.setString(++i, datos.getNumeroCuenta3());
				st.setFloat(++i, datos.getIRPF());
				st.setFloat(++i, datos.getEjercicio());
				st.setFloat(++i, datos.getPeriodo());
				st.executeUpdate();
				conn.commit();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				System.out.println("Cerrando?");
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Actualiza un proveedor en la Base de Datos.
	 * @param proveedor
	 * @throws Exception
	 */
	public void actualizarProveedor(ProveedorVO proveedor) {
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "update PROVEEDORES set \"ID_proveedor\" = ?,\"Nombre\" = ?,\"Direccion\" = ?,\"CP\" = ?" +
						",\"Ciudad\" = ?,\"Provincia\" = ?,\"NIF\" = ?,\"Telefono\" = ?" +
						",\"Movil\" = ?,\"Fax\" = ?,\"Email\" = ?,\"Web\" = ?" +
						",\"ID_formaPago\" = ?" +
						",\"NumeroCuenta\" = ?" +
						",\"ConIRPF\" = ?" +
						" where \"ID_proveedor\" = ?";
				st = conn.prepareStatement(query);
				int i = 0;
				st.setString(++i, proveedor.getIdProveedor());
				st.setString(++i, proveedor.getNombre());
				st.setString(++i, proveedor.getDireccion());
				st.setString(++i, proveedor.getCP());
				st.setString(++i, proveedor.getCiudad());
				st.setString(++i, proveedor.getProvincia());
				st.setString(++i, proveedor.getNIF());
				st.setString(++i, proveedor.getTelefono());
				st.setString(++i, proveedor.getMovil());
				st.setString(++i, proveedor.getFax());
				st.setString(++i, proveedor.getEmail());
				st.setString(++i, proveedor.getWeb());
				st.setString(++i, proveedor.getIdFormaPago());
				st.setString(++i, proveedor.getNumeroCuenta());
				st.setBoolean(++i, proveedor.getConIRPF());
				
				st.setString(++i, proveedor.getIdProveedor());
				st.executeUpdate();
				conn.commit();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Actualiza un cliente en la Base de Datos.
	 * @param cliente
	 * @throws Exception
	 */
	public void actualizarCliente(ClienteVO cliente) {
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "update CLIENTES set \"ID_cliente\" = ?,\"Nombre\" = ?,\"Direccion\" = ?,\"CP\" = ?" +
						",\"Ciudad\" = ?,\"Provincia\" = ?,\"NIF\" = ?,\"ConIRPF\" = ?,\"Telefono\" = ?" +
						",\"Movil\" = ?,\"Fax\" = ?,\"Email\" = ?,\"Web\" = ?" +
						",\"ID_formaPago\" = ?" +
						",\"NumeroCuenta\" = ?" +
						" where \"ID_cliente\" = ?";
				st = conn.prepareStatement(query);
				int i = 0;
				st.setString(++i, cliente.getIdCliente());
				st.setString(++i, cliente.getNombre());
				st.setString(++i, cliente.getDireccion());
				st.setString(++i, cliente.getCP());
				st.setString(++i, cliente.getCiudad());
				st.setString(++i, cliente.getProvincia());
				st.setString(++i, cliente.getNIF());
				st.setBoolean(++i, cliente.getConIRPF());
				st.setString(++i, cliente.getTelefono());
				st.setString(++i, cliente.getMovil());
				st.setString(++i, cliente.getFax());
				st.setString(++i, cliente.getEmail());
				st.setString(++i, cliente.getWeb());
				st.setString(++i, cliente.getIdFormaPago());
				st.setString(++i, cliente.getNumeroCuenta());
				st.setString(++i, cliente.getIdCliente());
				st.executeUpdate();
				conn.commit();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Actualiza un producto en la Base de Datos.
	 * @param producto
	 * @throws Exception
	 */
	public void actualizarProducto(ProductoVO producto) {
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "update PRODUCTOS set \"ID_producto\" = ?,\"Nombre\" = ?,\"Precio\" = ?,\"IVA\" = ?" +
						" where \"ID_producto\" = ?";
				st = conn.prepareStatement(query);
				int i = 0;
				st.setString(++i, producto.getIdProducto());
				st.setString(++i, producto.getNombre());
				st.setFloat(++i, producto.getPrecio());
				st.setFloat(++i, producto.getIVA());
				st.setString(++i, producto.getIdProducto());
				st.executeUpdate();
				conn.commit();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Actualiza un gasto en la Base de Datos.
	 * @param persona
	 * @throws Exception
	 */
	public void actualizarGasto(GastoVO gasto,ArrayList<DetalleGastoVO> detalles) throws Exception {
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "update GASTOS set \"ID_gasto\" = ?,\"Fecha\" = ?,\"ID_proveedor\" = ?,\"Nombre\" = ?," +
						"\"Direccion\" = ?,\"CP\" = ?,\"NIF\" = ?,\"Ciudad\" = ?,\"Provincia\" = ?,\"tipoIRPF\" = ?," +
						"\"Descuento\" = ?,\"BaseImponible1\" = ?,\"IVA1\" = ?,\"BaseImponible2\" = ?,\"IVA2\" = ?," +
						"\"BaseImponible3\" = ?,\"IVA3\" = ?,\"ID_formaPago\" = ?,\"NumeroCuenta\" = ?,\"TOTAL\" = ?" +
						" where \"ID_gasto\" = ?";
				st = conn.prepareStatement(query);
				int i = 0;
				st.setString(++i, gasto.getIdGasto());
				st.setDate(++i, new java.sql.Date(gasto.getFecha().getTime()));
				st.setString(++i, gasto.getIdProveedor());
				st.setString(++i, gasto.getNombre());
				st.setString(++i, gasto.getDireccion());
				st.setString(++i, gasto.getCP());
				st.setString(++i, gasto.getNIF());
				st.setString(++i, gasto.getCiudad());
				st.setString(++i, gasto.getProvincia());
				st.setFloat(++i, gasto.getTipoIRPF());
				st.setFloat(++i, gasto.getDescuento());
				st.setFloat(++i, gasto.getBaseImponible1());
				st.setFloat(++i, gasto.getIVA1());
				st.setFloat(++i, gasto.getBaseImponible2());
				st.setFloat(++i, gasto.getIVA2());
				st.setFloat(++i, gasto.getBaseImponible3());
				st.setFloat(++i, gasto.getIVA3());
				st.setString(++i, gasto.getIdFormaPago());
				st.setString(++i, gasto.getNumeroCuenta());
				st.setFloat(++i, gasto.getTOTAL());
				st.setString(++i, gasto.getIdGasto());
				st.executeUpdate();
				conn.commit();
				
				// DETALLES
				query = "DELETE FROM GASTOS_DATOS WHERE \"ID_gasto\"='" +
						gasto.getIdGasto() + "'";
				st = conn.prepareStatement(query);
				st.executeUpdate();
				conn.commit();
				for (int j=0;j<detalles.size();j++)
				{
					query = "insert into GASTOS_DATOS " +
							"(\"ID_gasto\",\"ID_producto\",\"Cantidad\",\"Descripcion\",\"Precio\",\"IVA\") " +
							"values (?,?,?,?,?,?)";
					st = conn.prepareStatement(query);
					i = 0;
					st.setString(++i, gasto.getIdGasto());
					st.setString(++i, detalles.get(j).getIdProducto());
					st.setFloat(++i, detalles.get(j).getCantidad());
					st.setString(++i, detalles.get(j).getDescripcion());
					st.setFloat(++i, detalles.get(j).getPrecio());
					st.setFloat(++i, detalles.get(j).getIVA());
					st.executeUpdate();
					conn.commit();
				}
			} catch(Exception e){
				e.printStackTrace();
				throw e;
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Actualiza un ingreso en la Base de Datos.
	 * @param ingreso
	 * @throws Exception
	 */
	public void actualizarIngreso(IngresoVO ingreso,ArrayList<DetalleIngresoVO> detalles) throws Exception {
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "update INGRESOS set \"ID_ingreso\" = ?,\"Fecha\" = ?,\"ID_cliente\" = ?,\"Nombre\" = ?," +
						"\"Direccion\" = ?,\"CP\" = ?,\"NIF\" = ?,\"Ciudad\" = ?,\"Provincia\" = ?,\"TipoIRPF\" = ?," +
						"\"Descuento\" = ?,\"BaseImponible1\" = ?,\"IVA1\" = ?,\"BaseImponible2\" = ?,\"IVA2\" = ?," +
						"\"BaseImponible3\" = ?,\"IVA3\" = ?,\"ID_formaPago\" = ?,\"NumeroCuenta\" = ?,\"TOTAL\" = ?" +
						" where \"ID_ingreso\" = ?";
				st = conn.prepareStatement(query);
				int i = 0;
				st.setString(++i, ingreso.getIdIngreso());
				st.setDate(++i, new java.sql.Date(ingreso.getFecha().getTime()));
				st.setString(++i, ingreso.getIdCliente());
				st.setString(++i, ingreso.getNombre());
				st.setString(++i, ingreso.getDireccion());
				st.setString(++i, ingreso.getCP());
				st.setString(++i, ingreso.getNIF());
				st.setString(++i, ingreso.getCiudad());
				st.setString(++i, ingreso.getProvincia());
				st.setFloat(++i, ingreso.getTipoIRPF());
				st.setFloat(++i, ingreso.getDescuento());
				st.setFloat(++i, ingreso.getBaseImponible1());
				st.setFloat(++i, ingreso.getIVA1());
				st.setFloat(++i, ingreso.getBaseImponible2());
				st.setFloat(++i, ingreso.getIVA2());
				st.setFloat(++i, ingreso.getBaseImponible3());
				st.setFloat(++i, ingreso.getIVA3());
				st.setString(++i, ingreso.getIdFormaPago());
				st.setString(++i, ingreso.getNumeroCuenta());
				st.setFloat(++i, ingreso.getTOTAL());
				st.setString(++i, ingreso.getIdIngreso());
				st.executeUpdate();
				conn.commit();
				
				// DETALLES
				query = "DELETE FROM INGRESOS_DATOS WHERE \"ID_ingreso\"='" +
						ingreso.getIdIngreso() + "'";
				st = conn.prepareStatement(query);
				st.executeUpdate();
				conn.commit();
				for (int j=0;j<detalles.size();j++)
				{
					query = "insert into INGRESOS_DATOS " +
							"(\"ID_ingreso\",\"ID_producto\",\"Cantidad\",\"Descripcion\",\"Precio\",\"IVA\") " +
							"values (?,?,?,?,?,?)";
					st = conn.prepareStatement(query);
					i = 0;
					st.setString(++i, ingreso.getIdIngreso());
					st.setString(++i, detalles.get(j).getIdProducto());
					st.setFloat(++i, detalles.get(j).getCantidad());
					st.setString(++i, detalles.get(j).getDescripcion());
					st.setFloat(++i, detalles.get(j).getPrecio());
					st.setFloat(++i, detalles.get(j).getIVA());
					st.executeUpdate();
					conn.commit();
				}
			} catch(Exception e){
				e.printStackTrace();
				throw e;
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/*
	 * DUPLICADOS
	 */

	/**
	 * Comprueba si un id proveedor está duplicado
	 */
	public Boolean idProveedorDuplicado (String idproveedor){
		Boolean duplicado = false;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			String cadena="select * from PROVEEDORES where \"ID_proveedor\" = '"+idproveedor+"'";
			try {
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(cadena);
				while(rs.next()){
					if(rs.getString("ID_proveedor").equals(idproveedor)) duplicado=true;
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return duplicado;
	}
	
	/**
	 * Comprueba si un id cliente está duplicado
	 */
	public Boolean idClienteDuplicado (String idcliente){
		Boolean duplicado = false;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			String cadena="select * from CLIENTES where \"ID_cliente\" = '"+idcliente+"'";
			try {
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(cadena);
				while(rs.next()){
					if(rs.getString("ID_cliente").equals(idcliente)) duplicado=true;
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return duplicado;
	}
	
	/**
	 * Comprueba si un id producto está duplicado
	 */
	public Boolean idProductoDuplicado (String idproducto){
		Boolean duplicado = false;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			String cadena="select * from PRODUCTOS where \"ID_producto\" = '"+idproducto+"'";
			try {
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(cadena);
				while(rs.next()){
					if(rs.getString("ID_producto").equals(idproducto)) duplicado=true;
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return duplicado;
	}
	
	/**
	 * Comprueba si un id gasto está duplicado
	 */
	public Boolean idGastoDuplicado (String idgasto){
		Boolean duplicado = false;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			String cadena="select * from gastos where \"ID_gasto\" = '"+idgasto+"'";
			try {
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(cadena);
				while(rs.next()){
					if(rs.getString("id_gasto").equals(idgasto)) duplicado=true;
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return duplicado;
	}
	
	/**
	 * Comprueba si un id ingreso está duplicado
	 */
	public Boolean idIngresoDuplicado (String idingreso){
		Boolean duplicado = false;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			String cadena="select * from INGRESOS where \"ID_ingreso\" = '"+idingreso+"'";
			try {
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(cadena);
				while(rs.next()){
					if(rs.getString("id_gasto").equals(idingreso)) duplicado=true;
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return duplicado;
	}
	
	/**
	 * Comprueba si un nombre de proveedor está duplicado
	 */
	public boolean nombreProveedorDuplicado (String nombre){
		Boolean duplicado = false;
		ResultSet rs = null;
		Statement st = null;	
		Connection conn = this.getConnection();
		if(conn != null){
			String cadena="select * from PROVEEDORES where \"Nombre\" = '"+nombre+"'";
			try {
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(cadena);
				while(rs.next()){
					if(rs.getString("Nombre").equals(nombre)) duplicado=true;
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return duplicado;
	}
	
	/**
	 * Comprueba si un nombre de cliente está duplicado
	 */
	public boolean nombreClienteDuplicado (String nombre){
		Boolean duplicado = false;
		ResultSet rs = null;
		Statement st = null;	
		Connection conn = this.getConnection();
		if(conn != null){
			String cadena="select * from CLIENTES where \"Nombre\" = '"+nombre+"'";
			try {
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(cadena);
				while(rs.next()){
					if(rs.getString("Nombre").equals(nombre)) duplicado=true;
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return duplicado;
	}
	
	/**
	 * Comprueba si un id cliente está duplicado
	 */
	public String getNombreCliente (String idcliente){
		String resultado="";
		ResultSet rs = null;
		Statement st = null;
		Connection conn = this.getConnection();
		if(conn != null){
			String cadena="select * from CLIENTES where \"ID_cliente\" = '"+idcliente+"'";
			try {
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(cadena);
				while(rs.next()){
					resultado=rs.getString("Nombre");
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return resultado;
	}
	
	/**
	 * Comprueba si un nombre de forma de pago está duplicado
	 */
	public boolean nombrePagoDuplicado (String nombre){
		Boolean duplicado = false;
		ResultSet rs = null;
		Statement st = null;	
		Connection conn = this.getConnection();
		if(conn != null){
			String cadena="select * from FORMAS_PAGO where \"Nombre\" = '"+nombre+"'";
			try {
				st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(cadena);
				while(rs.next()){
					if(rs.getString("Nombre").equals(nombre)) duplicado=true;
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, rs);
			}
		}
		return duplicado;
	}
	
	/*
	 * INSERTAR
	 */
	
	/**
	 * Inserta un nuevo proveedor en la Base de Datos.
	 * @param proveedor
	 * @throws Exception
	 */
	public void guardarProveedor(ProveedorVO proveedor) {
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "insert into PROVEEDORES " +
						"(\"ID_proveedor\",\"Nombre\",\"Direccion\",\"CP\"," +
						"\"Ciudad\",\"Provincia\",\"NIF\",\"Telefono\"," +
						"\"Movil\",\"Fax\",\"Email\",\"Web\"," +
						"\"ID_formaPago\",\"NumeroCuenta\",\"ConIRPF\")" +
						"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				st = conn.prepareStatement(query);
				int i = 0;
				st.setString(++i, proveedor.getIdProveedor());
				st.setString(++i, proveedor.getNombre());
				st.setString(++i, proveedor.getDireccion());
				st.setString(++i, proveedor.getCP());
				st.setString(++i, proveedor.getCiudad());
				st.setString(++i, proveedor.getProvincia());
				st.setString(++i, proveedor.getNIF());
				st.setString(++i, proveedor.getTelefono());
				st.setString(++i, proveedor.getMovil());
				st.setString(++i, proveedor.getFax());
				st.setString(++i, proveedor.getEmail());
				st.setString(++i, proveedor.getWeb());
				st.setString(++i, proveedor.getIdFormaPago());
				st.setString(++i, proveedor.getNumeroCuenta());
				st.setBoolean(++i, proveedor.getConIRPF());
				st.executeUpdate();
				conn.commit();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Inserta un nuevo cliente en la Base de Datos.
	 * @param proveedor
	 * @throws Exception
	 */
	public void guardarCliente(ClienteVO cliente) {
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "insert into CLIENTES " +
						"(\"ID_cliente\",\"Nombre\",\"Direccion\",\"CP\"," +
						"\"Ciudad\",\"Provincia\",\"NIF\",\"ConIRPF\",\"Telefono\"," +
						"\"Movil\",\"Fax\",\"Email\",\"Web\"," +
						"\"ID_formaPago\",\"NumeroCuenta\")" +
						"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				st = conn.prepareStatement(query);
				int i = 0;
				st.setString(++i, cliente.getIdCliente());
				st.setString(++i, cliente.getNombre());
				st.setString(++i, cliente.getDireccion());
				st.setString(++i, cliente.getCP());
				st.setString(++i, cliente.getCiudad());
				st.setString(++i, cliente.getProvincia());
				st.setString(++i, cliente.getNIF());
				st.setBoolean(++i, cliente.getConIRPF());
				st.setString(++i, cliente.getTelefono());
				st.setString(++i, cliente.getMovil());
				st.setString(++i, cliente.getFax());
				st.setString(++i, cliente.getEmail());
				st.setString(++i, cliente.getWeb());
				st.setString(++i, cliente.getIdFormaPago());
				st.setString(++i, cliente.getNumeroCuenta());
				st.executeUpdate();
				conn.commit();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Inserta un nuevo producto en la Base de Datos.
	 * @param persona
	 * @throws Exception
	 */
	public void guardarProducto(ProductoVO producto) {
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "insert into PRODUCTOS " +
						"(\"ID_producto\",\"Nombre\",\"Precio\",\"IVA\") " +
						"values (?,?,?,?)";
				st = conn.prepareStatement(query);
				int i = 0;
				st.setString(++i, producto.getIdProducto());
				st.setString(++i, producto.getNombre());
				st.setFloat(++i, producto.getPrecio());
				st.setFloat(++i, producto.getIVA());
				st.executeUpdate();
				conn.commit();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Inserta un nuevo gasto en la Base de Datos.
	 * @param persona
	 * @throws Exception
	 */
	public void guardarGasto(GastoVO gasto,ArrayList<DetalleGastoVO> detalles) throws Exception {
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "insert into GASTOS " +
						"(\"ID_gasto\",\"Fecha\",\"ID_proveedor\",\"Nombre\",\"Direccion\",\"CP\",\"NIF\",\"Ciudad\",\"Provincia\",\"tipoIRPF\",\"Descuento\",\"BaseImponible1\",\"IVA1\",\"BaseImponible2\",\"IVA2\",\"BaseImponible3\",\"IVA3\",\"ID_formaPago\",\"NumeroCuenta\",\"TOTAL\") " +
						"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				st = conn.prepareStatement(query);
				int i = 0;
				st.setString(++i, gasto.getIdGasto());
				st.setDate(++i, new java.sql.Date(gasto.getFecha().getTime()));
				st.setString(++i, gasto.getIdProveedor());
				st.setString(++i, gasto.getNombre());
				st.setString(++i, gasto.getDireccion());
				st.setString(++i, gasto.getCP());
				st.setString(++i, gasto.getNIF());
				st.setString(++i, gasto.getCiudad());
				st.setString(++i, gasto.getProvincia());
				st.setFloat(++i, gasto.getTipoIRPF());
				st.setFloat(++i, gasto.getDescuento());
				st.setFloat(++i, gasto.getBaseImponible1());
				st.setFloat(++i, gasto.getIVA1());
				st.setFloat(++i, gasto.getBaseImponible2());
				st.setFloat(++i, gasto.getIVA2());
				st.setFloat(++i, gasto.getBaseImponible3());
				st.setFloat(++i, gasto.getIVA3());
				st.setString(++i, gasto.getIdFormaPago());
				st.setString(++i, gasto.getNumeroCuenta());
				st.setFloat(++i, gasto.getTOTAL());
				st.executeUpdate();
				conn.commit();
				
				// DETALLES
				for (int j=0;j<detalles.size();j++)
				{
					query = "insert into GASTOS_DATOS " +
							"(\"ID_gasto\",\"ID_producto\",\"Cantidad\",\"Descripcion\",\"Precio\",\"IVA\") " +
							"values (?,?,?,?,?,?)";
					st = conn.prepareStatement(query);
					i = 0;
					st.setString(++i, gasto.getIdGasto());
					st.setString(++i, detalles.get(j).getIdProducto());
					st.setFloat(++i, detalles.get(j).getCantidad());
					st.setString(++i, detalles.get(j).getDescripcion());
					st.setFloat(++i, detalles.get(j).getPrecio());
					st.setFloat(++i, detalles.get(j).getIVA());
					st.executeUpdate();
					conn.commit();
				}
			} catch(Exception e){
				e.printStackTrace();
				throw e;
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Inserta un nuevo ingreso en la Base de Datos.
	 * @param persona
	 * @throws Exception
	 */
	public void guardarIngreso(IngresoVO ingreso,ArrayList<DetalleIngresoVO> detalles) throws Exception {
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "insert into INGRESOS " +
						"(\"ID_ingreso\",\"Fecha\",\"ID_cliente\",\"Nombre\",\"Direccion\",\"CP\",\"NIF\",\"Ciudad\",\"Provincia\",\"TipoIRPF\",\"Descuento\",\"BaseImponible1\",\"IVA1\",\"BaseImponible2\",\"IVA2\",\"BaseImponible3\",\"IVA3\",\"ID_formaPago\",\"NumeroCuenta\",\"TOTAL\") " +
						"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				st = conn.prepareStatement(query);
				int i = 0;
				st.setString(++i, ingreso.getIdIngreso());
				st.setDate(++i, new java.sql.Date(ingreso.getFecha().getTime()));
				st.setString(++i, ingreso.getIdCliente());
				st.setString(++i, ingreso.getNombre());
				st.setString(++i, ingreso.getDireccion());
				st.setString(++i, ingreso.getCP());
				st.setString(++i, ingreso.getNIF());
				st.setString(++i, ingreso.getCiudad());
				st.setString(++i, ingreso.getProvincia());
				st.setFloat(++i, ingreso.getTipoIRPF());
				st.setFloat(++i, ingreso.getDescuento());
				st.setFloat(++i, ingreso.getBaseImponible1());
				st.setFloat(++i, ingreso.getIVA1());
				st.setFloat(++i, ingreso.getBaseImponible2());
				st.setFloat(++i, ingreso.getIVA2());
				st.setFloat(++i, ingreso.getBaseImponible3());
				st.setFloat(++i, ingreso.getIVA3());
				st.setString(++i, ingreso.getIdFormaPago());
				st.setString(++i, ingreso.getNumeroCuenta());
				st.setFloat(++i, ingreso.getTOTAL());
				st.executeUpdate();
				conn.commit();
				
				// DETALLES
				for (int j=0;j<detalles.size();j++)
				{
					query = "insert into INGRESOS_DATOS " +
							"(\"ID_ingreso\",\"ID_producto\",\"Cantidad\",\"Descripcion\",\"Precio\",\"IVA\") " +
							"values (?,?,?,?,?,?)";
					st = conn.prepareStatement(query);
					i = 0;
					st.setString(++i, ingreso.getIdIngreso());
					st.setString(++i, detalles.get(j).getIdProducto());
					st.setFloat(++i, detalles.get(j).getCantidad());
					st.setString(++i, detalles.get(j).getDescripcion());
					st.setFloat(++i, detalles.get(j).getPrecio());
					st.setFloat(++i, detalles.get(j).getIVA());
					st.executeUpdate();
					conn.commit();
				}
			} catch(Exception e){
				e.printStackTrace();
				throw e;
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Inserta una nueva forma de pago en la Base de Datos.
	 * @param formaPago
	 * @throws Exception
	 */
	public void guardarFormaPago(FormaPagoVO forma) {
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "insert into FORMAS_PAGO " +
						"(\"ID_formapago\",\"Nombre\",\"numeroCuenta\") " +
						"values (?,?,?)";
				st = conn.prepareStatement(query);
				int i = 0;
				st.setString(++i, forma.getIdFormaPago());
				st.setString(++i, forma.getNombre());
				st.setBoolean(++i, forma.getNumeroCuenta());
				st.executeUpdate();
				conn.commit();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/*
	 * ELIMINAR
	 */
	
	/**
	 * Elimina proveedor de la Base de Datos.
	 * @param idproveedor
	 * 
	 */
	public void eliminarProveedor(String idProveedor){
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "DELETE FROM PROVEEDORES WHERE \"ID_proveedor\"='" +
						idProveedor + "'";
				st = conn.prepareStatement(query);
				st.executeUpdate();
				conn.commit();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Elimina cliente de la Base de Datos.
	 * @param idcliente
	 * 
	 */
	public void eliminarCliente(String idCliente){
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "DELETE FROM CLIENTES WHERE \"ID_cliente\"='" +
						idCliente + "'";
				st = conn.prepareStatement(query);
				st.executeUpdate();
				conn.commit();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Elimina forma de pago de la Base de Datos.
	 * @param idproveedor
	 * 
	 */
	public void eliminarFormaPago(String idFormaPago){
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "DELETE FROM FORMAS_PAGO WHERE \"ID_formapago\"='" +
						idFormaPago + "'";
				st = conn.prepareStatement(query);
				st.executeUpdate();
				conn.commit();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Elimina producto de la Base de Datos.
	 * @param idgasto
	 * 
	 */
	public void eliminarProducto(String idProducto){
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "DELETE FROM PRODUCTOS WHERE \"ID_producto\"='" +
						idProducto + "'";
				st = conn.prepareStatement(query);
				st.executeUpdate();
				conn.commit();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}

	
	
	/**
	 * Elimina gasto en la Base de Datos.
	 * @param idgasto
	 * 
	 */
	public void eliminarGasto(String idGasto){
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "DELETE FROM GASTOS WHERE \"ID_gasto\"='" +
						idGasto + "'";
				st = conn.prepareStatement(query);
				st.executeUpdate();
				conn.commit();
				query = "DELETE FROM GASTOS_DATOS WHERE \"ID_gasto\"='" +
						idGasto + "'";
				st = conn.prepareStatement(query);
				st.executeUpdate();
				conn.commit();		
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/**
	 * Elimina ingreso en la Base de Datos.
	 * @param idingreso
	 * 
	 */
	public void eliminarIngreso(String idIngreso){
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		if(conn != null){
			try{
				String query = "DELETE FROM INGRESOS WHERE \"ID_ingreso\"='" +
						idIngreso + "'";
				st = conn.prepareStatement(query);
				st.executeUpdate();
				conn.commit();
				query = "DELETE FROM INGRESOS_DATOS WHERE \"ID_ingreso\"='" +
						idIngreso + "'";
				st = conn.prepareStatement(query);
				st.executeUpdate();
				conn.commit();		
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				this.cerrarTodo(conn, st, null);
			}
		}
	}
	
	/*
	 * CONEXION BBDD
	 */
			
	/**
	 * Inicializa la conexión con base de datos.
	 * @return Devuelve la conexión con base de datos. Si no se ha podido establecer, devuelve null.
	 */
	private Connection getConnection(){
		Connection con = null;
		try{
			//File archivoLck=new File("database/factunomo.lck");
			//if (archivoLck.exists()) archivoLck.delete();
            // Load the HSQL Database Engine JDBC driver
            // hsqldb.jar should be in the class path or made part of the current jar
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:file:database/factunomo","sa","");  
		} catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	
	
	/**
	 * Cierra la conexion, el statement y el resultset que se pasa por parametro.
	 * @param conn
	 * @param st
	 * @param rs
	 */
	private void cerrarTodo(Connection conn, Statement st, ResultSet rs){
		try {
			st.execute("SHUTDOWN COMPACT");
//System.out.println("Cerrado");
			//st = conn.prepareStatement("SHUTDOWN");
		} catch(Exception e){
			//System.out.println("Error al cerrar");
		}
		try{
			if(rs != null)
				
				rs.close();
		} catch(Exception e){}
		try{
			if(st != null)
			st.close();
		} catch(Exception e){}
		try{
			if(conn != null)
				conn.close();
		} catch(Exception e){}
		//File archivoLck=new File("database/factunomo.lck");
		//if (archivoLck.exists()) archivoLck.delete();
	}
	
	public Double FDecimales (Float numero){
		Double respuesta = Math.rint(numero*100)/100;
		return respuesta;
	}
	
	public String Decimales (Float numero){
		DecimalFormat df = new DecimalFormat("0.00");
		String respuesta = df.format(numero);
		return respuesta;

	}
	
}
