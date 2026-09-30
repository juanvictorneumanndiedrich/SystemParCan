package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import dao.ClaseDAO;
import dao.GrupoCatequesisDAO;
import modelo.ClaseModelo;
import modelo.GrupoCatequesisModelo;
import net.sf.jasperreports.engine.JRException;
import reportes.GeneradorReportes;
import reportes.ReporteClaseDTO;
import tabla.ModeloTablaInformeClase;
import utilidades.ConexionJasper;
import utilidades.FechaUtil;
import vista.InformeClaseVista;

/**
 * Controlador del Informe de Clase (menu "Informes"). Filtra las clases
 * (sesiones) por grupo, rango de fecha y un buscador de texto libre
 * (descripcion), muestra el resultado en la grilla, y arma el Jasper con
 * la lista filtrada.
 */
public class InformeClaseController {

	/**
	 * Pantalla (vista) que maneja este controlador.
	 */
	private InformeClaseVista vista;
	/**
	 * DAO principal de la entidad que administra esta pantalla.
	 */
	private ClaseDAO dao;
	/**
	 * DAO auxiliar de grupos de catequesis (combos de filtro o seleccion).
	 */
	private GrupoCatequesisDAO grupoDao;
	/**
	 * Modelo de la tabla donde se listan los registros.
	 */
	private ModeloTablaInformeClase tabla;
	/**
	 * Lista ya filtrada que se ve en la grilla; es la que se envia al reporte Jasper.
	 */
	private List<ClaseModelo> filtradas = new ArrayList<ClaseModelo>();

	/**
	 * Crea el controlador: asocia la vista, carga los combos de filtro, registra las acciones y filtra una vez
	 * para que la grilla aparezca ya cargada al abrir.
	 *
	 * @param vista pantalla {@link InformeClaseVista} que se va a controlar
	 */
	public InformeClaseController(InformeClaseVista vista) {
		super();
		this.vista = vista;

		dao = new ClaseDAO();
		grupoDao = new GrupoCatequesisDAO();
		tabla = new ModeloTablaInformeClase();
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
	 * Recalcula la lista filtrada (por grupo, rango de fecha y buscador de descripcion), la ordena por fecha y actualiza la grilla.
	 */
	private void filtrar() {
		GrupoCatequesisModelo grupoFiltro = (GrupoCatequesisModelo) this.vista.getCbGrupo().getSelectedItem();
		String buscador = this.vista.getTfBuscador().getText().trim().toLowerCase();

		LocalDate desde = FechaUtil.stringAFecha(this.vista.getTfFechaDesde().getText());
		LocalDate hasta = FechaUtil.stringAFecha(this.vista.getTfFechaHasta().getText());

		filtradas = dao.recuperarTodo().stream()
				.filter(c -> grupoFiltro == null || (c.getGrupoCatequesis() != null
						&& c.getGrupoCatequesis().getGrup_id().equals(grupoFiltro.getGrup_id())))
				.filter(c -> desde == null
						|| (c.getClase_fechaClase() != null && !c.getClase_fechaClase().isBefore(desde)))
				.filter(c -> hasta == null
						|| (c.getClase_fechaClase() != null && !c.getClase_fechaClase().isAfter(hasta)))
				.filter(c -> buscador.isEmpty()
						|| (c.getClase_descripcion() != null && c.getClase_descripcion().toLowerCase().contains(buscador)))
				.collect(Collectors.toList());

		filtradas.sort(Comparator.comparing(ClaseModelo::getClase_fechaClase,
				Comparator.nullsLast(Comparator.naturalOrder())));

		tabla.setLista(filtradas);
	}

	/**
	 * Genera el reporte Jasper "InformeClase" con la lista filtrada y lo muestra en una ventana. Si falla la
	 * generacion muestra un mensaje de error.
	 */
	private void generarInforme() {
		try {
			List<ReporteClaseDTO> lista = GeneradorReportes.listarInformeClase(filtradas);
			ConexionJasper<ReporteClaseDTO> conexion = new ConexionJasper<>();
			conexion.generarReporte(lista, new HashMap<>(), "InformeClase");
			conexion.ventanaReporte.setLocationRelativeTo(this.vista);
			conexion.ventanaReporte.setVisible(true);
		} catch (JRException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this.vista, "No se pudo generar el informe de clase.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
