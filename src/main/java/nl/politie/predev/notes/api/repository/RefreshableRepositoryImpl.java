package nl.politie.predev.notes.api.repository;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
 
import javax.persistence.EntityManager;
import java.io.Serializable;
 
public class RefreshableRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements RefreshableRepository<T, ID> {
 
    private final EntityManager entityManager;
 
    public RefreshableRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }
 
    @Override
    @Transactional
    public void refresh(T t) {
        entityManager.refresh(t);
    }
    
    @Override
    public EntityManager getEM() {
    	return this.entityManager;
    }
}