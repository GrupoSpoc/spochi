package com.spochi.repository;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityRepository<E> {
    E create(E entity);
}
