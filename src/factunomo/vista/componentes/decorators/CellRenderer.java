package factunomo.vista.componentes.decorators;

import java.awt.Color;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


public class CellRenderer extends JLabel implements ListCellRenderer {
	
	private static final long serialVersionUID = 1L;
/*
	public CellRenderer() {
		setOpaque(true);
	}*/

	public Component getListCellRendererComponent(JList list, Object value,int index, boolean isSelected, boolean cellHasFocus) {
		setText(value.toString());
		if (isSelected) {
			setBackground(new Color(0x5A98D7));
			setForeground(Color.WHITE);
		} else {
			setFont( new Font("Roman", Font.BOLD,12));
			if(index % 2 == 0){
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
