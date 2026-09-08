package modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Entidad JPA que representa el registro individual de ASISTENCIA de un
 * alumno (a traves de su {@link InscripcionModelo}) a una {@link ClaseModelo}
 * concreta.
 *
 * Regla de negocio: un alumno no puede tener mas de un registro de
 * asistencia para la misma clase, garantizado por la restriccion unica
 * compuesta (inscrip_id, clase_id).
 *
 * Tabla asociada: tb_asistencia
 */
@Entity(name = "tb_asistencia")
@Table(name = "tb_asistencia", uniqueConstraints = @UniqueConstraint(columnNames = { "inscrip_id", "clase_id" }))
public class AsistenciaModelo {

	/** Identificador unico del registro de asistencia (clave primaria, autogenerada por la base de datos). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer asist_id;

	/**
	 * Estado de la asistencia. Se persiste como texto (STRING) en la base
	 * de datos usando los valores del enum {@link EstadoAsistencia}:
	 * PRESENTE, AUSENTE o JUSTIFICADO.
	 */
	// Estado de la asistencia: PRESENTE, AUSENTE o JUSTIFICADO
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EstadoAsistencia estado;

	/**
	 * Motivo/justificacion de la ausencia. Se completa tipicamente cuando
	 * el estado es JUSTIFICADO. Se almacena como texto largo (columna TEXT).
	 */
	// Motivo de la ausencia; se completa cuando el estado es JUSTIFICADO
	@Lob
	@Column(columnDefinition = "TEXT")
	private String observaciones;

	// ==================== MUCHOS A UNO ===========================
	/** Inscripcion (alumno) a la que corresponde este registro de asistencia (obligatoria). */
	@ManyToOne(optional = false)
	@JoinColumn(name = "inscrip_id")
	private InscripcionModelo inscripcion;

	/** Clase (sesion) a la que corresponde este registro de asistencia (obligatoria). */
	@ManyToOne(optional = false)
	@JoinColumn(name = "clase_id")
	private ClaseModelo clase;

	/** Constructor vacio requerido por JPA/Hibernate. */
	public AsistenciaModelo() {
		super();
	}

	// ------------------------- Getters y Setters -------------------------
	// Acceso estandar (JavaBeans) a cada atributo. No contienen logica de negocio.

	/** @return el id del registro de asistencia. */
	public Integer getAsist_id() {
		return asist_id;
	}

	/** Establece el id del registro de asistencia (normalmente lo asigna JPA al persistir). */
	public void setAsist_id(Integer asist_id) {
		this.asist_id = asist_id;
	}

	/** @return el estado de la asistencia (PRESENTE, AUSENTE o JUSTIFICADO). */
	public EstadoAsistencia getEstado() {
		return estado;
	}

	/** Cambia el estado de la asistencia. */
	public void setEstado(EstadoAsistencia estado) {
		this.estado = estado;
	}

	/** @return la observacion/justificacion de la ausencia (puede ser nula). */
	public String getObservaciones() {
		return observaciones;
	}

	/** Cambia la observacion/justificacion de la ausencia. */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/** @return la inscripcion (alumno) a la que pertenece este registro. */
	public InscripcionModelo getInscripcion() {
		return inscripcion;
	}

	/** Asigna la inscripcion (alumno) a la que pertenece este registro. */
	public void setInscripcion(InscripcionModelo inscripcion) {
		this.inscripcion = inscripcion;
	}

	/** @return la clase (sesion) a la que pertenece este registro. */
	public ClaseModelo getClase() {
		return clase;
	}

	/** Asigna la clase (sesion) a la que pertenece este registro. */
	public void setClase(ClaseModelo clase) {
		this.clase = clase;
	}

}