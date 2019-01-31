package factunomo.vista.componentes;


import java.io.FileOutputStream;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.awt.Color;

import javax.swing.JLabel;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.ColumnText;

import factunomo.modelo.obj.DetalleGastoVO;
import factunomo.modelo.obj.GastoVO;
import factunomo.modelo.obj.DetalleIngresoVO;
import factunomo.modelo.obj.IngresoVO;
import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.modelo.obj.FormaPagoVO;
import factunomo.modelo.Modelo;
import factunomo.utils.DateUtils;
import factunomo.utils.StringUtil;

import java.text.DecimalFormat;


public class ImprimePDF {



	/**
	 * Componentes
	 */
	private GastoVO gasto;
	private Modelo modelo;
	private BaseColor colorFondo;
	private String cadenapie;

	
	
	/**
	 * Constructores por defecto.
	 */
	public ImprimePDF(GastoVO gastoEditar, ArrayList<DetalleGastoVO> listaDetalleGasto, ConfiguracionVO datos) {

		// color fondo cabeceras
					int r=220;
					int g=241;
					int b=255;        
					colorFondo=new BaseColor(r,g,b);
		// cadenapie
		cadenapie = "";
		cadenapie = cadenapie + datos.getNombre() + " · ";
		cadenapie = cadenapie + datos.getDireccion() + " · ";
		cadenapie = cadenapie + datos.getCP() + " ";
		cadenapie = cadenapie + datos.getCiudad();
	    if (!datos.getCiudad().equals(datos.getProvincia()))
	    	{
	    	cadenapie = cadenapie + ", " + datos.getProvincia();
	    	}
	    if (!"".equals(datos.getTelefono()))
	    	{
	    	cadenapie = cadenapie + " · tel. " + datos.getTelefono();
	    	}
	    if (!"".equals(datos.getEmail()))
	    {
	    	cadenapie = cadenapie + " · Email " + datos.getEmail();
	    }
	    if (!"".equals(datos.getWeb()))
	    {
	    	cadenapie = cadenapie + " · Web " + datos.getWeb();
	    }
		try {
			Document documento = new Document(PageSize.A4, 50, 50, 50, 50);
			documento.addAuthor("Factunomo");
			documento.addTitle("Factura");
			String sFichero = "elemento.pdf";
			File fichero = new File(sFichero);
		    if (fichero.exists())
		        fichero.delete();
		    
			PdfWriter writer=PdfWriter.getInstance(documento, new FileOutputStream(sFichero));
			HeaderFooter event = new HeaderFooter();
			writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
	        writer.setPageEvent(event);
			documento.open();
			
			// CABECERA
			PdfPCell cell = null;
			Paragraph parrafo = null;
			String cadena = "";
			cadena = "Documento Informativo";
			parrafo = new Paragraph(cadena,
					FontFactory.getFont("arial",   // fuente
					16,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.LIGHT_GRAY));             // color
			parrafo.setAlignment(Element.ALIGN_CENTER);
			//parrafo.setBorder(PdfPCell.NO_BORDER);
			documento.add(parrafo);
			// Cuadro usuario
			PdfPTable cabecera = new PdfPTable(3);
			cabecera.setWidthPercentage(100);
			cabecera.setSpacingBefore(25);
			cabecera.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			PdfPTable datosUsuario = new PdfPTable(1);
			datosUsuario.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			cadena=gastoEditar.getNombre();
			datosUsuario.addCell(new Paragraph(cadena,
					FontFactory.getFont("arial",   // fuente
					14,                            // tamaño
					Font.BOLD,                   // estilo
					BaseColor.BLACK)));             // color
			if (gastoEditar.getDireccion() != "")
			{
				cadena=gastoEditar.getDireccion();
				datosUsuario.addCell(new Paragraph(cadena,
						FontFactory.getFont("arial",   // fuente
						12,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
			}
			cadena = "";
			if (gastoEditar.getCP() != null)
				{
				cadena=cadena + gastoEditar.getCP() + " ";
				}
			if (gastoEditar.getCiudad() != null)
				{
				cadena = cadena + gastoEditar.getCiudad();
				}
			if (gastoEditar.getProvincia() != null && !gastoEditar.getProvincia().equals(gastoEditar.getCiudad()))
				{
				cadena = cadena + ", " + gastoEditar.getProvincia();
				}
			
			datosUsuario.addCell(new Paragraph(cadena,
					FontFactory.getFont("arial",   // fuente
					12,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			if (gastoEditar.getNIF() != null)
			{
			cadena=gastoEditar.getNIF();
			datosUsuario.addCell(new Paragraph(cadena,
					FontFactory.getFont("arial",   // fuente
					12,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			}
			// Cuadro Logo
			PdfPTable logo = new PdfPTable(3);
			logo.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			logo.addCell(" ");			
			// Cuadro gasto
			PdfPTable datosGasto = new PdfPTable(2);
			//datosGasto.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPTable idgasto1 = new PdfPTable(1);
			cell  = new PdfPCell(new Phrase ("GASTO"));
			cell.setColspan(2);
		    cell.setBackgroundColor(colorFondo);
		    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    datosGasto.addCell(cell);
			cell  = new PdfPCell(new Phrase ("Número:"));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			idgasto1.addCell(cell);
			cell  = new PdfPCell(new Phrase ("Fecha:"));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			idgasto1.addCell(cell);
			PdfPTable idgasto2 = new PdfPTable(1);
			idgasto2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			idgasto2.addCell(gastoEditar.getIdGasto());
			cadena = ""+DateUtils.dateToString(gastoEditar.getFecha());
			idgasto2.addCell(cadena);
			cell = new PdfPCell (idgasto1);
			cell.setPadding(0);
			datosGasto.addCell(cell);
			cell = new PdfPCell (idgasto2);
			cell.setPadding(0);
			datosGasto.addCell(cell);

			// Constructor cabecera
			cabecera.addCell(datosUsuario);
			cabecera.addCell(logo);
			cabecera.addCell(datosGasto);
			documento.add(cabecera);
			
			// DATOS CLIENTE
			// Cuadro datosCliente
			PdfPTable cliente = new PdfPTable(2);
			cliente.setWidths(new int[]{2, 5});
			cliente.setWidthPercentage(100);
			cliente.setSpacingBefore(25);
			cell  = new PdfPCell(new Phrase ("Datos del Cliente"));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(colorFondo);
			cell.setColspan(2);
			cliente.addCell(cell);
			PdfPTable cliente1 = new PdfPTable(1);
			cliente1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			cliente1.setWidthPercentage(100);
			cell = new PdfPCell(new Phrase("Nombre:",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			cliente1.addCell(cell);
			cell = new PdfPCell(new Phrase("Dirección:",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			cliente1.addCell(cell);
			cell = new PdfPCell(new Phrase("CP y localidad:",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			cliente1.addCell(cell);
			cell = new PdfPCell(new Phrase("CIF:",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			cliente1.addCell(cell);
			PdfPTable cliente2 = new PdfPTable(1);
			cliente2.setWidthPercentage(75);
			cliente2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			cell = new PdfPCell(new Phrase(datos.getNombre(),
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cliente2.addCell(cell);
			cell = new PdfPCell(new Phrase(datos.getDireccion(),
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cliente2.addCell(cell);
			cadena = datos.getCP() + " " + datos.getCiudad();
			if (!datos.getCiudad().equals(datos.getProvincia())){
				cadena = cadena + ", " + datos.getProvincia();
			}
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cliente2.addCell(cell);
			cell = new PdfPCell(new Phrase(datos.getNIF(),
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cliente2.addCell(cell);
			//PdfPTable tabla  = new PdfPTable(cliente1);
			// Constructor datosCliente
			cell = new PdfPCell (cliente1);
			cell.setPadding(0);
			cliente.addCell(cell);
			cell = new PdfPCell (cliente2);
			cell.setPadding(0);
			cliente.addCell(cell);
		    documento.add(cliente);
		    
		    // DETALLE
		    PdfPTable detalle = new PdfPTable(6);
		    detalle.setWidths(new int[]{3, 24,3,4,3,6});
		    detalle.setWidthPercentage(100);
		    detalle.setSpacingBefore(25);
		    cell  = new PdfPCell(new Phrase ("Detalle"));
		    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    cell.setColspan(6);
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			cell = new PdfPCell(new Phrase("Cód.",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			cell = new PdfPCell(new Phrase("Descripción",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			cell = new PdfPCell(new Phrase("Cant.",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			cell = new PdfPCell(new Phrase("Precio",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			cell = new PdfPCell(new Phrase("IVA",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			cell = new PdfPCell(new Phrase("Subtotal",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			
			Float baseImponible = 0F;
			Float subTotal = 0F;
			for(int i = 0;i<listaDetalleGasto.size();i++){
				cadena = " ";
				if (listaDetalleGasto.get(i).getIdProducto() != null){
					cadena = listaDetalleGasto.get(i).getIdProducto();
				}
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detalle.addCell(cell);
					
				cadena = "" + listaDetalleGasto.get(i).getDescripcion();
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				detalle.addCell(cell);
				cadena = "" + listaDetalleGasto.get(i).getCantidad();
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detalle.addCell(cell);
				cadena = "" + listaDetalleGasto.get(i).getPrecio();
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detalle.addCell(cell);
				cadena = "" + listaDetalleGasto.get(i).getIVA();
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detalle.addCell(cell);
				subTotal = listaDetalleGasto.get(i).getPrecio()*listaDetalleGasto.get(i).getCantidad();
				cadena = "" + Decimales(subTotal);
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detalle.addCell(cell);
				
				baseImponible = baseImponible + subTotal;
			}
			// Constructor Detalle
			documento.add(detalle);
			
			// TOTAL
		    PdfPTable total = new PdfPTable(2);
		    total.setWidths(new int[]{20,6});
		    total.setWidthPercentage(60);
		    total.setSpacingBefore(25);
		    total.setHorizontalAlignment(Element.ALIGN_RIGHT);
		    cadena = "Base Imponible";
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					12,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBackgroundColor(colorFondo);
			total.addCell(cell);
			cadena = "" + baseImponible;
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBackgroundColor(colorFondo);
			total.addCell(cell);
			// total1
			PdfPTable total1 = new PdfPTable(4);
			total1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			total1.setWidths(new int[]{5,4,6,6});
			// total2
			PdfPTable total2 = new PdfPTable(1);
			total2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			if (gastoEditar.getDescuento() != 0){
				cadena = "Descuento";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = "" + gastoEditar.getDescuento() + "%";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = "" + Decimales(baseImponible);
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						8,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setBackgroundColor(colorFondo);
				total1.addCell(cell);
				subTotal = (gastoEditar.getDescuento()/100)*baseImponible;
				cadena = "-" + Decimales(subTotal);
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				baseImponible = baseImponible - subTotal;
				cadena = "" + Decimales(baseImponible);
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total2.addCell(cell);
			}
			Float baseDescuento=baseImponible;
			// IVA1
			cadena = "IVA";
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.NO_BORDER);
			total1.addCell(cell);
			cadena = " " + gastoEditar.getIVA1() + "%";
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.NO_BORDER);
			total1.addCell(cell);
			cadena = " " + Decimales(gastoEditar.getBaseImponible1());
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					8,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			total1.addCell(cell);
			subTotal = (gastoEditar.getIVA1()/100)*gastoEditar.getBaseImponible1();
			cadena = " " + Decimales(subTotal);
			baseImponible = baseImponible + subTotal;		
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.NO_BORDER);
			total1.addCell(cell);
			cadena = " " + Decimales(baseImponible);
				
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.NO_BORDER);
			total2.addCell(cell);
			// IVA2
			if (gastoEditar.getIVA2() != 0)
			{
				cadena = "IVA";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + gastoEditar.getIVA2() + "%";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + Decimales(gastoEditar.getBaseImponible2());
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						8,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setBackgroundColor(colorFondo);
				total1.addCell(cell);
				subTotal = (gastoEditar.getIVA2()/100)*gastoEditar.getBaseImponible2();
				cadena = " " + Decimales(subTotal);
				baseImponible = baseImponible + subTotal;		
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + Decimales(baseImponible);				
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total2.addCell(cell);
			}
			// IVA3
			if (gastoEditar.getIVA3() != 0)
			{
				cadena = "IVA";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + gastoEditar.getIVA3() + "%";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + Decimales(gastoEditar.getBaseImponible3());
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						8,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setBackgroundColor(colorFondo);
				total1.addCell(cell);
				subTotal = (gastoEditar.getIVA3()/100)*gastoEditar.getBaseImponible3();
				cadena = " " + Decimales(subTotal);
				baseImponible = baseImponible + subTotal;		
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + Decimales(baseImponible);				
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total2.addCell(cell);
			}
			// IRPF
			if (gastoEditar.getTipoIRPF() != 0f)
			{
				cadena = "IRPF";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + gastoEditar.getTipoIRPF() + "%";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + Decimales(baseDescuento);
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						8,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setBackgroundColor(colorFondo);
				total1.addCell(cell);
				subTotal = (gastoEditar.getTipoIRPF()/100)*baseDescuento;
				cadena = "-" + Decimales(subTotal);
				baseImponible = baseImponible - subTotal;		
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + Decimales(baseImponible);				
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total2.addCell(cell);
			}
			// Total
			total.addCell(total1);
			total.addCell(total2);
			cadena = "TOTAL";
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					12,                            // tamaño
					Font.BOLD,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			//cell.setBorder(PdfPCell.NO_BORDER);
			total.addCell(cell);
			cadena = "" + Decimales(gastoEditar.getTOTAL());
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					12,                            // tamaño
					Font.BOLD,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			//cell.setBorder(PdfPCell.NO_BORDER);
			total.addCell(cell);
			
			// Constructor total		
			documento.add(total);
			
			documento.close();
			} catch (DocumentException de) {
				System.err.println("error en documento");
			} catch (IOException ioe) {
				System.err.println("error entrada salida");
			}
		
		
		return;
	}
	
	public ImprimePDF(IngresoVO ingresoEditar, ArrayList<DetalleIngresoVO> listaDetalleIngreso, ConfiguracionVO datos) {
		// color fondo cabeceras
					int r=220;
					int g=241;
					int b=255;        
					colorFondo=new BaseColor(r,g,b);
		// cadenapie
		modelo=new Modelo();
		cadenapie = "";
		cadenapie = cadenapie + datos.getNombre() + " · ";
		cadenapie = cadenapie + datos.getDireccion() + " · ";
		cadenapie = cadenapie + datos.getCP() + " ";
		cadenapie = cadenapie + datos.getCiudad();
	    if (!datos.getCiudad().equals(datos.getProvincia()))
	    	{
	    	cadenapie = cadenapie + ", " + datos.getProvincia();
	    	}
	    if (!"".equals(datos.getTelefono()))
	    	{
	    	cadenapie = cadenapie + " · tel. " + datos.getTelefono();
	    	}
	    if (!"".equals(datos.getEmail()))
	    {
	    	cadenapie = cadenapie + " · Email " + datos.getEmail();
	    }
	    if (!"".equals(datos.getWeb()))
	    {
	    	cadenapie = cadenapie + " · Web " + datos.getWeb();
	    }
		try {
			Document documento = new Document(PageSize.A4, 50, 50, 50, 50);
			documento.addAuthor("Factunomo");
			documento.addTitle("Factura");
			String sFichero = "elemento.pdf";
			File fichero = new File(sFichero);
		    if (fichero.exists())
		        fichero.delete();
		    
			PdfWriter writer=PdfWriter.getInstance(documento, new FileOutputStream(sFichero));
			HeaderFooter event = new HeaderFooter();
			writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
	        writer.setPageEvent(event);
			documento.open();
			
			// CABECERA
			PdfPCell cell = null;
			Paragraph parrafo = null;
			String cadena = "";
			cadena = "Factura";
			parrafo = new Paragraph(cadena,
					FontFactory.getFont("arial",   // fuente
					16,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.LIGHT_GRAY));             // color
			parrafo.setAlignment(Element.ALIGN_CENTER);
			documento.add(parrafo);
			// Cuadro usuario
			PdfPTable cabecera = new PdfPTable(3);
			cabecera.setWidthPercentage(100);
			cabecera.setSpacingBefore(25);
			cabecera.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			PdfPTable datosUsuario = new PdfPTable(1);
			datosUsuario.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			cadena=datos.getNombre();
			datosUsuario.addCell(new Paragraph(cadena,
					FontFactory.getFont("arial",   // fuente
					14,                            // tamaño
					Font.BOLD,                   // estilo
					BaseColor.BLACK)));             // color
			if (datos.getDireccion() != "")
			{
				cadena=datos.getDireccion();
				datosUsuario.addCell(new Paragraph(cadena,
						FontFactory.getFont("arial",   // fuente
						12,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
			}
			cadena = "";
			if (datos.getCP() != null)
				{
				cadena=cadena + datos.getCP()+ " ";
				}
			if (datos.getCiudad() != null)
				{
				cadena = cadena + datos.getCiudad();
				}
			if (datos.getProvincia() != null && !datos.getProvincia().equals(datos.getCiudad()))
				{
				cadena = cadena + ", " + datos.getProvincia();
				}
			datosUsuario.addCell(new Paragraph(cadena,
					FontFactory.getFont("arial",   // fuente
					12,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			if (datos.getNIF() != null)
			{
			cadena=datos.getNIF();
			datosUsuario.addCell(new Paragraph(cadena,
					FontFactory.getFont("arial",   // fuente
					12,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			}
			// Cuadro Logo
			PdfPTable logo = new PdfPTable(3);
			logo.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			logo.addCell(" ");			
			// Cuadro gasto
			PdfPTable datosGasto = new PdfPTable(2);
			PdfPTable idgasto1 = new PdfPTable(1);
			cell  = new PdfPCell(new Phrase ("FACTURA"));
			cell.setColspan(2);
		    cell.setBackgroundColor(colorFondo);
		    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    datosGasto.addCell(cell);
			cell  = new PdfPCell(new Phrase ("Número:"));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			idgasto1.addCell(cell);
			cell  = new PdfPCell(new Phrase ("Fecha:"));
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			idgasto1.addCell(cell);
			PdfPTable idgasto2 = new PdfPTable(1);
			idgasto2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			idgasto2.addCell(ingresoEditar.getIdIngreso());
			cadena = ""+DateUtils.dateToString(ingresoEditar.getFecha());
			idgasto2.addCell(cadena);
			cell = new PdfPCell (idgasto1);
			cell.setPadding(0);
			datosGasto.addCell(cell);
			cell = new PdfPCell (idgasto2);
			cell.setPadding(0);
			datosGasto.addCell(cell);

			// Constructor cabecera
			cabecera.addCell(datosUsuario);
			cabecera.addCell(logo);
			cabecera.addCell(datosGasto);
			documento.add(cabecera);
			
			// DATOS CLIENTE
			// Cuadro datosCliente
			PdfPTable cliente = new PdfPTable(2);
			cliente.setWidths(new int[]{2, 5});
			cliente.setWidthPercentage(100);
			cliente.setSpacingBefore(25);
			cell  = new PdfPCell(new Phrase ("Datos del Cliente"));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(colorFondo);
			cell.setColspan(2);
			cliente.addCell(cell);
			PdfPTable cliente1 = new PdfPTable(1);
			cliente1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			cliente1.setWidthPercentage(100);
			cell = new PdfPCell(new Phrase("Nombre:",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			cliente1.addCell(cell);
			cell = new PdfPCell(new Phrase("Dirección:",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			cliente1.addCell(cell);
			cell = new PdfPCell(new Phrase("CP y localidad:",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			cliente1.addCell(cell);
			cell = new PdfPCell(new Phrase("CIF:",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			cliente1.addCell(cell);
			PdfPTable cliente2 = new PdfPTable(1);
			cliente2.setWidthPercentage(75);
			cliente2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			cell = new PdfPCell(new Phrase(ingresoEditar.getNombre(),
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cliente2.addCell(cell);
			cell = new PdfPCell(new Phrase(ingresoEditar.getDireccion(),
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cliente2.addCell(cell);
			cadena = ingresoEditar.getCP() + " " + ingresoEditar.getCiudad();
			if (!ingresoEditar.getCiudad().equals(ingresoEditar.getProvincia())){
				cadena = cadena + ", " + ingresoEditar.getProvincia();
			}
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cliente2.addCell(cell);
			cell = new PdfPCell(new Phrase(ingresoEditar.getNIF(),
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBorder(PdfPCell.NO_BORDER);
			cliente2.addCell(cell);
			// Constructor datosCliente
			cell = new PdfPCell (cliente1);
			cell.setPadding(0);
			cliente.addCell(cell);
			cell = new PdfPCell (cliente2);
			cell.setPadding(0);
			cliente.addCell(cell);
		    documento.add(cliente);
		    
		    // DETALLE
		    PdfPTable detalle = new PdfPTable(6);
		    detalle.setWidths(new int[]{3, 24,3,4,3,6});
		    detalle.setWidthPercentage(100);
		    detalle.setSpacingBefore(25);
		    cell  = new PdfPCell(new Phrase ("Detalle"));
		    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    cell.setColspan(6);
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			cell = new PdfPCell(new Phrase("Cód.",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			cell = new PdfPCell(new Phrase("Descripción",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			cell = new PdfPCell(new Phrase("Cant.",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			cell = new PdfPCell(new Phrase("Precio",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			cell = new PdfPCell(new Phrase("IVA",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			cell = new PdfPCell(new Phrase("Subtotal",
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setBackgroundColor(colorFondo);
			detalle.addCell(cell);
			
			Float baseImponible = 0F;
			Float subTotal = 0F;
			for(int i = 0;i<listaDetalleIngreso.size();i++){
				cadena = " ";
				if (listaDetalleIngreso.get(i).getIdProducto() != null){
					cadena = listaDetalleIngreso.get(i).getIdProducto();
				}
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detalle.addCell(cell);
					
				cadena = "" + listaDetalleIngreso.get(i).getDescripcion();
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				detalle.addCell(cell);
				cadena = "" + listaDetalleIngreso.get(i).getCantidad();
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detalle.addCell(cell);
				cadena = "" + listaDetalleIngreso.get(i).getPrecio();
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detalle.addCell(cell);
				cadena = "" + listaDetalleIngreso.get(i).getIVA();
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detalle.addCell(cell);
				subTotal = listaDetalleIngreso.get(i).getPrecio()*listaDetalleIngreso.get(i).getCantidad();
				cadena = "" + Decimales(subTotal);
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detalle.addCell(cell);
				
				baseImponible = baseImponible + subTotal;
			}
			// Constructor Detalle
			documento.add(detalle);

						
			// total
		    PdfPTable total = new PdfPTable(2);
		    total.setWidths(new int[]{20,6});
		    total.setWidthPercentage(60);
		    total.setSpacingBefore(25);
		    
		    total.setHorizontalAlignment(Element.ALIGN_RIGHT);
		    cadena = "Base Imponible";
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					12,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBackgroundColor(colorFondo);
			total.addCell(cell);
			cadena = "" + baseImponible;
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBackgroundColor(colorFondo);
			total.addCell(cell);
			// total1
			PdfPTable total1 = new PdfPTable(4);
			total1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			total1.setWidths(new int[]{5,4,6,6});
			// total2
			PdfPTable total2 = new PdfPTable(1);
			total2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			if (ingresoEditar.getDescuento() != 0){
				cadena = "Descuento";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = "" + ingresoEditar.getDescuento() + "%";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = "" + Decimales(baseImponible);
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						8,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setBackgroundColor(colorFondo);
				total1.addCell(cell);
				subTotal = (ingresoEditar.getDescuento()/100)*baseImponible;
				cadena = "-" + Decimales(subTotal);
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				baseImponible = baseImponible - subTotal;
				
				cadena = "" + Decimales(baseImponible);
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total2.addCell(cell);
			}
			Float baseDescuento=baseImponible;
			// IVA1
			cadena = "IVA";
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.NO_BORDER);
			total1.addCell(cell);
			cadena = " " + ingresoEditar.getIVA1() + "%";
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.NO_BORDER);
			total1.addCell(cell);
			cadena = " " + Decimales(ingresoEditar.getBaseImponible1()*(1-ingresoEditar.getDescuento()/100));
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					8,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(colorFondo);
			total1.addCell(cell);
			subTotal = (ingresoEditar.getIVA1()/100)*ingresoEditar.getBaseImponible1()*(1-ingresoEditar.getDescuento()/100);
			cadena = " " + Decimales(subTotal);
			baseImponible = baseImponible + subTotal;		
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.NO_BORDER);
			total1.addCell(cell);
			cadena = " " + Decimales(baseImponible);
				
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					10,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(PdfPCell.NO_BORDER);
			total2.addCell(cell);
			// IVA2
			if (ingresoEditar.getIVA2() != 0)
			{
				cadena = "IVA";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + ingresoEditar.getIVA2() + "%";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + Decimales(ingresoEditar.getBaseImponible2()*(1-ingresoEditar.getDescuento()/100));
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						8,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setBackgroundColor(colorFondo);
				total1.addCell(cell);
				subTotal = (ingresoEditar.getIVA2()/100)*ingresoEditar.getBaseImponible2()*(1-ingresoEditar.getDescuento()/100);
				cadena = " " + Decimales(subTotal);
				baseImponible = baseImponible + subTotal;		
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + Decimales(baseImponible);				
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total2.addCell(cell);
			}
			// IVA3
			if (ingresoEditar.getIVA3() != 0)
			{
				cadena = "IVA";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + ingresoEditar.getIVA3() + "%";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + Decimales(ingresoEditar.getBaseImponible3()*(1-ingresoEditar.getDescuento()/100));
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						8,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setBackgroundColor(colorFondo);
				total1.addCell(cell);
				subTotal = (ingresoEditar.getIVA3()/100)*ingresoEditar.getBaseImponible3()*(1-ingresoEditar.getDescuento()/100);
				cadena = " " + Decimales(subTotal);
				baseImponible = baseImponible + subTotal;		
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + Decimales(baseImponible);				
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total2.addCell(cell);
			}
			// IRPF
			if (ingresoEditar.getTipoIRPF() != 0f)
			{
				cadena = "IRPF";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + ingresoEditar.getTipoIRPF() + "%";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + Decimales(baseDescuento);
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						8,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setBackgroundColor(colorFondo);
				total1.addCell(cell);
				subTotal = (ingresoEditar.getTipoIRPF()/100)*baseDescuento;
				cadena = "-" + Decimales(subTotal);
				baseImponible = baseImponible - subTotal;		
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total1.addCell(cell);
				cadena = " " + Decimales(baseImponible);				
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(PdfPCell.NO_BORDER);
				total2.addCell(cell);
			}
			// Total
			total.addCell(total1);
			total.addCell(total2);
			cadena = "TOTAL";
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					12,                            // tamaño
					Font.BOLD,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			total.addCell(cell);
			cadena = "" + Decimales(ingresoEditar.getTOTAL());
			cell = new PdfPCell(new Phrase(cadena,
					FontFactory.getFont("arial",   // fuente
					12,                            // tamaño
					Font.BOLD,                   // estilo
					BaseColor.BLACK)));             // color
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			total.addCell(cell);
			
			// Constructor Total
			documento.add(total);
			
			// Cuadro forma pago
						PdfPTable formaPago = new PdfPTable(2);
						formaPago.setWidths(new int[]{3,7});
						formaPago.setWidthPercentage(100);
						formaPago.setSpacingBefore(25);
						formaPago.setExtendLastRow(false);
						formaPago.setHorizontalAlignment(Element.ALIGN_LEFT);
						PdfPTable cabeceras = new PdfPTable(1);
						cabeceras.setExtendLastRow(false);
						cell  = new PdfPCell(new Phrase ("Forma de pago"));
						cell.setColspan(2);
					    cell.setBackgroundColor(colorFondo);
					    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					    formaPago.addCell(cell);
						cell  = new PdfPCell(new Phrase ("Forma de pago:"));
						cell.setBorder(PdfPCell.NO_BORDER);
						cell.setBackgroundColor(colorFondo);
						cabeceras.addCell(cell);
						cell  = new PdfPCell(new Phrase ("Número de cuenta:"));
						cell.setBorder(PdfPCell.NO_BORDER);
						cell.setBackgroundColor(colorFondo);
						cabeceras.addCell(cell);
						PdfPTable contenidos = new PdfPTable(1);
						contenidos.setExtendLastRow(false);
						contenidos.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
						FormaPagoVO formaP=modelo.getFormaPago(ingresoEditar.getIdFormaPago());
						contenidos.addCell(formaP.getNombre());
						// Obtener nombre de forma de pago
						contenidos.addCell(ingresoEditar.getNumeroCuenta());
						cell = new PdfPCell (cabeceras);
						cell.setPadding(0);
						formaPago.addCell(cell);
						cell = new PdfPCell (contenidos);
						cell.setPadding(0);
						formaPago.addCell(cell);
						
						// Constructor forma pago
						documento.add(formaPago);
			
			documento.close();
			} catch (DocumentException de) {
				System.err.println("error en documento");
			} catch (IOException ioe) {
				System.err.println("error entrada salida");
			}
		
		
		return;
	}
	
	class HeaderFooter extends PdfPageEventHelper {
        /** Alternating phrase for the header. */
		String cadena = "";
        Phrase header = new Phrase();
        PdfPCell pie = new PdfPCell();
        /** Current page number (will be reset for every chapter). */
        int numPaginas = 0;
        int numero = 1;
 
        /**
         * Increase the page number.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onStartPage(PdfWriter writer, Document document) {     	
            numPaginas++;
        }
 
        /**
         * Adds the header and the footer.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
        	header = new Phrase("Pagina " + numero + " de " + numPaginas,
					FontFactory.getFont("arial",   // fuente
					8,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK));  
            Rectangle rect = writer.getBoxSize("art");
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, header,
                    (rect.getLeft() + rect.getRight()) / 2, rect.getTop() + 18, 0);
            PdfPTable tablaPie = new PdfPTable(1);
            tablaPie.setHorizontalAlignment(Element.ALIGN_CENTER);
		    
	    
			pie = new PdfPCell (new Phrase(cadenapie,
					FontFactory.getFont("arial",   // fuente
					8,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.BLACK)));             // color
			pie.setHorizontalAlignment(Element.ALIGN_CENTER);
			pie.setBorder(PdfPCell.NO_BORDER);
			pie.setBackgroundColor(colorFondo);
			tablaPie.addCell(pie);
			float x = (rect.getLeft() + rect.getRight()) / 2;
			float y =  rect.getBottom() - 18;
			float w = rect.getRight() - rect.getLeft();
			tablaPie.setTotalWidth(w);
			tablaPie.writeSelectedRows(0, -1, rect.getLeft(), y, writer.getDirectContent());
        }
    }
	
	public String Decimales (Float numero){
		//Double respuesta = Math.rint(numero*100)/100;
		
		DecimalFormat df = new DecimalFormat("0.00");
		String respuesta = df.format(numero);
		return respuesta;
	}
	
	
}
