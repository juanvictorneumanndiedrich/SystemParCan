package modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

/**
 * Entidad JPA que representa el catalogo de SACRAMENTOS
 * (ej: Bautismo, Primera Comunion, Confirmacion), compartido entre
 * catequizandos y catequistas.
 *
 * Los valores iniciales (sacramentos base) no forman parte del modelo:
 * deben cargarse como datos semilla (seed) al iniciar el sistema.
 *
 * Tabla asociada: tb_sacramento
 */
// Modulo de Sacramentos: compartido entre catequizandos y catequistas.
// Sacramentos iniciales esperados: Bautismo, Primera Comunion, Confirmacion
// (se deben cargar como datos iniciales / seed, no forman parte del modelo).
@Entity(name = "tb_sacramento")
public class SacramentoModelo {

	/** Identificador unico del sacramento (clave primaria, autogenerada por la base de datos). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sacr_id;

	/** Nombre del sacramento (obligatorio, unico, maximo 100 caracteres). No pueden existir dos sacramentos con el mismo nombre. */
	@Column(length = 100, nullable = false, unique = true)
	private String sacr_nombre;

	//========================= MUCHOS A MUCHOS (lado inverso) ==================================
	/**
	 * Catequizandos que poseen este sacramento. Lado inverso de la
	 * relacion definida en CatequizandoModelo.sacramentos.
	 */
	@ManyToMany(mappedBy = "sacramentos")
	private List<CatequizandoModelo> catequizandos;

	/**
	 * Catequistas que poseen este sacramento. Lado inverso de la
	 * relacion definida en CatequistaModelo.sacramentos.
	 */
	@ManyToMany(mappedBy = "sacramentos")
	private List<CatequistaModelo> catequistas;

	/** Constructor vacio requerido por JPA/Hibernate. */
	public SacramentoModelo() {
		super();
	}

	// ------------------------- Getters y Setters -------------------------
	// Acceso estandar (JavaBeans) a cada atributo. No contienen logica de negocio.

	/** @return el id del sacramento. */
	public Integer getSacr_id() {
		return sacr_id;
	}

	/** Establece el id del sacramento (normalmente lo asigna JPA al persistir). */
	public void setSacr_id(Integer sacr_id) {
		this.sacr_id = sacr_id;
	}

	/** @return el nombre del sacramento. */
	public String getSacr_nombre() {
		return sacr_nombre;
	}

	/** Cambia el nombre del sacramento. */
	public void setSacr_nombre(String sacr_nombre) {
		this.sacr_nombre = sacr_nombre;
	}

	/** @return la lista de catequizandos que poseen este sacramento. */
	public List<CatequizandoModelo> getCatequizandos() {
		return catequizandos;
	}

	/** Reemplaza la lista de catequizandos que poseen este sacramento. */
	public void setCatequizandos(List<CatequizandoModelo> catequizandos) {
		this.catequizandos = catequizandos;
	}

	/** @return la lista de catequistas que poseen este sacramento. */
	public List<CatequistaModelo> getCatequistas() {
		return catequistas;
	}

	/** Reemplaza la lista de catequistas que poseen este sacramento. */
	public void setCatequistas(List<CatequistaModelo> catequistas) {
		this.catequistas = catequistas;
	}

}