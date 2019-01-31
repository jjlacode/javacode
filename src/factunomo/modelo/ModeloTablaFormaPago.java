package factunomo.modelo;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.*;

import java.awt.KeyboardFocusManager;

import javax.swing.event.*;

import factunomo.modelo.obj.FormaPagoVO;
import factunomo.modelo.obj.ProductoVO;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ModeloTablaFormaPago implements TableModel {

	/**
	 * Lista con los datos. Cada elemento de la lista es una instancia de
	 * DetalleGasto
	 */
	private ArrayList<FormaPagoVO> datos = new ArrayList<FormaPagoVO>();
	/**
	 * Lista de suscriptores. El JTable será un suscriptor de este modelo de
	 * datos
	 */
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	JDialog ventana;
	JTable tabla;
	
	/**
	 * Constructor
	 * @param ventana
	 * @param tabla
	 */
	public ModeloTablaFormaPago(JDialog ventana, JTable tabla){
		this.ventana = ventana;
		this.tabla = tabla;
	}

	
	
	
	public int getColumnCount() {
		return 3;
	}

	public int getRowCount() {
		return datos.size();
	}
	
	
	public FormaPagoVO getForma(int rowIndex) {
		return (FormaPagoVO) (datos.get(rowIndex));
	}
	
	public void addTableModelListener(TableModelListener l) {
		// Añade el suscriptor a la lista de suscriptores
		listeners.add(l);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		FormaPagoVO aux = (FormaPagoVO) (datos.get(rowIndex));
		switch (columnIndex) {
		case 0:
			return aux.getIdFormaPago();
		case 1:
			return aux.getNombre();
		case 2:
			if (aux.getNumeroCuenta()) return "Sí";
			else return "No";
		default:
			return null;
		}
	}


	public void addForma(FormaPagoVO nuevaForma) {
		this.datos.add(nuevaForma);
	}
	
	public void borraTabla(){
		ArrayList<FormaPagoVO> vacio = new ArrayList<FormaPagoVO>();
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
			return "Num Cuenta";
		default:
			return null;
		}
	}


	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		default:
			return false;
		}
	}

	
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// Obtiene el detalle de la fila indicada
		FormaPagoVO aux = (FormaPagoVO) (datos.get(rowIndex));

		// Cambia el campo que indica columnIndex, poniendole el
		// aValue que se nos pasa.
		switch (columnIndex) {
		case 0:
			aux.setIdFormaPago((String) aValue);
		case 1:
			aux.setNombre((String) aValue);
			break;
		case 2:
			aux.setNumeroCuenta((Boolean) aValue);
			break;
		default:
			break;
		}
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
			//System.out.println("Hasta aqui sí");
			((TableModelListener) listeners.get(i)).tableChanged(evento);
		}
	}

}
