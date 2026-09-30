package reportes;

import modelo.InscripcionModelo;
import utilidades.FechaUtil;

/**
 * Bean plano para el informe Jasper de Inscripcion (menu "Informes"). Cada
 * fila es una {@link InscripcionModelo}, ya filtrada por la pantalla
 * (InformeInscripcionVista/Controller) antes de armar la lista.
 */
public class ReporteInscripcionDTO {

	/** Fecha de la inscripcion (dd/MM/yyyy) */
	private String fecha;
	/** Catequizando en formato "Apellido, Nombre" */
	private String catequizando;
	/** Documento del catequizando */
	private String documento;
	/** Nombre del grupo de catequesis */
	private String grupo;
	/** Estado como texto (Activo o Inactivo) */
	private String estado;

	/**
	 * Crea una fila del informe de Inscripcion.
	 *
	 * @param fecha fecha de la inscripcion (dd/MM/yyyy)
	 * @param catequizando catequizando en formato "Apellido, Nombre"
	 * @param documento documento del catequizando
	 * @param grupo nombre del grupo de catequesis
	 * @param estado estado como texto (Activo o Inactivo)
	 */
	public ReporteInscripcionDTO(String fecha, String catequizando, String documento, String grupo,
			String estado) {
		super();
		this.fecha = fecha;
		this.catequizando = catequizando;
		this.documento = documento;
		this.grupo = grupo;
		this.estado = estado;
	}

	/** Convierte una InscripcionModelo en una fila del informe. */
	public static ReporteInscripcionDTO desde(InscripcionModelo inscripcion) {
		String catequizando = "";
		String documento = "";
		if (inscripcion.getCatequizando() != null) {
			catequizando = inscripcion.getCatequizando().getCatz_apellido() + ", "
					+ inscripcion.getCatequizando().getCatz_nombre();
			documento = inscripcion.getCatequizando().getCatz_documento();
		}

		String grupo = inscripcion.getGrupoCatequesis() != null ? inscripcion.getGrupoCatequesis().getGrup_nombre()
				: "";

		String fecha = inscripcion.getInscrip_fecha() != null ? FechaUtil.fechaAString(inscripcion.getInscrip_fecha())
				: "";

		String estado = Boolean.TRUE.equals(inscripcion.isInscrip_estado()) ? "Activo" : "Inactivo";

		return new ReporteInscripcionDTO(fecha, catequizando, documento, grupo, estado);
	}

	/** @return fecha de la inscripcion (dd/MM/yyyy) */
	public String getFecha() {
		return fecha;
	}

	/** @return catequizando en formato "Apellido, Nombre" */
	public String getCatequizando() {
		return catequizando;
	}

	/** @return documento del catequizando */
	public String getDocumento() {
		return documento;
	}

	/** @return nombre del grupo de catequesis */
	public String getGrupo() {
		return grupo;
	}

	/** @return estado como texto (Activo o Inactivo) */
	public String getEstado() {
		return estado;
	}

}
