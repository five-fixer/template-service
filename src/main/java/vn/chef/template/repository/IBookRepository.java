package vn.chef.template.repository;

import vn.chef.template.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {

    Book findByCode(String code);

}
