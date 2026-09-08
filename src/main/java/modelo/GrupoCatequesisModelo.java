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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * Entidad JPA que representa un GRUPO DE CATEQUESIS (curso) dentro de una
 * {@link EtapaModelo} y un año determinado.
 *
 * Es la entidad central del modelo: conecta etapas, catequistas,
 * catequizandos (a traves de las inscripciones), clases (sesiones) y
 * transferencias de alumnos entre grupos.
 *
 * Tabla asociada: tb_grupo_catequesis
 */
@Entity(name = "tb_grupo_catequesis")
public class GrupoCatequesisModelo {

	/** Identificador unico del grupo (clave primaria, autogenerada por la base de datos). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer grup_id;

	/** Nombre del grupo (obligatorio, maximo 100 caracteres). */
	@Column(name = "grup_nombre", length = 100, nullable = false)
	private String grup_nombre;

	/** Año/periodo lectivo al que corresponde el grupo (obligatorio). */
	@Column(nullable = false)
	private LocalDate grup_anho;

	// ===================== MUCHOS A UNO ===================

	/**
	 * Etapa a la que pertenece este grupo (obligatoria: un grupo no puede
	 * existir sin una etapa asignada). Lado propietario de la relacion,
	 * mapeado por la columna etap_id.
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "etap_id")
	private EtapaModelo etapa;

	// ===================== MUCHOS A MUCHOS ===================

	/**
	 * Catequistas asignados a este grupo. Un catequista puede participar
	 * en varios grupos y un grupo puede tener varios catequistas.
	 * Relacion Muchos a Muchos gestionada mediante la tabla intermedia
	 * tb_grupo_catequista (columnas grupo_id / cat_id).
	 */
	// LISTA: catequistas asignados a este grupo (un catequista puede participar en varios grupos)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "tb_grupo_catequista",
			joinColumns = @JoinColumn(name = "grupo_id"),
			inverseJoinColumns = @JoinColumn(name = "cat_id")
	)
	private List<CatequistaModelo> catequistas;

	// ====================== UNO A MUCHOS ====================

	/**
	 * Todos los alumnos (catequizandos) inscritos en este grupo.
	 * cascade = ALL: si se elimina el grupo, se eliminan tambien sus inscripciones.
	 */
	// LISTA para obtener todos los alumnos inscritos en este grupo
	@OneToMany(mappedBy = "grupoCatequesis", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<InscripcionModelo> inscripciones;

	/**
	 * Todas las sesiones/clases dictadas a este grupo.
	 * cascade = ALL: si se elimina el grupo, se eliminan tambien sus clases
	 * (y, en cascada desde ClaseModelo, las asistencias tomadas en ellas).
	 */
	// LISTA para obtener todas las sesiones o clases de este grupo
	@OneToMany(mappedBy = "grupoCatequesis", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ClaseModelo> clases;

	/**
	 * Transferencias en las que este grupo fue el GRUPO DE ORIGEN, es decir,
	 * alumnos que salieron desde aca hacia otro grupo. Sin cascada: se
	 * conserva como historial aunque cambie el grupo.
	 */
	// LISTA: transferencias en las que este grupo fue el grupo de origen (alumnos que salieron de aca)
	@OneToMany(mappedBy = "grupoOrigen", fetch = FetchType.EAGER)
	private List<TransferenciaModelo> transferenciasOrigen;

	/**
	 * Transferencias en las que este grupo fue el GRUPO DE DESTINO, es decir,
	 * alumnos que llegaron a este grupo provenientes de otro. Sin cascada:
	 * se conserva como historial aunque cambie el grupo.
	 */
	// LISTA: transferencias en las que este grupo fue el grupo de destino (alumnos que llegaron aca)
	@OneToMany(mappedBy = "grupoDestino", fetch = FetchType.EAGER)
	private List<TransferenciaModelo> transferenciasDestino;

	/** Constructor vacio requerido por JPA/Hibernate. */
	public GrupoCatequesisModelo() {
		super();

	}

	// ------------------------- Getters y Setters -------------------------
	// Acceso estandar (JavaBeans) a cada atributo. No contienen logica de negocio.

	/** @return el id del grupo. */
	public Integer getGrup_id() {
		return grup_id;
	}

	/** Establece el id del grupo (normalmente lo asigna JPA al persistir). */
	public void setGrup_id(Integer grup_id) {
		this.grup_id = grup_id;
	}

	/** @return el nombre del grupo. */
	public String getGrup_nombre() {
		return grup_nombre;
	}

	/** Cambia el nombre del grupo. */
	public void setGrup_nombre(String grup_nombre) {
		this.grup_nombre = grup_nombre;
	}

	/** @return el año/periodo lectivo del grupo. */
	public LocalDate getGrup_anho() {
		return grup_anho;
	}

	/** Cambia el año/periodo lectivo del grupo. */
	public void setGrup_anho(LocalDate grup_anho) {
		this.grup_anho = grup_anho;
	}

	/** @return la etapa a la que pertenece este grupo. */
	public EtapaModelo getEtapa() {
		return etapa;
	}

	/** Asigna la etapa a la que pertenece este grupo. */
	public void setEtapa(EtapaModelo etapa) {
		this.etapa = etapa;
	}

	/** @return la lista de catequistas asignados a este grupo. */
	public List<CatequistaModelo> getCatequistas() {
		return catequistas;
	}

	/** Reemplaza la lista completa de catequistas asignados a este grupo. */
	public void setCatequistas(List<CatequistaModelo> catequistas) {
		this.catequistas = catequistas;
	}

	/** @return la lista de inscripciones (alumnos) de este grupo. */
	public List<InscripcionModelo> getInscripciones() {
		return inscripciones;
	}

	/** Reemplaza la lista completa de inscripciones de este grupo. */
	public void setInscripciones(List<InscripcionModelo> inscripciones) {
		this.inscripciones = inscripciones;
	}

	/** @return la lista de clases (sesiones) dictadas a este grupo. */
	public List<ClaseModelo> getClases() {
		return clases;
	}

	/** Reemplaza la lista completa de clases de este grupo. */
	public void setClases(List<ClaseModelo> clases) {
		this.clases = clases;
	}

	/** @return las transferencias en las que este grupo fue el origen. */
	public List<TransferenciaModelo> getTransferenciasOrigen() {
		return transferenciasOrigen;
	}

	/** Reemplaza la lista de transferencias en las que este grupo fue el origen. */
	public void setTransferenciasOrigen(List<TransferenciaModelo> transferenciasOrigen) {
		this.transferenciasOrigen = transferenciasOrigen;
	}

	/** @return las transferencias en las que este grupo fue el destino. */
	public List<TransferenciaModelo> getTransferenciasDestino() {
		return transferenciasDestino;
	}

	/** Reemplaza la lista de transferencias en las que este grupo fue el destino. */
	public void setTransferenciasDestino(List<TransferenciaModelo> transferenciasDestino) {
		this.transferenciasDestino = transferenciasDestino;
	}

}
