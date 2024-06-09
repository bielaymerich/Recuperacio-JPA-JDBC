package cat.uvic.teknos.coursemanagement.repositories;


import cat.uvic.teknos.coursemanagement.models.Genre;

import java.util.Set;

public interface GenreRepository extends Repository<Integer, Genre>{

    @Override
    void save(Genre model);

    @Override
    void delete(Genre model);

    @Override
    Genre get(Integer id);

    @Override
    Set<Genre> getAll();
}
