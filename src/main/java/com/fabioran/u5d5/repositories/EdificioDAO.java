package com.fabioran.u5d5.repositories;

import com.fabioran.u5d5.entities.Edificio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdificioDAO extends JpaRepository<Edificio, Long> {
}
