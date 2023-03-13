package vn.chef.template.domain;

import lombok.*;
import vn.chef.common.dom.base.BaseEntity;
import vn.chef.template.domain.enumtype.LoginMethodEnum;
import vn.chef.template.domain.enumtype.UserRole;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Builder
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "USER_ID", insertable = false, updatable = false))
public class User extends BaseEntity {
    @Size(max = 40)
    private String email;

    @Size(max = 100)
    private String password;
    @NotNull
    private String userRole;
    @NotNull
    @Enumerated(EnumType.STRING)
    private LoginMethodEnum provider;

    @Size(max = 1000)
    private String avatar;
}

