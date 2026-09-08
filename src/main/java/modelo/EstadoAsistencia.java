package modelo;

/**
 * Enum que representa los estados posibles de un registro de asistencia
 * por clase (ver {@link AsistenciaModelo#getEstado()}).
 *
 * Se persiste en la base de datos como texto (@Enumerated(EnumType.STRING)
 * en AsistenciaModelo), por lo que estos nombres deben mantenerse estables.
 */
// Estados posibles para un registro de asistencia por clase
public enum EstadoAsistencia {
	/** El alumno asistio a la clase. */
	PRESENTE,
	/** El alumno no asistio y la ausencia no fue justificada. */
	AUSENTE,
	/** El alumno no asistio, pero la ausencia tiene una justificacion registrada (ver AsistenciaModelo.observaciones). */
	JUSTIFICADO
}