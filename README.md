# 音乐商店（demoMusicStore）

一个基于 Spring Boot + MyBatis + Thymeleaf 的简单音乐商店示例项目，支持音乐浏览、收藏、购物车、下单、歌单管理与音频预览/播放。

## 运行环境
- JDK 17+
- Maven 3.8+
- MySQL 8.x（默认配置）

> 可通过环境变量覆盖数据库、端口与音频目录，便于在不同环境下部署。

## 快速开始
1. 创建数据库并初始化数据：在 MySQL 执行仓库根目录的 `initialize.sql`。
2. 配置音频目录：准备 `audios` 目录并放置示例 MP3 文件（文件名参考 `initialize.sql` 中的 `audio_path`）。
3. 构建：在项目根目录运行 `mvn -DskipTests package`。
4. 启动：`java -jar target/demo-0.0.1-SNAPSHOT.jar`。

### 可选：通过环境变量覆盖配置
- `SPRING_DATASOURCE_URL`（默认：`jdbc:mysql://127.0.0.1:3306/musicstore?...`）
- `SPRING_DATASOURCE_USERNAME`（默认：`root`）
- `SPRING_DATASOURCE_PASSWORD`（默认：项目中预设值）
- `SERVER_PORT`（默认：`8091`）
- `MEDIA_AUDIO_DIR`（默认：仓库中的 `audios` 路径）

示例（Windows PowerShell）：
```
$env:SERVER_PORT=8092; $env:MEDIA_AUDIO_DIR="C:\\path\\to\\audios"; java -jar target\demo-0.0.1-SNAPSHOT.jar
```

## 健康检查
已集成 Spring Boot Actuator，可在启动后访问：
- `http://localhost:<port>/actuator/health`
- `http://localhost:<port>/actuator/info`

## 目录说明
- `src/main/resources/application.yml`：应用主配置（支持环境变量覆盖）。
- `initialize.sql`：数据库表与样例数据初始化脚本（MySQL）。
- `src/main/java/com/music_store/Config/MediaDirInitializer.java`：启动时检查/创建音频目录。
- `src/main/java/com/music_store/Config/GlobalExceptionHandler.java`：统一异常处理与日志。

## 常见问题
- 端口被占用：使用 `--server.port=<port>` 或设置环境变量 `SERVER_PORT`。
- 音频无法播放：确认 `MEDIA_AUDIO_DIR` 指向包含 MP3 文件的目录且文件名与数据库一致。
- 数据库连接失败：检查 `SPRING_DATASOURCE_*` 环境变量或 `application.yml` 中的配置。

## 开发建议
- 开发环境建议使用本地 MySQL，并通过 `initialize.sql` 进行初始化。
- 如需更轻量的开发环境，可后续引入 H2 Profile（当前版本默认使用 MySQL）。
