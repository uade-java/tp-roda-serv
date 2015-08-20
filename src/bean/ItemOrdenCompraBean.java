package bean;
import javax.persistence.*;



@Entity
@Table(name="ItemOrdenCompra")
public class ItemOrdenCompraBean
{
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int numero;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="item_orden")
	private OrdenPedidoBean ordenPedido;
	private float monto;
	public OrdenPedidoBean getOrdenPedido() {
		return ordenPedido;
	}
	public void setOrdenPedido(OrdenPedidoBean ordenPedido) {
		this.ordenPedido = ordenPedido;
	}
	public float getMonto() {
		return monto;
	}
	public void setMonto(float monto) {
		this.monto = monto;
	}
}
