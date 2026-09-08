package modelo;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.UniqueConstraint;

/**
 * Entidad JPA que representa a un CATEQUISTA (formador) del sistema.
 *
 * Guarda sus datos personales, de contacto y los sacramentos que posee.
 * Puede estar asignado a uno o varios {@link GrupoCatequesisModelo}
 * (esa relacion se define desde el lado de GrupoCatequesisModelo.catequistas).
 *
 * Tabla asociada: tb_catequista
 */
@Entity(name = "tb_catequista")
public class CatequistaModelo {

	/** Identificador unico del catequista (clave primaria, autogenerada por la base de datos). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cat_id;

	/** Nombre del catequista (obligatorio, maximo 100 caracteres). */
	@Column(length = 100, nullable = false)
	private String cat_nombre;

	/** Apellido del catequista (obligatorio, maximo 100 caracteres). */
	@Column(length = 100, nullable = false)
	private String cat_apellido;

	/** Numero de documento de identidad (obligatorio, maximo 50 caracteres). No es unico: puede repetirse. */
	@Column(length = 50, nullable = false, unique = false)
	private String cat_documento;

	/** Telefono de contacto (obligatorio, maximo 45 caracteres). */
	@Column(length = 45, nullable = false)
	private String cat_telefono;

	/** Correo electronico (obligatorio, maximo 100 caracteres). */
	@Column(length = 100, nullable = false)
	private String cat_correo;

	/** Direccion de residencia (obligatoria, maximo 100 caracteres). */
	@Column(length = 100, nullable = false)
	private String cat_direccion;

	/** Fecha de nacimiento del catequista (obligatoria). */
	@Column(name = "cat_fechanacimiento", nullable = false)
	private LocalDate cat_fechaNacimiento;

	/** Fecha en que el catequista fue registrado en el sistema (obligatoria). */
	@Column(name = "cat_fecharegistro", nullable = false)
	private LocalDate cat_fechaRegistro;

	/** Indica si el catequista esta activo (true) o inactivo/dado de baja (false). Obligatorio. */
	@Column(nullable = false)
	private Boolean cat_estado;

	// ========================= MUCHOS A MUCHOS ==================================
	/**
	 * Sacramentos que posee el catequista (ej: Bautismo, Confirmacion).
	 * Regla de negocio: no puede repetir el mismo sacramento, garantizado por
	 * la restriccion unica compuesta (cat_id, sacr_id) en la tabla intermedia
	 * tb_catequista_sacramento.
	 */
	// LISTA: sacramentos que posee el catequista (regla: no puede repetir el mismo sacramento,
	// garantizado por la restriccion unica cat_id + sacr_id en la tabla intermedia)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "tb_catequista_sacramento",
			joinColumns = @JoinColumn(name = "cat_id"),
			inverseJoinColumns = @JoinColumn(name = "sacr_id"),
			uniqueConstraints = @UniqueConstraint(columnNames = { "cat_id", "sacr_id" })
	)
	private List<SacramentoModelo> sacramentos;

	/** Constructor vacio requerido por JPA/Hibernate. */
	public CatequistaModelo() {
		super();

	}

	// ------------------------- Getters y Setters -------------------------
	// Acceso estandar (JavaBeans) a cada atributo. No contienen logica de negocio.

	/** @return el id del catequista. */
	public Integer getCat_id() {
		return cat_id;
	}

	/** Establece el id del catequista (normalmente lo asigna JPA al persistir). */
	public void setCat_id(Integer cat_id) {
		this.cat_id = cat_id;
	}

	/** @return el nombre del catequista. */
	public String getCat_nombre() {
		return cat_nombre;
	}

	/** Cambia el nombre del catequista. */
	public void setCat_nombre(String cat_nombre) {
		this.cat_nombre = cat_nombre;
	}

	/** @return el apellido del catequista. */
	public String getCat_apellido() {
		return cat_apellido;
	}

	/** Cambia el apellido del catequista. */
	public void setCat_apellido(String cat_apellido) {
		this.cat_apellido = cat_apellido;
	}

	/** @return el numero de documento del catequista. */
	public String getCat_documento() {
		return cat_documento;
	}

	/** Cambia el numero de documento del catequista. */
	public void setCat_documento(String cat_documento) {
		this.cat_documento = cat_documento;
	}

	/** @return el telefono de contacto del catequista. */
	public String getCat_telefono() {
		return cat_telefono;
	}

	/** Cambia el telefono de contacto del catequista. */
	public void setCat_telefono(String cat_telefono) {
		this.cat_telefono = cat_telefono;
	}

	/** @return el correo electronico del catequista. */
	public String getCat_correo() {
		return cat_correo;
	}

	/** Cambia el correo electronico del catequista. */
	public void setCat_correo(String cat_correo) {
		this.cat_correo = cat_correo;
	}

	/** @return la direccion del catequista. */
	public String getCat_direccion() {
		return cat_direccion;
	}

	/** Cambia la direccion del catequista. */
	public void setCat_direccion(String cat_direccion) {
		this.cat_direccion = cat_direccion;
	}

	/** @return la fecha de nacimiento del catequista. */
	public LocalDate getCat_fechaNacimiento() {
		return cat_fechaNacimiento;
	}

	/** Cambia la fecha de nacimiento del catequista. */
	public void setCat_fechaNacimiento(LocalDate cat_fechaNacimiento) {
		this.cat_fechaNacimiento = cat_fechaNacimiento;
	}

	/** @return la fecha de registro del catequista en el sistema. */
	public LocalDate getCat_fechaRegistro() {
		return cat_fechaRegistro;
	}

	/** Cambia la fecha de registro del catequista en el sistema. */
	public void setCat_fechaRegistro(LocalDate cat_fechaRegistro) {
		this.cat_fechaRegistro = cat_fechaRegistro;
	}

	/** @return true si el catequista esta activo, false si esta dado de baja. */
	public Boolean isCat_estado() {
		return cat_estado;
	}

	/** Activa o desactiva al catequista. */
	public void setCat_estado(Boolean cat_estado) {
		this.cat_estado = cat_estado;
	}

	/** @return la lista de sacramentos que posee el catequista. */
	public List<SacramentoModelo> getSacramentos() {
		return sacramentos;
	}

	/** Reemplaza la lista completa de sacramentos del catequista. */
	public void setSacramentos(List<SacramentoModelo> sacramentos) {
		this.sacramentos = sacramentos;
	}

}