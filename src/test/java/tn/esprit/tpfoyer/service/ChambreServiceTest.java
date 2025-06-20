package tn.esprit.tpfoyer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.repository.ChambreRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChambreServiceTest {

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreServiceImpl chambreService;

    private Chambre chambre1;
    private Chambre chambre2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        chambre1 = new Chambre();
        chambre1.setIdChambre(1L);
        chambre1.setNumeroChambre(101L);
        chambre1.setTypeC(TypeChambre.SIMPLE);

        chambre2 = new Chambre();
        chambre2.setIdChambre(2L);
        chambre2.setNumeroChambre(102L);
        chambre2.setTypeC(TypeChambre.DOUBLE);
    }

    @Test
    void testRetrieveAllChambres_shouldReturnAllChambres() {
        when(chambreRepository.findAll()).thenReturn(Arrays.asList(chambre1, chambre2));

        List<Chambre> result = chambreService.retrieveAllChambres();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveChambre_shouldReturnChambreById() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre1));

        Chambre result = chambreService.retrieveChambre(1L);

        assertNotNull(result);
        assertEquals(101L, result.getNumeroChambre());
        verify(chambreRepository, times(1)).findById(1L);
    }

    @Test
    void testAddChambre_shouldSaveAndReturnChambre() {
        when(chambreRepository.save(chambre1)).thenReturn(chambre1);

        Chambre result = chambreService.addChambre(chambre1);

        assertNotNull(result);
        assertEquals(101L, result.getNumeroChambre());
        verify(chambreRepository, times(1)).save(chambre1);
    }

    @Test
    void testModifyChambre_shouldUpdateChambre() {
        when(chambreRepository.save(chambre1)).thenReturn(chambre1);

        Chambre result = chambreService.modifyChambre(chambre1);

        assertNotNull(result);
        assertEquals(101L, result.getNumeroChambre());
        verify(chambreRepository, times(1)).save(chambre1);
    }

    @Test
    void testRemoveChambre_shouldCallDeleteById() {
        chambreService.removeChambre(1L);
        verify(chambreRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRecupererChambresSelonTyp_shouldReturnFilteredChambres() {
        when(chambreRepository.findAllByTypeC(TypeChambre.SIMPLE)).thenReturn(Collections.singletonList(chambre1));

        List<Chambre> result = chambreService.recupererChambresSelonTyp(TypeChambre.SIMPLE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TypeChambre.SIMPLE, result.get(0).getTypeC());
    }

    @Test
    void testTrouverchambreSelonEtudiant_shouldReturnChambreByEtudiantCin() {
        when(chambreRepository.trouverChselonEt(123456L)).thenReturn(chambre1);

        Chambre result = chambreService.trouverchambreSelonEtudiant(123456L);

        assertNotNull(result);
        assertEquals(101L, result.getNumeroChambre());
        verify(chambreRepository, times(1)).trouverChselonEt(123456L);
    }
}