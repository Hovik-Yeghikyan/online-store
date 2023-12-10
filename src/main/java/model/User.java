package model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import types.UserType;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class User implements Serializable {

    private String id;
    private String name;
    private String email;
    private String password;
    private UserType type;


}
