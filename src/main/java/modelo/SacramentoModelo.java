package modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

// Modulo de Sacramentos: compartido entre catequizandos y catequistas.
// Sacramentos iniciales esperados: Bautismo, Primera Comunion, Confirmacion
// (se deben cargar como datos iniciales / seed, no forman parte del modelo).
@Entity(name = "tb_sacramento")
public class SacramentoModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sacr_id;

	@Column(length = 100, nullable = false, unique = true)
	private String sacr_nombre;

	//========================= MUCHOS A MUCHOS (lado inverso) ==================================
	@ManyToMany(mappedBy = "sacramentos")
	private List<CatequizandoModelo> catequizandos;

	@ManyToMany(mappedBy = "sacramentos")
	private List<CatequistaModelo> catequistas;

	public SacramentoModelo() {
		super();
	}

	public Integer getSacr_id() {
		return sacr_id;
	}

	public void setSacr_id(Integer sacr_id) {
		this.sacr_id = sacr_id;
	}

	public String getSacr_nombre() {
		return sacr_nombre;
	}

	public void setSacr_nombre(String sacr_nombre) {
		this.sacr_nombre = sacr_nombre;
	}

	public List<CatequizandoModelo> getCatequizandos() {
		return catequizandos;
	}

	public void setCatequizandos(List<CatequizandoModelo> catequizandos) {
		this.catequizandos = catequizandos;
	}

	public List<CatequistaModelo> getCatequistas() {
		return catequistas;
	}

	public void setCatequistas(List<CatequistaModelo> catequistas) {
		this.catequistas = catequistas;
	}

}