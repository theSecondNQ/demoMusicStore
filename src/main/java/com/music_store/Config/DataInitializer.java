package com.music_store.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        try {
            ensureUserDailyTrialTable();
            ensurePlaylistTables();
            ensureMusicDeletedColumn();
            System.out.println("[DataInitializer] 必要的表检查完成");
        } catch (Exception e) {
            System.err.println("[DataInitializer] 初始化失败: " + e.getMessage());
        }
    }

    private boolean tableExists(String tableName) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?",
                Integer.class,
                tableName
        );
        return count != null && count > 0;
    }

    private void ensureUserDailyTrialTable() {
        if (!tableExists("user_daily_trial")) {
            String sql = "CREATE TABLE IF NOT EXISTS user_daily_trial (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "date DATE NOT NULL, " +
                    "UNIQUE KEY uk_user_date (user_id, date)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
            jdbcTemplate.execute(sql);
            System.out.println("[DataInitializer] 已创建表 user_daily_trial");
        }
    }

    private void ensurePlaylistTables() {
        if (!tableExists("playlist")) {
            String sql = "CREATE TABLE IF NOT EXISTS playlist (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "description VARCHAR(255), " +
                    "visibility ENUM('private','shared') NOT NULL DEFAULT 'private', " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                    "INDEX idx_user (user_id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
            jdbcTemplate.execute(sql);
            System.out.println("[DataInitializer] 已创建表 playlist");
        }
        if (!tableExists("playlist_track")) {
            String sql = "CREATE TABLE IF NOT EXISTS playlist_track (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "playlist_id INT NOT NULL, " +
                    "music_id INT NOT NULL, " +
                    "position INT NOT NULL DEFAULT 1, " +
                    "add_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "UNIQUE KEY uk_playlist_music (playlist_id, music_id), " +
                    "INDEX idx_playlist_position (playlist_id, position)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
            jdbcTemplate.execute(sql);
            System.out.println("[DataInitializer] 已创建表 playlist_track");
        }
    }

    private void ensureMusicDeletedColumn() {
        try {
            String sqlCheck = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'music' AND COLUMN_NAME = 'deleted'";
            Integer count = jdbcTemplate.queryForObject(sqlCheck, Integer.class);
            if (count == null || count == 0) {
                String alter = "ALTER TABLE music ADD COLUMN deleted TINYINT(1) NOT NULL DEFAULT 0 AFTER create_time";
                jdbcTemplate.execute(alter);
                System.out.println("[DataInitializer] 已为 music 表添加 deleted 列（软删除）");
            }
        } catch (Exception e) {
            System.err.println("[DataInitializer] 检查/添加 music.deleted 失败: " + e.getMessage());
        }
    }
}