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

	/**
	 * Pantalla (vista) que maneja este controlador.
	 */
	private InformeInscripcionVista vista;
	/**
	 * DAO principal de la entidad que administra esta pantalla.
	 */
	private InscripcionDAO dao;
	/**
	 * DAO auxiliar de grupos de catequesis (combos de filtro o seleccion).
	 */
	private GrupoCatequesisDAO grupoDao;
	/**
	 * Modelo de la tabla donde se listan los registros.
	 */
	private ModeloTablaInformeInscripcion tabla;
	/**
	 * Lista ya filtrada que se ve en la grilla; es la que se envia al reporte Jasper.
	 */
	private List<InscripcionModelo> filtradas = new ArrayList<InscripcionModelo>();

	/**
	 * Crea el controlador: asocia la vista, carga los combos de filtro, registra las acciones y filtra una vez
	 * para que la grilla aparezca ya cargada al abrir.
	 *
	 * @param vista pantalla {@link InformeInscripcionVista} que se va a controlar
	 */
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

	/**
	 * Llena los combos de filtro. El primer item de cada combo es {@code null} y representa "-- Todos --".
	 */
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

	/**
	 * Conecta los botones Filtrar, Generar Informe y Cerrar y el buscador de texto, que filtra en tiempo real.
	 */
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

	/**
	 * Recalcula la lista filtrada (por grupo, estado, rango de fecha y buscador de catequizando), la ordena por fecha y actualiza la grilla.
	 */
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

	/**
	 * Indica si un texto contiene el buscador, sin distinguir mayusculas.
	 *
	 * @param texto texto donde buscar (puede ser {@code null})
	 * @param buscador texto buscado, ya en minusculas
	 */
	private boolean contiene(String texto, String buscador) {
		return texto != null && texto.toLowerCase().contains(buscador);
	}

	/**
	 * Genera el reporte Jasper "InformeInscripcion" con la lista filtrada y lo muestra en una ventana. Si falla la
	 * generacion muestra un mensaje de error.
	 */
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
