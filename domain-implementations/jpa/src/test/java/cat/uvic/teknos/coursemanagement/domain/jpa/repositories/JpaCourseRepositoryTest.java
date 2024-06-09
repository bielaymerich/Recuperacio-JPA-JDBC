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
class JpaCourseRepositoryTest {
    private static RepositoryFactory repositoryFactory;
    private static ModelFactory modelFactory;
    private final Connection connection;

    public JpaCourseRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @BeforeAll
    static void SetUp() {
        repositoryFactory = new JpaRepositoryFactory();
        modelFactory = new JpaModelFactory();
    }

    @Test
    @DisplayName("Given a new COURSE (id = 0), when save is called, then a new record is added to the COURSE table")
    void save() {
        var course = modelFactory.createCourse();
        course.setName("Undefined");
        course.setYear(2024);

        var repository = repositoryFactory.getCourseRepository();

        // Test
        repository.save(course);

        assertTrue(course.getId() > 0);

        DbAssertions.assertThat(connection)
                .table("COURSE")
                .where("ID", course.getId())
                .hasOneLine();
    }

    @Test
    @DisplayName("Given an existing course with modified fields, when save is called, then course table is updated")
    void shouldUpdateACourseTest() throws SQLException {
        var course = modelFactory.createCourse();
        course.setId(1);
        course.setName("Teknos2025");
        course.setYear(2025);

        var repository = repositoryFactory.getCourseRepository();
        repository.save(course);

        //TODO: test database table updated
        DbAssertions.assertThat(connection)
                .table("COURSE")
                .where("ID", course.getId())
                .column("NAME")
                .valueEqual("Teknos2025");
    }

    @Test
    @DisplayName("Given an existing course, when delete is called, then course table is updated")
    void delete() {
        var course = modelFactory.createCourse();
        course.setId(1);

        var repository = repositoryFactory.getCourseRepository();
        repository.delete(course);

        DbAssertions.assertThat(connection)
                .table("COURSE")
                .where("ID", course.getId())
                .doesNotExist();
    }

    @Test
    @DisplayName("Given an existing course, when get is called, then the method return an instance of course")
    void get() {
        var repository = repositoryFactory.getCourseRepository();
        assertNotNull(repository.get(2));
    }

    @Test
    @DisplayName("Given existing courses, when getAll is called, then the method return all the courses")
    void getAll() {
        var repository = repositoryFactory.getCourseRepository();

        var courses = repository.getAll();

        assertNotNull(courses);

        DbAssertions.assertThat(connection)
                .table("COURSE")
                .hasLines(courses.size());
    }
}