package sample;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public class Request {

    private Random r = new Random();
    @Getter
    private double time;
    private Integer refuse = 0;
    private Integer requestCounter = 0;
    @Getter
    private int id;
    @Getter @Setter
    private double waitTime = 0;
    @Getter @Setter
    private double startTime = 0;
    @Getter @Setter
    private double endTime = 0;
    @Getter @Setter
    private int id_source;

    public Request (int id, double time, int requestCounter)
    {
        this.id = (id+1)*100000+requestCounter;
        this.id_source = id;
        this.time = time;
        startTime = time;
    }
}
