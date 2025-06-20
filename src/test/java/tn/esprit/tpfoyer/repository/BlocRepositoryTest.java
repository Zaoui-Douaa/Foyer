package tn.esprit.tpfoyer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.tpfoyer.entity.Bloc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BlocRepositoryTest {

    @Autowired
    private BlocRepository blocRepository;

    @Test
    void testFindAllByFoyerIsNull_shouldReturnBlocsWithoutFoyer() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc Orphelin");
        bloc.setCapaciteBloc(100);
        bloc.setFoyer(null);
        blocRepository.save(bloc);

        List<Bloc> result = blocRepository.findAllByFoyerIsNull();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getNomBloc()).isEqualTo("Bloc Orphelin");
    }
}