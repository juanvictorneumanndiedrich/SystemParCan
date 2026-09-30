package reportes;

import modelo.AsistenciaModelo;
import modelo.EstadoAsistencia;
import utilidades.FechaUtil;

/**
 * Bean plano para el informe Jasper de Asistencia (menu "Informes"). Cada
 * fila es un registro individual de {@link AsistenciaModelo} (un
 * catequizando, en una clase puntual), ya filtrado por la pantalla
 * (InformeAsistenciaVista/Controller) antes de armar la lista.
 *
 * JasperReports lee estos campos por reflexion via JRBeanCollectionDataSource,
 * asi que cada campo lleva su getter.
 */
public class ReporteAsistenciaDTO {

	/** Fecha de la clase (dd/MM/yyyy) */
	private String fecha;
	/** Catequizando en formato "Apellido, Nombre" */
	private String catequizando;
	/** Nombre del grupo de catequesis */
	private String grupo;
	/** Estado de asistencia como texto (Presente, Ausente, Justificado o vacio) */
	private String estado;
	/** Observaciones registradas */
	private String observaciones;

	/**
	 * Crea una fila del informe de Asistencia.
	 *
	 * @param fecha fecha de la clase (dd/MM/yyyy)
	 * @param catequizando catequizando en formato "Apellido, Nombre"
	 * @param grupo nombre del grupo de catequesis
	 * @param estado estado de asistencia como texto (Presente, Ausente, Justificado o vacio)
	 * @param observaciones observaciones registradas
	 */
	public ReporteAsistenciaDTO(String fecha, String catequizando, String grupo, String estado,
			String observaciones) {
		super();
		this.fecha = fecha;
		this.catequizando = catequizando;
		this.grupo = grupo;
		this.estado = estado;
		this.observaciones = observaciones;
	}

	/** Convierte un AsistenciaModelo en una fila del informe. */
	public static ReporteAsistenciaDTO desde(AsistenciaModelo asistencia) {
		String catequizando = "";
		String grupo = "";
		if (asistencia.getInscripcion() != null && asistencia.getInscripcion().getCatequizando() != null) {
			catequizando = asistencia.getInscripcion().getCatequizando().getCatz_apellido() + ", "
					+ asistencia.getInscripcion().getCatequizando().getCatz_nombre();
		}
		if (asistencia.getInscripcion() != null && asistencia.getInscripcion().getGrupoCatequesis() != null) {
			grupo = asistencia.getInscripcion().getGrupoCatequesis().getGrup_nombre();
		}

		String fecha = "";
		if (asistencia.getClase() != null && asistencia.getClase().getClase_fechaClase() != null) {
			fecha = FechaUtil.fechaAString(asistencia.getClase().getClase_fechaClase());
		}

		return new ReporteAsistenciaDTO(fecha, catequizando, grupo, textoEstado(asistencia.getEstado()),
				asistencia.getObservaciones());
	}

	/**
	 * Convierte un estado de asistencia en el texto que muestra el informe.
	 *
	 * @param estado estado a convertir; {@code null} da texto vacio
	 */
	private static String textoEstado(EstadoAsistencia estado) {
		if (estado == null) return "";
		switch (estado) {
		case PRESENTE:
			return "Presente";
		case AUSENTE:
			return "Ausente";
		case JUSTIFICADO:
			return "Justificado";
		}
		return "";
	}

	/** @return fecha de la clase (dd/MM/yyyy) */
	public String getFecha() {
		return fecha;
	}

	/** @return catequizando en formato "Apellido, Nombre" */
	public String getCatequizando() {
		return catequizando;
	}

	/** @return nombre del grupo de catequesis */
	public String getGrupo() {
		return grupo;
	}

	/** @return estado de asistencia como texto (Presente, Ausente, Justificado o vacio) */
	public String getEstado() {
		return estado;
	}

	/** @return observaciones registradas */
	public String getObservaciones() {
		return observaciones;
	}

}
