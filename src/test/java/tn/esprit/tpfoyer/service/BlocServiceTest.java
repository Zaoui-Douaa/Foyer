package tn.esprit.tpfoyer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlocServiceTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocServiceImpl blocService;

    private Bloc bloc1;
    private Bloc bloc2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bloc1 = new Bloc();
        bloc1.setIdBloc(1L);
        bloc1.setNomBloc("Bloc A");
        bloc1.setCapaciteBloc(100);

        bloc2 = new Bloc();
        bloc2.setIdBloc(2L);
        bloc2.setNomBloc("Bloc B");
        bloc2.setCapaciteBloc(150);
    }

    @Test
    void testRetrieveAllBlocs_shouldReturnAllBlocs() {
        // Arrange
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc1, bloc2));

        // Act
        List<Bloc> result = blocService.retrieveAllBlocs();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveBloc_shouldReturnBlocById() {
        // Arrange
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc1));

        // Act
        Bloc result = blocService.retrieveBloc(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void testAddBloc_shouldSaveAndReturnBloc() {
        // Arrange
        when(blocRepository.save(bloc1)).thenReturn(bloc1);

        // Act
        Bloc result = blocService.addBloc(bloc1);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.getCapaciteBloc());
        verify(blocRepository, times(1)).save(bloc1);
    }

    @Test
    void testModifyBloc_shouldUpdateBloc() {
        // Arrange
        when(blocRepository.save(bloc1)).thenReturn(bloc1);

        // Act
        Bloc result = blocService.modifyBloc(bloc1);

        // Assert
        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
        verify(blocRepository, times(1)).save(bloc1);
    }

    @Test
    void testRemoveBloc_shouldCallDeleteById() {
        // Act
        blocService.removeBloc(1L);

        // Assert
        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrieveBlocsSelonCapacite_shouldReturnFilteredBlocs() {
        // Arrange
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc1, bloc2));

        // Act
        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(120);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bloc B", result.get(0).getNomBloc());
    }

    @Test
    void testTrouverBlocsParNomEtCap_shouldReturnMatchingBlocs() {
        // Arrange
        when(blocRepository.findAllByNomBlocAndCapaciteBloc("Bloc A", 100L))
                .thenReturn(Arrays.asList(bloc1));

        // Act
        List<Bloc> result = blocService.trouverBlocsParNomEtCap("Bloc A", 100);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getCapaciteBloc());
    }

    @Test
    void testTrouverBlocsSansFoyer_shouldReturnBlocsWithoutFoyer() {
        when(blocRepository.findAllByFoyerIsNull()).thenReturn(Collections.singletonList(bloc1));

        List<Bloc> result = blocService.trouverBlocsSansFoyer();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getFoyer());
        verify(blocRepository, times(1)).findAllByFoyerIsNull();
    }
}