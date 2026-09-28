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

	private InformeAsistenciaVista vista;
	private AsistenciaDAO dao;
	private GrupoCatequesisDAO grupoDao;
	private CatequizandoDAO catequizandoDao;
	private ModeloTablaInformeAsistencia tabla;
	private List<AsistenciaModelo> filtradas = new ArrayList<AsistenciaModelo>();

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

	private void setAcciones() {
		this.vista.getBtnFiltrar().addActionListener(e -> filtrar());
		this.vista.getBtnGenerar().addActionListener(e -> generarInforme());
		this.vista.getBtnCerrar().addActionListener(e -> this.vista.dispose());
	}

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
