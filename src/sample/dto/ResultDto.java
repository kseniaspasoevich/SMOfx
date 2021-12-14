package sample.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResultDto {
    List<BufferDto> bufferDtoList;
    List<DeviceDto> deviceDtoList;
    List<SourceDto> sourceDtoList;

    Double deviceAlpha;
    Double deviceBeta;
    Double sourceLam;
    Integer totalAmount;
    Integer obr;

    Integer k;
    Double pRefuse;
    Double kUse;
}
