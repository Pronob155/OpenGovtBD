// OpenGovtBD — shared front-end behaviors

document.addEventListener('DOMContentLoaded', function () {

  // --- Mobile sidebar toggle ---
  const menuBtn = document.querySelector('.mobile-menu-btn');
  const sidebar = document.querySelector('.sidebar');
  const overlay = document.querySelector('.sidebar-overlay');
  if (menuBtn && sidebar) {
    menuBtn.addEventListener('click', function () {
      sidebar.classList.toggle('open');
      if (overlay) overlay.classList.toggle('show');
    });
  }
  if (overlay) {
    overlay.addEventListener('click', function () {
      sidebar.classList.remove('open');
      overlay.classList.remove('show');
    });
  }

  // --- Auto-dismiss flash alerts ---
  document.querySelectorAll('[data-flash]').forEach(function (el) {
    setTimeout(function () {
      el.style.transition = 'opacity .4s ease';
      el.style.opacity = '0';
      setTimeout(function () { el.remove(); }, 400);
    }, 4500);
  });

  document.querySelectorAll('.progress-ring-fg').forEach(function (circle) {
    const pct = parseFloat(circle.getAttribute('data-pct') || '0');
    const radius = circle.r.baseVal.value;
    const circumference = 2 * Math.PI * radius;
    circle.style.strokeDasharray = circumference;
    circle.style.strokeDashoffset = circumference;
    requestAnimationFrame(function () {
      const offset = circumference - (pct / 100) * circumference;
      circle.style.strokeDashoffset = offset;
    });
  });

  // --- Animate progress / poll bars from width 0 ---
  document.querySelectorAll('[data-bar-pct]').forEach(function (bar) {
    const pct = bar.getAttribute('data-bar-pct');
    requestAnimationFrame(function () { bar.style.width = pct + '%'; });
  });

  // --- Notification dropdown ---
  const bell = document.querySelector('[data-notif-toggle]');
  const panel = document.querySelector('[data-notif-panel]');
  if (bell && panel) {
    bell.addEventListener('click', function (e) {
      e.stopPropagation();
      panel.classList.toggle('show');
    });
    document.addEventListener('click', function (e) {
      if (!panel.contains(e.target)) panel.classList.remove('show');
    });
  }

  document.querySelectorAll('[data-autosubmit]').forEach(function (el) {
    el.addEventListener('change', function () { el.closest('form').submit(); });
  });

  const revealEls = document.querySelectorAll('.reveal');
  if ('IntersectionObserver' in window && revealEls.length) {
    const io = new IntersectionObserver(function (entries) {
      entries.forEach(function (entry) {
        if (entry.isIntersecting) {
          entry.target.classList.add('fade-up');
          io.unobserve(entry.target);
        }
      });
    }, { threshold: 0.15 });
    revealEls.forEach(function (el) { io.observe(el); });
  }

  document.querySelectorAll('[data-client-theme-toggle]').forEach(function (btn) {
    const stored = localStorage.getItem('ogbd-theme');
    if (stored === 'dark') { document.body.classList.add('dark'); btn.classList.add('on'); }
    btn.addEventListener('click', function () {
      const nowDark = !document.body.classList.contains('dark');
      document.body.classList.toggle('dark', nowDark);
      btn.classList.toggle('on', nowDark);
      localStorage.setItem('ogbd-theme', nowDark ? 'dark' : 'light');
    });
  });

  // --- Tab strips (Saved items, etc.) ---
  document.querySelectorAll('[data-tabs]').forEach(function (wrap) {
    const buttons = wrap.querySelectorAll('.tab-strip button');
    const panels = wrap.querySelectorAll('.tab-panel');
    buttons.forEach(function (btn) {
      btn.addEventListener('click', function () {
        buttons.forEach(function (b) { b.classList.remove('active'); });
        panels.forEach(function (p) { p.classList.remove('active'); });
        btn.classList.add('active');
        const target = wrap.querySelector('[data-tab-panel="' + btn.getAttribute('data-tab') + '"]');
        if (target) target.classList.add('active');
      });
    });
  });

  document.querySelectorAll('[data-reply-toggle]').forEach(function (btn) {
    btn.addEventListener('click', function () {
      const form = document.querySelector('[data-reply-form="' + btn.getAttribute('data-reply-toggle') + '"]');
      if (form) form.classList.toggle('show');
    });
  });

  // --- Verification upload preview ---
  document.querySelectorAll('.upload-box input[type=file]').forEach(function (input) {
    input.addEventListener('change', function () {
      const box = input.closest('.upload-box');
      if (!input.files || !input.files[0]) return;
      box.classList.add('has-file');
      let preview = box.querySelector('.preview');
      if (!preview) {
        preview = document.createElement('img');
        preview.className = 'preview';
        box.appendChild(preview);
      }
      preview.src = URL.createObjectURL(input.files[0]);
      const label = box.querySelector('.upload-label');
      if (label) label.textContent = input.files[0].name;
    });
  });

  const celebrationTrigger = document.querySelector('[data-celebrate-completion]');
  if (celebrationTrigger) {
    const pct = parseInt(celebrationTrigger.getAttribute('data-celebrate-completion'), 10);
    const key = 'ogbd-celebrated-' + (celebrationTrigger.getAttribute('data-user-id') || 'me');
    if (pct >= 100 && !localStorage.getItem(key)) {
      localStorage.setItem(key, '1');
      showCelebration();
    }
  }

  function showCelebration() {
    const backdrop = document.createElement('div');
    backdrop.className = 'modal-backdrop';
    backdrop.innerHTML =
      '<div class="celebration-card">' +
      '<div class="trophy">🎉</div>' +
      '<h2 style="margin:0 0 8px;">Profile Complete!</h2>' +
      '<p class="text-muted" style="margin:0 0 20px;">Congratulations — your citizen profile is 100% complete. You now have full access to every service.</p>' +
      '<button class="btn btn-primary btn-block" type="button" data-close-celebration>Awesome, thanks!</button>' +
      '</div>';
    document.body.appendChild(backdrop);
    const colors = ['#0B4F8A', '#046A38', '#F5A623', '#DC2626', '#8B5CF6'];
    for (let i = 0; i < 40; i++) {
      const c = document.createElement('div');
      c.className = 'confetti';
      c.style.left = Math.random() * 100 + '%';
      c.style.background = colors[i % colors.length];
      c.style.animationDelay = (Math.random() * 0.6) + 's';
      backdrop.querySelector('.celebration-card').appendChild(c);
    }
    function close() { backdrop.remove(); }
    backdrop.addEventListener('click', function (e) { if (e.target === backdrop) close(); });
    backdrop.querySelector('[data-close-celebration]').addEventListener('click', close);
  }

  window.ogbdToast = function (message, type) {
    let stack = document.querySelector('.toast-stack');
    if (!stack) {
      stack = document.createElement('div');
      stack.className = 'toast-stack';
      document.body.appendChild(stack);
    }
    const toast = document.createElement('div');
    toast.className = 'toast ' + (type || 'info');
    const icon = type === 'success' ? 'check_circle' : type === 'error' ? 'error' : 'info';
    toast.innerHTML = '<span class="material-symbols-rounded">' + icon + '</span><span>' + message + '</span>';
    stack.appendChild(toast);
    setTimeout(function () {
      toast.style.transition = 'opacity .3s ease, transform .3s ease';
      toast.style.opacity = '0';
      toast.style.transform = 'translateX(30px)';
      setTimeout(function () { toast.remove(); }, 300);
    }, 3500);
  };

  // --- Facebook-style profile preview popover on @mention hover/click ---
  let popoverEl = null;
  let popoverHideTimer = null;

  function ensurePopover() {
    if (!popoverEl) {
      popoverEl = document.createElement('div');
      popoverEl.className = 'profile-popover';
      document.body.appendChild(popoverEl);
      popoverEl.addEventListener('mouseenter', function () { clearTimeout(popoverHideTimer); });
      popoverEl.addEventListener('mouseleave', hidePopoverSoon);
    }
    return popoverEl;
  }

  function hidePopoverSoon() {
    popoverHideTimer = setTimeout(function () {
      if (popoverEl) popoverEl.classList.remove('show');
    }, 200);
  }

  document.addEventListener('mouseover', function (e) {
    const link = e.target.closest && e.target.closest('[data-mention-preview]');
    if (!link) return;
    clearTimeout(popoverHideTimer);
    const url = link.getAttribute('data-mention-preview');
    const el = ensurePopover();
    fetch(url).then(function (r) { return r.text(); }).then(function (html) {
      el.innerHTML = html;
      const rect = link.getBoundingClientRect();
      el.style.left = Math.max(12, Math.min(window.innerWidth - 296, rect.left)) + 'px';
      el.style.top = (rect.bottom + 8) + 'px';
      el.classList.add('show');
    }).catch(function () {});
  });

  document.addEventListener('mouseout', function (e) {
    const link = e.target.closest && e.target.closest('[data-mention-preview]');
    if (link) hidePopoverSoon();
  });

  document.querySelectorAll('[data-mention-input]').forEach(function (input) {
    let box = null;
    input.addEventListener('input', function () {
      const value = input.value;
      const caret = input.selectionStart;
      const upToCaret = value.slice(0, caret);
      const match = upToCaret.match(/@([a-zA-Z0-9._-]{1,40})$/);
      if (!match) { if (box) box.remove(); box = null; return; }
      fetch('/api/mentions/search?q=' + encodeURIComponent(match[1]))
        .then(function (r) { return r.json(); })
        .then(function (users) {
          if (box) box.remove();
          if (!users.length) { box = null; return; }
          box = document.createElement('div');
          box.className = 'card';
          box.style.cssText = 'position:absolute;z-index:50;max-width:260px;padding:6px;margin-top:4px;';
          users.forEach(function (u) {
            const item = document.createElement('div');
            item.textContent = '@' + u.username + '  ·  ' + u.fullName;
            item.style.cssText = 'padding:8px 10px;border-radius:8px;cursor:pointer;font-size:13px;';
            item.addEventListener('mouseenter', function () { item.style.background = 'var(--bg)'; });
            item.addEventListener('mouseleave', function () { item.style.background = 'transparent'; });
            item.addEventListener('click', function () {
              input.value = upToCaret.replace(/@([a-zA-Z0-9._-]{1,40})$/, '@' + u.username + ' ') + value.slice(caret);
              input.focus();
              box.remove(); box = null;
            });
            box.appendChild(item);
          });
          input.parentElement.style.position = 'relative';
          input.parentElement.appendChild(box);
        }).catch(function () {});
    });
    input.addEventListener('blur', function () { setTimeout(function () { if (box) { box.remove(); box = null; } }, 150); });
  });
});
