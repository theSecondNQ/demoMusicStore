package com.music_store.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class MediaDirInitializer implements ApplicationRunner {

    @Value("${media.audio-dir}")
    private String audioDir;

    @Override
    public void run(ApplicationArguments args) {
        try {
            String dirPath = audioDir;
            // 兼容环境变量或配置中误带的引号
            if (dirPath != null && dirPath.length() >= 2 && dirPath.startsWith("\"") && dirPath.endsWith("\"")) {
                dirPath = dirPath.substring(1, dirPath.length() - 1);
            }
            File dir = new File(dirPath);
            if (!dir.exists()) {
                boolean ok = dir.mkdirs();
                if (ok) {
                    System.out.println("[MediaDirInitializer] 已创建音频目录: " + dir.getAbsolutePath());
                } else {
                    System.err.println("[MediaDirInitializer] 无法创建音频目录: " + dir.getAbsolutePath());
                }
            } else {
                System.out.println("[MediaDirInitializer] 音频目录存在: " + dir.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("[MediaDirInitializer] 音频目录初始化失败: " + e.getMessage());
        }
    }
}