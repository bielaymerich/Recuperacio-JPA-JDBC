package cat.uvic.teknos.coursemanagement.domain.jpa.repositories;

import cat.uvic.teknos.coursemanagement.domain.jpa.models.JpaModelFactory;
import cat.uvic.teknos.coursemanagement.models.ModelFactory;
import cat.uvic.teknos.coursemanagement.repositories.RepositoryFactory;
import com.fcardara.dbtestutils.junit.DbAssertions;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GetConnectionExtension.class)
class JpaGenreRepositoryTest {
    private static RepositoryFactory repositoryFactory;
    private static ModelFactory modelFactory;
    private final Connection connection;

    public JpaGenreRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @BeforeAll
    static void SetUp() {
        repositoryFactory = new JpaRepositoryFactory();
        modelFactory = new JpaModelFactory();
    }

    @Test
    @DisplayName("Given a new genre (id = 0), when save is called, then a new record is added to the GENRE table")
    void save() {
        var genre = modelFactory.createGenre();
        genre.setDescription("Undefined");

        var repository = repositoryFactory.getGenreRepository();

        // Test
        repository.save(genre);

        assertTrue(genre.getId() > 0);

        DbAssertions.assertThat(connection)
                .table("GENRE")
                .where("ID", genre.getId())
                .hasOneLine();
    }

    @Test
    @DisplayName("Given an existing genre with modified fields, when save is called, then GENRE table is updated")
    void shouldUpdateAGenreTest() throws SQLException {
        var genre = modelFactory.createGenre();
        genre.setId(1);
        genre.setDescription("Super Male");

        var repository = repositoryFactory.getGenreRepository();
        repository.save(genre);

        //TODO: test database table updated
        DbAssertions.assertThat(connection)
                .table("GENRE")
                .where("ID", genre.getId())
                .column("DESCRIPTION")
                .valueEqual("Super Male");
    }

    @Test
    @DisplayName("Given an existing genre, when delete is called, then GENRE table is updated")
    void delete() {
        var genre = modelFactory.createGenre();
        genre.setId(1);

        var repository = repositoryFactory.getGenreRepository();
        repository.delete(genre);

        DbAssertions.assertThat(connection)
                .table("GENRE")
                .where("ID", genre.getId())
                .doesNotExist();
    }

    @Test
    @DisplayName("Given an existing genre, when get is called, then the method return an instance of Genre")
    void get() {
        var repository = repositoryFactory.getGenreRepository();
        assertNotNull(repository.get(2));
    }

    @Test
    @DisplayName("Given existing genres, when getAll is called, then the method return all the genres")
    void getAll() {
        var repository = repositoryFactory.getGenreRepository();

        var genres = repository.getAll();

        assertNotNull(genres);

        DbAssertions.assertThat(connection)
                .table("GENRE")
                .hasLines(genres.size());
    }
}