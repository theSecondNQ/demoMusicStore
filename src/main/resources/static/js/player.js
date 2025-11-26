// 简易歌单播放器：顺序/单曲循环/随机模式
(() => {
  const audio = document.getElementById('audio');
  const btnPlay = document.getElementById('btnPlay');
  const btnPrev = document.getElementById('btnPrev');
  const btnNext = document.getElementById('btnNext');
  const btnMode = document.getElementById('btnMode');
  const nowPlaying = document.getElementById('nowPlaying');
  const playStatus = document.getElementById('playStatus');
  const playlistSelect = document.getElementById('playlistSelect');
  const btnLoadPlaylist = document.getElementById('btnLoadPlaylist');
  const btnPlayAll = document.getElementById('btnPlayAll');

  if (!audio || !btnPlay || !btnPrev || !btnNext || !btnMode || !nowPlaying) {
    return; // 页面不包含播放器
  }

  const MODE = { SEQ: 'sequence', SINGLE: 'single', SHUFFLE: 'shuffle' };
  let mode = MODE.SEQ;
  let idx = 0;
  let playlist = Array.isArray(window.PLAYLIST) ? window.PLAYLIST : [];
  // 随机模式：维护一个不重复的播放顺序
  let shuffleOrder = [];
  let shufflePos = 0; // 指向下一个要播放的位置

  const rebuildPlaylistFromDOM = () => {
    const anchors = document.querySelectorAll('a[data-role="track"]');
    playlist = Array.from(anchors).map(a => ({
      id: parseInt(a.getAttribute('data-id')), title: a.textContent.trim()
    })).filter(t => Number.isFinite(t.id));
    // 重建后重置指针与随机序列
    idx = 0;
    shuffleOrder = [];
    shufflePos = 0;
  };

  const setModeLabel = () => {
    if (mode === MODE.SEQ) btnMode.textContent = '🔁 顺序播放';
    else if (mode === MODE.SINGLE) btnMode.textContent = '🔂 单曲循环';
    else btnMode.textContent = '🔀 随机播放';
    audio.loop = (mode === MODE.SINGLE);
  };

  const updateNowPlaying = () => {
    const t = playlist[idx];
    nowPlaying.textContent = t ? `正在播放：${t.title}` : '未播放';
  };

  const setStatus = (msg, type = 'info') => {
    if (!playStatus) return;
    playStatus.textContent = msg || '';
    let color = 'var(--ios-secondary)';
    if (type === 'warn') color = '#f0ad4e';
    else if (type === 'error') color = '#d9534f';
    playStatus.style.color = color;
  };

  const streamUrl = (id) => `/music/stream/${id}`;
  const previewUrl = (id) => `/music/preview/${id}`;

  const setSourceWithFallback = (id) => {
    let triedPreview = false;
    let previewFailed = false;
    const tryPlay = (url) => {
      audio.src = url;
      audio.play().catch(() => {
        // play() 可能因自动播放策略失败，依靠用户互动按钮
      });
    };
    audio.onerror = () => {
      if (!triedPreview) {
        triedPreview = true;
        setStatus('整曲播放受限，已为你切换到预览', 'warn');
        tryPlay(previewUrl(id));
      } else if (!previewFailed) {
        previewFailed = true;
        setStatus('播放失败，请登录后重试或稍后再试', 'error');
      }
    };
    setStatus('正在尝试播放...', 'info');
    tryPlay(streamUrl(id));
  };

  // 加载指定歌单（通过解析歌单页面HTML中的曲目链接）
  const loadPlaylistById = async (pid) => {
    try {
      setStatus('正在加载歌单...', 'info');
      const res = await fetch(`/playlist/view/${pid}`, { headers: { 'Accept': 'text/html' } });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const html = await res.text();
      const doc = new DOMParser().parseFromString(html, 'text/html');
      const anchors = doc.querySelectorAll('a[data-role="track"]');
      const nameEl = doc.querySelector('.ios-brand');
      const name = nameEl ? nameEl.textContent.trim() : `歌单 #${pid}`;
      const tracks = Array.from(anchors).map(a => ({
        id: parseInt(a.getAttribute('data-id')), title: a.textContent.trim()
      })).filter(t => Number.isFinite(t.id));
      if (!tracks.length) {
        setStatus(`歌单无曲目：${name}`, 'warn');
        return;
      }
      playlist = tracks;
      idx = 0;
      shuffleOrder = [];
      shufflePos = 0;
      updateNowPlaying();
      setModeLabel();
      setStatus(`已切换到歌单：${name}（${tracks.length}首）`, 'info');
    } catch (e) {
      setStatus('加载歌单失败，请稍后重试', 'error');
    }
  };

  const bindPlaylistControls = () => {
    if (btnLoadPlaylist) {
      btnLoadPlaylist.addEventListener('click', () => {
        const pid = playlistSelect && playlistSelect.value ? String(playlistSelect.value).trim() : '';
        if (!pid) {
          setStatus('请选择一个歌单', 'warn');
          return;
        }
        loadPlaylistById(pid);
      });
    }
    if (btnPlayAll) {
      btnPlayAll.addEventListener('click', () => {
        rebuildPlaylistFromDOM();
        updateNowPlaying();
        setStatus(`已载入全部音乐（${playlist.length}首）`, 'info');
      });
    }
  };

  const playAt = (i) => {
    if (!playlist.length) return;
    if (i < 0 || i >= playlist.length) return;
    idx = i;
    updateNowPlaying();
    setSourceWithFallback(playlist[idx].id);
    btnPlay.textContent = '⏸ 暂停';
  };

  const next = () => {
    if (!playlist.length) return;
    if (mode === MODE.SHUFFLE) {
      // 构建不含当前曲目的随机序列，并按序播放，直到用尽再重建
      if (!shuffleOrder.length || shufflePos >= shuffleOrder.length) {
        shuffleOrder = Array.from({ length: playlist.length }, (_, i) => i).filter(i => i !== idx);
        // Fisher-Yates 洗牌
        for (let i = shuffleOrder.length - 1; i > 0; i--) {
          const j = Math.floor(Math.random() * (i + 1));
          [shuffleOrder[i], shuffleOrder[j]] = [shuffleOrder[j], shuffleOrder[i]];
        }
        shufflePos = 0;
      }
      const n = shuffleOrder[shufflePos++];
      playAt(n);
    } else {
      // 顺序模式：末尾自动回到第一首循环
      const n = (idx + 1) % playlist.length;
      playAt(n);
    }
  };

  const prev = () => {
    if (!playlist.length) return;
    if (mode === MODE.SHUFFLE) {
      // 尝试回退一个位置；若不可回退，则随机选择一个非当前曲目
      if (shufflePos > 1) {
        const n = shuffleOrder[shufflePos - 2];
        shufflePos -= 2; // 回到上一个，再由 next 逻辑 +1
        playAt(n);
      } else {
        let n = Math.floor(Math.random() * playlist.length);
        if (playlist.length > 1) {
          while (n === idx) n = Math.floor(Math.random() * playlist.length);
        }
        playAt(n);
      }
    } else {
      if (idx - 1 >= 0) playAt(idx - 1);
      else {
        // 顺序模式：到开头回到最后一首
        playAt(playlist.length - 1);
      }
    }
  };

  // 事件绑定
  btnPlay.addEventListener('click', () => {
    if (!playlist.length) {
      rebuildPlaylistFromDOM();
    }
    if (!playlist.length) return;
    if (!audio.src) {
      playAt(idx);
      return;
    }
    if (audio.paused) {
      audio.play();
      btnPlay.textContent = '⏸ 暂停';
    } else {
      audio.pause();
      btnPlay.textContent = '▶ 播放';
    }
  });

  btnPrev.addEventListener('click', prev);
  btnNext.addEventListener('click', next);

  btnMode.addEventListener('click', () => {
    mode = mode === MODE.SEQ ? MODE.SINGLE : (mode === MODE.SINGLE ? MODE.SHUFFLE : MODE.SEQ);
    setModeLabel();
  });

  audio.addEventListener('ended', () => {
    if (mode === MODE.SINGLE) {
      audio.currentTime = 0;
      audio.play();
    } else {
      next();
    }
  });

  audio.addEventListener('playing', () => {
    const isPreview = (audio.src || '').includes('/preview/');
    setStatus(isPreview ? '正在播放预览片段' : '正在播放整曲', 'info');
  });

  audio.addEventListener('pause', () => {
    setStatus('已暂停', 'info');
  });

  // 列表点击播放
  const bindTrackClicks = () => {
    // 简易 iOS 风格右键菜单
    const ContextMenu = (() => {
      let menuEl = null;
      let backdropEl = null;
      const ensureMenu = () => {
        if (!menuEl) {
          menuEl = document.createElement('div');
          menuEl.id = 'ios-context-menu';
          Object.assign(menuEl.style, {
            position: 'fixed',
            zIndex: 9999,
            background: 'var(--blur-bg)',
            backdropFilter: 'blur(20px)',
            WebkitBackdropFilter: 'blur(20px)',
            border: '1px solid rgba(255,255,255,0.2)',
            borderRadius: '12px',
            boxShadow: '0 8px 24px rgba(0,0,0,0.15)',
            padding: '6px',
            minWidth: '180px',
            display: 'none',
            color: 'var(--ios-gray-1)'
          });
          document.body.appendChild(menuEl);
        }
        if (!backdropEl) {
          backdropEl = document.createElement('div');
          Object.assign(backdropEl.style, {
            position: 'fixed', left: '0', top: '0', right: '0', bottom: '0',
            zIndex: 9998, display: 'none'
          });
          backdropEl.addEventListener('click', () => hide());
          document.body.appendChild(backdropEl);
        }
      };
      const buildItems = (items) => {
        menuEl.innerHTML = '';
        items.forEach((it, idx) => {
          const btn = document.createElement('button');
          btn.type = 'button';
          btn.textContent = it.label;
          Object.assign(btn.style, {
            display: 'block', width: '100%', textAlign: 'left',
            padding: '10px 12px',
            border: 'none', background: 'transparent', color: 'var(--ios-gray-1)',
            borderRadius: '8px'
          });
          btn.addEventListener('mouseenter', () => {
            btn.style.background = 'rgba(255,255,255,0.08)';
          });
          btn.addEventListener('mouseleave', () => {
            btn.style.background = 'transparent';
          });
          btn.addEventListener('click', () => {
            hide();
            try { it.action && it.action(); } catch (e) {}
          });
          menuEl.appendChild(btn);
          if (idx < items.length - 1) {
            const hr = document.createElement('div');
            Object.assign(hr.style, {
              height: '1px', margin: '4px 0',
              background: 'rgba(255,255,255,0.12)'
            });
            menuEl.appendChild(hr);
          }
        });
      };
      const hide = () => {
        if (menuEl) menuEl.style.display = 'none';
        if (backdropEl) backdropEl.style.display = 'none';
        document.removeEventListener('keydown', onKeyDown);
      };
      const onKeyDown = (e) => { if (e.key === 'Escape') hide(); };
      const show = (x, y, items) => {
        ensureMenu();
        buildItems(items || []);
        backdropEl.style.display = 'block';
        menuEl.style.display = 'block';
        // 初步定位
        menuEl.style.left = Math.max(8, x || window.innerWidth / 2 - 90) + 'px';
        menuEl.style.top = Math.max(8, y || window.innerHeight / 2 - 60) + 'px';
        // 边界修正
        const rect = menuEl.getBoundingClientRect();
        let nx = rect.left, ny = rect.top;
        if (rect.right > window.innerWidth - 8) nx = window.innerWidth - rect.width - 8;
        if (rect.bottom > window.innerHeight - 8) ny = window.innerHeight - rect.height - 8;
        menuEl.style.left = nx + 'px';
        menuEl.style.top = ny + 'px';
        document.addEventListener('keydown', onKeyDown);
        return { hide };
      };
      return { show, hide };
    })();

    document.querySelectorAll('a[data-role="track"]').forEach((a, i) => {
      a.addEventListener('click', (e) => {
        e.preventDefault();
        // 若列表尚未建立，以 DOM 为准
        if (!playlist.length) rebuildPlaylistFromDOM();
        // 播放所点曲目
        playAt(i);
        // 显示 iOS 风格菜单
        ContextMenu.show(e.clientX, e.clientY, [
          { label: '进入该歌曲详情', action: () => { window.location.href = a.href; } },
          { label: '继续播放', action: () => {} }
        ]);
      });
      // 右键菜单支持
      a.addEventListener('contextmenu', (e) => {
        e.preventDefault();
        if (!playlist.length) rebuildPlaylistFromDOM();
        ContextMenu.show(e.clientX, e.clientY, [
          { label: '播放此曲目', action: () => { playAt(i); } },
          { label: '进入该歌曲详情', action: () => { window.location.href = a.href; } },
          { label: '取消', action: () => {} }
        ]);
      });
    });
  };

  document.addEventListener('DOMContentLoaded', () => {
    rebuildPlaylistFromDOM();
    bindTrackClicks();
    bindPlaylistControls();
    setModeLabel();
    updateNowPlaying();
    setStatus('', 'info');
  });
})();