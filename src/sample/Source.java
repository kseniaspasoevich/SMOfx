package sample;

import lombok.Getter;

import java.util.Random;

public class Source {

    private Random r = new Random();
    private final double lam;
    @Getter
    private double time = 0;
    @Getter
    private Integer refuse = 0;
    @Getter
    private Integer requestCounter = 0;
    @Getter
    private double waitTime = 0;
    @Getter
    private double timeInSystem = 0;
    @Getter
    private double processingTime = 0;
    @Getter
    private int id;


    public Source (double sourceLam, int id)
    {
        refuse = 0;
        this.id = id;
        lam = sourceLam;
        time = 0;
    }


    public Request GenNew(){
        requestCounter++;
        time = time + -1 /(lam) * (Math.log(r.nextDouble()));
        return new Request(id, time, requestCounter);
    }



    public void incRefuse() {
        refuse++;
    }

    public void incWaitTime(double waitTime) {
        this.waitTime += waitTime;
    }

    public void incTimeInSystem(double timeInSystem) {
        this.timeInSystem += timeInSystem;
    }

    public void incProcessingTime(double time) {
        this.processingTime += time;
    }
}
