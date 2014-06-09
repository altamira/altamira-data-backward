package br.com.altamira.master.data.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.altamira.master.data.model.Request;

@Stateless
public class RequestDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Request> getAll() {
		return entityManager.createNamedQuery("Request.findAll", Request.class).getResultList();
	}
}
