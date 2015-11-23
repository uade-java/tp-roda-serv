package dao;

import hbt.HibernateUtil;

import java.util.*;

import negocio.*;

import org.hibernate.*;

public class CotizacionDAO extends HibernateDAO{
	
	private static CotizacionDAO instancia;

	private CotizacionDAO() {
		super();
	}
	
	public static CotizacionDAO getinstancia(){
		
		if (instancia == null) 
			instancia = new CotizacionDAO();
		return instancia;
	}

	//Levanta las cotizaciones en un estado pasado por parametro
	public List<CotizacionNegocio> obtenerCotizacionesAprobada(String estado){
		Session s = HibernateUtil.getSessionFactory().openSession();
		
		@SuppressWarnings("unchecked")
		List<CotizacionNegocio> cotizacionesAprobadas = s.createQuery("from CotizacionNegocio c where c.estado = ?").setParameter(1, estado).list();
		
		List<CotizacionNegocio> cotizacionSalida = new ArrayList<CotizacionNegocio>();
		for(int i=0; i<cotizacionesAprobadas.size(); i++){
			CotizacionNegocio miCotizacion = new CotizacionNegocio();
			cotizacionSalida.add(miCotizacion);
		}
		
		return cotizacionSalida;
	}
	
	
	//Levantar las cotizaciones de un cliente en estado distinto de "solicitada"
	@SuppressWarnings("unchecked")
	public List<CotizacionNegocio> obtenerCotizacionesDeCiente(ClienteNegocio clie){
		Session se = HibernateUtil.getSessionFactory().getCurrentSession();
		List<CotizacionNegocio> salida;
		Transaction tr = se.getTransaction();
		tr.begin();
		Query q = se.createQuery("from CotizacionNegocio cot "
				+ "where cot.cliente = :clie "
				+ "and cot.estado <> 'solicitada'").setParameter("clie", clie);
		salida = q.list();
		tr.commit();
		se = null;
		return salida;
	}
	
	// Levantar cotizacion segun id recibido
	public CotizacionNegocio buscarCotizacion(int idCot){
		Session se = HibernateUtil.getSessionFactory().openSession();
		CotizacionNegocio salida = (CotizacionNegocio) se.get(CotizacionNegocio.class, idCot);
		se = null;
		return salida;
	}
	
	//Levantar todas las cotizaciones de una OV
	@SuppressWarnings("unchecked")
	public List<CotizacionNegocio> cotizacionesTodas (OVNegocio ov){
		Session se = HibernateUtil.getSessionFactory().getCurrentSession();
		List<CotizacionNegocio> salida = new ArrayList<CotizacionNegocio>();
		Transaction trx = se.getTransaction();
		trx.begin();
		Query q = se.createQuery("select cotis "
				+ "from OVNegocio ovn join ovn.cotizaciones cotis "
				+ "where ovn = :ov ").setParameter("ov", ov);
		salida = q.list();
		trx.commit();
		se = null;
		return salida;
	}
	
	// Levantar Items Cotización de determinas cotizaciones
//	@SuppressWarnings("unchecked")
//	public List<Object[]> itemsCotizacionAgrupadosPorRodamiento(List<Integer> cotizaciones){
//		Session se = HibernateUtil.getSessionFactory().getCurrentSession();
//		List<Object[]> salida;
//		Transaction tr = se.getTransaction();
//		tr.begin();
//		Query q = se.createQuery("Select ro.IdRodamiento, sum(itCot.cant), sum(itCot.subtotal)  "
//				+ "from CotizacionNegocio cot join cot.items itCot join itCot.rodamiento ro "
//				+ "where cot.idCotizacion in (:ids) "
//				+ "group by ro.IdRodamiento").setParameterList("ids", cotizaciones);
//		salida = q.list();
//		tr.commit();
//		se = null;
//		return salida;
//	}
	
	// Levantar Rodamiento, cantidad y subtotal de los itemsCotización de un listado de cotizaciones para una ov un estado determinado
	
	@SuppressWarnings("unchecked")
	public List<Object[]> rodaPorItemsCotizacion_OV_Estado(List<CotizacionNegocio> cotizaciones, OVNegocio ov, String estado){
		Session se = HibernateUtil.getSessionFactory().getCurrentSession();
		List<Object[]> salida;
		Transaction tr = se.getTransaction();
		tr.begin();
		Query q = se.createQuery("Select ro.IdRodamiento, sum(itCot.cant), sum(itCot.precio)  "
				+ "from OVNegocio ov join ov.cotizaciones cot join cot.items itCot join itCot.rodamiento ro "
				+ "where ov = :ov "
				+ "and cot.estado = :estado "
				+ "and cot in (:ids) "
				+ "group by ro.IdRodamiento ").setParameter("ov", ov).setParameter("estado", estado).setParameterList("ids", cotizaciones);
		salida = q.list();
		tr.commit();
		se = null;
		return salida;
	}
	
	// Levantar Rodamiento, cantidad y subtotal de los itemsCotización de un listado de cotizaciones 
	// para una ov un estado determinado y para un cliente
	
		@SuppressWarnings("unchecked")
		public List<Object[]> rodaPorItemsCotizacion_OV_Estado_x_Cliente(List<CotizacionNegocio> cotizaciones, 
				OVNegocio ov, String estado, ClienteNegocio clie){
			Session se = HibernateUtil.getSessionFactory().getCurrentSession();
			List<Object[]> salida;
			Transaction tr = se.getTransaction();
			tr.begin();
			Query q = se.createQuery("Select ro.IdRodamiento, sum(itCot.cant), sum(itCot.precio)  "
					+ "from OVNegocio ov join ov.cotizaciones cot join cot.cliente cli join cot.items itCot join itCot.rodamiento ro "
					+ "where ov = :ov "
					+ "and cot.estado = :estado "
					+ "and cot in (:ids) "
					+ "and cli = :cliente "
					+ "group by ro.IdRodamiento ").setParameter("ov", ov).setParameter("estado", estado)
					.setParameter("estado", clie)
					.setParameterList("ids", cotizaciones);
			salida = q.list();
			tr.commit();
			se = null;
			return salida;
		}
	
	// Levantar Rodamientos de Items Cotización de determinas cotizaciones para un proveedor dado
	@SuppressWarnings("unchecked")
	public List<Object[]> rodamientoDeItemsCotizacionAgrupadosPorRodamiento(ProveedorNegocio prove){
		Session se = HibernateUtil.getSessionFactory().getCurrentSession();
		List<Object[]> salida;
		Transaction tr = se.getTransaction();
		tr.begin();
		Query q = se.createQuery("Select ro, sum(itCot.cant), sum(itCot.subtotal) "
				+ "from CotizacionNegocio cot join cot.items itCot join itCot.rodamiento ro join ro.proveedor prov "
				+ "where prov = (:ids) "
				+ "group by ro.IdRodamiento").setParameter("ids", prove);
		salida = q.list();
		tr.commit();
		se = null;
		return salida;
	}
	
	// Levantar proveedores de los rodamiento de los Items Cotización de determinas cotizaciones
	@SuppressWarnings("unchecked")
	public List<ProveedorNegocio> proveedorDeItemsCotizacion(List<CotizacionNegocio> cotizaciones){
		Session se = HibernateUtil.getSessionFactory().getCurrentSession();
		List<ProveedorNegocio> salida;
		Transaction tr = se.getTransaction();
		tr.begin();
		Query q = se.createQuery("Select prov "
				+ "from CotizacionNegocio cot join cot.items itCot join itCot.rodamiento ro join ro.proveedor prov "
				+ "where cot in (:ids) "
				+ "group by prov ").setParameterList("ids", cotizaciones);
		salida = q.list();
		tr.commit();
		se = null;
		return salida;
	}

}
