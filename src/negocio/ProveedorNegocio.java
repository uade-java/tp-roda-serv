package negocio;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import dto.ProveedorDto;

@Entity
@Table(name="Proveedor")
public class ProveedorNegocio{

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int IdProveedor;
	private String nombre;
	//@OneToMany (cascade=CascadeType.ALL)
	//@JoinColumn(name="rodamiento_proveedor")
	@OneToMany(mappedBy = "proveedor")
	private List<RodamientoNegocio> rodamientos;
	
	
	
	public ProveedorNegocio(String nombre, List<RodamientoNegocio> rodamientos) {
		super();
		this.nombre = nombre;
		this.rodamientos = rodamientos;
	}
	
	public ProveedorNegocio(){
		
	}
	
	public ProveedorDto aProveedorDto(){
		ProveedorDto prove = new ProveedorDto();
		prove.setNombre(nombre);
		return prove;
	}
	
	public ProveedorNegocio aProveedorNegocio(ProveedorDto proveedor){
		ProveedorNegocio prove = new ProveedorNegocio();
		prove.setNombre(proveedor.getNombre());
		return prove;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<RodamientoNegocio> getRodamientos() {
		return rodamientos;
	}
	
	public void setRodamientos(List<RodamientoNegocio> regulares) {
		rodamientos = regulares;
	}
	
	
}
