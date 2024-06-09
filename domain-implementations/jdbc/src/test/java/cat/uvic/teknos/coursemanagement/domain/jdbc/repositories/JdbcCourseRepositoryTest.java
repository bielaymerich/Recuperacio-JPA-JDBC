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
class JdbcCourseRepositoryTest {
    private final ModelFactory modelFactory = new JdbcModelFactory();
    private final Connection connection;

    public JdbcCourseRepositoryTest(Connection connection) {
        this.connection = connection;
    }
    @Test
    @DisplayName("Given a new course (id = 0), when save, then a new record is added to the COURSE table")
    void shouldInsertNewCourseTest() {
        var course = modelFactory.createCourse();
        course.setName("M6 UF5");
        course.setYear(2024);

        var repository = new JdbcCourseRepository(connection);

        // Test
        repository.save(course);

        assertTrue(course.getId() > 0);

        DbAssertions.assertThat(connection)
                .table("COURSE")
                .where("ID", course.getId())
                .hasOneLine();
    }

    @Test
    @DisplayName("Given an existing course with modified fields, when save, then COURSE table is updated")
    void shouldUpdateACourseTest() {
        var course = modelFactory.createCourse();
        course.setId(1);
        course.setName("M6 UF6");
        course.setYear(2024);

        var repository = new JdbcCourseRepository(connection);
        repository.save(course);

        //TODO: test database table updated
        DbAssertions.assertThat(connection)
                .table("COURSE")
                .where("ID", course.getId())
                .column("Name")
                .valueEqual("M6 UF6");
    }

    @Test
    @DisplayName("Given an existing course, when delete is called, then COURSE table is updated")
    void delete() {
        var course = modelFactory.createCourse();
        course.setId(1);

        var repository = new JdbcCourseRepository(connection);
        repository.delete(course);

        DbAssertions.assertThat(connection)
                .table("COURSE")
                .where("ID", course.getId())
                .doesNotExist();
    }

    @Test
    @DisplayName("Given an existing genre, when get is called, then the method return an instance of Genre")
    void get() {
        var repository = new JdbcCourseRepository(connection);
        assertNotNull(repository.get(2));
    }

    @Test
    @DisplayName("Given existing genres, when getAll is called, then the method return all the genres")
    void getAll() {
        var repository = new JdbcCourseRepository(connection);
        var courses = repository.getAll();

        assertNotNull(courses);

        DbAssertions.assertThat(connection)
                .table("COURSE")
                .hasLines(courses.size());
    }
}