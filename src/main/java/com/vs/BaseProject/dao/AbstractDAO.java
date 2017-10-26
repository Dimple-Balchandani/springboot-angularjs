package com.vs.BaseProject.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractDAO<PK extends Serializable, T>{
	private Class<T> persistentClass;
	
	public final void setpersistentClass( Class< T > classToSet ){
	      persistentClass = classToSet;
	   }

	@PersistenceContext
	EntityManager entityManager;

	protected EntityManager getEntityManager() {
		return this.entityManager;
	}

	protected List<T> findAll(){
	      return entityManager.createQuery( "from " + persistentClass.getName() )
	       .getResultList();
	   }
	protected T getByKey(PK key) {
		return entityManager.find(persistentClass, key);
	}

	protected void persist(T entity) {
		entityManager.persist(entity);
	}

	protected void update(T entity) {
		entityManager.merge(entity);
	}

	protected void delete(T entity) {
		entityManager.remove(entity);
	}

}
