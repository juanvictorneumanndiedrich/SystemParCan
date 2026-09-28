package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import dao.GrupoCatequesisDAO;
import dao.TransferenciaDAO;
import modelo.GrupoCatequesisModelo;
import modelo.TransferenciaModelo;
import net.sf.jasperreports.engine.JRException;
import reportes.GeneradorReportes;
import reportes.ReporteTransferenciaDTO;
import tabla.ModeloTablaInformeTransferencia;
import utilidades.ConexionJasper;
import utilidades.FechaUtil;
import vista.InformeTransferenciaVista;

/**
 * Controlador del Informe de Transferencia (menu "Informes"). Filtra el
 * historial de transferencias por grupo de origen, grupo de destino,
 * rango de fecha y un buscador de texto libre (nombre, apellido o
 * documento del catequizando), muestra el resultado en la grilla, y arma
 * el Jasper con la lista filtrada.
 */
public class InformeTransferenciaController {

	private InformeTransferenciaVista vista;
	private TransferenciaDAO dao;
	private GrupoCatequesisDAO grupoDao;
	private ModeloTablaInformeTransferencia tabla;
	private List<TransferenciaModelo> filtradas = new ArrayList<TransferenciaModelo>();

	public InformeTransferenciaController(InformeTransferenciaVista vista) {
		super();
		this.vista = vista;

		dao = new TransferenciaDAO();
		grupoDao = new GrupoCatequesisDAO();
		tabla = new ModeloTablaInformeTransferencia();
		this.vista.getTabla().setModel(tabla);

		cargarCombos();
		setAcciones();
		filtrar();
	}

	private void cargarCombos() {
		List<GrupoCatequesisModelo> grupos = grupoDao.recuperarTodo();

		this.vista.getCbGrupoOrigen().removeAllItems();
		this.vista.getCbGrupoOrigen().addItem(null);
		for (GrupoCatequesisModelo grupo : grupos) {
			this.vista.getCbGrupoOrigen().addItem(grupo);
		}

		this.vista.getCbGrupoDestino().removeAllItems();
		this.vista.getCbGrupoDestino().addItem(null);
		for (GrupoCatequesisModelo grupo : grupos) {
			this.vista.getCbGrupoDestino().addItem(grupo);
		}
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
		GrupoCatequesisModelo origenFiltro = (GrupoCatequesisModelo) this.vista.getCbGrupoOrigen().getSelectedItem();
		GrupoCatequesisModelo destinoFiltro = (GrupoCatequesisModelo) this.vista.getCbGrupoDestino()
				.getSelectedItem();
		String buscador = this.vista.getTfBuscador().getText().trim().toLowerCase();

		LocalDate desde = FechaUtil.stringAFecha(this.vista.getTfFechaDesde().getText());
		LocalDate hasta = FechaUtil.stringAFecha(this.vista.getTfFechaHasta().getText());

		filtradas = dao.recuperarTodo().stream()
				.filter(t -> origenFiltro == null || (t.getGrupoOrigen() != null
						&& t.getGrupoOrigen().getGrup_id().equals(origenFiltro.getGrup_id())))
				.filter(t -> destinoFiltro == null || (t.getGrupoDestino() != null
						&& t.getGrupoDestino().getGrup_id().equals(destinoFiltro.getGrup_id())))
				.filter(t -> desde == null || (t.getTransf_fecha() != null && !t.getTransf_fecha().isBefore(desde)))
				.filter(t -> hasta == null || (t.getTransf_fecha() != null && !t.getTransf_fecha().isAfter(hasta)))
				.filter(t -> buscador.isEmpty() || (t.getCatequizando() != null && (
						contiene(t.getCatequizando().getCatz_nombre(), buscador)
						|| contiene(t.getCatequizando().getCatz_apellido(), buscador)
						|| contiene(t.getCatequizando().getCatz_documento(), buscador))))
				.collect(Collectors.toList());

		filtradas.sort(Comparator.comparing(TransferenciaModelo::getTransf_fecha,
				Comparator.nullsLast(Comparator.naturalOrder())));

		tabla.setLista(filtradas);
	}

	private boolean contiene(String texto, String buscador) {
		return texto != null && texto.toLowerCase().contains(buscador);
	}

	private void generarInforme() {
		try {
			List<ReporteTransferenciaDTO> lista = GeneradorReportes.listarInformeTransferencia(filtradas);
			ConexionJasper<ReporteTransferenciaDTO> conexion = new ConexionJasper<>();
			conexion.generarReporte(lista, new HashMap<>(), "InformeTransferencia");
			conexion.ventanaReporte.setLocationRelativeTo(this.vista);
			conexion.ventanaReporte.setVisible(true);
		} catch (JRException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this.vista, "No se pudo generar el informe de transferencia.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
