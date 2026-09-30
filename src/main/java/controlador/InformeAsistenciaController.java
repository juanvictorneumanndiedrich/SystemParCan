package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import dao.AsistenciaDAO;
import dao.CatequizandoDAO;
import dao.GrupoCatequesisDAO;
import modelo.AsistenciaModelo;
import modelo.CatequizandoModelo;
import modelo.EstadoAsistencia;
import modelo.GrupoCatequesisModelo;
import net.sf.jasperreports.engine.JRException;
import reportes.GeneradorReportes;
import reportes.ReporteAsistenciaDTO;
import tabla.ModeloTablaInformeAsistencia;
import utilidades.ConexionJasper;
import utilidades.FechaUtil;
import vista.InformeAsistenciaVista;

/**
 * Controlador del Informe de Asistencia (menu "Informes"). Filtra los
 * registros de AsistenciaModelo por grupo, catequizando, estado y rango de
 * fecha (fecha de la clase), muestra el resultado en la grilla, y arma el
 * Jasper con la lista actualmente filtrada.
 */
public class InformeAsistenciaController {

	/**
	 * Pantalla (vista) que maneja este controlador.
	 */
	private InformeAsistenciaVista vista;
	/**
	 * DAO principal de la entidad que administra esta pantalla.
	 */
	private AsistenciaDAO dao;
	/**
	 * DAO auxiliar de grupos de catequesis (combos de filtro o seleccion).
	 */
	private GrupoCatequesisDAO grupoDao;
	/**
	 * DAO auxiliar de catequizandos.
	 */
	private CatequizandoDAO catequizandoDao;
	/**
	 * Modelo de la tabla donde se listan los registros.
	 */
	private ModeloTablaInformeAsistencia tabla;
	/**
	 * Lista ya filtrada que se ve en la grilla; es la que se envia al reporte Jasper.
	 */
	private List<AsistenciaModelo> filtradas = new ArrayList<AsistenciaModelo>();

	/**
	 * Crea el controlador: asocia la vista, carga los combos de filtro, registra las acciones y filtra una vez
	 * para que la grilla aparezca ya cargada al abrir.
	 *
	 * @param vista pantalla {@link InformeAsistenciaVista} que se va a controlar
	 */
	public InformeAsistenciaController(InformeAsistenciaVista vista) {
		super();
		this.vista = vista;

		dao = new AsistenciaDAO();
		grupoDao = new GrupoCatequesisDAO();
		catequizandoDao = new CatequizandoDAO();
		tabla = new ModeloTablaInformeAsistencia();
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

		List<CatequizandoModelo> catequizandos = catequizandoDao.recuperarTodo();
		catequizandos.sort(Comparator.comparing(CatequizandoModelo::getCatz_apellido)
				.thenComparing(CatequizandoModelo::getCatz_nombre));
		this.vista.getCbCatequizando().removeAllItems();
		this.vista.getCbCatequizando().addItem(null);
		for (CatequizandoModelo catequizando : catequizandos) {
			this.vista.getCbCatequizando().addItem(catequizando);
		}

		this.vista.getCbEstado().removeAllItems();
		this.vista.getCbEstado().addItem(null);
		for (EstadoAsistencia estado : EstadoAsistencia.values()) {
			this.vista.getCbEstado().addItem(estado);
		}
	}

	/**
	 * Conecta los botones Filtrar, Generar Informe y Cerrar.
	 */
	private void setAcciones() {
		this.vista.getBtnFiltrar().addActionListener(e -> filtrar());
		this.vista.getBtnGenerar().addActionListener(e -> generarInforme());
		this.vista.getBtnCerrar().addActionListener(e -> this.vista.dispose());
	}

	/**
	 * Recalcula la lista filtrada (por grupo, catequizando, estado y rango de fecha de la clase), la ordena por fecha y actualiza la grilla.
	 */
	private void filtrar() {
		GrupoCatequesisModelo grupoFiltro = (GrupoCatequesisModelo) this.vista.getCbGrupo().getSelectedItem();
		CatequizandoModelo catequizandoFiltro = (CatequizandoModelo) this.vista.getCbCatequizando().getSelectedItem();
		EstadoAsistencia estadoFiltro = (EstadoAsistencia) this.vista.getCbEstado().getSelectedItem();

		LocalDate desde = FechaUtil.stringAFecha(this.vista.getTfFechaDesde().getText());
		LocalDate hasta = FechaUtil.stringAFecha(this.vista.getTfFechaHasta().getText());

		filtradas = dao.recuperarTodo().stream()
				.filter(a -> grupoFiltro == null
						|| (a.getInscripcion() != null && a.getInscripcion().getGrupoCatequesis() != null
								&& a.getInscripcion().getGrupoCatequesis().getGrup_id()
										.equals(grupoFiltro.getGrup_id())))
				.filter(a -> catequizandoFiltro == null
						|| (a.getInscripcion() != null && a.getInscripcion().getCatequizando() != null
								&& a.getInscripcion().getCatequizando().getCatz_id()
										.equals(catequizandoFiltro.getCatz_id())))
				.filter(a -> estadoFiltro == null || a.getEstado() == estadoFiltro)
				.filter(a -> desde == null || (a.getClase() != null && a.getClase().getClase_fechaClase() != null
						&& !a.getClase().getClase_fechaClase().isBefore(desde)))
				.filter(a -> hasta == null || (a.getClase() != null && a.getClase().getClase_fechaClase() != null
						&& !a.getClase().getClase_fechaClase().isAfter(hasta)))
				.collect(Collectors.toList());

		filtradas.sort(Comparator.comparing(
				a -> a.getClase() != null ? a.getClase().getClase_fechaClase() : null,
				Comparator.nullsLast(Comparator.naturalOrder())));

		tabla.setLista(filtradas);
	}

	/**
	 * Genera el reporte Jasper "InformeAsistencia" con la lista filtrada y lo muestra en una ventana. Si falla la
	 * generacion muestra un mensaje de error.
	 */
	private void generarInforme() {
		try {
			List<ReporteAsistenciaDTO> lista = GeneradorReportes.listarInformeAsistencia(filtradas);
			ConexionJasper<ReporteAsistenciaDTO> conexion = new ConexionJasper<>();
			conexion.generarReporte(lista, new HashMap<>(), "InformeAsistencia");
			conexion.ventanaReporte.setLocationRelativeTo(this.vista);
			conexion.ventanaReporte.setVisible(true);
		} catch (JRException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this.vista, "No se pudo generar el informe de asistencia.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
