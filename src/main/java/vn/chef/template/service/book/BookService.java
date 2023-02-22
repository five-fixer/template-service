package vn.chef.template.service.book;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.chef.common.exception.ChefValidationException;
import vn.chef.common.exception.DataNotFoundException;
import vn.chef.template.domain.Book;
import vn.chef.template.domain.QBook;
import vn.chef.template.repository.IBookRepository;
import vn.chef.template.web.api.model.BookCreateRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class BookService implements IBookService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final IBookRepository bookRepository;

    @Override
    public Book createBook(BookCreateRequest request) {
        var book = bookRepository.findByCode(request.getCode());
        if (ObjectUtils.isNotEmpty(book)) {
            throw new ChefValidationException(String.format("Book with code [%s] already existed", request.getCode()),
                    HttpStatus.CONFLICT.value());
        }

        return bookRepository.save(Book.builder()
                .title(request.getTitle())
                .code(request.getCode())
                .publicDate(request.getPublicDate())
                .publisher(request.getPublisher())
                .build());
    }

    @Override
    public Book getBookByBookId(UUID bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Book not found"));
    }

    @Override
    public Pair<Integer, List<Book>> getBooks(Integer limit, Integer offset) {
        JPAQuery<Book> query = new JPAQuery<Book>()
            .clone(entityManager)
            .from(QBook.book);
        long total = query.fetchCount();

        query.offset(offset);
        query.limit(limit);

        return Pair.of((int) total, query.fetch());
    }

}
