package vn.chef.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.chef.template.domain.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);


}
