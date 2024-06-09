package cat.uvic.teknos.coursemanagement.domain.fake.models;

import cat.uvic.teknos.coursemanagement.models.*;

public class FakeModelFactory implements ModelFactory {

    @Override
    public Address createAddress() {
        return new FakeAddress();
    }

    @Override
    public Course createCourse() {
        return new FakeCourse();
    }

    @Override
    public Student createStudent() {
        return  new FakeStudent();
    }

    @Override
    public Genre createGenre() {
        return new FakeGenre();
    }
}
