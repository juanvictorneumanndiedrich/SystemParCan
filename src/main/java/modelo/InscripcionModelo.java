package modelo;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Entidad JPA que representa la INSCRIPCION vigente de un catequizando
 * en un grupo de catequesis.
 *
 * Es el vinculo entre {@link CatequizandoModelo} y
 * {@link GrupoCatequesisModelo}, y el punto de origen de las asistencias
 * del alumno.
 *
 * Regla de negocio importante: un catequizando solamente puede pertenecer
 * a UN grupo de catequesis a la vez. Esto se garantiza con la restriccion
 * unica sobre la columna catz_id (ver anotacion @Table mas abajo): no puede
 * existir mas de una fila de inscripcion para el mismo catequizando. Cuando
 * un alumno cambia de grupo, esta misma fila se actualiza y el cambio queda
 * registrado como historial en {@link TransferenciaModelo}.
 *
 * Tabla asociada: tb_inscripcion
 */
@Entity(name = "tb_inscripcion")
// Regla: un catequizando solamente puede pertenecer a un Grupo de Catequesis
// (catz_id no se puede repetir en la tabla de inscripciones)
@Table(name = "tb_inscripcion", uniqueConstraints = @UniqueConstraint(columnNames = "catz_id"))
public class InscripcionModelo {

	/** Identificador unico de la inscripcion (clave primaria, autogenerada por la base de datos). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer inscrip_id;

	/** Fecha en que se registro la inscripcion (obligatoria). */
	@Column(nullable = false)
	private LocalDate inscrip_fecha;

	/** Indica si la inscripcion esta activa (true) o inactiva (false). Obligatorio. */
	@Column(nullable = false)
	private Boolean inscrip_estado;

	// ======================== MUCHOS A UNO ==================================
	/** Catequizando (alumno) al que pertenece esta inscripcion (obligatorio). */
	@ManyToOne(optional = false)
	@JoinColumn(name = "catz_id")
	private CatequizandoModelo catequizando;

	/** Grupo de catequesis en el que esta inscrito actualmente el catequizando (obligatorio). */
	@ManyToOne(optional = false)
	@JoinColumn(name = "grupo_id")
	private GrupoCatequesisModelo grupoCatequesis;

	// ======================= UNO A MUCHOS ===================================

	/**
	 * Historial de asistencia de este alumno en su grupo actual.
	 * cascade = ALL: si se elimina la inscripcion, se eliminan tambien
	 * sus registros de asistencia.
	 */
	// LISTA: la historia de asistencia de este alumno en este grupo

	@OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<AsistenciaModelo> asistencias;


	/** Constructor vacio requerido por JPA/Hibernate. */
	public InscripcionModelo() {
		super();

	}

	// ------------------------- Getters y Setters -------------------------
	// Acceso estandar (JavaBeans) a cada atributo. No contienen logica de negocio.

	/** @return el id de la inscripcion. */
	public Integer getInscrip_id() {
		return inscrip_id;
	}

	/** Establece el id de la inscripcion (normalmente lo asigna JPA al persistir). */
	public void setInscrip_id(Integer inscrip_id) {
		this.inscrip_id = inscrip_id;
	}

	/** @return la fecha en que se realizo la inscripcion. */
	public LocalDate getInscrip_fecha() {
		return inscrip_fecha;
	}

	/** Cambia la fecha de la inscripcion. */
	public void setInscrip_fecha(LocalDate inscrip_fecha) {
		this.inscrip_fecha = inscrip_fecha;
	}

	/** @return true si la inscripcion esta activa, false en caso contrario. */
	public Boolean isInscrip_estado() {
		return inscrip_estado;
	}

	/** Activa o desactiva la inscripcion. */
	public void setInscrip_estado(Boolean inscrip_estado) {
		this.inscrip_estado = inscrip_estado;
	}

	/** @return el catequizando (alumno) dueño de esta inscripcion. */
	public CatequizandoModelo getCatequizando() {
		return catequizando;
	}

	/** Asigna el catequizando (alumno) dueño de esta inscripcion. */
	public void setCatequizando(CatequizandoModelo catequizando) {
		this.catequizando = catequizando;
	}

	/** @return el grupo de catequesis en el que esta inscrito el alumno. */
	public GrupoCatequesisModelo getGrupoCatequesis() {
		return grupoCatequesis;
	}

	/** Cambia el grupo de catequesis en el que esta inscrito el alumno (por ejemplo, al transferirlo). */
	public void setGrupoCatequesis(GrupoCatequesisModelo grupoCatequesis) {
		this.grupoCatequesis = grupoCatequesis;
	}

	/** @return el historial de asistencia asociado a esta inscripcion. */
	public List<AsistenciaModelo> getAsistencias() {
		return asistencias;
	}

	/** Reemplaza el historial completo de asistencia de esta inscripcion. */
	public void setAsistencias(List<AsistenciaModelo> asistencias) {
		this.asistencias = asistencias;
	}

}