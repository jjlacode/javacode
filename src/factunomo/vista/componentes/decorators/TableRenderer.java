package factunomo.vista.componentes.decorators;

import java.awt.Color;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


public class TableRenderer extends JLabel implements TableCellRenderer {
	
	private static final long serialVersionUID = 1L;

	public TableRenderer() {
		setOpaque(true);
	}
	
	
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
		setForeground(Color.BLACK);
		setBackground(new Color(133,181,215,84));
		
		setText(value.toString());
		
		if (isSelected) {
			setBackground(new Color(0x5A98D7));
			setForeground(Color.WHITE);
		} else {
			setFont( new Font("Roman", Font.BOLD,12));
			if(row % 2 == 0){
				setForeground(Color.BLACK);
				setBackground(new Color(133,181,215,84));
			} else{
				setForeground(new Color(0xA93A0D));
				setBackground(new Color(208,235,253,99));
			}
		}
		
		return this;
		
	}
	
}
