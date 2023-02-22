package vn.chef.template.service.book;

import org.apache.commons.lang3.tuple.Pair;
import vn.chef.template.domain.Book;
import vn.chef.template.web.api.model.BookCreateRequest;

import java.util.List;
import java.util.UUID;

public interface IBookService {

    /**
     * Create a new book
     *
     * @param request {@link BookCreateRequest} the request
     * @return {@link Book} the created book
     */
    Book createBook(BookCreateRequest request);

    /**
     * Get a book by id
     *
     * @param bookId {@link UUID} the book id
     * @return {@link Book} the book
     */
    Book getBookByBookId(UUID bookId);

    /**
     * Get books
     *
     * @param limit  {@link Integer} the limit
     * @param offset {@link Integer} the offset
     * @return {@link Pair} the pair of total and list of books
     */
    Pair<Integer, List<Book>> getBooks(Integer limit, Integer offset);

}
