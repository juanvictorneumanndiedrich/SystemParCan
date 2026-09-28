package reportes;

import modelo.TransferenciaModelo;
import utilidades.FechaUtil;

/**
 * Bean plano para el informe Jasper de Transferencia (menu "Informes").
 * Cada fila es una {@link TransferenciaModelo}, ya filtrada por la
 * pantalla (InformeTransferenciaVista/Controller) antes de armar la lista.
 */
public class ReporteTransferenciaDTO {

	private String fecha;
	private String catequizando;
	private String grupoOrigen;
	private String grupoDestino;
	private String observacion;

	public ReporteTransferenciaDTO(String fecha, String catequizando, String grupoOrigen, String grupoDestino,
			String observacion) {
		super();
		this.fecha = fecha;
		this.catequizando = catequizando;
		this.grupoOrigen = grupoOrigen;
		this.grupoDestino = grupoDestino;
		this.observacion = observacion;
	}

	/** Convierte una TransferenciaModelo en una fila del informe. */
	public static ReporteTransferenciaDTO desde(TransferenciaModelo transferencia) {
		String catequizando = "";
		if (transferencia.getCatequizando() != null) {
			catequizando = transferencia.getCatequizando().getCatz_apellido() + ", "
					+ transferencia.getCatequizando().getCatz_nombre();
		}

		String grupoOrigen = transferencia.getGrupoOrigen() != null ? transferencia.getGrupoOrigen().getGrup_nombre()
				: "";
		String grupoDestino = transferencia.getGrupoDestino() != null
				? transferencia.getGrupoDestino().getGrup_nombre()
				: "";
		String fecha = transferencia.getTransf_fecha() != null
				? FechaUtil.fechaAString(transferencia.getTransf_fecha())
				: "";

		return new ReporteTransferenciaDTO(fecha, catequizando, grupoOrigen, grupoDestino,
				transferencia.getTransf_observacion());
	}

	public String getFecha() {
		return fecha;
	}

	public String getCatequizando() {
		return catequizando;
	}

	public String getGrupoOrigen() {
		return grupoOrigen;
	}

	public String getGrupoDestino() {
		return grupoDestino;
	}

	public String getObservacion() {
		return observacion;
	}

}
