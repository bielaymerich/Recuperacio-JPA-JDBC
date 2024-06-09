package cat.uvic.teknos.coursemanagement.models;

public interface ModelFactory {
    Address createAddress();
    Course createCourse();
    Student createStudent();
    Genre createGenre();
}
