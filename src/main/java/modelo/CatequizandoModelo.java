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
import jakarta.persistence.OneToMany;
import jakarta.persistence.UniqueConstraint;

/**
 * Entidad JPA que representa a un CATEQUIZANDO (alumno/catecumeno) que
 * recibe la catequesis.
 *
 * Es la entidad con mas relaciones salientes: inscripciones (a grupos),
 * transferencias (historial de cambios de grupo) y sacramentos recibidos.
 *
 * Tabla asociada: tb_catequizando
 */
@Entity(name = "tb_catequizando")
public class CatequizandoModelo {

	/** Identificador unico del catequizando (clave primaria, autogenerada por la base de datos). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer catz_id;

	/** Nombre del catequizando (obligatorio, maximo 100 caracteres). */
	@Column(length = 100, nullable = false)
	private String catz_nombre;

	/** Apellido del catequizando (obligatorio, maximo 100 caracteres). */
	@Column(length = 100, nullable = false)
	private String catz_apellido;

	/** Numero de documento de identidad (obligatorio, maximo 50 caracteres). No es unico: puede repetirse. */
	@Column(length = 50, nullable = false, unique = false)
	private String catz_documento;

	/** Telefono de contacto (opcional, maximo 45 caracteres). */
	@Column(length = 45, nullable = true)
	private String catz_telefono;

	/** Correo electronico (opcional, maximo 100 caracteres). */
	@Column(length = 100, nullable = true)
	private String catz_correo;

	/** Direccion de residencia (obligatoria, maximo 100 caracteres). */
	@Column(length = 100, nullable = false)
	private String catz_direccion;

	/** Nombre del responsable/tutor del catequizando (obligatorio, maximo 100 caracteres). */
	@Column(name = "catz_nombreresponsable", length = 100, nullable = false)
	private String catz_nombreResponsable;

	/** Contacto (telefono) del responsable/tutor del catequizando (obligatorio, maximo 45 caracteres). */
	@Column(name = "catz_contactoresponsable", length = 45, nullable = false)
	private String catz_contactoResponsable;

	/** Fecha de nacimiento del catequizando (obligatoria). */
	@Column(name = "catz_fechanacimiento", nullable = false)
	private LocalDate catz_fechaNacimiento;

	/** Fecha en que el catequizando fue registrado en el sistema (obligatoria). */
	@Column(name = "catz_fecharegistro", nullable = false)
	private LocalDate catz_fechaRegistro;

	/** Indica si el catequizando esta activo (true) o inactivo/dado de baja (false). Obligatorio. */
	@Column(nullable = false)
	private Boolean catz_estado;

	//========================= UNO A MUCHOS ==================================
	/**
	 * Inscripciones del catequizando. Un alumno puede tener varias
	 * inscripciones a lo largo del tiempo, aunque la restriccion unica
	 * definida en InscripcionModelo obliga a que solo exista una fila de
	 * inscripcion por catequizando (la vigente). cascade = ALL: si se
	 * elimina el catequizando, se elimina tambien su inscripcion.
	 */
	// LISTA: Un alumno puede tener varias inscripciones
	@OneToMany(mappedBy = "catequizando", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<InscripcionModelo> inscripciones;

	/**
	 * Historial de transferencias (cambios de grupo) de este catequizando.
	 * cascade = ALL: si se elimina el catequizando, se elimina tambien su
	 * historial de transferencias.
	 */
	// LISTA: historial de transferencias de grupo de este catequizando
	@OneToMany(mappedBy = "catequizando", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<TransferenciaModelo> transferencias;

	//========================= MUCHOS A MUCHOS ==================================
	/**
	 * Sacramentos que posee el catequizando (ej: Bautismo, Primera Comunion).
	 * Regla de negocio: no puede repetir el mismo sacramento, garantizado por
	 * la restriccion unica compuesta (catz_id, sacr_id) en la tabla intermedia
	 * tb_catequizando_sacramento.
	 */
	// LISTA: sacramentos que posee el catequizando (regla: no puede repetir el mismo sacramento,
	// garantizado por la restriccion unica catz_id + sacr_id en la tabla intermedia)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "tb_catequizando_sacramento",
			joinColumns = @JoinColumn(name = "catz_id"),
			inverseJoinColumns = @JoinColumn(name = "sacr_id"),
			uniqueConstraints = @UniqueConstraint(columnNames = { "catz_id", "sacr_id" })
	)
	private List<SacramentoModelo> sacramentos;

	/** Constructor vacio requerido por JPA/Hibernate. */
	public CatequizandoModelo() {
		super();

	}

	// ------------------------- Getters y Setters -------------------------
	// Acceso estandar (JavaBeans) a cada atributo. No contienen logica de negocio.

	/** @return el id del catequizando. */
	public Integer getCatz_id() {
		return catz_id;
	}

	/** Establece el id del catequizando (normalmente lo asigna JPA al persistir). */
	public void setCatz_id(Integer catz_id) {
		this.catz_id = catz_id;
	}

	/** @return el nombre del catequizando. */
	public String getCatz_nombre() {
		return catz_nombre;
	}

	/** Cambia el nombre del catequizando. */
	public void setCatz_nombre(String catz_nombre) {
		this.catz_nombre = catz_nombre;
	}

	/** @return el apellido del catequizando. */
	public String getCatz_apellido() {
		return catz_apellido;
	}

	/** Cambia el apellido del catequizando. */
	public void setCatz_apellido(String catz_apellido) {
		this.catz_apellido = catz_apellido;
	}

	/** @return el numero de documento del catequizando. */
	public String getCatz_documento() {
		return catz_documento;
	}

	/** Cambia el numero de documento del catequizando. */
	public void setCatz_documento(String catz_documento) {
		this.catz_documento = catz_documento;
	}

	/** @return el telefono de contacto del catequizando. */
	public String getCatz_telefono() {
		return catz_telefono;
	}

	/** Cambia el telefono de contacto del catequizando. */
	public void setCatz_telefono(String catz_telefono) {
		this.catz_telefono = catz_telefono;
	}

	/** @return el correo electronico del catequizando. */
	public String getCatz_correo() {
		return catz_correo;
	}

	/** Cambia el correo electronico del catequizando. */
	public void setCatz_correo(String catz_correo) {
		this.catz_correo = catz_correo;
	}

	/** @return la direccion del catequizando. */
	public String getCatz_direccion() {
		return catz_direccion;
	}

	/** Cambia la direccion del catequizando. */
	public void setCatz_direccion(String catz_direccion) {
		this.catz_direccion = catz_direccion;
	}

	/** @return el nombre del responsable/tutor del catequizando. */
	public String getCatz_nombreResponsable() {
		return catz_nombreResponsable;
	}

	/** Cambia el nombre del responsable/tutor del catequizando. */
	public void setCatz_nombreResponsable(String catz_nombreResponsable) {
		this.catz_nombreResponsable = catz_nombreResponsable;
	}

	/** @return el contacto del responsable/tutor del catequizando. */
	public String getCatz_contactoResponsable() {
		return catz_contactoResponsable;
	}

	/** Cambia el contacto del responsable/tutor del catequizando. */
	public void setCatz_contactoResponsable(String catz_contactoResponsable) {
		this.catz_contactoResponsable = catz_contactoResponsable;
	}

	/** @return la fecha de nacimiento del catequizando. */
	public LocalDate getCatz_fechaNacimiento() {
		return catz_fechaNacimiento;
	}

	/** Cambia la fecha de nacimiento del catequizando. */
	public void setCatz_fechaNacimiento(LocalDate catz_fechaNacimiento) {
		this.catz_fechaNacimiento = catz_fechaNacimiento;
	}

	/** @return la fecha de registro del catequizando en el sistema. */
	public LocalDate getCatz_fechaRegistro() {
		return catz_fechaRegistro;
	}

	/** Cambia la fecha de registro del catequizando en el sistema. */
	public void setCatz_fechaRegistro(LocalDate catz_fechaRegistro) {
		this.catz_fechaRegistro = catz_fechaRegistro;
	}

	/** @return true si el catequizando esta activo, false si esta dado de baja. */
	public Boolean isCatz_estado() {
		return catz_estado;
	}

	/** Activa o desactiva al catequizando. */
	public void setCatz_estado(Boolean catz_estado) {
		this.catz_estado = catz_estado;
	}

	/** @return la lista de inscripciones del catequizando. */
	public List<InscripcionModelo> getInscripciones() {
		return inscripciones;
	}

	/** Reemplaza la lista completa de inscripciones del catequizando. */
	public void setInscripciones(List<InscripcionModelo> inscripciones) {
		this.inscripciones = inscripciones;
	}

	/** @return el historial de transferencias (cambios de grupo) del catequizando. */
	public List<TransferenciaModelo> getTransferencias() {
		return transferencias;
	}

	/** Reemplaza el historial completo de transferencias del catequizando. */
	public void setTransferencias(List<TransferenciaModelo> transferencias) {
		this.transferencias = transferencias;
	}

	/** @return la lista de sacramentos que posee el catequizando. */
	public List<SacramentoModelo> getSacramentos() {
		return sacramentos;
	}

	/** Reemplaza la lista completa de sacramentos del catequizando. */
	public void setSacramentos(List<SacramentoModelo> sacramentos) {
		this.sacramentos = sacramentos;
	}

}