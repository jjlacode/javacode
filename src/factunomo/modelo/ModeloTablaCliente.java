package factunomo.modelo;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.*;

import java.awt.KeyboardFocusManager;

import javax.swing.event.*;

import factunomo.modelo.obj.ClienteVO;
import factunomo.modelo.obj.FormaPagoVO;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ModeloTablaCliente implements TableModel {

	/**
	 * Lista con los datos. Cada elemento de la lista es una instancia de
	 * DetalleGasto
	 */
	private ArrayList<ClienteVO> datos = new ArrayList<ClienteVO>();
	/**
	 * Lista de suscriptores. El JTable será un suscriptor de este modelo de
	 * datos
	 */
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	JDialog ventana;
	JTable tabla;
	Modelo modelo;
	
	/**
	 * Constructor
	 * @param ventana
	 * @param tabla
	 */
	public ModeloTablaCliente(JDialog ventana, JTable tabla){
		this.ventana = ventana;
		this.tabla = tabla;
		this.modelo=new Modelo();
	}

	
	
	
	public int getColumnCount() {
		return 5;
	}

	public int getRowCount() {
		return datos.size();
	}
	
	
	public ClienteVO getCliente(int rowIndex) {
		return (ClienteVO) (datos.get(rowIndex));
	}
	
	public void addTableModelListener(TableModelListener l) {
		// Añade el suscriptor a la lista de suscriptores
		listeners.add(l);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		ClienteVO aux = (ClienteVO) (datos.get(rowIndex));
		switch (columnIndex) {
		case 0:
			return aux.getIdCliente();
		case 1:
			return aux.getNombre();
		case 2:
			return aux.getDireccion();
		case 3:
			return aux.getNIF();
		case 4:
			FormaPagoVO formaPago=new FormaPagoVO();
			formaPago=modelo.getFormaPago(aux.getIdFormaPago());
			return formaPago.getNombre();
		default:
			return null;
		}
	}


	public void addCliente(ClienteVO nuevoIngreso) {
		this.datos.add(nuevoIngreso);

		// Avisa a los suscriptores creando un TableModelEvent...
		//TableModelEvent evento;
		//evento = new TableModelEvent(this, this.getRowCount() - 1, this.getRowCount() - 1, TableModelEvent.ALL_COLUMNS,TableModelEvent.INSERT);

		// ... y avisando a los suscriptores
		//avisaSuscriptores(evento);
	}
	
	public void borraTabla(){
		ArrayList<ClienteVO> vacio = new ArrayList<ClienteVO>();
		datos = vacio; 
	}
	
	public Class<String> getColumnClass(int columnIndex) {
		return String.class;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "ID";
		case 1:
			return "Nombre";
		case 2:
			return "Direccion";
		case 3:
			return "CIF";
		case 4:
			return "Forma pago";
		default:
			return null;
		}
	}


	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// Obtiene el detalle de la fila indicada
		ClienteVO aux = (ClienteVO) (datos.get(rowIndex));

		// Avisa a los suscriptores del cambio, creando un TableModelEvent ...
		TableModelEvent evento = new TableModelEvent(this);

		// ... y pasándoselo a los suscriptores.
		avisaSuscriptores(evento);
	}

	/**
	 * Pasa a los suscriptores el evento.
	 */
	private void avisaSuscriptores(TableModelEvent evento) {
		int i;
		//CambioTablaDetalle.tableChanged();
		// Bucle para todos los suscriptores en la lista, se llama al metodo
		// tableChanged() de los mismos, pasándole el evento.
		for (i = 0; i < listeners.size(); i++){
			((TableModelListener) listeners.get(i)).tableChanged(evento);
		}
	}


	public void mostrarError(){
		int result = JOptionPane.showConfirmDialog(ventana,
				"El campo requiere un número decimal", "Entrada incorrecta",
				JOptionPane.DEFAULT_OPTION);
	}
	
	public String Decimales (Float numero){
		DecimalFormat df = new DecimalFormat("0.00");
		String respuesta = df.format(numero);
		return respuesta;
	}
	

}
