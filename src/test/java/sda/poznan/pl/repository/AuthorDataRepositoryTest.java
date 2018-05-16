package sda.poznan.pl.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sda.poznan.pl.domain.AuthorData;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorDataRepositoryTest {

    @Autowired
    private AuthorDataRepository authorDataRepository;

    @Test
    public void shouldAddDataToRepository() {
        //given
        AuthorData authorData = new AuthorData();
        authorData.setDate(LocalDate.now());
        authorData.setEmail("email@test.pl");
        authorData.setName("name");
        //when
        authorDataRepository.save(authorData);
        //then
        List<AuthorData> underTest = authorDataRepository.findAll();
        System.out.println(underTest);
        AuthorData author = underTest.get(0);
        assertThat(underTest.size()).isEqualTo(1);
        assertThat(author.getDate()).isEqualTo(authorData.getDate());
        assertThat(author.getId()).isNotNull();
    }

}