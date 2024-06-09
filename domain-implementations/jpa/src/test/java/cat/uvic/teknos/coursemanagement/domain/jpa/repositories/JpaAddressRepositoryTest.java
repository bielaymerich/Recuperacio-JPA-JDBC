package cat.uvic.teknos.coursemanagement.domain.jpa.repositories;

import cat.uvic.teknos.coursemanagement.domain.jpa.models.JpaModelFactory;
import cat.uvic.teknos.coursemanagement.models.ModelFactory;
import cat.uvic.teknos.coursemanagement.repositories.RepositoryFactory;
import com.fcardara.dbtestutils.junit.DbAssertions;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GetConnectionExtension.class)
class JpaAddressRepositoryTest {
    private static RepositoryFactory repositoryFactory;
    private static ModelFactory modelFactory;
    private final Connection connection;

    public JpaAddressRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @BeforeAll
    static void SetUp() {
        repositoryFactory = new JpaRepositoryFactory();
        modelFactory = new JpaModelFactory();
    }

    @Test
    @DisplayName("Given a new address (id = 0), when save is called, then a new record is added to the ADDRESS table")
    void save() {
        var address = modelFactory.createAddress();
        address.setStreet("C/ Vilamirosa, 48");
        address.setZip("08560");

        var repository = repositoryFactory.getAddressRepository();

        // Test
        repository.save(address);

        assertTrue(address.getId() > 0);

        DbAssertions.assertThat(connection)
                .table("ADDRESS")
                .where("ID", address.getId())
                .hasOneLine();
    }

    @Test
    @DisplayName("Given an existing address with modified fields, when save is called, then ADDRESS table is updated")
    void shouldUpdateAADDRESSTest() throws SQLException {
        var address = modelFactory.createAddress();
        address.setId(1);
        address.setStreet("C/ Vilamirosa, 48");
        address.setZip("08560");

        var repository = repositoryFactory.getAddressRepository();
        repository.save(address);

        //TODO: test database table updated
        DbAssertions.assertThat(connection)
                .table("ADDRESS")
                .where("ID", address.getId())
                .column("STREET")
                .valueEqual("C/ Vilamirosa, 48");
    }

    @Test
    @DisplayName("Given an existing address, when delete is called, then ADDRESS table is updated")
    void delete() {
        var address = modelFactory.createAddress();
        address.setId(1);

        var repository = repositoryFactory.getAddressRepository();
        repository.delete(address);

        DbAssertions.assertThat(connection)
                .table("ADDRESS")
                .where("ID", address.getId())
                .doesNotExist();
    }

    @Test
    @DisplayName("Given an existing address, when get is called, then the method return an instance of Address")
    void get() {
        var repository = repositoryFactory.getAddressRepository();
        assertNotNull(repository.get(2));
    }

    @Test
    @DisplayName("Given existing Addresses, when getAll is called, then the method return all the Addresses")
    void getAll() {
        var repository = repositoryFactory.getAddressRepository();

        var addresses = repository.getAll();

        assertNotNull(addresses);

        DbAssertions.assertThat(connection)
                .table("ADDRESS")
                .hasLines(addresses.size());
    }
}