package com.luv2code.springboot.todos.repository;

import com.luv2code.springboot.todos.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query("select count(u) from User u join u.authorities authority where authority.authority = 'ROLE_ADMIN'")
    long countAdminUsers();
}
