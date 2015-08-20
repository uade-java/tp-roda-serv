package bean;

import java.util.List;

import javax.persistence.*;



@Entity
@Table(name="Proveedor")
public class ProveedorBean
{
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int IdProveedor;
	private String nombre;
	@OneToMany (cascade=CascadeType.ALL)
	@JoinColumn(name="Rodamiento_proveedor")
	private List<RodamientoBean> Rodamientos;

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<RodamientoBean> getRegulares() {
		return Rodamientos;
	}
	public void setRegulares(List<RodamientoBean> regulares) {
		Rodamientos = regulares;
	}
	
}
