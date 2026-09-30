package utilidades;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Puente entre el sistema y JasperReports: compila una plantilla {@code .jrxml},
 * la llena con una lista de beans y prepara la ventana para visualizar el reporte.
 *
 * @param <E> tipo de bean (DTO de {@code reportes}) que alimenta el reporte
 */
public class ConexionJasper<E> {

	/** Ventana modal donde se muestra el reporte; el llamador debe centrarla y hacerla visible. */
	public JDialog ventanaReporte = new JDialog(new JFrame(), "Visualizar Reporte", true);

	/**
	 * Genera el reporte y lo carga en {@link #ventanaReporte} (no la muestra).
	 *
	 * @param lista      beans que se usan como origen de datos (una fila por elemento)
	 * @param parametros parametros que recibe la plantilla (puede ir vacio)
	 * @param reporte    nombre del archivo en {@code /jasper} sin la extension {@code .jrxml}
	 * @throws JRException si la plantilla no se puede compilar o llenar
	 */
	public void generarReporte(List<E> lista, Map<String, Object> parametros, String reporte) throws JRException {
		this.ventanaReporte.setSize(1080, 720);
		this.ventanaReporte.setModal(true);
		InputStream stream = ConexionJasper.class.getResourceAsStream("/jasper/"+reporte+".jrxml");
		JasperReport report = JasperCompileManager.compileReport(stream);
		JasperPrint print = JasperFillManager.fillReport(report, parametros, new JRBeanCollectionDataSource(lista));
		JasperViewer viewer = new JasperViewer(print);
		this.ventanaReporte.getContentPane().add(viewer.getContentPane());
	}
	
}