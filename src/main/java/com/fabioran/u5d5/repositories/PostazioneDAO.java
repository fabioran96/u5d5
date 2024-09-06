package com.fabioran.u5d5.repositories;

import com.fabioran.u5d5.entities.Postazione;
import com.fabioran.u5d5.entities.TipoPostazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostazioneDAO extends JpaRepository<Postazione, Long> {
    List<Postazione> findByTipoAndEdificio_Citta(TipoPostazione tipo, String citta);
}
