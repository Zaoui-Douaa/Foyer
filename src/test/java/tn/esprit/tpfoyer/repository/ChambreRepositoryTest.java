package tn.esprit.tpfoyer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChambreRepositoryTest {

    @Autowired
    private ChambreRepository chambreRepository;

    @Test
    void testFindAllByTypeC_shouldReturnChambresOfTypeSimple() {
        Chambre c1 = new Chambre();
        c1.setNumeroChambre(101L);
        c1.setTypeC(TypeChambre.SIMPLE);

        Chambre c2 = new Chambre();
        c2.setNumeroChambre(102L);
        c2.setTypeC(TypeChambre.SIMPLE);

        chambreRepository.save(c1);
        chambreRepository.save(c2);

        List<Chambre> result = chambreRepository.findAllByTypeC(TypeChambre.SIMPLE);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTypeC()).isEqualTo(TypeChambre.SIMPLE);
    }

    @Test
    void testFindByNumeroChambre() {
        Chambre c = new Chambre();
        c.setNumeroChambre(101L);
        c.setTypeC(TypeChambre.SIMPLE);
        chambreRepository.save(c);

        Chambre result = chambreRepository.findChambreByNumeroChambre(101L);

        assertThat(result).isNotNull();
        assertThat(result.getNumeroChambre()).isEqualTo(101L);
    }
}