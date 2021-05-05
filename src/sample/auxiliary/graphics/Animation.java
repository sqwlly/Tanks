package sample.auxiliary.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {

    private int frameCount;                 // Counts ticks for change
    private int frameDelay;                 // frame delay 1-12 (You will have to play around with this)
    private int currentFrame;               // animations current frame
    private int animationDirection;         // animation direction (i.e counting forward or backward)
    private int totalFrames;                // total amount of frames for your animation
    private long startTime;
    private long timesPlayed;

    private boolean stopped;                // has animations stopped

    private List<Frame> frames = new ArrayList<>();    // Arraylist of frames

    public Animation(BufferedImage[] frames, int frameDelay) {
        this.frameDelay = frameDelay;
        this.stopped = true;

        for (int i = 0; i < frames.length; i++) {
            addFrame(frames[i], frameDelay);
        }
        this.timesPlayed = 0;
        this.frameCount = 0;
        this.frameDelay = frameDelay;
        this.currentFrame = 0;
        this.animationDirection = 1;
        this.totalFrames = this.frames.size();

    }

    public void start() {
        if (!stopped) {
            return;
        }

        if (frames.size() == 0) {
            return;
        }

        stopped = false;
    }

    public void stop() {
        if (frames.size() == 0) {
            return;
        }

        stopped = true;
    }

    public void restart() {
        if (frames.size() == 0) {
            return;
        }

        stopped = false;
        currentFrame = 0;
    }

    public void reset() {
        this.stopped = true;
        this.frameCount = 0;
        this.currentFrame = 0;
    }

    private void addFrame(BufferedImage frame, int duration) {
        if (duration <= 0) {
            System.err.println("Invalid duration: " + duration);
            throw new RuntimeException("Invalid duration: " + duration);
        }

        frames.add(new Frame(frame, duration));
        currentFrame = 0;
    }

    public BufferedImage getSprite() {
        return frames.get(currentFrame).getFrame();
    }

    public void update() {
        if (!stopped) {

            if(frameDelay == -1) {
                timesPlayed = 0;
                return;
            }

            long elapsed = (System.nanoTime() - startTime) / 1000000;
            if(elapsed > frameDelay) {
                currentFrame++;
                startTime = System.nanoTime();
            }
            if(currentFrame == totalFrames) {
                currentFrame = 0;
                timesPlayed++;
            }
        }

    }

    public boolean oncePlayed() { return timesPlayed > 0; }
    public boolean hasPlayed(int times) {
        return timesPlayed >= times;
    }

    public long getFrameDelay() { return frameDelay; }
}
