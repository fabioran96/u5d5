package com.fabioran.u5d5.repositories;

import com.fabioran.u5d5.entities.Postazione;
import com.fabioran.u5d5.entities.Prenotazione;
import com.fabioran.u5d5.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrenotazioneDAO extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByDataAndPostazione(LocalDate data, Postazione postazione);
    List<Prenotazione> findByUtenteAndData(Utente utente, LocalDate data);
    List<Prenotazione> findByUtente(Utente utente);

}
