package sample;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//@Slf4j
public final class Simulation {

    private static Simulation instance;

    @Setter
    private int bufSize = 55;
    @Setter
    private int deviceSize = 44;
    @Setter
    private int sourceAmount = 40;
    @Setter
    private int N = 5000;

    private int count = 0;
    InputStreamReader read = new InputStreamReader(System.in);
    BufferedReader in = new BufferedReader(read);
    @Getter
    double deviceAlpha = 0.6;
    @Getter
    double deviceBeta = 1.1;
    @Getter
    double sourceLam = 1.3;
    boolean short_results = false;


    private Simulation () {}

    public static Simulation getInstance() {
        if (instance == null) {
            instance = new Simulation();
        }
        return instance;
    }


    public void runSimulation() throws IOException {


        while (true) {
            count = 0;
            int num = 0;
            InputStreamReader read = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(read);

            System.out.println("Choose: \n1 - step mode \n2 - auto mode \n3 - find N \n4 - vary Mode");

            try {
                num = Integer.parseInt(in.readLine());
            } catch (Exception e) {
                System.out.println(123);
                //log.error(e.getMessage());
            }

            switch (num) {
                case (1): {
                    init();
                    stepAndAuto(1);
                    break;
                }
                case (2): {
                    init();
                    stepAndAuto(2);
                    break;
                }
                case (3): {
                    findN();
                    break;
                }
                case (4): {
                    init();
                    varyMode();
                    break;
                }
                default:
                    //log.error("No such method!");
                    System.out.println("No such method!");
                    break;
            }
        }
    }


    public void init() throws IOException {

        System.out.println("Source Size:");
        try {
            sourceAmount = Integer.parseInt(in.readLine());
        } catch (Exception e) {
            //log.error(e.getMessage());
            System.out.println("No such method!");
        }
        System.out.println("Device Size:");
        try {
            deviceSize = Integer.parseInt(in.readLine());
        } catch (Exception e) {
            //log.error(e.getMessage());
            System.out.println("No such method!");
        }

        System.out.println("Buffer Size: ");
        try {
            bufSize = Integer.parseInt(in.readLine());
        } catch (Exception e) {
            //log.error(e.getMessage());
            System.out.println("No such method!");
        }

        System.out.println("N");
        try {
            N = Integer.parseInt(in.readLine());
        } catch (Exception e) {
            //log.error(e.getMessage());
            System.out.println("No such method!");
        }
    }

    public void stepAndAuto(int mode){
        Buffer buffer = Buffer.getInstance(bufSize);
        DeviceManager devMan = DeviceManager.getInstance(deviceSize, buffer, deviceAlpha, deviceBeta);
        SourceManager sourceManager = SourceManager.getInstance(sourceAmount, buffer, sourceLam);
        buffer.setSourceManager();
        PrintData printData = PrintData.getInstance(devMan, sourceManager, buffer);

        while (nextStep(devMan, sourceManager, buffer) == 0){
            if (mode == 1) {
                printData.printStep();
                try {
                    mode = Integer.parseInt(in.readLine());
                } catch (Exception e) {
                }
            }
        }
        if (!short_results)
            printData.printTotal();
        printData.printAvg(0);
        Buffer.resetInstance();
        DeviceManager.resetInstance();
        SourceManager.resetInstance();
        PrintData.resetInstance();
    }


    public int nextStep(DeviceManager devMan, SourceManager sourceManager, Buffer buffer) {
        if (count < N) {
            if (!buffer.isEmpty() &&
                    (devMan.getMinTime() < sourceManager.getMinTime() | devMan.getMinTime() < buffer.GetLastTime()))
            {
                devMan.takeRequest();
            } else {
                sourceManager.GenNew();
                ++count;
            }
        } else if (!buffer.isEmpty()) {
            devMan.takeRequest();
        } else {
            return 1;
        }
        return 0;
    }

    public void findN(){
        N = 1000;

        while (N < 50000) {
            count = 0;
            Buffer buffer = Buffer.getInstance(bufSize);
            DeviceManager devMan = DeviceManager.getInstance(deviceSize, buffer, deviceAlpha, deviceBeta);
            SourceManager sourceManager = SourceManager.getInstance(sourceAmount, buffer, sourceLam);
            buffer.setSourceManager();
            PrintData printData = PrintData.getInstance(devMan, sourceManager, buffer);

            while (nextStep(devMan, sourceManager, buffer) == 0) {}

            double p0 = printData.getP();
            Buffer.resetInstance();
            DeviceManager.resetInstance();
            SourceManager.resetInstance();
            PrintData.resetInstance();

            double t = 1.643;
            double b = 0.1;
            int tempn = N;
            N = (int) (t * t * (1 - p0) / (p0 * b * b)); //оптимальное количество зявок
            System.out.println(N);

            buffer = Buffer.getInstance(bufSize);
            devMan = DeviceManager.getInstance(deviceSize, buffer, deviceAlpha, deviceBeta);
            sourceManager = SourceManager.getInstance(sourceAmount, buffer, sourceLam);
            buffer.setSourceManager();
            printData = PrintData.getInstance(devMan, sourceManager, buffer);

            while (nextStep(devMan, sourceManager, buffer) == 0) {
            }
            double p1 = printData.getP();
            Buffer.resetInstance();
            DeviceManager.resetInstance();
            SourceManager.resetInstance();
            PrintData.resetInstance();


            if ((Math.abs(p0 - p1)/p0) * 100 < 10) {
                System.out.println(tempn);
                break;
            }
        }

    }

    public void varyMode() throws IOException {
        System.out.println("1 - buffer size \n2 - devise size \n3 - source size \n4 - source alpha");
        int num = 0;
        int a;
        int b;
        try {
            num = Integer.parseInt(in.readLine());
        } catch (Exception e) {
        }

        switch (num) {
            case (1):
                System.out.println("Введите нижний порог и верхний порог \n");
                a = Integer.parseInt(in.readLine());
                b = Integer.parseInt(in.readLine());
                for (int i = a; a < b && i < b; i++) {
                    bufSize = i;
                    varyModeAnalyze(i);
                }
                break;
            case (2): {
                System.out.println("Введите нижний порог и верхний порог \n");
                a = Integer.parseInt(in.readLine());
                b = Integer.parseInt(in.readLine());
                for (int i = a; a < b && i < b; i++) {
                    deviceSize = i;
                    varyModeAnalyze(i);
                }
                break;
            }
            case (3): {
                System.out.println("Введите нижний порог и верхний порог \n");
                a = Integer.parseInt(in.readLine());
                b = Integer.parseInt(in.readLine());
                for (int i = a; a < b && i < b; i++) {
                    sourceAmount = i;
                    varyModeAnalyze(i);
                }
                break;
            }
            case (4): {
                for (double i = 0.1; i < 4; i+=0.05) {
                    sourceLam = i;
                    System.out.print(i + ") ");
                    varyModeAnalyze(1);
                }
                break;
            }
            default:
                System.out.println("ой");
                break;
        }
    }

    public void varyModeAnalyze(int k) {
        count = 0;
        Buffer buffer = Buffer.getInstance(bufSize);
        DeviceManager devMan = DeviceManager.getInstance(deviceSize, buffer, deviceAlpha, deviceBeta);
        SourceManager sourceManager = SourceManager.getInstance(sourceAmount, buffer, sourceLam);
        buffer.setSourceManager();
        PrintData printData = PrintData.getInstance(devMan, sourceManager, buffer);

        while (nextStep(devMan, sourceManager, buffer) == 0){}
        printData.printAvg(k);
        Buffer.resetInstance();
        DeviceManager.resetInstance();
        SourceManager.resetInstance();
        PrintData.resetInstance();
    }

    public double getDeviceAlpha() {
        return deviceAlpha;
    }

    public double getDeviceBeta() {
        return deviceBeta;
    }
}
