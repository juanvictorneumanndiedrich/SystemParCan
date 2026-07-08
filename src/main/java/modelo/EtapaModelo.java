package modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name = "tb_etapa")
public class EtapaModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer etap_id;

	@Column(nullable = false, length = 100)
	private String etap_descripcion;

	@Column(nullable = false)
	private Boolean etap_estado;

	//========================= UNO A MUCHOS ==================================
	// LISTA: una etapa puede tener varios grupos de catequesis
	@OneToMany(mappedBy = "etapa", fetch = FetchType.EAGER)
	private List<GrupoCatequesisModelo> grupos;

	public EtapaModelo() {
		super();

	}

	public Integer getEtap_id() {
		return etap_id;
	}

	public void setEtap_id(Integer etap_id) {
		this.etap_id = etap_id;
	}

	public String getEtap_descripcion() {
		return etap_descripcion;
	}

	public void setEtap_descripcion(String etap_descripcion) {
		this.etap_descripcion = etap_descripcion;
	}

	public Boolean isEtap_estado() {
		return etap_estado;
	}

	public void setEtap_estado(Boolean etap_estado) {
		this.etap_estado = etap_estado;
	}

	public List<GrupoCatequesisModelo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<GrupoCatequesisModelo> grupos) {
		this.grupos = grupos;
	}

}