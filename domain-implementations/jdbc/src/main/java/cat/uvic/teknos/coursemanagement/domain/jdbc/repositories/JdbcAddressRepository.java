package cat.uvic.teknos.coursemanagement.domain.jdbc.repositories;

import cat.uvic.teknos.coursemanagement.domain.jdbc.models.JdbcAddress;
import cat.uvic.teknos.coursemanagement.domain.jdbc.models.JdbcGenre;
import cat.uvic.teknos.coursemanagement.models.Address;
import cat.uvic.teknos.coursemanagement.models.Genre;
import cat.uvic.teknos.coursemanagement.repositories.AddressRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class JdbcAddressRepository implements AddressRepository {
    private final Connection connection;

    public JdbcAddressRepository(Connection connection) {

        this.connection = connection;
    }


    @Override
    public void save(Address model) {
        if (model.getId() <= 0) {
            insert(model);
        } else {
            update(model);
        }
    }
    private void update(Address model) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE ADDRESS SET STREET = ?, ZIP = ? WHERE ID = ?")) {
            statement.setString(1, model.getStreet());
            statement.setString(2, model.getZip());
            statement.setInt(3, model.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void insert(Address model) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO ADDRESS (STREET,ZIP) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getStreet());
            statement.setString(2, model.getZip());
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
    public void delete(Address model) {
        try {
            connection.setAutoCommit(false);
        try (PreparedStatement updateRelation = connection.prepareStatement("UPDATE STUDENT SET ADDRESS = NULL  WHERE ADDRESS = ?")) {
                updateRelation.setInt(1, model.getId());
                updateRelation.executeUpdate();
            }
            try (PreparedStatement deleteAddress = connection.prepareStatement("DELETE FROM ADDRESS WHERE ID = ?")) {
                deleteAddress.setInt(1, model.getId());
                deleteAddress.executeUpdate();
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
    public Address get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM ADDRESS WHERE ID =  ?")) {
        Address address = null;

        statement.setInt(1, id);

        var resultSet = statement.executeQuery();
        if (resultSet.next()) {
            address = new JdbcAddress();
            address.setId(resultSet.getInt("ID"));
            address.setStreet(resultSet.getString("Street"));
            address.setZip(resultSet.getString("ZIP"));
        }

        return address;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }

    @Override
    public Set<Address> getAll() {
        Set<Address> addresses = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM ADDRESS")) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Address address = new JdbcAddress();
                address.setId(resultSet.getInt("ID"));
                address.setStreet(resultSet.getString("Street"));
                address.setZip(resultSet.getString("ZIP"));
                addresses.add(address);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return addresses;    }
}
