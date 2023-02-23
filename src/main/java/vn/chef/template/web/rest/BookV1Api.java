package vn.chef.template.web.rest;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.chef.template.mapper.IBookMapper;
import vn.chef.template.service.book.IBookService;
import vn.chef.template.web.api.BookV1ApiDelegate;
import vn.chef.template.web.api.model.BookCreateRequest;
import vn.chef.template.web.api.model.BookItemResponse;
import vn.chef.template.web.api.model.BooksResponse;

import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class BookV1Api implements BookV1ApiDelegate {

    private final IBookService bookService;

    @Override
    public ResponseEntity<BookItemResponse> createBook(final BookCreateRequest request) {
        return ResponseEntity.ok(IBookMapper.INSTANCE.toBookItemResponse(bookService.createBook(request)));
    }

    @Override
    public ResponseEntity<BookItemResponse> getBook(Long bookId) {
        return ResponseEntity.ok(IBookMapper.INSTANCE.toBookItemResponse(bookService.getBookByBookId(bookId)));
    }

    @Override
    public ResponseEntity<BooksResponse> getBooks(final Integer limit, final Integer offset) {
        var books = bookService.getBooks(limit, offset);
        return ResponseEntity.ok(new BooksResponse()
                .total(books.getLeft())
                .books(CollectionUtils.emptyIfNull(books.getRight()).stream()
                        .map(IBookMapper.INSTANCE::toBookItemResponse)
                        .collect(Collectors.toList())));
    }

}
