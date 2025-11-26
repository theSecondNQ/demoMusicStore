-- 歌单与歌单曲目表（MySQL）

CREATE TABLE IF NOT EXISTS playlist (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(255),
  visibility ENUM('private','shared') NOT NULL DEFAULT 'private',
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS playlist_track (
  id INT AUTO_INCREMENT PRIMARY KEY,
  playlist_id INT NOT NULL,
  music_id INT NOT NULL,
  position INT NOT NULL DEFAULT 1,
  add_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_playlist_music (playlist_id, music_id),
  INDEX idx_playlist_position (playlist_id, position),
  CONSTRAINT fk_playlist_track_playlist FOREIGN KEY (playlist_id) REFERENCES playlist(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;