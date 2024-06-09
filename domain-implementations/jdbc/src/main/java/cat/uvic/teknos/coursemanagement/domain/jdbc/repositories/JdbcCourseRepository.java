package cat.uvic.teknos.coursemanagement.domain.jdbc.repositories;

import cat.uvic.teknos.coursemanagement.domain.jdbc.models.JdbcCourse;
import cat.uvic.teknos.coursemanagement.models.Course;
import cat.uvic.teknos.coursemanagement.repositories.CourseRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcCourseRepository implements CourseRepository {
    private final Connection connection;

    public JdbcCourseRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Course model) {
        if (model.getId() <= 0) {
            insert(model);
        } else {
            update(model);
        }
    }

    private void update(Course model) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE COURSE SET YEAR = ?, NAME = ? WHERE ID = ?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(3, model.getId());
            statement.setInt(1, model.getYear());
            statement.setString(2, model.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insert(Course model) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO COURSE (YEAR,NAME) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, model.getYear());
            statement.setString(2, model.getName());
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                model.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Course model) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteRelation = connection.prepareStatement("DELETE FROM STUDENT_COURSE WHERE COURSE = ?")) {
                deleteRelation.setInt(1, model.getId());
                deleteRelation.executeUpdate();
            }
            try (PreparedStatement deleteCourse = connection.prepareStatement("DELETE FROM COURSE WHERE ID = ?")) {
                deleteCourse.setInt(1, model.getId());
                deleteCourse.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Rollback failed" + ex.getMessage(), ex);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public Course get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM COURSE WHERE ID = ?")) {
            statement.setInt(1, id);
            Course course = null;
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                course = new JdbcCourse();
                course.setId(resultSet.getInt("ID"));
                course.setYear(resultSet.getInt("YEAR"));
                course.setName(resultSet.getString("NAME"));
            }
            return course;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Course> getAll() {
        Set<Course> courses = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM COURSE")) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Course course = new JdbcCourse();
                course.setId(resultSet.getInt("ID"));
                course.setYear(resultSet.getInt("YEAR"));
                course.setName(resultSet.getString("NAME"));
                courses.add(course);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courses;
    }
}