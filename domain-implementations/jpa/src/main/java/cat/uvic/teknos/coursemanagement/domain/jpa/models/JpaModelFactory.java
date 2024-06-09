package cat.uvic.teknos.coursemanagement.domain.jpa.models;

import cat.uvic.teknos.coursemanagement.models.*;

public class JpaModelFactory implements ModelFactory {
    @Override
    public Address createAddress() {
        return new JpaAddress();
    }

    @Override
    public Course createCourse() {
        return new JpaCourse();
    }

    @Override
    public cat.uvic.teknos.coursemanagement.models.Student createStudent() {
        return new JpaStudent();
    }

    @Override
    public Genre createGenre() {
        return new JpaGenre();
    }
}
