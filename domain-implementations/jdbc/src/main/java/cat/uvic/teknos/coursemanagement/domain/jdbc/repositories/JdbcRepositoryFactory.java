package cat.uvic.teknos.coursemanagement.domain.jdbc.repositories;

import cat.uvic.teknos.coursemanagement.exceptions.RepositoryException;
import cat.uvic.teknos.coursemanagement.repositories.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcRepositoryFactory implements RepositoryFactory {
    private Connection connection;
    public JdbcRepositoryFactory() {
        try {
            var properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/datasource.properties"));

            connection = DriverManager.getConnection(String.format("%s:%s://%s/%s",
                    properties.getProperty("protocol"),
                    properties.getProperty("subprotocol"),
                    properties.getProperty("url"),
                    properties.getProperty("database")), properties.getProperty("user"), properties.getProperty("password"));
        } catch (SQLException e) {
            throw new RepositoryException(e);
        } catch (IOException e) {
            throw new RepositoryException();
        }
    }


    @Override
    public GenreRepository getGenreRepository() {
        return new JdbcGenreRepository(connection);
    }

    @Override
    public CourseRepository getCourseRepository() {
        return new JdbcCourseRepository(connection);
    }
    @Override
    public AddressRepository getAddressRepository() {
        return new JdbcAddressRepository(connection);
    }
    @Override
    public StudentRepository getStudentRepository() {
        return new JdbcStudentRepository(connection) {
        };
    }
}
