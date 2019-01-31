package factunomo.vista.componentes.decorators;

import javax.swing.*;

import java.awt.Component;
import java.awt.Color;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.ComponentUI;
import javax.swing.DefaultCellEditor;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.ArrayList;

public class CellEditor extends AbstractCellEditor implements TableCellEditor {

	JComponent component = new JTextField();
	JDialog ventana;
	
	public CellEditor(JDialog ventana){
		this.ventana=ventana;
	}
	

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
			int rowIndex, int vColIndex) {
		switch (vColIndex){
		case 2:
			((JTextField) component).setText((String) value);
			break;
		default:

			String cadena = "" + value;
			cadena=cadena.replace(",","."); 
			((JTextField) component).setText(cadena);

		}


		((JTextField) component).selectAll();

		// Avisa a los suscriptores del cambio, creando un CellEditorEvent ...
		ChangeEvent evento = new ChangeEvent("comienzo");

		// ... y pasándoselo a los suscriptores.
		avisaSuscriptores(evento);

		return component;
	}

	public Object getCellEditorValue() {

		return ((JTextField) component).getText();
	}

	public boolean isCellEditable(EventObject anEvent) {  
		if (anEvent instanceof MouseEvent) {   
			return ((MouseEvent)anEvent).getClickCount() >= 2;  
		}  
		return true;  
	}  
	
	/**
	 * Pasa a los suscriptores el evento.
	 */

	private void avisaSuscriptores(ChangeEvent evento) {
		int i;
		//CambioTablaDetalle.tableChanged();
		// Bucle para todos los suscriptores en la lista, se llama al metodo
		// tableChanged() de los mismos, pasándole el evento.
		CellEditorListener[] listeners = this.getCellEditorListeners();
		for (i = 0; i < listeners.length; i++){
			((CellEditorListener) listeners[i]).editingCanceled(evento);
		}
	}
}
