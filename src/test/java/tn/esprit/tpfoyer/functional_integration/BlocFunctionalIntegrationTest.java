/*
 * Teste l'ensemble de la chaîne de traitement des requêtes HTTP 
 * liées à l’entité Bloc, depuis le contrôleur REST jusqu’à la base de données.
 * 
 * Ce test vérifie que :
 *      Le serveur web démarre correctement.
 *      Les interactions avec la base de données via Hibernate/JPA
 *      Le bon fonctionnement des endpoints REST
 *      L’intégration entre les couches est correcte (contrôleur, service, repository).
*/

package tn.esprit.tpfoyer.functional_integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Lance l'application Spring Boot sur un port aléatoire pour éviter les conflits.
 * Utilise @ActiveProfiles("test") pour charger une configuration spécifique aux tests si nécessaire.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Optionnel, si tu veux charger une config spécifique
public class BlocFunctionalIntegrationTest {

    /**
     * Port local attribué lors du démarrage de l’application.
     * Permet de construire les URLs dynamiquement.
     */
    @LocalServerPort
    private int port;

    /**
     * Client REST utilisé pour simuler des appels HTTP vers l’API.
     */
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Repository injecté pour manipuler la base de données pendant les tests.
     */
    @Autowired
    private BlocRepository blocRepository;

    /**
     * Méthode utilitaire pour construire les URLs des endpoints.
     * @return URL racine de l'API Bloc
     */
    private String getRootUrl() {
        return "http://localhost:" + port + "/bloc";
    }

    /**
     * Teste l'endpoint GET /retrieve-all-blocs
     * Vérifie que tous les blocs sont bien récupérés.
     */
    @Test
    void testRetrieveAllBlocs_shouldReturnListOfBlocs() throws Exception {
        // Arrange : création de deux blocs et sauvegarde en base
        Bloc bloc1 = new Bloc();
        bloc1.setNomBloc("Bloc X");
        bloc1.setCapaciteBloc(100);

        Bloc bloc2 = new Bloc();
        bloc2.setNomBloc("Bloc Y");
        bloc2.setCapaciteBloc(150);

        blocRepository.save(bloc1);
        blocRepository.save(bloc2);

        // Act : appel à l'endpoint REST
        ResponseEntity<Bloc[]> response = restTemplate.getForEntity(getRootUrl() + "/retrieve-all-blocs", Bloc[].class);

        // Assert : vérifications des résultats
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Bloc> blocs = Arrays.asList(response.getBody());
        assertThat(blocs).hasSizeGreaterThan(1);
        assertThat(blocs).extracting(Bloc::getNomBloc).contains("Bloc X", "Bloc Y");
    }

    /**
     * Teste l'endpoint POST /add-bloc et GET /retrieve-bloc/{id}
     * Vérifie que l’ajout d’un bloc via l’API est persisté et qu’on peut le récupérer par son ID.
     */
    @Test
    void testAddAndRetrieveBlocById_shouldWork() {
        // Arrange : création d’un nouveau bloc
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc Z");
        bloc.setCapaciteBloc(200);

        // Act : envoi d’une requête POST pour créer le bloc
        ResponseEntity<Bloc> postResponse = restTemplate.postForEntity(getRootUrl() + "/add-bloc", bloc, Bloc.class);
        Bloc created = postResponse.getBody();

        // Act : récupération du bloc créé via son ID
        ResponseEntity<Bloc> getResponse = restTemplate.getForEntity(getRootUrl() + "/retrieve-bloc/" + created.getIdBloc(), Bloc.class);

        // Assert : vérifie que la création et la récupération ont réussi
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getIdBloc()).isNotNull();
        assertThat(getResponse.getBody().getNomBloc()).isEqualTo("Bloc Z");
    }
}