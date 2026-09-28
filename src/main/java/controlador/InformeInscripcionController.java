package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import dao.GrupoCatequesisDAO;
import dao.InscripcionDAO;
import modelo.GrupoCatequesisModelo;
import modelo.InscripcionModelo;
import net.sf.jasperreports.engine.JRException;
import reportes.GeneradorReportes;
import reportes.ReporteInscripcionDTO;
import tabla.ModeloTablaInformeInscripcion;
import utilidades.ConexionJasper;
import utilidades.FechaUtil;
import vista.InformeInscripcionVista;

/**
 * Controlador del Informe de Inscripcion (menu "Informes"). Filtra las
 * inscripciones por grupo, estado, rango de fecha y un buscador de texto
 * libre (nombre, apellido o documento del catequizando), muestra el
 * resultado en la grilla, y arma el Jasper con la lista filtrada.
 */
public class InformeInscripcionController {

	private InformeInscripcionVista vista;
	private InscripcionDAO dao;
	private GrupoCatequesisDAO grupoDao;
	private ModeloTablaInformeInscripcion tabla;
	private List<InscripcionModelo> filtradas = new ArrayList<InscripcionModelo>();

	public InformeInscripcionController(InformeInscripcionVista vista) {
		super();
		this.vista = vista;

		dao = new InscripcionDAO();
		grupoDao = new GrupoCatequesisDAO();
		tabla = new ModeloTablaInformeInscripcion();
		this.vista.getTabla().setModel(tabla);

		cargarCombos();
		setAcciones();
		filtrar();
	}

	private void cargarCombos() {
		this.vista.getCbGrupo().removeAllItems();
		this.vista.getCbGrupo().addItem(null);
		for (GrupoCatequesisModelo grupo : grupoDao.recuperarTodo()) {
			this.vista.getCbGrupo().addItem(grupo);
		}

		this.vista.getCbEstado().removeAllItems();
		this.vista.getCbEstado().addItem(null);
		this.vista.getCbEstado().addItem(Boolean.TRUE);
		this.vista.getCbEstado().addItem(Boolean.FALSE);
	}

	private void setAcciones() {
		this.vista.getBtnFiltrar().addActionListener(e -> filtrar());
		this.vista.getBtnGenerar().addActionListener(e -> generarInforme());
		this.vista.getBtnCerrar().addActionListener(e -> this.vista.dispose());

		this.vista.getTfBuscador().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
			public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
			public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
		});
	}

	private void filtrar() {
		GrupoCatequesisModelo grupoFiltro = (GrupoCatequesisModelo) this.vista.getCbGrupo().getSelectedItem();
		Boolean estadoFiltro = (Boolean) this.vista.getCbEstado().getSelectedItem();
		String buscador = this.vista.getTfBuscador().getText().trim().toLowerCase();

		LocalDate desde = FechaUtil.stringAFecha(this.vista.getTfFechaDesde().getText());
		LocalDate hasta = FechaUtil.stringAFecha(this.vista.getTfFechaHasta().getText());

		filtradas = dao.recuperarTodo().stream()
				.filter(i -> grupoFiltro == null || (i.getGrupoCatequesis() != null
						&& i.getGrupoCatequesis().getGrup_id().equals(grupoFiltro.getGrup_id())))
				.filter(i -> estadoFiltro == null || estadoFiltro.equals(i.isInscrip_estado()))
				.filter(i -> desde == null
						|| (i.getInscrip_fecha() != null && !i.getInscrip_fecha().isBefore(desde)))
				.filter(i -> hasta == null || (i.getInscrip_fecha() != null && !i.getInscrip_fecha().isAfter(hasta)))
				.filter(i -> buscador.isEmpty() || (i.getCatequizando() != null && (
						contiene(i.getCatequizando().getCatz_nombre(), buscador)
						|| contiene(i.getCatequizando().getCatz_apellido(), buscador)
						|| contiene(i.getCatequizando().getCatz_documento(), buscador))))
				.collect(Collectors.toList());

		filtradas.sort(Comparator.comparing(InscripcionModelo::getInscrip_fecha,
				Comparator.nullsLast(Comparator.naturalOrder())));

		tabla.setLista(filtradas);
	}

	private boolean contiene(String texto, String buscador) {
		return texto != null && texto.toLowerCase().contains(buscador);
	}

	private void generarInforme() {
		try {
			List<ReporteInscripcionDTO> lista = GeneradorReportes.listarInformeInscripcion(filtradas);
			ConexionJasper<ReporteInscripcionDTO> conexion = new ConexionJasper<>();
			conexion.generarReporte(lista, new HashMap<>(), "InformeInscripcion");
			conexion.ventanaReporte.setLocationRelativeTo(this.vista);
			conexion.ventanaReporte.setVisible(true);
		} catch (JRException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this.vista, "No se pudo generar el informe de inscripcion.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
