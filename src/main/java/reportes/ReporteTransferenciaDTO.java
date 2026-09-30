package reportes;

import modelo.TransferenciaModelo;
import utilidades.FechaUtil;

/**
 * Bean plano para el informe Jasper de Transferencia (menu "Informes").
 * Cada fila es una {@link TransferenciaModelo}, ya filtrada por la
 * pantalla (InformeTransferenciaVista/Controller) antes de armar la lista.
 */
public class ReporteTransferenciaDTO {

	/** Fecha de la transferencia (dd/MM/yyyy) */
	private String fecha;
	/** Catequizando en formato "Apellido, Nombre" */
	private String catequizando;
	/** Nombre del grupo de origen */
	private String grupoOrigen;
	/** Nombre del grupo de destino */
	private String grupoDestino;
	/** Observacion registrada */
	private String observacion;

	/**
	 * Crea una fila del informe de Transferencia.
	 *
	 * @param fecha fecha de la transferencia (dd/MM/yyyy)
	 * @param catequizando catequizando en formato "Apellido, Nombre"
	 * @param grupoOrigen nombre del grupo de origen
	 * @param grupoDestino nombre del grupo de destino
	 * @param observacion observacion registrada
	 */
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

	/** @return fecha de la transferencia (dd/MM/yyyy) */
	public String getFecha() {
		return fecha;
	}

	/** @return catequizando en formato "Apellido, Nombre" */
	public String getCatequizando() {
		return catequizando;
	}

	/** @return nombre del grupo de origen */
	public String getGrupoOrigen() {
		return grupoOrigen;
	}

	/** @return nombre del grupo de destino */
	public String getGrupoDestino() {
		return grupoDestino;
	}

	/** @return observacion registrada */
	public String getObservacion() {
		return observacion;
	}

}
