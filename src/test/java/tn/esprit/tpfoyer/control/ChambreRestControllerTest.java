package tn.esprit.tpfoyer.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.service.IChambreService;

import java.util.Arrays;
import java.util.List;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ChambreRestController.class)
class ChambreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IChambreService chambreService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAllChambres_shouldReturnListOfChambres() throws Exception {
        Chambre c1 = new Chambre();
        c1.setIdChambre(1L);
        c1.setNumeroChambre(101L);
        c1.setTypeC(TypeChambre.SIMPLE);

        Chambre c2 = new Chambre();
        c2.setIdChambre(2L);
        c2.setNumeroChambre(102L);
        c2.setTypeC(TypeChambre.DOUBLE);

        List<Chambre> listChambres = Arrays.asList(c1, c2);

        when(chambreService.retrieveAllChambres()).thenReturn(listChambres);

        mockMvc.perform(get("/chambre/retrieve-all-chambres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].numeroChambre").value(101));
    }

    @Test
    void testAddChambre_shouldReturnCreatedChambre() throws Exception {
        Chambre c = new Chambre();
        c.setIdChambre(1L);
        c.setNumeroChambre(101L);
        c.setTypeC(TypeChambre.SIMPLE);

        when(chambreService.addChambre(any(Chambre.class))).thenReturn(c);

        mockMvc.perform(post("/chambre/add-chambre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroChambre").value(101));
    }

    @Test
    void testFindChambreByType() throws Exception {
        Chambre c = new Chambre();
        c.setIdChambre(1L);
        c.setTypeC(TypeChambre.SIMPLE);

        when(chambreService.recupererChambresSelonTyp(TypeChambre.SIMPLE)).thenReturn(Collections.singletonList(c));

        mockMvc.perform(get("/chambre/trouver-chambres-selon-typ/SIMPLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].typeC").value("SIMPLE"));
    }

    @Test
    void testFindChambreByEtudiantCin() throws Exception {
        Chambre c = new Chambre();
        c.setIdChambre(1L);
        c.setNumeroChambre(101L);

        when(chambreService.trouverchambreSelonEtudiant(123456L)).thenReturn(c);

        mockMvc.perform(get("/chambre/trouver-chambre-selon-etudiant/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idChambre").value(1L));
    }
}