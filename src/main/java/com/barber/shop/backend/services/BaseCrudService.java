package com.barber.shop.backend.services;

import java.util.List;

public interface BaseCrudService<D> {

    D create(D dto);

    D update(Long id, D dto);

    D getById(Long id);

    List<D> getAll();

    void delete(Long id);
}
