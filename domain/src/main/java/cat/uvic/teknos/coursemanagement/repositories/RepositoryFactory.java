package cat.uvic.teknos.coursemanagement.repositories;

public interface RepositoryFactory {
    GenreRepository getGenreRepository();
    CourseRepository  getCourseRepository();
    StudentRepository getStudentRepository();
    AddressRepository getAddressRepository();
}
