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

@Entity(name = "tb_catequista")
public class CatequistaModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cat_id;

	@Column(length = 100, nullable = false)
	private String cat_nombre;

	@Column(length = 100, nullable = false)
	private String cat_apellido;

	@Column(length = 50, nullable = false, unique = false)
	private String cat_documento;

	@Column(length = 45, nullable = false)
	private String cat_telefono;

	@Column(length = 100, nullable = false)
	private String cat_correo;

	@Column(length = 100, nullable = false)
	private String cat_direccion;

	@Column(name = "cat_fechanacimiento", nullable = false)
	private LocalDate cat_fechaNacimiento;

	@Column(name = "cat_fecharegistro", nullable = false)
	private LocalDate cat_fechaRegistro;

	@Column(nullable = false)
	private Boolean cat_estado;

	//========================= MUCHOS A MUCHOS ==================================
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

	public CatequistaModelo() {
		super();

	}

	public Integer getCat_id() {
		return cat_id;
	}

	public void setCat_id(Integer cat_id) {
		this.cat_id = cat_id;
	}

	public String getCat_nombre() {
		return cat_nombre;
	}

	public void setCat_nombre(String cat_nombre) {
		this.cat_nombre = cat_nombre;
	}

	public String getCat_apellido() {
		return cat_apellido;
	}

	public void setCat_apellido(String cat_apellido) {
		this.cat_apellido = cat_apellido;
	}

	public String getCat_documento() {
		return cat_documento;
	}

	public void setCat_documento(String cat_documento) {
		this.cat_documento = cat_documento;
	}

	public String getCat_telefono() {
		return cat_telefono;
	}

	public void setCat_telefono(String cat_telefono) {
		this.cat_telefono = cat_telefono;
	}

	public String getCat_correo() {
		return cat_correo;
	}

	public void setCat_correo(String cat_correo) {
		this.cat_correo = cat_correo;
	}

	public String getCat_direccion() {
		return cat_direccion;
	}

	public void setCat_direccion(String cat_direccion) {
		this.cat_direccion = cat_direccion;
	}

	public LocalDate getCat_fechaNacimiento() {
		return cat_fechaNacimiento;
	}

	public void setCat_fechaNacimiento(LocalDate cat_fechaNacimiento) {
		this.cat_fechaNacimiento = cat_fechaNacimiento;
	}

	public LocalDate getCat_fechaRegistro() {
		return cat_fechaRegistro;
	}

	public void setCat_fechaRegistro(LocalDate cat_fechaRegistro) {
		this.cat_fechaRegistro = cat_fechaRegistro;
	}

	public Boolean isCat_estado() {
		return cat_estado;
	}

	public void setCat_estado(Boolean cat_estado) {
		this.cat_estado = cat_estado;
	}

	public List<SacramentoModelo> getSacramentos() {
		return sacramentos;
	}

	public void setSacramentos(List<SacramentoModelo> sacramentos) {
		this.sacramentos = sacramentos;
	}

}