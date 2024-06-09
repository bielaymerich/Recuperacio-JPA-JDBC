package cat.uvic.teknos.coursemanagement.domain.jdbc.repositories;

import cat.uvic.teknos.coursemanagement.domain.jdbc.models.JdbcGenre;
import cat.uvic.teknos.coursemanagement.models.Genre;
import cat.uvic.teknos.coursemanagement.repositories.GenreRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class JdbcGenreRepository implements GenreRepository {
    private final Connection connection;

    public JdbcGenreRepository(Connection connection) {

        this.connection = connection;
    }

    @Override
    public void save(Genre model) {
        if (model.getId() <= 0) {
            insert(model);
        } else {
            update(model);
        }
    }

    private void update(Genre model) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE GENRE SET DESCRIPTION = ? WHERE ID = ?")) {
            statement.setString(1, model.getDescription());
            statement.setInt(2, model.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insert(Genre model) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO GENRE (DESCRIPTION) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getDescription());

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
    public void delete(Genre model) {
        try {
            // Start a transaction
            connection.setAutoCommit(false);

            // Update students to remove the foreign key reference to the genre
            try (PreparedStatement updateStudents = connection.prepareStatement("UPDATE STUDENT SET GENRE = NULL WHERE GENRE = ?")) {
                updateStudents.setInt(1, model.getId());
                updateStudents.executeUpdate();
            }

            // Now delete the genre
            try (PreparedStatement deleteGenre = connection.prepareStatement("DELETE FROM GENRE WHERE ID = ?")) {
                deleteGenre.setInt(1, model.getId());
                deleteGenre.executeUpdate();
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
    public Genre get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM GENRE WHERE ID =  ?")) {
            Genre genre = null;

            statement.setInt(1, id);

            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                genre = new JdbcGenre();
                genre.setId(resultSet.getInt("ID"));
                genre.setDescription(resultSet.getString("Description"));
            }

            return genre;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Genre> getAll() {
        Set<Genre> genres = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM GENRE")) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Genre genre = new JdbcGenre();
                genre.setId(resultSet.getInt("ID"));
                genre.setDescription(resultSet.getString("Description"));
                genres.add(genre);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return genres;
    }
}
