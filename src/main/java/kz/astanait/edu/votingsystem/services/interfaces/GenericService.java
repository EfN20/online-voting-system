package kz.astanait.edu.votingsystem.services.interfaces;

import java.util.List;

public interface GenericService<T> {

    T findById(Long id);
    T save(T entity);
    void delete(T entity);
    void deleteById(Long id);
    Long count();
    List<T> findAll();
}
