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

/**
 * Entidad JPA que representa una CLASE (sesion puntual, un dia de encuentro)
 * de un {@link GrupoCatequesisModelo}.
 *
 * Es el contenedor de las asistencias tomadas ese dia: por cada alumno
 * inscrito en el grupo se espera un registro de {@link AsistenciaModelo}
 * asociado a esta clase.
 *
 * Tabla asociada: tb_clase
 */
@Entity(name = "tb_clase")
public class ClaseModelo {

	// La clase representa el dia del evento

	/** Identificador unico de la clase (clave primaria, autogenerada por la base de datos). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer clase_id;

	/** Fecha en la que se dicta la clase (obligatoria). */
	@Column(name = "clase_fechaclase", nullable = false)
	private LocalDate clase_fechaClase;

	/** Observacion/descripcion de la clase. Es opcional segun el requisito del sistema. */
	// Observacion de la clase: es opcional segun el requisito
	@Column(nullable = true, length = 255)
	private String clase_descripcion;

	// ======================== MUCHOS A UNO ==================================
	/** Grupo de catequesis al que pertenece esta clase (obligatorio: cada clase es de un unico grupo). */
	// Cada clase pertenece a un unico Grupo de Catequesis
	@ManyToOne(optional = false)
	@JoinColumn(name = "grupo_id")
	private GrupoCatequesisModelo grupoCatequesis;

	// ======================= UNO A MUCHOS ===================================

	/**
	 * Todas las asistencias marcadas para esta clase (un registro por
	 * alumno inscrito). cascade = ALL: si se elimina la clase, se eliminan
	 * tambien sus registros de asistencia.
	 */
	// LISTA de todas las asistencias marcadas en esta fecha especifica y la asistencia es el registro individual
	@OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<AsistenciaModelo> asistencias;

	/** Constructor vacio requerido por JPA/Hibernate. */
	public ClaseModelo() {
		super();

	}

	// ------------------------- Getters y Setters -------------------------
	// Acceso estandar (JavaBeans) a cada atributo. No contienen logica de negocio.

	/** @return el id de la clase. */
	public Integer getClase_id() {
		return clase_id;
	}

	/** Establece el id de la clase (normalmente lo asigna JPA al persistir). */
	public void setClase_id(Integer clase_id) {
		this.clase_id = clase_id;
	}

	/** @return la fecha en que se dicta la clase. */
	public LocalDate getClase_fechaClase() {
		return clase_fechaClase;
	}

	/** Cambia la fecha en que se dicta la clase. */
	public void setClase_fechaClase(LocalDate clase_fechaClase) {
		this.clase_fechaClase = clase_fechaClase;
	}

	/** @return la observacion/descripcion de la clase (puede ser nula). */
	public String getClase_descripcion() {
		return clase_descripcion;
	}

	/** Cambia la observacion/descripcion de la clase. */
	public void setClase_descripcion(String clase_descripcion) {
		this.clase_descripcion = clase_descripcion;
	}

	/** @return el grupo de catequesis al que pertenece esta clase. */
	public GrupoCatequesisModelo getGrupoCatequesis() {
		return grupoCatequesis;
	}

	/** Asigna el grupo de catequesis al que pertenece esta clase. */
	public void setGrupoCatequesis(GrupoCatequesisModelo grupoCatequesis) {
		this.grupoCatequesis = grupoCatequesis;
	}

	/** @return la lista de asistencias registradas en esta clase. */
	public List<AsistenciaModelo> getAsistencias() {
		return asistencias;
	}

	/** Reemplaza la lista completa de asistencias de esta clase. */
	public void setAsistencias(List<AsistenciaModelo> asistencias) {
		this.asistencias = asistencias;
	}

}