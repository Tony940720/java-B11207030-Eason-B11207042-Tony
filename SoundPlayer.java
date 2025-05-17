import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    private static int globalVolumePercent = 100; // 全局音量，0~100

    private Clip clip;
    private int instanceVolume;

    // 設定全局音量（影響所有新建的 SoundPlayer 及 playSoundOnce）
    public static void setGlobalVolume(int percent) {
        globalVolumePercent = Math.min(100, Math.max(0, percent));
    }

    // 取得全局音量
    public static int getGlobalVolume() {
        return globalVolumePercent;
    }

    // 建構子，使用當前全局音量
    public SoundPlayer(String filePath) {
        this(filePath, globalVolumePercent);
    }

    // 建構子，可指定音量
    public SoundPlayer(String filePath, int volumePercent) {
        instanceVolume = volumePercent;
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            setVolume(instanceVolume);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // 持續播放
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // 靜態方法播放一次音效，使用全局音量
    public static void playSoundOnce(String filePath) {
        playSoundOnce(filePath, globalVolumePercent);
    }

    // 靜態方法播放一次音效，可指定音量
    public static void playSoundOnce(String filePath, int volumePercent) {
        new Thread(() -> {
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    float min = volume.getMinimum();
                    float max = volume.getMaximum();
                    float gain = min + (max - min) * (volumePercent / 100.0f);
                    volume.setValue(gain);
                }
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 變更此物件音量（只影響背景音樂之類持續播放的）
    public void setVolume(int percent) {
        instanceVolume = percent;
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = volume.getMinimum();
            float max = volume.getMaximum();
            float gain = min + (max - min) * (percent / 100.0f);
            volume.setValue(gain);
        }
    }

    // 停止播放
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
