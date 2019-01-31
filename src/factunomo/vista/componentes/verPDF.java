package factunomo.vista.componentes;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;



import javax.swing.*;


import com.sun.pdfview.*;





public class verPDF {
	 private JDialog ventana;
	 private Container contenedor;
	 private PDFPage pagina, siguiente;
	 private JPanel pabajo;
	 private JButton banterior;
	 private JButton bsiguiente;
	 private String idGasto;
	 private JFrame principal;
	 
	 PagePanel panelpdf;
	 PDFFile pdffile;
	 private Integer indice;
	 
	 /**
	  * Constructor por defecto.
	  */
	 public verPDF(String idgasto, JFrame principal){
		 this.principal = principal;
		 
		 idGasto = idgasto;
		 indice=1;
		 
		 leePDF();
		 
		 creaVentana();
		 
		 
	 }
	 
	 private void leePDF(){
		 pdffile =null;
		 
		 try {
			 File documento = null;
			 RandomAccessFile raf = null;
			 FileChannel canal = null;
			 ByteBuffer buf = null;
			 
			 documento = new File("elemento.pdf");
			 raf = new RandomAccessFile(documento, "r");
			 canal = raf.getChannel();
			 buf = ByteBuffer.allocate((int)canal.size());
			 canal.read(buf);
			 
			 pdffile = new PDFFile(buf);
			 
			 canal.close();
			 canal = null;
			 raf.close();
			 buf.clear();

			 
		 } catch(IOException ioe) {
			 JOptionPane.showMessageDialog(null, "Error al abrir el archivo"); 
		 }
	 }
	 
	 private void creaVentana(){
		 // Nueva ventana
		 ventana = new JDialog(principal,"Visor de PDF",true);
		 ventana.addWindowListener(new cerrarWin());
		 contenedor = ventana.getContentPane();
		 ventana.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		 Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

		 contenedor.setBackground(Color.WHITE);
		 contenedor.setLayout(new BorderLayout());
		 
		 panelpdf=new PagePanel();
		 panelpdf.setLayout(new BorderLayout());

		 
		 pabajo = new JPanel();
		 pabajo.setLayout(new FlowLayout());
		 bsiguiente=new JButton("Siguiente");
		 bsiguiente.addActionListener(new SiguienteAL());
		 JButton bcerrar=new JButton("Cerrar");
		 bcerrar.addActionListener(new CerrarAL());
		 JButton bguardar=new JButton("Guardar");
		 bguardar.addActionListener(new GuardarAL());
		 banterior=new JButton("Anterior");
		 banterior.addActionListener(new AnteriorAL());
		 
		 
		 banterior.setEnabled(false);
		 
		 
		 pabajo.add(bguardar);
		 pabajo.add(bcerrar);
		 pabajo.add(banterior);
		 pabajo.add(bsiguiente);

		 
		 contenedor.add(panelpdf,BorderLayout.CENTER);
		 contenedor.add(pabajo,BorderLayout.SOUTH);
		 ventana.pack();
		 int ancho=(int) (d.height/1.5);
		 ventana.setSize(new Dimension(ancho, d.height-50));
		 muestraPDF();
		 ventana.setLocationRelativeTo(null);
		 ventana.setVisible(true);

		 

		 
	 }
	 
	 public void muestraPDF(){
		 siguiente = pdffile.getPage(indice+1);
		 pagina = pdffile.getPage(indice);
		 System.out.println(indice);
		 panelpdf.showPage(pagina);
		 panelpdf.repaint();
		 if (siguiente!=null){
			 bsiguiente.setEnabled(true);
		 } else {
			 bsiguiente.setEnabled(false);
		 }
		 if (indice > 1){
			 banterior.setEnabled(true);
		 } else {
			 banterior.setEnabled(false);
		 }
		 
	 }
	 
	 class SiguienteAL implements ActionListener {
		 public void actionPerformed(ActionEvent evento){
			 indice++;
			 muestraPDF();
		 }
	 }
	 
	 class GuardarAL implements ActionListener {
		 public void actionPerformed(ActionEvent evento){
			 JFileChooser fileChooser = new JFileChooser();
			 fileChooser.setDialogTitle("Guardar");
			 String cadena = idGasto + ".pdf";
			 File archivo = new File(cadena);
			 fileChooser.setSelectedFile(archivo);
			  
			 int userSelection = fileChooser.showSaveDialog(ventana);
			 File f1 = new File("elemento.pdf");
	         File f2 = fileChooser.getSelectedFile();
	         boolean seguro = true;
	         if (f2.exists()) {
	                int result = JOptionPane.showConfirmDialog(ventana,
	                        "El archivo ya existe, ¿Sobreescribir?", "Sobreescribir",
	                        JOptionPane.YES_NO_OPTION);
	                switch (result) {
	                case JOptionPane.YES_OPTION:
	                    seguro = true;
	                    return;
	                case JOptionPane.CANCEL_OPTION:
	                    seguro = false;
	                    return;
	                default:
	                    return;
	                }
	            }
			  
			 if (seguro && userSelection == JFileChooser.APPROVE_OPTION) {
			     
			     try{
			          InputStream in = new FileInputStream(f1);

			          //For Append the file.
			          //OutputStream out = new FileOutputStream(f2,true);

			          //For Overwrite the file.
			          OutputStream out = new FileOutputStream(f2);

			          byte[] buf = new byte[1024];
			          int len;
			          while ((len = in.read(buf)) > 0){
			            out.write(buf, 0, len);
			          }
			          in.close();
			          out.close();
			          System.out.println("File copied.");
			        }
			        catch(FileNotFoundException ex){
			          System.out.println(ex.getMessage() + " in the specified directory.");
			          System.exit(0);
			        }
			        catch(IOException e){
			          System.out.println(e.getMessage());      
			        }
			 }
			 return;
		 }
	 }
	 
	 class CerrarAL implements ActionListener {
		 public void actionPerformed(ActionEvent evento){
			 ventana.dispose();
			 String sFichero = "elemento.pdf";
				File fichero = new File(sFichero);
			    if (fichero.exists())
			        fichero.delete();
			 return;
		 }
	 }
	 
	 class AnteriorAL implements ActionListener {
		 public void actionPerformed(ActionEvent evento){
			 indice--;
			 muestraPDF();
		 }
	 }
	 
	 class cerrarWin extends WindowAdapter
	 {
	    public void windowClosing(WindowEvent e)
	   {
	    	String sFichero = "elemento.pdf";
			File fichero = new File(sFichero);
		    if (fichero.exists())
		        fichero.delete();
		    ventana.dispose();
	   }
	 }
	 
	 
}