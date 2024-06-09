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
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GetConnectionExtension.class)
class JpaStudentRepositoryTest {
    private static RepositoryFactory repositoryFactory;
    private static ModelFactory modelFactory;
    private final Connection connection;

    public JpaStudentRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @BeforeAll
    static void SetUp() {
        repositoryFactory = new JpaRepositoryFactory();
        modelFactory = new JpaModelFactory();
    }

    @Test
    @DisplayName("Given a new student (id = 0), when save is called, then a new record is added to the STUDENT table")
    void save() {
        var student = modelFactory.createStudent();

        var address = modelFactory.createAddress();
        address.setZip("08553");
        address.setStreet("C/ Perelada. 85");
        var repositoryA = repositoryFactory.getAddressRepository();
        repositoryA.save(address);

        var repositoryG = repositoryFactory.getGenreRepository();
        var genre = repositoryG.get(1);
        student.setFirstName("Jordi");
        student.setLastName("Forcada");
        student.setAddress(address);
        student.setGenre(genre);
        student.setBornOn(LocalDate.of(1997, 10, 20));
        var repository = repositoryFactory.getStudentRepository();

        // Test
        repository.save(student);

        assertTrue(student.getId() > 0);

        DbAssertions.assertThat(connection)
                .table("STUDENT")
                .where("ID", student.getId())
                .hasOneLine();
    }

    @Test
    @DisplayName("Given an existing student with modified fields, when save is called, then STUDENT table is updated")
    void shouldUpdateAStudentTest() throws SQLException {
        var student = modelFactory.createStudent();

        var address = modelFactory.createAddress();
        address.setZip("08554");
        address.setStreet("C/ Osona, 45 ");
        var repositoryA = repositoryFactory.getAddressRepository();
        repositoryA.save(address);

        var repositoryG = repositoryFactory.getGenreRepository();
        var genre = repositoryG.get(1);
        student.setId(1);
        student.setFirstName("Adria");
        student.setLastName("Coma");
        student.setAddress(address);
        student.setGenre(genre);
        student.setBornOn(LocalDate.of(2000, 8, 25));
        var repository = repositoryFactory.getStudentRepository();

        // Test
        repository.save(student);

        //TODO: test database table updated
        DbAssertions.assertThat(connection)
                .table("STUDENT")
                .where("ID", student.getId())
                .column("FIRST_NAME")
                .valueEqual("Adria");
    }

    @Test
    @DisplayName("Given an existing student, when delete is called, then STUDENT table is updated")
    void delete() {
        var student = modelFactory.createStudent();
        student.setId(1);

        var repository = repositoryFactory.getStudentRepository();
        repository.delete(student);

        DbAssertions.assertThat(connection)
                .table("STUDENT")
                .where("ID", student.getId())
                .doesNotExist();
    }
    @Test
    @DisplayName("Given an existing student, when get is called, then the method return an instance of Student")
    void get() {
        var repository = repositoryFactory.getStudentRepository();
        assertNotNull(repository.get(2));
    }
    @Test
    @DisplayName("Given existing students, when getAll is called, then the method return all the students")
    void getAll() {
        var repository = repositoryFactory.getStudentRepository();

        var students = repository.getAll();

        assertNotNull(students);

        DbAssertions.assertThat(connection)
                .table("STUDENT")
                .hasLines(students.size());
    }

}