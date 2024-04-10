package com.edison.springboot.jpa.springbootjparelationship.repositories;

import org.springframework.data.repository.CrudRepository;

import com.edison.springboot.jpa.springbootjparelationship.entities.Course;

public interface CourseRepository extends CrudRepository<Course, Long> {

}
