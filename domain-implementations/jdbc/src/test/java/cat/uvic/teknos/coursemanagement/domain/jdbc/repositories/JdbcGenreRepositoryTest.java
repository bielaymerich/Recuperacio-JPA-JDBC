package cat.uvic.teknos.coursemanagement.domain.jdbc.repositories;

import cat.uvic.teknos.coursemanagement.models.ModelFactory;
import cat.uvic.teknos.coursemanagement.domain.jdbc.models.JdbcModelFactory;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.DbAssertions;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})
class JdbcGenreRepositoryTest {
    private final ModelFactory modelFactory = new JdbcModelFactory();
    private final Connection connection;

    public JdbcGenreRepositoryTest(Connection connection) {
        this.connection = connection;
    }
    @Test
    @DisplayName("Given a new genre (id = 0), when save, then a new record is added to the GENRE table")
    void shouldInsertNewGenreTest() {
        var genre = modelFactory.createGenre();
        genre.setDescription("Undefined");

        var repository = new JdbcGenreRepository(connection);

        // Test
        repository.save(genre);

        assertTrue(genre.getId() > 0);

        DbAssertions.assertThat(connection)
                .table("GENRE")
                .where("ID", genre.getId())
                .hasOneLine();
    }

    @Test
    @DisplayName("Given an existing genre with modified fields, when save, then GENRE table is updated")
    void shouldUpdateAGenreTest() {
        var genre = modelFactory.createGenre();
        genre.setId(1);
        genre.setDescription("Super Male");

        var repository = new JdbcGenreRepository(connection);
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

        var repository = new JdbcGenreRepository(connection);
        repository.delete(genre);

        DbAssertions.assertThat(connection)
                .table("GENRE")
                .where("ID", genre.getId())
                .doesNotExist();
    }

    @Test
    @DisplayName("Given an existing genre, when get is called, then the method return an instance of Genre")
    void get() {
        var repository = new JdbcGenreRepository(connection);
        assertNotNull(repository.get(2));
    }

    @Test
    @DisplayName("Given existing genres, when getAll is called, then the method return all the genres")
    void getAll() {
        var repository = new JdbcGenreRepository(connection);
        var genres = repository.getAll();

        assertNotNull(genres);

        DbAssertions.assertThat(connection)
                .table("GENRE")
                .hasLines(genres.size());
    }
}