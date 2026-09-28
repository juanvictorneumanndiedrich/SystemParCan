package reportes;

import modelo.InscripcionModelo;
import utilidades.FechaUtil;

/**
 * Bean plano para el informe Jasper de Inscripcion (menu "Informes"). Cada
 * fila es una {@link InscripcionModelo}, ya filtrada por la pantalla
 * (InformeInscripcionVista/Controller) antes de armar la lista.
 */
public class ReporteInscripcionDTO {

	private String fecha;
	private String catequizando;
	private String documento;
	private String grupo;
	private String estado;

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

	public String getFecha() {
		return fecha;
	}

	public String getCatequizando() {
		return catequizando;
	}

	public String getDocumento() {
		return documento;
	}

	public String getGrupo() {
		return grupo;
	}

	public String getEstado() {
		return estado;
	}

}
