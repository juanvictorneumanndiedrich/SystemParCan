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

	private String fecha;
	private String catequizando;
	private String grupo;
	private String estado;
	private String observaciones;

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

	public String getFecha() {
		return fecha;
	}

	public String getCatequizando() {
		return catequizando;
	}

	public String getGrupo() {
		return grupo;
	}

	public String getEstado() {
		return estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

}
