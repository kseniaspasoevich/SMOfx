package sample.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceDto {
    Integer i = -1;
    Integer id = -1;
    Integer refuse = -1;
    Integer amount = -1;
    Double time = -1.0;

    Double pRefuse;
    Double averageTimeInSystem;
    Double averageWaitInSystem;
    Double avgProcTime;
}
