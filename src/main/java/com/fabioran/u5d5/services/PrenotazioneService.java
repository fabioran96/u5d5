package com.fabioran.u5d5.services;

import com.fabioran.u5d5.entities.Postazione;
import com.fabioran.u5d5.entities.Prenotazione;
import com.fabioran.u5d5.entities.TipoPostazione;
import com.fabioran.u5d5.entities.Utente;
import com.fabioran.u5d5.exceptions.NotFoundException;
import com.fabioran.u5d5.repositories.PostazioneDAO;
import com.fabioran.u5d5.repositories.PrenotazioneDAO;
import com.fabioran.u5d5.repositories.UtenteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneDAO prenotazioneDAO;

    @Autowired
    private PostazioneDAO postazioneDAO;

    @Autowired
    private UtenteDAO utenteDAO;

    @Autowired
    private DateTimeFormatter dateFormatter;

    public Prenotazione prenotaPostazione(String username, Long postazioneId, LocalDate data) throws Exception {
        Utente utente = utenteDAO.findById(username)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        Postazione postazione = postazioneDAO.findById(postazioneId)
                .orElseThrow(() -> new NotFoundException("Postazione non trovata"));

        List<Prenotazione> prenotazioniPostazione = prenotazioneDAO.findByDataAndPostazione(data, postazione);
        if (!prenotazioniPostazione.isEmpty()) {
            throw new Exception("Postazione già prenotata per la data selezionata");
        }

        List<Prenotazione> prenotazioniUtente = prenotazioneDAO.findByUtenteAndData(utente, data);
        if (!prenotazioniUtente.isEmpty()) {
            throw new Exception("Utente ha già una prenotazione per la data selezionata");
        }

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUtente(utente);
        prenotazione.setPostazione(postazione);
        prenotazione.setData(data);

        return prenotazioneDAO.save(prenotazione);
    }

    public List<Postazione> ricercaPostazioni(TipoPostazione tipo, String citta) {
        return postazioneDAO.findByTipoAndEdificio_Citta(tipo, citta);
    }

    public String formatDate(LocalDate date) {
        return date.format(dateFormatter);
    }

    public void cancellaPrenotazione(Long prenotazioneId) throws Exception {
        Optional<Prenotazione> prenotazione = prenotazioneDAO.findById(prenotazioneId);
        if (prenotazione.isPresent()) {
            prenotazioneDAO.deleteById(prenotazioneId);
        } else {
            throw new Exception("Prenotazione non trovata");
        }
    }

    public List<Prenotazione> getPrenotazioniByUtente(String username) {
        Utente utente = utenteDAO.findById(username).orElse(null);
        if (utente != null) {
            return prenotazioneDAO.findByUtente(utente); // Usa il metodo creato in PrenotazioneDAO
        }
        return List.of();
    }


}

