package vn.chef.template.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.chef.template.domain.Book;
import vn.chef.template.web.api.model.BookItemResponse;


@Mapper(componentModel = "spring", uses = {IBookMapper.class})
public interface IBookMapper {

    IBookMapper INSTANCE = Mappers.getMapper(IBookMapper.class);

    @Mapping(target = "bookId", source = "id")
    BookItemResponse toBookItemResponse(Book book);

}
