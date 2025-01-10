package exercise.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class Post {
    private String id;
    private String title;
    private String body;
}
