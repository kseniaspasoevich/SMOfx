package sample;

import sample.dto.BufferDto;
import sample.dto.DeviceDto;
import sample.dto.ResultDto;
import sample.dto.SourceDto;

import java.util.ArrayList;
import java.util.List;

public final class PrintData {

    private static PrintData instance;
    private DeviceManager devMan;
    private SourceManager sourceManager;
    private Buffer buffer;

    private PrintData (DeviceManager devMan, SourceManager sourceManager, Buffer buffer) {
        this.devMan = devMan;
        this.sourceManager = sourceManager;
        this.buffer = buffer;
    }

    public static PrintData getInstance(DeviceManager devMan, SourceManager sourceManager, Buffer buffer) {
        if (instance == null) {
            instance = new PrintData(devMan, sourceManager, buffer);
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public ResultDto printTotal() { //автоматический режим, сводная таблица результатов

        System.out.println("Sources");
        System.out.println("  No. |  id  |     time        |refuse| total|  P Refuse  |avg T in Sys| avg Wait T | avg Proc T | ");
        int totalAmount = 0;
        int obr = -1;

        List<SourceDto> sourceDtoList = new ArrayList<>();
        for (int i = 0; i < sourceManager.getSourceAmount(); i++) {

            int id = 0;
            int refuse = 0;
            int amount = 0;
            double time = 0;
            double pRefuse = 0;
            double averageTimeInSystem = 0;
            double averageWaitInSystem = 0;
            double avgProcTime = 0;


            if (sourceManager.getSource()[i] != null){
                id = sourceManager.getSource()[i].getId();
                time = sourceManager.getSource()[i].getTime();
                refuse = sourceManager.getSource()[i].getRefuse();
                amount = sourceManager.getSource()[i].getRequestCounter();
                if (amount - refuse != 0) {
                    pRefuse = refuse * 1.0 / amount;
                    averageTimeInSystem = sourceManager.getSource()[i].getTimeInSystem() / (amount - refuse);
                    averageWaitInSystem = sourceManager.getSource()[i].getWaitTime() / (amount - refuse);
                    avgProcTime = sourceManager.getSource()[i].getProcessingTime() / (amount - refuse);
                }
                totalAmount += amount;
                obr += amount - refuse;
            }

            System.out.println(String.format(
                    "%1$7d | %2$4d | %3$15.8f | %4$4d | %5$4d | %6$10.5f | %7$10.5f | %8$10.5f | %9$10.5f |",
                    i+1, id+1, time, refuse, amount, pRefuse, averageTimeInSystem, averageWaitInSystem, avgProcTime));

            sourceDtoList.add(SourceDto.builder().i(i+1).id(id+1).time(time).refuse(refuse).amount(amount)
                    .pRefuse(pRefuse).averageTimeInSystem(averageTimeInSystem).averageWaitInSystem(averageWaitInSystem).avgProcTime(avgProcTime).build());
        }

        System.out.println("Devices: ");
        System.out.println("  No. |    K use    |");

        List<DeviceDto> deviceDtoList = new ArrayList<>();
        for (int i = 0; i < devMan.getDeviceAmount(); i++) {

               double procTime = devMan.getDevices()[i].getProcessingTime();
               double time = devMan.getMaxTime();
               double kUse = procTime/time;
                System.out.println(String.format(
                        "%1$7d | %2$10.5f  |",
                        i + 1, kUse));

                deviceDtoList.add(DeviceDto.builder().i(i).kUse(kUse).build());
        }

        System.out.println("Device Alpha " + Simulation.getInstance().getDeviceAlpha());
        System.out.println("Device Beta " + Simulation.getInstance().getDeviceBeta());
        System.out.println("Source Lam " + Simulation.getInstance().getSourceLam());
        System.out.println("Total Amount " + totalAmount);
        System.out.println("Total obr " + obr);

        return ResultDto.builder().sourceDtoList(sourceDtoList).deviceDtoList(deviceDtoList)
                .deviceAlpha(SimulationFx.getInstance().getDeviceAlpha())
                .deviceBeta(SimulationFx.getInstance().getDeviceBeta())
                .sourceLam(SimulationFx.getInstance().getSourceLam())
                .totalAmount(totalAmount)
                .obr(obr).build();
    }

    public ResultDto printStep(){ //пошаговый режим: календарь событий

        System.out.println();
        System.out.println("Sources:");
        System.out.println("  No.  |  id  |     time        |refuse| total| ");
        List<SourceDto> sourceDtoList = new ArrayList<>();
        for (int i = 0; i < sourceManager.getSourceAmount(); i++) {

            int id = -1;
            int refuse = -1;
            int amount = -1;
            double time = -1;

            if (sourceManager.getSource()[i] != null){
                id = sourceManager.getSource()[i].getId();
                time = sourceManager.getSource()[i].getTime();
                refuse = sourceManager.getSource()[i].getRefuse();
                amount = sourceManager.getSource()[i].getRequestCounter();
            }

            sourceDtoList.add(SourceDto.builder().i(i*1).id(id*1).time(time).refuse(refuse).amount(amount).build());
            System.out.println(String.format(
                    "%1$7d | %2$4d | %3$15.8f | %4$4d | %5$4d |",
                    i+1, id+1, time, refuse, amount));
        }
        System.out.println("Devices:");
        System.out.println("  No. | req_id | start time |  end time  |amount|  Work Time | ");
        List<DeviceDto> deviceDtoList = new ArrayList<>();

        for (int i = 0; i < devMan.getDeviceAmount(); i++) {

            int id = -1;
            double time = -1;
            double startTime = -1;
            int amount = -1;
            double workTime = -1;

            if (devMan.getDevices()[i].getRequest() != null){
                id = devMan.getDevices()[i].getRequest().getId();
                startTime = devMan.getDevices()[i].getStartTime();
                time = devMan.getDevices()[i].getTime();
                amount = devMan.getDevices()[i].getRequestAmount();
                workTime = devMan.getDevices()[i].getProcessingTime();
            }
            if (id > 0) {
                System.out.println(String.format(
                        "%1$7d | %2$4d | %3$10.5f | %4$10.5f | %5$4d | %6$10.5f |",
                        i + 1, id, startTime, time, amount, workTime));
                deviceDtoList.add(DeviceDto.builder().i(i+1).id(id).startTime(startTime).time(time).amount(amount).workTime(workTime).isFree(false).build());
            } else {
                DeviceDto deviceDto = new DeviceDto();
                deviceDto.setId(id);
                deviceDto.setI(i+1);
                deviceDto.setIsFree(true);
                deviceDtoList.add(deviceDto);
                System.out.println(String.format("%1$7d | FREE",i + 1));
            }
        }

        System.out.println("Buffer");
        System.out.println("  No. | Time |  Source | Req_id  |");
        List<BufferDto> bufferDtoList = new ArrayList<>();
        for (int i = 0; i < buffer.getSize_(); i++) {

            int id = -1;
            int idSource=0;
            double time = -1;

            if (buffer.getBuffer_()[i] != null){
                id = buffer.getBuffer_()[i].getId();
                idSource = sourceManager.getSource()[i].getId();
                time = buffer.getBuffer_()[i].getTime();
            }

            if (id > 0) {
                System.out.println(String.format(
                        "%1$7d | %2$15.8f | %3$8d | %4$8d |",
                        i + 1, time, idSource, id));
                bufferDtoList.add(BufferDto.builder().i(i+1).isFree(false).idSource(idSource).time(time).id(id).build());
            } else {
                System.out.println(String.format("%1$7d | FREE",i + 1));
                BufferDto bufferDto = new BufferDto();
                bufferDto.setId(idSource);
                bufferDto.setI(i+1);
                bufferDto.setIsFree(true);
                bufferDtoList.add(bufferDto);
            }

        }
        ResultDto resultDto = ResultDto.builder().deviceDtoList(deviceDtoList).bufferDtoList(bufferDtoList).sourceDtoList(sourceDtoList).build();
        return printAvg(0, resultDto);
    }

    public ResultDto printAvg(int k, ResultDto ... resultDto)
    {
        int totalAmount = 0;
        int obr = -1;
        double pRefuse = 0;
        double kUse = 0;
        for (int i = 0; i < sourceManager.getSourceAmount(); i++) {
            int amount = -1;
            if (sourceManager.getSource()[i] != null){
                amount = sourceManager.getSource()[i].getRequestCounter();
                pRefuse += sourceManager.getSource()[i].getRefuse()*1.0 / amount;
            }
        }
        pRefuse = pRefuse / sourceManager.getSourceAmount();



        for (int i = 0; i < devMan.getDeviceAmount(); i++) {
            double procTime = devMan.getDevices()[i].getProcessingTime();
            double time = devMan.getMaxTime();
            kUse += procTime/time;
        }
        kUse /= devMan.getDeviceAmount();

        System.out.println(String.format(
                "%1$7d|  pRefuse: %2$10.5f | kUse:  %3$10.5f ",
                k, pRefuse, kUse));

        if (resultDto != null && resultDto.length > 0 && resultDto[0] != null){
            resultDto[0].setPRefuse(pRefuse);
            resultDto[0].setKUse(kUse);
            resultDto[0].setK(k);
            return resultDto[0];
        } else {
            return ResultDto.builder().k(k).kUse(kUse).pRefuse(pRefuse).build();
        }
    }

    public double getP(){
        double pRefuse = 0;
        for (int i = 0; i < sourceManager.getSourceAmount(); i++) {
            int amount = -1;
            if (sourceManager.getSource()[i] != null){
                amount = sourceManager.getSource()[i].getRequestCounter();
                pRefuse += sourceManager.getSource()[i].getRefuse()*1.0 / amount;
            }
        }
        pRefuse = pRefuse / sourceManager.getSourceAmount();
        return pRefuse;
    }

}
