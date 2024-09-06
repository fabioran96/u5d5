package com.fabioran.u5d5;

import com.fabioran.u5d5.entities.Edificio;
import com.fabioran.u5d5.entities.Postazione;
import com.fabioran.u5d5.entities.Prenotazione;
import com.fabioran.u5d5.entities.TipoPostazione;
import com.fabioran.u5d5.repositories.EdificioDAO;
import com.fabioran.u5d5.repositories.PostazioneDAO;
import com.fabioran.u5d5.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class U5d5Application implements CommandLineRunner {

	@Autowired
	private PrenotazioneService prenotazioneService;

	@Autowired
	private PostazioneDAO postazioneDAO;

	@Autowired
	private EdificioDAO edificioDAO;

	public static void main(String[] args) {
		SpringApplication.run(U5d5Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		popolaDatabase();

		// Scanner per la gestione delle prenotazioni
		GestionePrenotazioni();
	}

	private void popolaDatabase() {
		if (edificioDAO.count() == 0) {
			Edificio edificio1 = new Edificio("Edificio A", "Via Roma, 1", "Milano");
			Edificio edificio2 = new Edificio("Edificio B", "Piazza San Marco, 10", "Venezia");
			Edificio edificio3 = new Edificio("Edificio C", "Corso Vittorio Emanuele, 12", "Napoli");
			Edificio edificio4 = new Edificio("Edificio D", "Via del Corso, 20", "Roma");
			Edificio edificio5 = new Edificio("Edificio E", "Via Garibaldi, 3", "Firenze");

			edificioDAO.save(edificio1);
			edificioDAO.save(edificio2);
			edificioDAO.save(edificio3);
			edificioDAO.save(edificio4);
			edificioDAO.save(edificio5);

			postazioneDAO.save(new Postazione("P001", "Postazione privata con finestra", TipoPostazione.PRIVATO, 1, edificio1));
			postazioneDAO.save(new Postazione("P002", "Postazione open space vicino alla reception", TipoPostazione.OPENSPACE, 4, edificio1));
			postazioneDAO.save(new Postazione("P003", "Sala riunioni media", TipoPostazione.SALA_RIUNIONI, 6, edificio2));
			postazioneDAO.save(new Postazione("P004", "Sala riunioni grande", TipoPostazione.SALA_RIUNIONI, 12, edificio2));
			postazioneDAO.save(new Postazione("P005", "Postazione privata con vista su Piazza", TipoPostazione.PRIVATO, 1, edificio3));
			postazioneDAO.save(new Postazione("P006", "Postazione open space in coworking", TipoPostazione.OPENSPACE, 6, edificio3));
			postazioneDAO.save(new Postazione("P007", "Sala riunioni piccola", TipoPostazione.SALA_RIUNIONI, 4, edificio4));
			postazioneDAO.save(new Postazione("P008", "Postazione privata con balcone", TipoPostazione.PRIVATO, 1, edificio4));
			postazioneDAO.save(new Postazione("P009", "Postazione open space con aria condizionata", TipoPostazione.OPENSPACE, 8, edificio5));
			postazioneDAO.save(new Postazione("P010", "Sala riunioni executive", TipoPostazione.SALA_RIUNIONI, 10, edificio5));

			System.out.println("Database popolato con postazioni in diverse città italiane!");
		}
	}




	private void GestionePrenotazioni() {
		Scanner scanner = new Scanner(System.in);

		boolean running = true;

		while (running) {
			System.out.println("\nBenvenuto nel sistema di gestione prenotazioni!");
			System.out.println("Scegli un'opzione:");
			System.out.println("1. Prenotare una postazione");
			System.out.println("2. Cercare una postazione");
			System.out.println("3. Visualizzare le prenotazioni per utente");
			System.out.println("4. Cancellare una prenotazione");
			System.out.println("5. Esci");

			int scelta = scanner.nextInt();
			scanner.nextLine();

			switch (scelta) {
				case 1:
					prenotarePostazione(scanner);
					break;
				case 2:
					cercarePostazione(scanner);
					break;
				case 3:
					visualizzarePrenotazioni(scanner);
					break;
				case 4:
					cancellarePrenotazione(scanner);
					break;
				case 5:
					System.out.println("Grazie per aver utilizzato il sistema di gestione prenotazioni!");
					running = false;
					break;
				default:
					System.out.println("Scelta non valida. Riprova.");
					break;
			}
		}
		scanner.close();
	}

	// Metodo per prenotare una postazione
	private void prenotarePostazione(Scanner scanner) {
		System.out.println("\nInserisci il tuo username:");
		String username = scanner.nextLine();

		System.out.println("Inserisci l'ID della postazione che desideri prenotare:");
		Long postazioneId = scanner.nextLong();

		System.out.println("Inserisci la data della prenotazione (formato: yyyy-MM-dd):");
		String dataStr = scanner.next();
		LocalDate data = LocalDate.parse(dataStr);

		try {
			Prenotazione prenotazione = prenotazioneService.prenotaPostazione(username, postazioneId, data);
			System.out.println("Prenotazione effettuata con successo per il giorno: " + prenotazione.getData());
		} catch (Exception e) {
			System.out.println("Errore durante la prenotazione: " + e.getMessage());
		}
	}

	// Metodo per cercare una postazione
	private void cercarePostazione(Scanner scanner) {
		System.out.println("\nInserisci il tipo di postazione (PRIVATO, OPENSPACE, SALA_RIUNIONI):");
		String tipoStr = scanner.nextLine();
		TipoPostazione tipo = TipoPostazione.valueOf(tipoStr.toUpperCase());

		System.out.println("Inserisci la città:");
		String citta = scanner.nextLine();

		List<Postazione> postazioni = prenotazioneService.ricercaPostazioni(tipo, citta);
		if (!postazioni.isEmpty()) {
			System.out.println("Postazioni trovate nella città di " + citta + ":");
			postazioni.forEach(System.out::println);
		} else {
			System.out.println("Nessuna postazione trovata per i criteri selezionati.");
		}
	}

	// Metodo per visualizzare le prenotazioni per utente
	private void visualizzarePrenotazioni(Scanner scanner) {
		System.out.println("\nInserisci il tuo username:");
		String username = scanner.nextLine();

		List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniByUtente(username);
		if (!prenotazioni.isEmpty()) {
			System.out.println("Prenotazioni per l'utente " + username + ":");
			prenotazioni.forEach(System.out::println);
		} else {
			System.out.println("Nessuna prenotazione trovata per l'utente " + username);
		}
	}

	// Metodo per cancellare una prenotazione
	private void cancellarePrenotazione(Scanner scanner) {
		System.out.println("\nInserisci l'ID della prenotazione che desideri cancellare:");
		Long prenotazioneId = scanner.nextLong();

		try {
			prenotazioneService.cancellaPrenotazione(prenotazioneId);
			System.out.println("Prenotazione con ID " + prenotazioneId + " cancellata con successo.");
		} catch (Exception e) {
			System.out.println("Errore durante la cancellazione: " + e.getMessage());
		}
	}
}
