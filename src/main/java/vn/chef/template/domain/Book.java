package vn.chef.template.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.chef.common.dom.base.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Builder
@Table(name = "BOOKS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "BOOK_ID", insertable = false, updatable = false))
public class Book extends BaseEntity {

    @NotNull
    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "PUBLISHER")
    private String publisher;

    @Column(name = "PUBLIC_DATE")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate publicDate;

}
