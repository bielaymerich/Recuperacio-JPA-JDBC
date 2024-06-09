package cat.uvic.teknos.coursemanagement.domain.jdbc.repositories;

import cat.uvic.teknos.coursemanagement.domain.jdbc.models.JdbcStudent;
import cat.uvic.teknos.coursemanagement.models.Address;
import cat.uvic.teknos.coursemanagement.models.Course;
import cat.uvic.teknos.coursemanagement.models.Genre;
import cat.uvic.teknos.coursemanagement.models.Student;
import cat.uvic.teknos.coursemanagement.repositories.StudentRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcStudentRepository implements StudentRepository {
    private final Connection connection;

    public JdbcStudentRepository(Connection connection) {

        this.connection = connection;
    }

    @Override
    public void save(Student model) {
        if (model.getId() <= 0) {
            insert(model);
        } else {
            update(model);
        }
    }
    private void update(Student model) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE STUDENT SET  BORN_ON = ?, GENRE = ?, FIRST_NAME = ?, LAST_NAME = ? WHERE ID = ?")) {

            statement.setDate(1, Date.valueOf(model.getBornOn()));
            statement.setInt(2, model.getGenre().getId());
            statement.setString(3, model.getFirstName());
            statement.setString(4, model.getLastName());
            statement.setInt(5, model.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insert(Student model) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO STUDENT ( BORN_ON, GENRE, FIRST_NAME, LAST_NAME) VALUES ( ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            statement.setDate(1, Date.valueOf(model.getBornOn()));
            statement.setInt(2, model.getGenre().getId());
            statement.setString(3, model.getFirstName());
            statement.setString(4, model.getLastName());
            statement.executeUpdate();


            var keys =  statement.getGeneratedKeys();

            if (keys.next()) {
                model.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Student model) {
        try {
            // Start a transaction
            connection.setAutoCommit(false);

            // Update students to remove the foreign key reference to the genre
            try (PreparedStatement deleteRelation = connection.prepareStatement("DELETE FROM STUDENT_COURSE WHERE STUDENT = ?")) {
                deleteRelation.setInt(1, model.getId());
                deleteRelation.executeUpdate();
            }

            // Now delete the genre
            try (PreparedStatement deleteStudent = connection.prepareStatement("DELETE FROM STUDENT WHERE ID = ?")) {
                deleteStudent.setInt(1, model.getId());
                deleteStudent.executeUpdate();
            }

            // Commit the transaction
            connection.commit();
        } catch (SQLException e) {
            try {
                // Rollback the transaction in case of an error
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Rollback failed: " + rollbackEx.getMessage(), rollbackEx);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                // Restore auto-commit mode
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public Student get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM STUDENT WHERE ID =  ?")) {
            Student student = null;

            statement.setInt(1, id);

            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                student = new JdbcStudent();
                student.setId(resultSet.getInt("ID"));
                Address address = new JdbcAddressRepository(connection).get(resultSet.getInt("Address"));
                Genre genre = new JdbcGenreRepository(connection).get(resultSet.getInt("Genre"));
                student.setAddress(address);
                student.setGenre(genre);
                if (resultSet.getDate("BORN_ON") != null) {
                    student.setBornOn(resultSet.getDate("Born_on").toLocalDate());}
                student.setFirstName(resultSet.getString("First_name"));
                student.setLastName(resultSet.getString("Last_name"));
                Set<Course> courses = new HashSet<>();
                try (PreparedStatement statementCourse = connection.prepareStatement("SELECT * FROM STUDENT_COURSE WHERE STUDENT = ?")){
                    statementCourse.setInt(1, student.getId());
                    var resultSetCourse = statementCourse.executeQuery();
                    while (resultSetCourse.next()) {
                        Course course = new JdbcCourseRepository(connection).get(resultSetCourse.getInt("COURSE"));
                        courses.add(course);
                    }
                }
                student.setCourses(courses);
            }

            return student;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Student> getAll() {
        Set<Student> students = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM STUDENT")) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Student student = new JdbcStudent();
                student.setId(resultSet.getInt("ID"));
                Address address = new JdbcAddressRepository(connection).get(resultSet.getInt("Address"));
                Genre genre = new JdbcGenreRepository(connection).get(resultSet.getInt("Genre"));
                student.setAddress(address);
                student.setGenre(genre);
                if (resultSet.getDate("BORN_ON") != null) {
                    student.setBornOn(resultSet.getDate("Born_on").toLocalDate());}
                student.setFirstName(resultSet.getString("First_name"));
                student.setLastName(resultSet.getString("Last_name"));
                Set<Course> courses = new HashSet<>();
                try (PreparedStatement statementCourse = connection.prepareStatement("SELECT * FROM STUDENT_COURSE WHERE STUDENT = ?")) {
                    statementCourse.setInt(1, student.getId());
                    var resultSetCourse = statementCourse.executeQuery();
                    while (resultSetCourse.next()) {
                        Course course = new JdbcCourseRepository(connection).get(resultSetCourse.getInt("COURSE"));
                        courses.add(course);
                    }
                }
                student.setCourses(courses);
                students.add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
    }
    }