package sample;

import lombok.Getter;

import java.util.Random;

public class Device {

    @Getter
    private double time;
    @Getter
    private double startTime = 0;
    private final double a;
    private final double b;
    @Getter
    private int requestAmount = 0;
    @Getter
    private double processingTime = 0;
    @Getter
    private Request request;

    private Random r = new Random();

    public Device(double alpha, double beta) {
        this.time = 0;
        a = alpha;
        b = beta;
    }

    public void takeRequest(Request request1)
    {
        this.request = request1;
        if (request.getTime() > time) {
            time = request.getTime();
        }

        startTime = time;
        request.setWaitTime(time - request.getStartTime());
        request.setStartTime(time);

        double x = a + (b - a) * r.nextDouble();
        time = time + x;
        processingTime = processingTime + x;
        request.setEndTime(time);

        SourceManager.getInstance().requestInfoHandler(request);
        requestAmount++;
    }
}
