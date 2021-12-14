package sample;

import lombok.Getter;

public final class Buffer {

    private static Buffer instance;

    @Getter
    private Request[] buffer_;
    private Integer space_;
    private SourceManager sourceManager;
    private Integer refuse = 0;
    @Getter
    private final Integer size_;
    private int bufferPointer;

    private Buffer(int size) {
        this.size_ = size;
        space_ = size_;
        buffer_ = new Request[size];
        sourceManager = SourceManager.getInstance();
        bufferPointer = 0;
    }

    public static Buffer getInstance(int size) {
        if (instance == null) {
            instance = new Buffer(size);
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public int incrBufferPointer() {
        if (bufferPointer < size_ - 1)
            bufferPointer++;
        else
            bufferPointer = 0;
        return bufferPointer;
    }


    public void PlaceInBuffer(Request request) {
        if (space_ != 0)
        {
            for (int i = 0; i < size_; i++)
            {
                if (buffer_[i] == null)
                {
                    buffer_[i] = request;
                    space_ --;
                    break;
                }
            }
        }
        else if (space_ == 0)
        {
            Integer oldest = 0;
            for (int i = 1; i < size_; i++)
            {
                if (buffer_[i].getTime() < buffer_[oldest].getTime())
                {
                    oldest = i;
                }
            }
            sourceManager.refuseHandler(buffer_[oldest]);
            buffer_[oldest] = request;
            refuse++;
        }
    }


    public Request getFirstRequest() { //ласт

        //ok
        if (space_ != size_) {
            for (int i = 0; i < size_; i++) {
                if (buffer_[i] != null) {
                    int last = i;
                    for (int j = i; j < size_; j++) {
                        if (buffer_[j] != null) {
                            if (buffer_[j].getTime() < buffer_[last].getTime()) {
                                last = j;
                            }
                        }
                    }

                    Request request = buffer_[last];
                    buffer_[last] = null;
                    space_ ++;
                    return request;
                }
            }
        }
        return null;
    }

    public double GetLastTime() {
        if (space_ != size_) {
            for (int i = 0; i < size_; i++) {
                if (buffer_[i] != null) {
                    Request lastRequest = buffer_[i];
                    for (int j = i; j < size_; j++) {
                        if (buffer_[j] != null) {
                            if (buffer_[j].getTime() < lastRequest.getTime()) {
                                lastRequest = buffer_[j];
                            }
                        }
                    }
                    return lastRequest.getTime();
                }
            }
        }
        return 0;
    }


    public boolean isEmpty() {
        return (size_ == space_);
    }

    public void setSourceManager() {
        this.sourceManager = SourceManager.getInstance();
    }
}
