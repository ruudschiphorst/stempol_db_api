package nl.politie.predev.notes.api.repository;


import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
 
@NoRepositoryBean
public interface RefreshableRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
    void refresh(T t);

	EntityManager getEM();
}