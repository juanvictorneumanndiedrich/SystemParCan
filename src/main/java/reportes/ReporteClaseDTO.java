package reportes;

import modelo.AsistenciaModelo;
import modelo.ClaseModelo;
import modelo.EstadoAsistencia;
import utilidades.FechaUtil;

/**
 * Bean plano para el informe Jasper de Clase (menu "Informes"). Cada fila
 * es una {@link ClaseModelo} (sesion puntual de un grupo), ya filtrada por
 * la pantalla (InformeClaseVista/Controller) antes de armar la lista, con
 * un resumen de cuantas asistencias se registraron y cuantos catequizandos
 * quedaron marcados como Presente en esa clase.
 */
public class ReporteClaseDTO {

	private String fecha;
	private String grupo;
	private String descripcion;
	private Integer asistenciasRegistradas;
	private Integer presentes;

	public ReporteClaseDTO(String fecha, String grupo, String descripcion, Integer asistenciasRegistradas,
			Integer presentes) {
		super();
		this.fecha = fecha;
		this.grupo = grupo;
		this.descripcion = descripcion;
		this.asistenciasRegistradas = asistenciasRegistradas;
		this.presentes = presentes;
	}

	/** Convierte una ClaseModelo en una fila del informe. */
	public static ReporteClaseDTO desde(ClaseModelo clase) {
		String fecha = clase.getClase_fechaClase() != null ? FechaUtil.fechaAString(clase.getClase_fechaClase())
				: "";
		String grupo = clase.getGrupoCatequesis() != null ? clase.getGrupoCatequesis().getGrup_nombre() : "";

		int registradas = 0;
		int presentes = 0;
		if (clase.getAsistencias() != null) {
			registradas = clase.getAsistencias().size();
			for (AsistenciaModelo asistencia : clase.getAsistencias()) {
				if (asistencia.getEstado() == EstadoAsistencia.PRESENTE) presentes++;
			}
		}

		return new ReporteClaseDTO(fecha, grupo, clase.getClase_descripcion(), registradas, presentes);
	}

	public String getFecha() {
		return fecha;
	}

	public String getGrupo() {
		return grupo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Integer getAsistenciasRegistradas() {
		return asistenciasRegistradas;
	}

	public Integer getPresentes() {
		return presentes;
	}

}
