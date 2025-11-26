package com.music_store.Controller;

import com.music_store.Entity.Music;
import com.music_store.Entity.User;
import com.music_store.Service.CategoryService;
import com.music_store.Service.FavouriteService;
import com.music_store.Service.MusicService;
import com.music_store.Service.OrderService;
import com.music_store.Service.UserDailyTrialService;
import com.music_store.Service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserDailyTrialService trialService;

    @Autowired
    private PlaylistService playlistService;

    @Value("${media.audio-dir}")
    private String audioDir;

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String category,
                       HttpSession session,
                       Model model) {
        List<Music> musicList = musicService.listMusic(keyword, category);
        model.addAttribute("musicList", musicList);
        model.addAttribute("categories", categoryService.getAllCategories());

        User user = (User) session.getAttribute("user");
        if (user != null) {
            // 为每个音乐标记是否已被用户收藏
            for (Music music : musicList) {
                boolean isFav = favouriteService.isFavourite(user.getId(), music.getId());
                music.setFavourite(isFav);
            }
            // 为列表页播放器提供歌单选择数据
            model.addAttribute("playlists", playlistService.listByUser(user.getId()));
        }

        return "music_list";
    }

    @GetMapping("/daily")
    public String daily(HttpSession session, Model model) {
        // 优先热门Top12，若不足则从全库补齐随机12
        List<Music> daily = musicService.topPopular(12);
        if (daily == null || daily.isEmpty()) {
            List<Music> all = musicService.listMusic(null, null);
            if (all != null && !all.isEmpty()) {
                Collections.shuffle(all);
                daily = all.stream().limit(12).toList();
            }
        }

        model.addAttribute("dailyList", daily);

        // 适配新版页面：统计总时长（分钟）与总价格
        int totalDurationMinutes = 0;
        double totalPrice = 0.0;
        if (daily != null) {
            for (Music m : daily) {
                if (m != null) {
                    Integer dur = m.getDuration();
                    Double price = m.getPrice();
                    if (dur != null && dur > 0) totalDurationMinutes += (dur / 60);
                    if (price != null && price > 0) totalPrice += price;
                }
            }
        }
        model.addAttribute("totalDuration", totalDurationMinutes);
        model.addAttribute("totalPrice", String.format("%.2f", totalPrice));

        User user = (User) session.getAttribute("user");
        if (user != null && daily != null) {
            for (Music m : daily) {
                boolean isFav = favouriteService.isFavourite(user.getId(), m.getId());
                m.setFavourite(isFav);
            }
            // 提供歌单选择（如需将推荐加入歌单等扩展）
            model.addAttribute("playlists", playlistService.listByUser(user.getId()));
        }

        return "music_daily";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, HttpSession session, Model model) {
        Music music = musicService.getById(id);
        if (music == null) {
            return "redirect:/music/list";
        }

        // 增加播放量（访问详情页也计数一次）
        musicService.incrementPlayCount(id);
        music.setPlayCount(music.getPlayCount() + 1);

        model.addAttribute("music", music);

        User user = (User) session.getAttribute("user");
        if (user != null) {
            boolean isFavourite = favouriteService.isFavourite(user.getId(), id);
            model.addAttribute("isFavourite", isFavourite);
            boolean purchased = orderService.hasPurchasedMusic(user.getId(), id);
            boolean hasUsedToday = trialService.hasUsedToday(user.getId());
            model.addAttribute("purchased", purchased);
            model.addAttribute("hasTrialLeft", !hasUsedToday);

            // 加载歌单用于“加入到歌单”入口
            model.addAttribute("playlists", playlistService.listByUser(user.getId()));
        }

        // 推荐：优先同分类Top5，其次同艺术家Top5，最后热门Top5
        List<Music> recommended = null;
        if (music.getCategoryId() != null) {
            recommended = musicService.topByCategory(music.getCategoryId(), id, 5);
        }
        if ((recommended == null || recommended.isEmpty()) && music.getArtist() != null) {
            recommended = musicService.topByArtist(music.getArtist(), id, 5);
        }
        if (recommended == null || recommended.isEmpty()) {
            recommended = musicService.topPopular(5);
        }
        model.addAttribute("recommended", recommended);

        return "music_detail";
    }

    @GetMapping("/add")
    public String addPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        return "music_add";
    }

    @PostMapping("/add")
    public String add(Music music,
                      @RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
                      HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        // 处理文件上传并自动填充元数据
        if (audioFile != null && !audioFile.isEmpty()) {
            try {
                // 确保目录存在
                java.io.File dir = new java.io.File(audioDir);
                if (!dir.exists()) dir.mkdirs();

                String original = audioFile.getOriginalFilename();
                String ext = ".mp3";
                if (original != null && original.contains(".")) {
                    ext = original.substring(original.lastIndexOf('.'));
                }
                String safeName = java.util.UUID.randomUUID() + ext;
                java.io.File dest = new java.io.File(dir, safeName);
                audioFile.transferTo(dest);

                // 相对路径仅保存文件名（基于 audioDir）
                music.setAudioPath(safeName);

                // 使用 mp3agic 读取元数据
                try {
                    com.mpatric.mp3agic.Mp3File mp3 = new com.mpatric.mp3agic.Mp3File(dest.getAbsolutePath());
                    // 时长（秒）
                    if (music.getDuration() == null || music.getDuration() <= 0) {
                        music.setDuration((int) mp3.getLengthInSeconds());
                    }
                    // 优先 ID3v2
                    if (mp3.hasId3v2Tag()) {
                        com.mpatric.mp3agic.ID3v2 id3 = mp3.getId3v2Tag();
                        if ((music.getTitle() == null || music.getTitle().isBlank()) && id3.getTitle() != null) {
                            music.setTitle(id3.getTitle());
                        }
                        if ((music.getArtist() == null || music.getArtist().isBlank()) && id3.getArtist() != null) {
                            music.setArtist(id3.getArtist());
                        }
                        if ((music.getAlbum() == null || music.getAlbum().isBlank()) && id3.getAlbum() != null) {
                            music.setAlbum(id3.getAlbum());
                        }
                    } else if (mp3.hasId3v1Tag()) {
                        com.mpatric.mp3agic.ID3v1 id3 = mp3.getId3v1Tag();
                        if ((music.getTitle() == null || music.getTitle().isBlank()) && id3.getTitle() != null) {
                            music.setTitle(id3.getTitle());
                        }
                        if ((music.getArtist() == null || music.getArtist().isBlank()) && id3.getArtist() != null) {
                            music.setArtist(id3.getArtist());
                        }
                        if ((music.getAlbum() == null || music.getAlbum().isBlank()) && id3.getAlbum() != null) {
                            music.setAlbum(id3.getAlbum());
                        }
                    }
                } catch (Exception ignored) {
                    // 忽略元数据读取失败，继续保存文件与基本信息
                }

                // 兜底：若标题为空，用文件名（去扩展名）
                if (music.getTitle() == null || music.getTitle().isBlank()) {
                    String base = (original != null) ? original : safeName;
                    int dot = base.lastIndexOf('.');
                    if (dot > 0) base = base.substring(0, dot);
                    music.setTitle(base);
                }
                if (music.getArtist() == null || music.getArtist().isBlank()) {
                    music.setArtist("未知艺术家");
                }
                if (music.getAlbum() == null || music.getAlbum().isBlank()) {
                    music.setAlbum("未知专辑");
                }
                if (music.getPrice() == null) {
                    music.setPrice(0.0);
                }
            } catch (Exception e) {
                // 上传失败则回退到普通添加逻辑（但不设置音频路径）
            }
        } else {
            // 未上传文件时，确保基本字段存在
            if (music.getTitle() == null || music.getTitle().isBlank()) {
                music.setTitle("未命名歌曲");
            }
            if (music.getArtist() == null || music.getArtist().isBlank()) {
                music.setArtist("未知艺术家");
            }
            if (music.getAlbum() == null || music.getAlbum().isBlank()) {
                music.setAlbum("未知专辑");
            }
            if (music.getPrice() == null) {
                music.setPrice(0.0);
            }
        }

        musicService.addMusic(music);
        return "redirect:/music/list";
    }

    // 管理员功能：编辑音乐页面
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Integer id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        Music music = musicService.getById(id);
        model.addAttribute("music", music);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "music_edit";
    }

    @PostMapping("/edit")
    public String edit(Music music, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        musicService.updateMusic(music);
        return "redirect:/music/list";
    }

    // 管理员功能：删除音乐
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        try {
            musicService.deleteMusic(id);
            return "redirect:/music/list?msg=删除成功";
        } catch (IllegalStateException ise) {
            // 业务阻断：已有关联订单
            return "redirect:/music/list?error=" + java.net.URLEncoder.encode(ise.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            // 其它删除失败
            return "redirect:/music/list?error=" + java.net.URLEncoder.encode("删除失败，请稍后再试", java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}