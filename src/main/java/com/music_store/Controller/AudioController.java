package com.music_store.Controller;

import com.music_store.Entity.Music;
import com.music_store.Entity.User;
import com.music_store.Service.MusicService;
import com.music_store.Service.OrderService;
import com.music_store.Service.UserDailyTrialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/music")
public class AudioController {

    @Value("${media.audio-dir}")
    private String audioDir;

    @Value("${media.preview-seconds:30}")
    private int previewSeconds;

    @Autowired
    private MusicService musicService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserDailyTrialService trialService;

    private MediaType getMediaType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".mp3")) return MediaType.valueOf("audio/mpeg");
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    private Resource getAudioResource(Music music) {
        String path = music.getAudioPath();
        File file = new File(audioDir, path);
        return new FileSystemResource(file);
    }

    private long calcPreviewBytes(File file, Integer duration) {
        try {
            long fileLen = file.length();
            if (duration != null && duration > 0) {
                long bytes = (long) Math.floor(fileLen * (previewSeconds / (double) duration));
                // 兜底最小预览大小（避免过小）
                return Math.max(bytes, 512 * 1024);
            }
            // 无时长数据时兜底 5MB
            return 5L * 1024 * 1024;
        } catch (Exception e) {
            return 2L * 1024 * 1024;
        }
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<ResourceRegion> preview(@PathVariable Integer id,
                                                  @RequestHeader HttpHeaders headers) throws IOException {
        Music music = musicService.getById(id);
        if (music == null || music.getAudioPath() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Resource resource = getAudioResource(music);
        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        long contentLength = resource.contentLength();
        long previewLength = Math.min(calcPreviewBytes(new File(audioDir, music.getAudioPath()), music.getDuration()), contentLength);

        List<HttpRange> ranges = headers.getRange();
        ResourceRegion region;
        if (ranges != null && !ranges.isEmpty()) {
            HttpRange range = ranges.get(0);
            long start = range.getRangeStart(contentLength);
            long end = Math.min(range.getRangeEnd(contentLength), previewLength - 1);
            if (start >= previewLength) {
                // 请求超出预览范围
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
            }
            long rangeLen = end - start + 1;
            region = new ResourceRegion(resource, start, rangeLen);
        } else {
            region = new ResourceRegion(resource, 0, previewLength);
        }

        MediaType mediaType = getMediaType(music.getAudioPath());
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(mediaType)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(region);
    }

    @GetMapping("/stream/{id}")
    public ResponseEntity<ResourceRegion> stream(@PathVariable Integer id,
                                                 @RequestHeader HttpHeaders headers,
                                                 HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Music music = musicService.getById(id);
        if (music == null || music.getAudioPath() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Resource resource = getAudioResource(music);
        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 授权：已购买或当天首次试用允许播放整曲
        boolean purchased = orderService.hasPurchasedMusic(user.getId(), id);
        boolean hasUsedToday = trialService.hasUsedToday(user.getId());
        if (!purchased) {
            if (hasUsedToday) {
                // 当日已使用试用
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else {
                // 标记今日已使用试用
                trialService.markUsedToday(user.getId());
            }
        }

        long contentLength = resource.contentLength();
        List<HttpRange> ranges = headers.getRange();
        ResourceRegion region;
        if (ranges != null && !ranges.isEmpty()) {
            HttpRange range = ranges.get(0);
            long start = range.getRangeStart(contentLength);
            long end = range.getRangeEnd(contentLength);
            long rangeLen = end - start + 1;
            region = new ResourceRegion(resource, start, rangeLen);
        } else {
            region = new ResourceRegion(resource, 0, contentLength);
        }

        MediaType mediaType = getMediaType(music.getAudioPath());
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(mediaType)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(region);
    }
}