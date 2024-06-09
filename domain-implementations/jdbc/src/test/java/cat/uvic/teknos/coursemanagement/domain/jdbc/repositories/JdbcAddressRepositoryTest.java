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
class JdbcAddressRepositoryTest {
    private final ModelFactory modelFactory = new JdbcModelFactory();
    private final Connection connection;

    public JdbcAddressRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @Test
    @DisplayName("Given a new address (id = 0), when save, then a new record is added to the ADDRESS table")
    void shouldInsertNewCourseTest() {
        var address = modelFactory.createAddress();
        address.setStreet("Elm Street, 15º");
        address.setZip("222-222-222");

        var repository = new JdbcAddressRepository(connection);

        // Test
        repository.save(address);

        assertTrue(address.getId() > 0);

        DbAssertions.assertThat(connection)
                .table("ADDRESS")
                .where("ID", address.getId())
                .hasOneLine();
    }

    @Test
    @DisplayName("Given an existing address with modified fields, when save, then ADDRESS table is updated")
    void shouldUpdateAAddressTest() {
        var address = modelFactory.createAddress();
        address.setId(1);
        address.setStreet("5º Avenue");
        address.setZip("234324");

        var repository = new JdbcAddressRepository(connection);
        repository.save(address);

        //TODO: test database table updated
        DbAssertions.assertThat(connection)
                .table("ADDRESS")
                .where("ID", address.getId())
                .column("Street")
                .valueEqual("5º Avenue");
    }

    @Test
    @DisplayName("Given an existing course, when delete is called, then COURSE table is updated")
    void delete() {
        var address = modelFactory.createAddress();
        address.setId(1);

        var repository = new JdbcAddressRepository(connection);
        repository.delete(address);

        DbAssertions.assertThat(connection)
                .table("ADDRESS")
                .where("ID", address.getId())
                .doesNotExist();
    }

    @Test
    @DisplayName("Given an existing genre, when get is called, then the method return an instance of Genre")
    void get() {
        var repository = new JdbcAddressRepository(connection);
        assertNotNull(repository.get(2));
    }

    @Test
    @DisplayName("Given existing genres, when getAll is called, then the method return all the genres")
    void getAll() {
        var repository = new JdbcAddressRepository(connection);
        var addresses = repository.getAll();

        assertNotNull(addresses);

        DbAssertions.assertThat(connection)
                .table("ADDRESS")
                .hasLines(addresses.size());
    }
}