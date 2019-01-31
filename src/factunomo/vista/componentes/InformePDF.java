package factunomo.vista.componentes;


import java.io.FileOutputStream;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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

import factunomo.modelo.obj.FiltroVO;
import factunomo.modelo.obj.GastoVO;
import factunomo.modelo.obj.IngresoVO;
import factunomo.modelo.obj.ConfiguracionVO;
import factunomo.modelo.obj.FormaPagoVO;
import factunomo.modelo.obj.IrpfVO;
import factunomo.modelo.obj.IvaVO;
import factunomo.modelo.Modelo;
import factunomo.utils.DateUtils;
import factunomo.utils.StringUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;


public class InformePDF {



	/**
	 * Componentes
	 */
	private ArrayList<IvaVO> listaIvaIngresos;
	private IvaVO ivaIngresos;
	private ArrayList<IvaVO> listaIvaGastos;
	private IvaVO ivaGastos;
	private ArrayList<IrpfVO> listaIrpfIngresos;
	private IrpfVO irpfIngresos;
	private ArrayList<IrpfVO> listaIrpfGastos;
	private IrpfVO irpfGastos;
	private Modelo modelo;
	private BaseColor colorFondo;
	private String cadenapie;
	private ConfiguracionVO datos;
	private FiltroVO filtroInforme;
	private ArrayList<IngresoVO> listaIngresos;
	private ArrayList<GastoVO> listaGastos;
	private Float cantidadIvaIngresos;
	private Float cantidadIRPFIngresos;
	private Float cantidadBaseIngresos;
	private Float cantidadTotalIngresos;
	private Float cantidadIvaGastos;
	private Float cantidadIRPFGastos;
	private Float cantidadBaseGastos;
	private Float cantidadTotalGastos;
	
	private SimpleDateFormat formato=new SimpleDateFormat("dd-MM-yyyy");

	
	
	/**
	 * Constructor por defecto.
	 */
	public InformePDF(FiltroVO filtroInforme, Boolean informe1, Boolean informe2, Boolean informe3, Boolean informe4, Boolean informe5, Boolean informe6) {
		
		// Inicializar modelo
		modelo=new Modelo();
		
		// Obtener datos usuario
		datos=modelo.getDatosFactunomo();
		
		// Inicializar variables de IVA
		Float tipoIVA[]=new Float[3];
		Float base[]=new Float[3];
		cantidadIvaIngresos=0f;
		cantidadIRPFIngresos=0f;
		cantidadBaseIngresos=0F;
		cantidadTotalIngresos=0f;
		cantidadIvaGastos=0f;
		cantidadIRPFGastos=0f;
		cantidadBaseGastos=0F;
		cantidadTotalGastos=0f;
		
		// Inicializar variables de IVA
		
		
		// Inicializar tablas
		PdfPTable ingresos=null;
		PdfPTable ingresos1=null;
		PdfPTable gastos=null;
		PdfPTable gastos1=null;

		// color fondo cabeceras
					int r=220;
					int g=241;
					int b=255;        
					colorFondo=new BaseColor(r,g,b);
		// PIE DE PÁGINA
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
			documento.addTitle("Informe");
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
			cadena = "Informe";
			parrafo = new Paragraph(cadena,
					FontFactory.getFont("arial",   // fuente
					16,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.LIGHT_GRAY));             // color
			parrafo.setAlignment(Element.ALIGN_CENTER);
			documento.add(parrafo);
			SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formatoAnho = new SimpleDateFormat("yyyy");
			
			
			Date fechaInicio=filtroInforme.getFechaInicio();	
			Date fechaFinal=filtroInforme.getFechaFinal();
			int anhoInicio=Integer.parseInt(formatoAnho.format(fechaInicio));
			int anhoFinal=Integer.parseInt(formatoAnho.format(fechaFinal));
			
			cadena = "Periodo del ";
			cadena+= formatoFecha.format(fechaInicio) + " al " + formatoFecha.format(fechaFinal);
			if (fechaInicio.equals(modelo.fechaInicio(anhoInicio, modelo.getPeriodo(fechaInicio)))
					&& fechaFinal.equals(modelo.fechaFinal(anhoFinal, modelo.getPeriodo(fechaFinal)))
					&& anhoInicio==anhoFinal)
			{
				if (modelo.getPeriodo(fechaInicio)==modelo.getPeriodo(fechaFinal))
				{
					cadena ="Periodo T" + modelo.getPeriodo(fechaFinal) + " " + anhoInicio;
				}
				if (modelo.getPeriodo(fechaInicio)==1 && modelo.getPeriodo(fechaFinal)==4)
				{
					cadena ="Periodo Anual " + anhoInicio;
				}
			}
			
			parrafo = new Paragraph(cadena,
					FontFactory.getFont("arial",   // fuente
					12,                            // tamaño
					Font.NORMAL,                   // estilo
					BaseColor.GRAY));             // color
			parrafo.setAlignment(Element.ALIGN_CENTER);
			documento.add(parrafo);
			
			// Cuadro usuario
			PdfPTable cabecera = new PdfPTable(2);
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
				cadena=cadena + datos.getCP();
				}
			if (datos.getCiudad() != null)
				{
				cadena = cadena + " " + datos.getCiudad();
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
			

			// Constructor cabecera
			cabecera.addCell(datosUsuario);
			cabecera.addCell(logo);
			documento.add(cabecera);
			
			
			// INGRESOS
			if (informe1)
			{
				// Obtener listado de ingresos
				listaIngresos=modelo.getIngresos(filtroInforme);
				
				listaIvaIngresos=new ArrayList<IvaVO>();
				listaIrpfIngresos=new ArrayList<IrpfVO>();
				
				// Tabla Ingresos
				if (informe3)
				{
					ingresos = new PdfPTable(7);
					ingresos.setWidths(new int[]{5,5,14,4,4,4,4});
					ingresos.setWidthPercentage(100);
					ingresos.setSpacingBefore(25);
					ingresos.setHeaderRows(2);				
					cell  = new PdfPCell(new Phrase ("INGRESOS"));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBackgroundColor(colorFondo);
					cell.setColspan(7);
					ingresos.addCell(cell);
					
					// Cabecera tabla ingresos
					cell = new PdfPCell(new Phrase("Ingreso",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					ingresos.addCell(cell);
					cell = new PdfPCell(new Phrase("Fecha",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					ingresos.addCell(cell);
					cell = new PdfPCell(new Phrase("Cliente",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					ingresos.addCell(cell);
					cell = new PdfPCell(new Phrase("Base",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					ingresos.addCell(cell);
					cell = new PdfPCell(new Phrase("IVA",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					ingresos.addCell(cell);
					cell = new PdfPCell(new Phrase("IRPF",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					ingresos.addCell(cell);
					cell = new PdfPCell(new Phrase("Total",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					ingresos.addCell(cell);
				}
				
				// Recorrido lista Ingresos
				for (int i=0;i<listaIngresos.size();i++)
				{
					tipoIVA[0]=listaIngresos.get(i).getIVA1();
					tipoIVA[1]=listaIngresos.get(i).getIVA2();
					tipoIVA[2]=listaIngresos.get(i).getIVA3();
					base[0]=listaIngresos.get(i).getBaseImponible1();
					base[1]=listaIngresos.get(i).getBaseImponible2();
					base[2]=listaIngresos.get(i).getBaseImponible3();
					Float baseImponible=base[0]+base[1]+base[2];
					Float cantidadIva=0f;
					cantidadTotalIngresos+=listaIngresos.get(i).getTOTAL();
					cantidadBaseIngresos+=baseImponible;
					
					// Sumar y agrupar tipos de IVA
					for (int k=0;k<tipoIVA.length;k++)
					{
						Boolean nuevo=true;
						for (int j=0;j<listaIvaIngresos.size();j++)
						{		
							if (listaIvaIngresos.get(j).getTipoIVA().equals(tipoIVA[k]))
							{
								listaIvaIngresos.get(j).setBase(listaIvaIngresos.get(j).getBase()+base[k]);
								listaIvaIngresos.get(j).setCantidad(listaIvaIngresos.get(j).getCantidad()+base[k]*tipoIVA[k]/100);
								nuevo=false;
								break;
							}
							
						}
						cantidadIva+=base[k]*tipoIVA[k]/100;
						if (nuevo)
						{
							ivaIngresos=new IvaVO();
							ivaIngresos.setTipoIVA(tipoIVA[k]);
							ivaIngresos.setCantidad(base[k]*tipoIVA[k]/100);
							ivaIngresos.setBase(base[k]);
							listaIvaIngresos.add(ivaIngresos);
						}
					}
					cantidadIvaIngresos+=cantidadIva;
					
					// Sumar y agrupar tipos de IRPF
					Boolean nuevo=true;
					for (int j=0;j<listaIrpfIngresos.size();j++)
					{		
						if (listaIrpfIngresos.get(j).getIdCliente().equals(listaIngresos.get(i).getNombre()))
						{
							listaIrpfIngresos.get(j).setBase(listaIrpfIngresos.get(j).getBase()+baseImponible);
							listaIrpfIngresos.get(j).setCantidad(listaIrpfIngresos.get(j).getCantidad()+baseImponible*listaIngresos.get(i).getTipoIRPF()/100);
							nuevo=false;
							break;
						}
						
					}
					if (nuevo)
					{
						irpfIngresos=new IrpfVO();
						irpfIngresos.setIdCliente(listaIngresos.get(i).getNombre());
						irpfIngresos.setTipoIRPF(listaIngresos.get(i).getTipoIRPF());
						irpfIngresos.setCantidad(baseImponible*listaIngresos.get(i).getTipoIRPF()/100);
						irpfIngresos.setBase(baseImponible);
						listaIrpfIngresos.add(irpfIngresos);
					}
					cantidadIRPFIngresos+=baseImponible*listaIngresos.get(i).getTipoIRPF()/100;
					
					// Lineas ingresos
					if (informe3)
					{
						cell = new PdfPCell(new Phrase(listaIngresos.get(i).getIdIngreso(),
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						ingresos.addCell(cell);
						cadena=formato.format(listaIngresos.get(i).getFecha());
						cell = new PdfPCell(new Phrase(cadena,
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						ingresos.addCell(cell);
						cell = new PdfPCell(new Phrase(listaIngresos.get(i).getNombre(),
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						ingresos.addCell(cell);	
						cell = new PdfPCell(new Phrase(Decimales(baseImponible),
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						ingresos.addCell(cell);
						cell = new PdfPCell(new Phrase(Decimales(cantidadIva),
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						ingresos.addCell(cell);
						cell = new PdfPCell(new Phrase(Decimales(listaIngresos.get(i).getTipoIRPF()*baseImponible/100),
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						ingresos.addCell(cell);
						cell = new PdfPCell(new Phrase(Decimales(listaIngresos.get(i).getTOTAL()),
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						ingresos.addCell(cell);
					}
				}
				if (informe3)
				{
		
					// Pie tabla ingresos
					cell = new PdfPCell(new Phrase("TOTAL",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					cell.setColspan(3);
					ingresos.addCell(cell);
					cell = new PdfPCell(new Phrase(Decimales(cantidadBaseIngresos),
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					ingresos.addCell(cell);
					cell = new PdfPCell(new Phrase(Decimales(cantidadIvaIngresos),
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					ingresos.addCell(cell);
					cell = new PdfPCell(new Phrase(Decimales(cantidadIRPFIngresos),
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					ingresos.addCell(cell);
					cell = new PdfPCell(new Phrase(Decimales(cantidadTotalIngresos),
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					ingresos.addCell(cell);
					documento.add(ingresos);
				}
			}
			
			// GASTOS
			if (informe2)
			{
				// Obtener listado de gastos
				listaGastos=modelo.getGastos(filtroInforme);
				
				listaIvaGastos=new ArrayList<IvaVO>();
				listaIrpfGastos=new ArrayList<IrpfVO>();
				
				// Tabla Gastos
				if (informe3)
				{
					gastos = new PdfPTable(7);
				    gastos.setWidths(new int[]{5,5,14,4,4,4,4});
					gastos.setWidthPercentage(100);
					gastos.setSpacingBefore(25);
					gastos.setHeaderRows(2);
					cell  = new PdfPCell(new Phrase ("GASTOS"));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBackgroundColor(colorFondo);
					cell.setColspan(7);
					gastos.addCell(cell);
								
					// Cabecera tabla gastos
					cell = new PdfPCell(new Phrase("Gasto",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					gastos.addCell(cell);
					cell = new PdfPCell(new Phrase("Fecha",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					gastos.addCell(cell);
					cell = new PdfPCell(new Phrase("Proveedor",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					gastos.addCell(cell);
					cell = new PdfPCell(new Phrase("Base",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					gastos.addCell(cell);
					cell = new PdfPCell(new Phrase("IVA",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					gastos.addCell(cell);
					cell = new PdfPCell(new Phrase("IRPF",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					gastos.addCell(cell);
					cell = new PdfPCell(new Phrase("Total",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setBackgroundColor(colorFondo);
					gastos.addCell(cell);
				}
				
				// Recorrer lista de gastos
				for (int i=0;i<listaGastos.size();i++)
				{
					tipoIVA[0]=listaGastos.get(i).getIVA1();
					tipoIVA[1]=listaGastos.get(i).getIVA2();
					tipoIVA[2]=listaGastos.get(i).getIVA3();
					base[0]=listaGastos.get(i).getBaseImponible1();
					base[1]=listaGastos.get(i).getBaseImponible2();
					base[2]=listaGastos.get(i).getBaseImponible3();
					Float baseImponible=base[0]+base[1]+base[2];					Float cantidadIva=0f;
					cantidadTotalGastos+=listaGastos.get(i).getTOTAL();
					cantidadBaseGastos+=baseImponible;
					
					// Sumar y agrupar tipos de IVA
					for (int k=0;k<tipoIVA.length;k++)
					{
						Boolean nuevo=true;
						for (int j=0;j<listaIvaGastos.size();j++)
						{
							if (listaIvaGastos.get(j).getTipoIVA().equals(tipoIVA[k]))
							{
								listaIvaGastos.get(j).setBase(listaIvaGastos.get(j).getBase()+base[k]);
								listaIvaGastos.get(j).setCantidad(listaIvaGastos.get(j).getCantidad()+base[k]*tipoIVA[k]/100);
								nuevo=false;
								break;
							}
							
						}
						cantidadIva+=base[k]*tipoIVA[k]/100;
						if (nuevo)
						{
							ivaGastos = new IvaVO();
							ivaGastos.setTipoIVA(tipoIVA[k]);
							ivaGastos.setCantidad(base[k]*tipoIVA[k]/100);
							ivaGastos.setBase(base[k]);
							listaIvaGastos.add(ivaGastos);
						}
					}
					cantidadIvaGastos+=cantidadIva;
					
					// Sumar y agrupar tipos de IRPF
					Boolean nuevo=true;
					for (int j=0;j<listaIrpfGastos.size();j++)
					{		
						if (listaIrpfGastos.get(j).getIdCliente().equals(listaGastos.get(i).getNombre()))
						{
							listaIrpfGastos.get(j).setBase(listaIrpfGastos.get(j).getBase()+baseImponible);
							listaIrpfGastos.get(j).setCantidad(listaIrpfGastos.get(j).getCantidad()+baseImponible*listaGastos.get(i).getTipoIRPF()/100);
							nuevo=false;
							break;
						}
						
					}
					if (nuevo)
					{
						irpfGastos=new IrpfVO();
						irpfGastos.setIdCliente(listaGastos.get(i).getNombre());
						irpfGastos.setTipoIRPF(listaGastos.get(i).getTipoIRPF());
						irpfGastos.setCantidad(baseImponible*listaGastos.get(i).getTipoIRPF()/100);
						irpfGastos.setBase(baseImponible);
						listaIrpfGastos.add(irpfGastos);
					}
					cantidadIRPFGastos+=baseImponible*listaGastos.get(i).getTipoIRPF()/100;
					
					// Lineas gastos
					if (informe3)
					{
						cell = new PdfPCell(new Phrase(listaGastos.get(i).getIdGasto(),
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						gastos.addCell(cell);
						cadena=formato.format(listaGastos.get(i).getFecha());
						cell = new PdfPCell(new Phrase(cadena,
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						gastos.addCell(cell);
						cell = new PdfPCell(new Phrase(listaGastos.get(i).getNombre(),
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						gastos.addCell(cell);	
						cell = new PdfPCell(new Phrase(Decimales(baseImponible),
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						gastos.addCell(cell);
						cell = new PdfPCell(new Phrase(Decimales(cantidadIva),
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						gastos.addCell(cell);
						cell = new PdfPCell(new Phrase(Decimales(listaGastos.get(i).getTipoIRPF()*baseImponible/100),
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						gastos.addCell(cell);
						cell = new PdfPCell(new Phrase(Decimales(listaGastos.get(i).getTOTAL()),
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setBorder(PdfPCell.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						gastos.addCell(cell);
					}
				}
				
				if (informe3)
				{
					
					// Pie tabla gastos
					cell = new PdfPCell(new Phrase("TOTAL",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					cell.setColspan(3);
					gastos.addCell(cell);
					cell = new PdfPCell(new Phrase(Decimales(cantidadBaseGastos),
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					gastos.addCell(cell);
					cell = new PdfPCell(new Phrase(Decimales(cantidadIvaGastos),
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					gastos.addCell(cell);
					cell = new PdfPCell(new Phrase(Decimales(cantidadIRPFGastos),
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					gastos.addCell(cell);
					cell = new PdfPCell(new Phrase(Decimales(cantidadTotalGastos),
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					gastos.addCell(cell);
					documento.add(gastos);
				}
			}
			
			// RESUMEN IVA
			if (informe4)
			{
				// Inicializar tablas
				PdfPTable cliente1=null;
				PdfPTable cliente2=null;
				PdfPTable cliente3=null;
				
				// Cuadro IVA
				PdfPTable IVA = new PdfPTable(3);
				IVA.setWidths(new int[]{5, 3, 3});
				IVA.setWidthPercentage(60);
				IVA.setSpacingBefore(25);
				IVA.setKeepTogether(true);
				IVA.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell = new PdfPCell(new Phrase("RESUMEN IVA"));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				cell.setColspan(3);
				IVA.addCell(cell);
				cell = new PdfPCell(new Phrase("Tipo IVA",
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				IVA.addCell(cell);
				cell = new PdfPCell(new Phrase("Base",
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				IVA.addCell(cell);
				cell  = new PdfPCell(new Phrase ("Subtotal"));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				IVA.addCell(cell);
				
				// IVA RERPERCUTIDO
				if (informe1)
				{
					cell = new PdfPCell(new Phrase("IVA Devengado",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(3);
					IVA.addCell(cell);
					cliente1 = new PdfPTable(1);
					cliente1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
					cliente2 = new PdfPTable(1);
					cliente2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
					cliente3 = new PdfPTable(1);
					cliente3.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
					for (int i=0;i<listaIvaIngresos.size();i++)
					{
						if (!listaIvaIngresos.get(i).getBase().equals(0f))
						{
							cadena="IVA "+listaIvaIngresos.get(i).getTipoIVA()+"%";
							cell = new PdfPCell(new Phrase(cadena,
									FontFactory.getFont("arial",   // fuente
									10,                            // tamaño
									Font.NORMAL,                   // estilo
									BaseColor.BLACK)));             // color
							cell.setBorder(PdfPCell.NO_BORDER);
							cliente1.addCell(cell);
							cell = new PdfPCell(new Phrase(Decimales(listaIvaIngresos.get(i).getBase()),
									FontFactory.getFont("arial",   // fuente
									10,                            // tamaño
									Font.NORMAL,                   // estilo
									BaseColor.BLACK)));             // color
							cell.setBorder(PdfPCell.NO_BORDER);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cliente2.addCell(cell);
							cell = new PdfPCell(new Phrase(Decimales(listaIvaIngresos.get(i).getCantidad()),
									FontFactory.getFont("arial",   // fuente
									10,                            // tamaño
									Font.NORMAL,                   // estilo
									BaseColor.BLACK)));             // color
							cell.setBorder(PdfPCell.NO_BORDER);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cliente3.addCell(cell);
						}
					}
					cell = new PdfPCell (cliente1);
					cell.setPadding(0);
					IVA.addCell(cell);
					cell = new PdfPCell (cliente2);
					cell.setPadding(0);
					IVA.addCell(cell);
					cell = new PdfPCell (cliente3);
					cell.setPadding(0);
					IVA.addCell(cell);
					cell = new PdfPCell(new Phrase("TOTAL",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBackgroundColor(colorFondo);
					IVA.addCell(cell);
					cell = new PdfPCell(new Phrase(Decimales(cantidadBaseIngresos),
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					IVA.addCell(cell);
					cell = new PdfPCell(new Phrase(Decimales(cantidadIvaIngresos),
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					IVA.addCell(cell);
				}
				
				// IVA SOPORTADO
				if (informe2)
				{
					cell = new PdfPCell(new Phrase("IVA Deducible",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(3);
					IVA.addCell(cell);
					cliente1 = new PdfPTable(1);
					cliente1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
					cliente2 = new PdfPTable(1);
					cliente2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
					cliente3 = new PdfPTable(1);
					cliente3.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
	
					for (int i=0;i<listaIvaGastos.size();i++)
					{
						if (!listaIvaGastos.get(i).getBase().equals(0f))
						{
							cadena="IVA "+listaIvaGastos.get(i).getTipoIVA()+"%";
							cell = new PdfPCell(new Phrase(cadena,
									FontFactory.getFont("arial",   // fuente
									10,                            // tamaño
									Font.NORMAL,                   // estilo
									BaseColor.BLACK)));             // color
							cell.setBorder(PdfPCell.NO_BORDER);
							cliente1.addCell(cell);
							cell = new PdfPCell(new Phrase(Decimales(listaIvaGastos.get(i).getBase()),
									FontFactory.getFont("arial",   // fuente
									10,                            // tamaño
									Font.NORMAL,                   // estilo
									BaseColor.BLACK)));             // color
							cell.setBorder(PdfPCell.NO_BORDER);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cliente2.addCell(cell);
							cell = new PdfPCell(new Phrase(Decimales(listaIvaGastos.get(i).getCantidad()),
									FontFactory.getFont("arial",   // fuente
									10,                            // tamaño
									Font.NORMAL,                   // estilo
									BaseColor.BLACK)));             // color
							cell.setBorder(PdfPCell.NO_BORDER);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cliente3.addCell(cell);
						}
					}
					cell = new PdfPCell (cliente1);
					cell.setPadding(0);
					IVA.addCell(cell);
					cell = new PdfPCell (cliente2);
					cell.setPadding(0);
					IVA.addCell(cell);
					cell = new PdfPCell (cliente3);
					cell.setPadding(0);
					IVA.addCell(cell);
					cell = new PdfPCell(new Phrase("TOTAL",
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBackgroundColor(colorFondo);
					IVA.addCell(cell);
					cell = new PdfPCell(new Phrase(Decimales(cantidadBaseGastos),
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					IVA.addCell(cell);
					cell = new PdfPCell(new Phrase(Decimales(cantidadIvaGastos),
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBackgroundColor(colorFondo);
					IVA.addCell(cell);
				}
				
				// Balance IVA
				cadena="IVA a Ingresar";
				if (cantidadIvaIngresos<cantidadIvaGastos) cadena="IVA a Deducir";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				IVA.addCell(cell);
				cell = new PdfPCell(new Phrase(Decimales(cantidadIvaIngresos-cantidadIvaGastos),
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBackgroundColor(colorFondo);
				cell.setColspan(2);
				IVA.addCell(cell);
			    documento.add(IVA);
			}
			
			// RESUMEN IRPF
			if (informe5)
			{
				Boolean total=false;
				// Inicializar tablas
				PdfPTable cliente1=null;
				PdfPTable cliente2=null;
				PdfPTable cliente3=null;
				
				// Cuadro IRPF
				PdfPTable IRPF = new PdfPTable(3);
				IRPF.setWidths(new int[]{15, 4, 4});
				IRPF.setWidthPercentage(80);
				IRPF.setSpacingBefore(25);
				IRPF.setKeepTogether(true);
				IRPF.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell = new PdfPCell(new Phrase("RESUMEN IRPF"));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				cell.setColspan(3);
				IRPF.addCell(cell);
				cell = new PdfPCell(new Phrase("Cliente",
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				IRPF.addCell(cell);
				cell = new PdfPCell(new Phrase("Base",
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				IRPF.addCell(cell);
				cell  = new PdfPCell(new Phrase ("Subtotal"));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				IRPF.addCell(cell);
				
				// IRPF RETENIDO
				if (informe1)
				{
					if (cantidadIRPFIngresos>0)
					{
						cell = new PdfPCell(new Phrase("IRPF Retenido",
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setColspan(3);
						IRPF.addCell(cell);
						cliente1 = new PdfPTable(1);
						cliente1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
						cliente2 = new PdfPTable(1);
						cliente2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
						cliente3 = new PdfPTable(1);
						cliente3.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
						for (int i=0;i<listaIrpfIngresos.size();i++)
						{
							if (listaIrpfIngresos.get(i).getTipoIRPF()>0f)
							{
								total=true;
								cell = new PdfPCell(new Phrase(listaIrpfIngresos.get(i).getIdCliente(),
										FontFactory.getFont("arial",   // fuente
										10,                            // tamaño
										Font.NORMAL,                   // estilo
										BaseColor.BLACK)));             // color
								cell.setBorder(PdfPCell.NO_BORDER);
								cliente1.addCell(cell);
								cell = new PdfPCell(new Phrase(Decimales(listaIrpfIngresos.get(i).getBase()),
										FontFactory.getFont("arial",   // fuente
										10,                            // tamaño
										Font.NORMAL,                   // estilo
										BaseColor.BLACK)));             // color
								cell.setBorder(PdfPCell.NO_BORDER);
								cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								cliente2.addCell(cell);
								cell = new PdfPCell(new Phrase(Decimales(listaIrpfIngresos.get(i).getCantidad()),
										FontFactory.getFont("arial",   // fuente
										10,                            // tamaño
										Font.NORMAL,                   // estilo
										BaseColor.BLACK)));             // color
								cell.setBorder(PdfPCell.NO_BORDER);
								cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								cliente3.addCell(cell);
							}
						}
						cell = new PdfPCell (cliente1);
						cell.setPadding(0);
						IRPF.addCell(cell);
						cell = new PdfPCell (cliente2);
						cell.setPadding(0);
						IRPF.addCell(cell);
						cell = new PdfPCell (cliente3);
						cell.setPadding(0);
						IRPF.addCell(cell);
							cell = new PdfPCell(new Phrase("TOTAL",
									FontFactory.getFont("arial",   // fuente
									10,                            // tamaño
									Font.NORMAL,                   // estilo
									BaseColor.BLACK)));             // color
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(colorFondo);
							IRPF.addCell(cell);
							cell = new PdfPCell(new Phrase(Decimales(cantidadBaseIngresos),
									FontFactory.getFont("arial",   // fuente
									10,                            // tamaño
									Font.NORMAL,                   // estilo
									BaseColor.BLACK)));             // color
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setBackgroundColor(colorFondo);
							IRPF.addCell(cell);
							cell = new PdfPCell(new Phrase(Decimales(cantidadIRPFIngresos),
									FontFactory.getFont("arial",   // fuente
									10,                            // tamaño
									Font.NORMAL,                   // estilo
									BaseColor.BLACK)));             // color
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setBackgroundColor(colorFondo);
							IRPF.addCell(cell);
					}
				}

				// IRPF PRACTICADO
				if (informe2)
				{
					if (cantidadIRPFGastos>0)
					{
						cell = new PdfPCell(new Phrase("IRPF Practicado",
								FontFactory.getFont("arial",   // fuente
								10,                            // tamaño
								Font.NORMAL,                   // estilo
								BaseColor.BLACK)));             // color
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setColspan(3);
						IRPF.addCell(cell);
						cliente1 = new PdfPTable(1);
						cliente1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
						cliente2 = new PdfPTable(1);
						cliente2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
						cliente3 = new PdfPTable(1);
						cliente3.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
						for (int i=0;i<listaIrpfGastos.size();i++)
						{
							if (!listaIrpfGastos.get(i).getTipoIRPF().equals(0f))
							{
								cadena=""+listaIrpfGastos.get(i).getIdCliente();
								cell = new PdfPCell(new Phrase(cadena,
										FontFactory.getFont("arial",   // fuente
										10,                            // tamaño
										Font.NORMAL,                   // estilo
										BaseColor.BLACK)));             // color
								cell.setBorder(PdfPCell.NO_BORDER);
								cliente1.addCell(cell);
								cell = new PdfPCell(new Phrase(Decimales(listaIrpfGastos.get(i).getBase()),
										FontFactory.getFont("arial",   // fuente
										10,                            // tamaño
										Font.NORMAL,                   // estilo
										BaseColor.BLACK)));             // color
								cell.setBorder(PdfPCell.NO_BORDER);
								cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								cliente2.addCell(cell);
								cell = new PdfPCell(new Phrase(Decimales(listaIrpfGastos.get(i).getCantidad()),
										FontFactory.getFont("arial",   // fuente
										10,                            // tamaño
										Font.NORMAL,                   // estilo
										BaseColor.BLACK)));             // color
								cell.setBorder(PdfPCell.NO_BORDER);
								cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								cliente3.addCell(cell);
							}
						}
						cell = new PdfPCell (cliente1);
						cell.setPadding(0);
						IRPF.addCell(cell);
						cell = new PdfPCell (cliente2);
						cell.setPadding(0);
						IRPF.addCell(cell);
						cell = new PdfPCell (cliente3);
						cell.setPadding(0);
						IRPF.addCell(cell);
							cell = new PdfPCell(new Phrase("TOTAL",
									FontFactory.getFont("arial",   // fuente
									10,                            // tamaño
									Font.NORMAL,                   // estilo
									BaseColor.BLACK)));             // color
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBackgroundColor(colorFondo);
							IRPF.addCell(cell);
							cell = new PdfPCell(new Phrase(Decimales(cantidadBaseGastos),
									FontFactory.getFont("arial",   // fuente
									10,                            // tamaño
									Font.NORMAL,                   // estilo
									BaseColor.BLACK)));             // color
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setBackgroundColor(colorFondo);
							IRPF.addCell(cell);
							cell = new PdfPCell(new Phrase(Decimales(cantidadIRPFGastos),
									FontFactory.getFont("arial",   // fuente
									10,                            // tamaño
									Font.NORMAL,                   // estilo
									BaseColor.BLACK)));             // color
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setBackgroundColor(colorFondo);
							IRPF.addCell(cell);
					}
				}
							
				// Balance IRPF
				cadena="IRPF a Ingresar";
				if (cantidadIRPFIngresos>cantidadIRPFGastos) cadena="IRPF a Deducir";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				IRPF.addCell(cell);
				cell = new PdfPCell(new Phrase(Decimales(cantidadIRPFIngresos-cantidadIRPFGastos),
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBackgroundColor(colorFondo);
				cell.setColspan(2);
				IRPF.addCell(cell);

			    documento.add(IRPF);
			}
			
			// BALANCE
			if (informe6)
			{
				// Inicializar tablas
				PdfPTable cliente1=null;
				PdfPTable cliente2=null;
				PdfPTable cliente3=null;
				PdfPTable cliente4=null;
				PdfPTable cliente5=null;
				cliente1 = new PdfPTable(1);
				cliente1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				cliente2 = new PdfPTable(1);
				cliente2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				cliente3 = new PdfPTable(1);
				cliente3.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				cliente4 = new PdfPTable(1);
				cliente4.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				cliente5 = new PdfPTable(1);
				cliente5.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				
				// Cuadro BALANCE
				PdfPTable Balance = new PdfPTable(5);
				Balance.setWidths(new int[]{15,4,4,4,4});
				Balance.setWidthPercentage(80);
				Balance.setSpacingBefore(25);
				Balance.setKeepTogether(true);
				Balance.setHorizontalAlignment(Element.ALIGN_LEFT);
				// Cabecera
				cell = new PdfPCell(new Phrase("BALANCE"));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				cell.setColspan(5);
				Balance.addCell(cell);
				cell = new PdfPCell(new Phrase("",
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				Balance.addCell(cell);
				cell = new PdfPCell(new Phrase("Base",
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				Balance.addCell(cell);
				cell = new PdfPCell(new Phrase("IVA",
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				Balance.addCell(cell);
				cell = new PdfPCell(new Phrase("IRPF",
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				Balance.addCell(cell);
				cell = new PdfPCell(new Phrase("Subtotal",
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				Balance.addCell(cell);
				
				// Ingresos
				if (informe1)
				{
					cadena="Ingresos";
					cell = new PdfPCell(new Phrase(cadena,
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cliente1.addCell(cell);
					cadena=Decimales(cantidadBaseIngresos);
					cell = new PdfPCell(new Phrase(cadena,
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cliente2.addCell(cell);
					cadena=Decimales(cantidadIvaIngresos);
					cell = new PdfPCell(new Phrase(cadena,
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cliente3.addCell(cell);
					cadena=Decimales(cantidadIRPFIngresos);
					cell = new PdfPCell(new Phrase(cadena,
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cliente4.addCell(cell);
					cadena=Decimales(cantidadTotalIngresos);
					cell = new PdfPCell(new Phrase(cadena,
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cliente5.addCell(cell);
				}
				
				// Gastos
				if (informe2)
				{
					cadena="Gastos";
					cell = new PdfPCell(new Phrase(cadena,
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cliente1.addCell(cell);
					cadena=Decimales(cantidadBaseGastos);
					cell = new PdfPCell(new Phrase(cadena,
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cliente2.addCell(cell);
					cadena=Decimales(cantidadIvaGastos);
					cell = new PdfPCell(new Phrase(cadena,
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cliente3.addCell(cell);
					cadena=Decimales(cantidadIRPFGastos);
					cell = new PdfPCell(new Phrase(cadena,
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cliente4.addCell(cell);
					cadena=Decimales(cantidadTotalGastos);
					cell = new PdfPCell(new Phrase(cadena,
							FontFactory.getFont("arial",   // fuente
							10,                            // tamaño
							Font.NORMAL,                   // estilo
							BaseColor.BLACK)));             // color
					cell.setBorder(PdfPCell.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cliente5.addCell(cell);
				}
				
				// Insertar Tabla ingresos y gastos
				cell = new PdfPCell (cliente1);
				cell.setPadding(0);
				Balance.addCell(cell);
				cell = new PdfPCell (cliente2);
				cell.setPadding(0);
				Balance.addCell(cell);
				cell = new PdfPCell (cliente3);
				cell.setPadding(0);
				Balance.addCell(cell);
				cell = new PdfPCell (cliente4);
				cell.setPadding(0);
				Balance.addCell(cell);
				cell = new PdfPCell (cliente5);
				cell.setPadding(0);
				Balance.addCell(cell);

				// Pie Balance
				cadena="Beneficio Bruto";
				if (cantidadTotalIngresos<cantidadTotalGastos) cadena="Pérdida Bruto";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				Balance.addCell(cell);
				cell = new PdfPCell(new Phrase(Decimales(cantidadTotalIngresos-cantidadTotalGastos),
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBackgroundColor(colorFondo);
				cell.setColspan(4);
				Balance.addCell(cell);
				
				cadena="Beneficio Neto";
				if (cantidadTotalIngresos<cantidadTotalGastos) cadena="Pérdida Neto";
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(colorFondo);
				Balance.addCell(cell);
				cadena=Decimales(cantidadTotalIngresos-cantidadIRPFIngresos-cantidadIvaIngresos-cantidadTotalGastos+cantidadIvaGastos-cantidadIRPFGastos);
				cell = new PdfPCell(new Phrase(cadena,
						FontFactory.getFont("arial",   // fuente
						10,                            // tamaño
						Font.NORMAL,                   // estilo
						BaseColor.BLACK)));             // color
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBackgroundColor(colorFondo);
				cell.setColspan(4);
				Balance.addCell(cell);

			    documento.add(Balance);
			}
		    
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
