package sample.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDto {
    Integer i = -1;
    Integer id = -1;
    Double startTime;
    Double time;
    Integer amount;
    Double workTime;
    Boolean isFree = true;

    Double kUse;
}
