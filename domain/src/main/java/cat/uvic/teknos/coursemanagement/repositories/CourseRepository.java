package cat.uvic.teknos.coursemanagement.repositories;

import cat.uvic.teknos.coursemanagement.models.Course;

import java.util.Set;

public interface CourseRepository extends Repository<Integer, Course>{

    @Override
    void save(Course model);

    @Override
    void delete(Course model);

    @Override
    Course get(Integer id);

    @Override
    Set<Course> getAll();
}
