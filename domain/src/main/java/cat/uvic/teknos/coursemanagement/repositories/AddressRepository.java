package cat.uvic.teknos.coursemanagement.repositories;


import cat.uvic.teknos.coursemanagement.models.Address;

import java.util.Set;

public interface AddressRepository extends Repository<Integer, Address>{

    @Override
    void save(Address model);

    @Override
    void delete(Address model);

    @Override
    Address get(Integer id);

    @Override
    Set<Address> getAll();
}
