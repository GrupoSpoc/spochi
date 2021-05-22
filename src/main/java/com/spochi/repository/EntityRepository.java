package com.spochi.repository;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityRepository<E> {
    E persist(E entity);
}
