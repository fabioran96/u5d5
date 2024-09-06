package com.fabioran.u5d5.repositories;

import com.fabioran.u5d5.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteDAO extends JpaRepository<Utente, String> {
}
