package modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Entidad JPA que representa una ETAPA del proceso de catequesis
 * (por ejemplo: Iniciacion, Comunion, Confirmacion, etc.).
 *
 * Es el nivel mas alto de la jerarquia del sistema: una etapa agrupa
 * a varios {@link GrupoCatequesisModelo} (grupos/cursos).
 *
 * Tabla asociada: tb_etapa
 */
@Entity(name = "tb_etapa")
public class EtapaModelo {

	/** Identificador unico de la etapa (clave primaria, autogenerada por la base de datos). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer etap_id;

	/** Nombre o descripcion de la etapa (obligatorio, maximo 100 caracteres). */
	@Column(nullable = false, length = 100)
	private String etap_descripcion;

	/** Indica si la etapa esta activa (true) o inactiva/dada de baja (false). Obligatorio. */
	@Column(nullable = false)
	private Boolean etap_estado;

	// ========================= UNO A MUCHOS ==================================
	/**
	 * Lista de todos los grupos de catequesis que pertenecen a esta etapa.
	 * Relacion inversa (el lado "dueño" de la relacion es GrupoCatequesisModelo.etapa).
	 * No tiene cascada configurada: eliminar una etapa NO elimina automaticamente sus grupos.
	 */
	@OneToMany(mappedBy = "etapa", fetch = FetchType.EAGER)
	private List<GrupoCatequesisModelo> grupos;

	/** Constructor vacio requerido por JPA/Hibernate. */
	public EtapaModelo() {
		super();

	}

	// ------------------------- Getters y Setters -------------------------
	// Acceso estandar (JavaBeans) a cada atributo. No contienen logica de negocio.

	/** @return el id de la etapa. */
	public Integer getEtap_id() {
		return etap_id;
	}

	/** Establece el id de la etapa (normalmente lo asigna JPA al persistir). */
	public void setEtap_id(Integer etap_id) {
		this.etap_id = etap_id;
	}

	/** @return la descripcion/nombre de la etapa. */
	public String getEtap_descripcion() {
		return etap_descripcion;
	}

	/** Cambia la descripcion/nombre de la etapa. */
	public void setEtap_descripcion(String etap_descripcion) {
		this.etap_descripcion = etap_descripcion;
	}

	/** @return true si la etapa esta activa, false si esta dada de baja. */
	public Boolean isEtap_estado() {
		return etap_estado;
	}

	/** Activa o desactiva la etapa. */
	public void setEtap_estado(Boolean etap_estado) {
		this.etap_estado = etap_estado;
	}

	/** @return la lista de grupos de catequesis que pertenecen a esta etapa. */
	public List<GrupoCatequesisModelo> getGrupos() {
		return grupos;
	}

	/** Reemplaza la lista completa de grupos asociados a esta etapa. */
	public void setGrupos(List<GrupoCatequesisModelo> grupos) {
		this.grupos = grupos;
	}

}