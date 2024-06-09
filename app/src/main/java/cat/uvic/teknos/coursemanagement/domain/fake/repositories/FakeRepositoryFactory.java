package cat.uvic.teknos.coursemanagement.domain.fake.repositories;

import cat.uvic.teknos.coursemanagement.repositories.CourseRepository;
import cat.uvic.teknos.coursemanagement.repositories.GenreRepository;
import cat.uvic.teknos.coursemanagement.repositories.RepositoryFactory;
import cat.uvic.teknos.coursemanagement.repositories.StudentRepository;

public class FakeRepositoryFactory implements RepositoryFactory {
    @Override
    public GenreRepository getGenreRepository() {
        return new FakeGenreRepository();
    }

    @Override
    public CourseRepository getCourseRepository() {
        return new FakeCourseRepository();
    }

    @Override
    public StudentRepository getStudentRepository() {
        return new FakeStudentRepository();
    }
}
