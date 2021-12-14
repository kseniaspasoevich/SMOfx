package sample.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BufferDto {

    Integer i = 0;
    Integer id = -1;
    Integer idSource;
    Double time;
    Boolean isFree = true;
}
