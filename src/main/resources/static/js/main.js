/* =========================================================
   OpenGovtBD — Modern front-end (v2)
   Modular, accessible interactions and UX enhancements.
   ========================================================= */

(function () {
  'use strict';

  // ------- Utilities -------
  const $ = (sel, ctx) => (ctx || document).querySelector(sel);
  const $$ = (sel, ctx) => Array.from((ctx || document).querySelectorAll(sel));
  const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

  // ------- Storage (never throws) -------
  // localStorage access raises in private windows and when site data is
  // blocked; every call site used to assume it always succeeds.
  const store = {
    get(key) {
      try {
        return localStorage.getItem(key);
      } catch (e) {
        return null;
      }
    },
    set(key, value) {
      try {
        localStorage.setItem(key, value);
      } catch (e) {
        /* preference simply will not persist */
      }
    },
  };

  // ------- Theme management -------
  //
  // Three inputs used to fight over the theme: the inline pre-paint script,
  // this module's localStorage read, and the server-rendered `body.dark` that
  // reflects Citizen.darkMode. They are reconciled here — a signed-in user's
  // stored preference wins on load, and any in-page toggle writes back to both
  // localStorage and the server so the two cannot drift apart again.
  const Theme = {
    init() {
      const root = document.documentElement;
      const serverDark = document.body.classList.contains('dark');
      const stored = store.get('ogbd-theme');
      // A server-rendered preference is the account's own setting, so it wins
      // over whatever this browser happened to remember.
      const dark = serverDark || stored === 'dark';
      this.apply(dark);
      if (serverDark && stored !== 'dark') store.set('ogbd-theme', 'dark');
      root.classList.remove('pre-dark');
      this.bindToggles();
      this.bindForms();
    },

    apply(dark) {
      document.body.classList.toggle('dark', dark);
      document.documentElement.style.colorScheme = dark ? 'dark' : 'light';
      $$('[data-client-theme-toggle]').forEach((btn) => {
        btn.classList.toggle('on', dark);
        btn.setAttribute('aria-pressed', String(dark));
        const icon = btn.querySelector('.theme-icon');
        if (icon) icon.textContent = dark ? 'light_mode' : 'dark_mode';
      });
      $$('[data-theme-switch]').forEach((sw) => {
        sw.classList.toggle('on', dark);
        sw.setAttribute('aria-checked', String(dark));
      });
    },

    set(dark) {
      this.apply(dark);
      store.set('ogbd-theme', dark ? 'dark' : 'light');
      // Fire-and-forget: signed-out visitors get a 302/401 here, which is fine.
      fetch('/citizen/theme/set?dark=' + dark, {
        method: 'POST',
        credentials: 'same-origin',
        headers: { 'X-Requested-With': 'fetch' },
      }).catch(() => {});
      window.dispatchEvent(new CustomEvent('themechange', { detail: { dark } }));
    },

    bindToggles() {
      $$('[data-client-theme-toggle]').forEach((btn) => {
        btn.addEventListener('click', () => {
          this.set(!document.body.classList.contains('dark'));
        });
      });
    },

    // The sidebar switch posts a real form so it still works without JS. With
    // JS we intercept it and flip in place rather than round-tripping.
    bindForms() {
      $$('[data-theme-form]').forEach((form) => {
        form.addEventListener('submit', (e) => {
          e.preventDefault();
          this.set(!document.body.classList.contains('dark'));
        });
      });
    },
  };

  // ------- Sidebar (mobile + desktop) -------
  const Sidebar = {
    init() {
      const menuBtn = $('.mobile-menu-btn');
      const sidebar = $('.sidebar');
      const overlay = $('.sidebar-overlay');
      if (!menuBtn || !sidebar) return;
      this.menuBtn = menuBtn;
      menuBtn.addEventListener('click', () => {
        const open = !sidebar.classList.contains('open');
        sidebar.classList.toggle('open', open);
        if (overlay) overlay.classList.toggle('show', open);
        menuBtn.setAttribute('aria-expanded', String(open));
        // The drawer sits over the page on mobile, so focus has to follow it
        // in and come back to the trigger on close.
        if (open) {
          const first = sidebar.querySelector('a, button');
          if (first) first.focus();
        }
      });
      if (overlay) {
        overlay.addEventListener('click', () => this.close());
      }
      // Close on link click in mobile
      $$('.sidebar .nav-link').forEach((link) => {
        link.addEventListener('click', () => {
          if (window.innerWidth <= 960) this.close(false);
        });
      });
      // Close on ESC
      document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && sidebar.classList.contains('open')) this.close();
      });
    },
    close(restoreFocus = true) {
      const sidebar = $('.sidebar');
      const overlay = $('.sidebar-overlay');
      if (sidebar) sidebar.classList.remove('open');
      if (overlay) overlay.classList.remove('show');
      if (this.menuBtn) {
        this.menuBtn.setAttribute('aria-expanded', 'false');
        if (restoreFocus) this.menuBtn.focus();
      }
    },
  };

  // ------- Flash alerts -------
  const Flash = {
    init() {
      $$('[data-flash]').forEach((el) => {
        setTimeout(() => {
          el.style.transition = 'opacity .4s ease, transform .4s ease';
          el.style.opacity = '0';
          el.style.transform = 'translateY(-6px)';
          setTimeout(() => el.remove(), 400);
        }, 4500);
      });
    },
  };

  // ------- Progress ring animation -------
  const Progress = {
    init() {
      $$('.progress-ring-fg').forEach((circle) => {
        const pct = parseFloat(circle.getAttribute('data-pct') || '0');
        const radius = circle.r.baseVal.value;
        const circumference = 2 * Math.PI * radius;
        circle.style.strokeDasharray = circumference;
        circle.style.strokeDashoffset = circumference;
        requestAnimationFrame(() => {
          circle.style.strokeDashoffset = circumference - (pct / 100) * circumference;
        });
      });

      // Poll / progress bars
      $$('[data-bar-pct]').forEach((bar) => {
        const pct = bar.getAttribute('data-bar-pct');
        requestAnimationFrame(() => {
          bar.style.width = pct + '%';
        });
      });
    },
  };

  // ------- Auto-submit forms -------
  const AutoSubmit = {
    init() {
      $$('[data-autosubmit]').forEach((el) => {
        el.addEventListener('change', () => {
          // Guard: the control is not always inside a form, and calling
          // .submit() on undefined threw and killed the rest of the handler.
          const form = el.closest('form');
          if (!form) return;
          // requestSubmit runs validation and submit handlers; .submit() skips
          // both, which silently bypassed any required field on the form.
          if (typeof form.requestSubmit === 'function') form.requestSubmit();
          else form.submit();
        });
      });
    },
  };

  // ------- Reveal on scroll -------
  const Reveal = {
    init() {
      const revealEls = $$('.reveal');
      if (!('IntersectionObserver' in window) || !revealEls.length) {
        revealEls.forEach((el) => el.classList.add('fade-up'));
        return;
      }
      const io = new IntersectionObserver(
        (entries) => {
          entries.forEach((entry) => {
            if (entry.isIntersecting) {
              entry.target.classList.add('fade-up');
              io.unobserve(entry.target);
            }
          });
        },
        { threshold: 0.15, rootMargin: '0px 0px -40px 0px' }
      );
      revealEls.forEach((el) => io.observe(el));
    },
  };

  // ------- Tab strips -------
  const Tabs = {
    init() {
      $$('[data-tabs]').forEach((wrap) => {
        const buttons = $$('.tab-strip button, .tab-underline button', wrap);
        const panels = $$('.tab-panel', wrap);
        buttons.forEach((btn) => {
          btn.addEventListener('click', () => {
            buttons.forEach((b) => b.classList.remove('active'));
            panels.forEach((p) => p.classList.remove('active'));
            btn.classList.add('active');
            const target = wrap.querySelector(
              '[data-tab-panel="' + btn.getAttribute('data-tab') + '"]'
            );
            if (target) target.classList.add('active');
          });
        });
      });
    },
  };

  // ------- Reply-to-comment -------
  const ReplyToggle = {
    init() {
      $$('[data-reply-toggle]').forEach((btn) => {
        btn.addEventListener('click', () => {
          const form = document.querySelector(
            '[data-reply-form="' + btn.getAttribute('data-reply-toggle') + '"]'
          );
          if (form) form.classList.toggle('show');
        });
      });
    },
  };

  // ------- Upload preview -------
  const Upload = {
    init() {
      $$('.upload-box input[type=file]').forEach((input) => {
        input.addEventListener('change', () => {
          const box = input.closest('.upload-box');
          if (!input.files || !input.files[0]) return;
          box.classList.add('has-file');
          let preview = box.querySelector('.preview');
          if (!preview) {
            preview = document.createElement('img');
            preview.className = 'preview';
            preview.alt = '';
            box.appendChild(preview);
          }
          // Each createObjectURL pins its blob in memory until revoked; picking
          // a new file repeatedly used to leak every previous one.
          if (preview.dataset.objectUrl) URL.revokeObjectURL(preview.dataset.objectUrl);
          const url = URL.createObjectURL(input.files[0]);
          preview.dataset.objectUrl = url;
          preview.src = url;
          const label = box.querySelector('.upload-label');
          if (label) label.textContent = input.files[0].name;
        });
      });
    },
  };

  // ------- Celebration modal -------
  const Celebration = {
    init() {
      const trigger = $('[data-celebrate-completion]');
      if (!trigger) return;
      const pct = parseInt(trigger.getAttribute('data-celebrate-completion'), 10);
      const key = 'ogbd-celebrated-' + (trigger.getAttribute('data-user-id') || 'me');
      if (pct >= 100 && !store.get(key)) {
        store.set(key, '1');
        this.show();
      }
    },
    show() {
      const previousFocus = document.activeElement;
      const backdrop = document.createElement('div');
      backdrop.className = 'modal-backdrop';

      const card = document.createElement('div');
      card.className = 'celebration-card';
      card.setAttribute('role', 'dialog');
      card.setAttribute('aria-modal', 'true');
      card.setAttribute('aria-labelledby', 'celebration-title');

      const trophy = document.createElement('div');
      trophy.className = 'trophy';
      trophy.textContent = '🎉';
      trophy.setAttribute('aria-hidden', 'true');

      const title = document.createElement('h2');
      title.id = 'celebration-title';
      // Confident, not shouted — the exclamation mark added nothing.
      title.textContent = 'Your profile is complete';

      const body = document.createElement('p');
      body.className = 'text-muted';
      body.textContent =
        'Every section of your citizen profile is filled in, so all services on OpenGovtBD are now open to you.';

      const dismiss = document.createElement('button');
      dismiss.type = 'button';
      dismiss.className = 'btn btn-primary btn-block';
      dismiss.textContent = 'Continue';

      card.append(trophy, title, body, dismiss);
      backdrop.appendChild(card);
      document.body.appendChild(backdrop);

      if (!reduceMotion) {
        const colors = ['#0B4F8A', '#046A38', '#F59E0B', '#16564F'];
        for (let i = 0; i < 40; i++) {
          const c = document.createElement('div');
          c.className = 'confetti';
          c.style.left = Math.random() * 100 + '%';
          c.style.background = colors[i % colors.length];
          c.style.animationDelay = Math.random() * 0.6 + 's';
          card.appendChild(c);
        }
      }

      // The dialog previously trapped nothing and ignored Escape, so keyboard
      // users could tab behind it with no way to dismiss it.
      const onKey = (e) => {
        if (e.key === 'Escape') close();
        if (e.key !== 'Tab') return;
        const focusable = $$('button, [href], input, select, textarea', card);
        if (!focusable.length) return;
        const first = focusable[0];
        const last = focusable[focusable.length - 1];
        if (e.shiftKey && document.activeElement === first) {
          e.preventDefault();
          last.focus();
        } else if (!e.shiftKey && document.activeElement === last) {
          e.preventDefault();
          first.focus();
        }
      };

      const close = () => {
        document.removeEventListener('keydown', onKey);
        backdrop.remove();
        if (previousFocus && previousFocus.focus) previousFocus.focus();
      };

      document.addEventListener('keydown', onKey);
      backdrop.addEventListener('click', (e) => {
        if (e.target === backdrop) close();
      });
      dismiss.addEventListener('click', close);
      dismiss.focus();
    },
  };

  // ------- Toast notifications -------
  const Toast = {
    stack: null,
    ensureStack() {
      if (!this.stack) {
        this.stack = document.createElement('div');
        this.stack.className = 'toast-stack';
        // Announced politely so screen readers hear the confirmation the
        // sighted user sees; the stack used to be silent to assistive tech.
        this.stack.setAttribute('role', 'status');
        this.stack.setAttribute('aria-live', 'polite');
        document.body.appendChild(this.stack);
      }
    },
    show(message, type = 'info', options = {}) {
      this.ensureStack();
      const toast = document.createElement('div');
      toast.className = 'toast-rich ' + type;

      const iconName = type === 'success' ? 'check_circle' : type === 'error' ? 'error' : 'info';
      const iconWrap = document.createElement('div');
      iconWrap.className = 'toast-icon';
      const icon = document.createElement('span');
      icon.className = 'material-symbols-rounded';
      icon.textContent = iconName;
      iconWrap.appendChild(icon);

      // Message and title are built as text nodes. They were concatenated into
      // innerHTML, so any caller passing server or user text injected markup.
      const body = document.createElement('div');
      body.className = 'toast-body';
      if (options.title) {
        const title = document.createElement('div');
        title.className = 'toast-title';
        title.textContent = options.title;
        body.appendChild(title);
      }
      const msg = document.createElement('div');
      msg.className = 'toast-msg';
      msg.textContent = message;
      body.appendChild(msg);

      toast.append(iconWrap, body);
      this.stack.appendChild(toast);

      const dur = options.duration || 3500;
      setTimeout(() => {
        toast.style.transition = 'opacity .3s ease, transform .3s ease';
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(30px)';
        setTimeout(() => toast.remove(), 300);
      }, dur);
    },
  };

  // ------- Notification dropdown -------
  const Notif = {
    init() {
      const bell = $('[data-notif-toggle]');
      const panel = $('[data-notif-panel]');
      if (!bell || !panel) return;

      const setOpen = (open) => {
        panel.classList.toggle('show', open);
        bell.setAttribute('aria-expanded', String(open));
      };
      bell.setAttribute('aria-expanded', 'false');
      bell.setAttribute('aria-haspopup', 'true');

      bell.addEventListener('click', (e) => {
        e.stopPropagation();
        setOpen(!panel.classList.contains('show'));
      });
      document.addEventListener('click', (e) => {
        if (!panel.contains(e.target) && !bell.contains(e.target)) setOpen(false);
      });
      document.addEventListener('keydown', (e) => {
        if (e.key !== 'Escape' || !panel.classList.contains('show')) return;
        setOpen(false);
        bell.focus();
      });
    },
  };

  // ------- @Mention autocomplete -------
  const Mention = {
    HANDLE: /@([a-zA-Z0-9._-]{1,40})$/,
    init() {
      $$('[data-mention-input]').forEach((input) => {
        let box = null;
        // Responses can arrive out of order; only the newest query may render.
        let seq = 0;

        const dismiss = () => {
          if (box) box.remove();
          box = null;
        };

        input.addEventListener('input', () => {
          const value = input.value;
          const caret = input.selectionStart;
          const upToCaret = value.slice(0, caret);
          const match = upToCaret.match(this.HANDLE);
          if (!match) {
            dismiss();
            return;
          }
          const token = ++seq;
          fetch('/api/mentions/search?q=' + encodeURIComponent(match[1]))
            .then((r) => (r.ok ? r.json() : []))
            .then((users) => {
              if (token !== seq) return;
              dismiss();
              if (!users.length) return;

              box = document.createElement('div');
              box.className = 'card mention-box';
              users.forEach((u) => {
                // Built from DOM nodes rather than innerHTML: usernames and
                // display names are user-controlled, so concatenating them
                // into markup made every mention search an XSS sink.
                const item = document.createElement('button');
                item.type = 'button';
                item.className = 'mention-option';

                const avatar = document.createElement('span');
                avatar.className = 'avatar mention-option-avatar';
                avatar.textContent = u.fullName ? u.fullName.charAt(0).toUpperCase() : 'U';

                const text = document.createElement('span');
                const handle = document.createElement('b');
                handle.textContent = '@' + u.username;
                const name = document.createElement('span');
                name.className = 'mention-option-name';
                name.textContent = u.fullName || '';
                text.appendChild(handle);
                text.appendChild(name);

                item.appendChild(avatar);
                item.appendChild(text);
                item.addEventListener('mousedown', (e) => e.preventDefault());
                item.addEventListener('click', () => {
                  // Function replacement, not a string: a '$' in a username
                  // would otherwise be read as a $& / $1 substitution pattern.
                  input.value =
                    upToCaret.replace(this.HANDLE, () => '@' + u.username + ' ') + value.slice(caret);
                  const pos = input.value.length - value.slice(caret).length;
                  input.focus();
                  input.setSelectionRange(pos, pos);
                  dismiss();
                });
                box.appendChild(item);
              });
              input.parentElement.classList.add('mention-anchor');
              input.parentElement.appendChild(box);
            })
            .catch(() => {});
        });

        input.addEventListener('keydown', (e) => {
          if (e.key === 'Escape' && box) {
            e.stopPropagation();
            dismiss();
          }
        });
        input.addEventListener('blur', () => setTimeout(dismiss, 150));
      });
    },
  };

  // ------- Profile preview popover -------
  const Popover = {
    el: null,
    hideTimer: null,
    init() {
      document.addEventListener('mouseover', (e) => {
        const link = e.target.closest && e.target.closest('[data-mention-preview]');
        if (!link) return;
        clearTimeout(this.hideTimer);
        this.show(link);
      });
      document.addEventListener('mouseout', (e) => {
        const link = e.target.closest && e.target.closest('[data-mention-preview]');
        if (link) this.hideSoon();
      });
    },
    show(link) {
      const url = link.getAttribute('data-mention-preview');
      if (!this.el) {
        this.el = document.createElement('div');
        this.el.className = 'profile-popover';
        document.body.appendChild(this.el);
        this.el.addEventListener('mouseenter', () => clearTimeout(this.hideTimer));
        this.el.addEventListener('mouseleave', () => this.hideSoon());
      }
      const rect = link.getBoundingClientRect();
      fetch(url)
        .then((r) => r.text())
        .then((html) => {
          this.el.innerHTML = html;
          this.el.style.left =
            Math.max(12, Math.min(window.innerWidth - 320, rect.left)) + 'px';
          this.el.style.top = rect.bottom + 8 + 'px';
          this.el.classList.add('show');
        })
        .catch(() => {});
    },
    hideSoon() {
      this.hideTimer = setTimeout(() => {
        if (this.el) this.el.classList.remove('show');
      }, 200);
    },
  };

  // ------- Tilt on hover (landing cards) -------
  const Tilt = {
    init() {
      if (reduceMotion) return;
      $$('.tilt').forEach((card) => {
        card.addEventListener('mousemove', (e) => {
          const rect = card.getBoundingClientRect();
          const x = (e.clientX - rect.left) / rect.width - 0.5;
          const y = (e.clientY - rect.top) / rect.height - 0.5;
          const maxTilt = 6;
          card.style.transform =
            'perspective(800px) rotateX(' +
            -y * maxTilt +
            'deg) rotateY(' +
            x * maxTilt +
            'deg) translateY(-2px)';
        });
        card.addEventListener('mouseleave', () => {
          card.style.transform = '';
        });
      });
    },
  };

  // ------- Floating CTA visibility -------
  const FloatingCTA = {
    init() {
      const fab = $('.floating-cta');
      const hero = $('.hero');
      const footer = $('.site-footer, .footer-v2');
      if (!fab || !hero) return;
      window.addEventListener(
        'scroll',
        () => {
          const heroBottom = hero.getBoundingClientRect().bottom;
          const footerTop = footer ? footer.getBoundingClientRect().top : Infinity;
          const pastHero = heroBottom < 0;
          const nearFooter = footerTop < window.innerHeight;
          if (pastHero && !nearFooter) fab.classList.add('show');
          else fab.classList.remove('show');
        },
        { passive: true }
      );
    },
  };

  // ------- Sliding-pill auth tabs -------
  const SlidingPill = {
    init() {
      $$('.role-tabs-slide').forEach((group) => {
        const thumb = group.querySelector('.slide-thumb');
        const active = group.querySelector('a.active');
        if (!thumb || !active) return;
        const place = () => {
          thumb.style.left = active.offsetLeft + 'px';
          thumb.style.width = active.offsetWidth + 'px';
        };
        place();
        window.addEventListener('resize', place);
        // Update on tab click
        $$('a', group).forEach((a) => {
          a.addEventListener('click', () => {
            $$('a', group).forEach((x) => x.classList.remove('active'));
            a.classList.add('active');
            place();
          });
        });
      });
    },
  };

  // ------- Animated counters -------
  const Counters = {
    // The suffix ("+", "%") lives on the element, not baked into the number,
    // so the animation no longer strips it off the server-rendered value.
    render(el, value) {
      const suffix = el.getAttribute('data-counter-suffix') || '';
      el.textContent = value + suffix;
    },
    init() {
      const counters = $$('[data-counter]');
      if (!counters.length) return;
      if (reduceMotion) {
        counters.forEach((el) => this.render(el, el.getAttribute('data-counter')));
        return;
      }
      const animate = (el) => {
        const target = parseFloat(el.getAttribute('data-counter'));
        if (!Number.isFinite(target)) return;
        const dur = parseInt(el.getAttribute('data-counter-duration') || '1200', 10);
        const start = performance.now();
        const startVal = 0;
        const tick = (now) => {
          const t = Math.min(1, (now - start) / dur);
          const eased = 1 - Math.pow(1 - t, 3);
          const v = startVal + (target - startVal) * eased;
          this.render(el, Number.isInteger(target) ? Math.round(v) : v.toFixed(1));
          if (t < 1) requestAnimationFrame(tick);
        };
        requestAnimationFrame(tick);
      };
      if ('IntersectionObserver' in window) {
        const io = new IntersectionObserver(
          (entries) => {
            entries.forEach((entry) => {
              if (entry.isIntersecting) {
                animate(entry.target);
                io.unobserve(entry.target);
              }
            });
          },
          { threshold: 0.3 }
        );
        counters.forEach((el) => io.observe(el));
      } else {
        counters.forEach(animate);
      }
    },
  };

  // ------- Accordion -------
  const Accordion = {
    init() {
      $$('.accordion-item').forEach((item) => {
        const header = item.querySelector('.accordion-header');
        if (!header) return;
        header.addEventListener('click', () => {
          const wasOpen = item.classList.contains('open');
          // Close siblings
          const parent = item.parentElement;
          if (parent && parent.classList.contains('accordion-group')) {
            $$('.accordion-item', parent).forEach((i) => i.classList.remove('open'));
          }
          if (!wasOpen) item.classList.add('open');
        });
      });
    },
  };

  // ------- Tag input -------
  const TagInput = {
    init() {
      $$('[data-tag-input]').forEach((wrap) => {
        const input = wrap.querySelector('input');
        const hidden = wrap.querySelector('input[type=hidden]');
        if (!input) return;
        const update = () => {
          if (hidden) hidden.value = $$('.tag', wrap).map((t) => t.dataset.value).join(',');
        };
        const addTag = (val) => {
          const tag = document.createElement('span');
          tag.className = 'tag';
          tag.dataset.value = val;
          tag.innerHTML = val + ' <button type="button" aria-label="Remove">&times;</button>';
          tag.querySelector('button').addEventListener('click', () => {
            tag.remove();
            update();
          });
          wrap.insertBefore(tag, input);
          update();
        };
        input.addEventListener('keydown', (e) => {
          if (e.key === 'Enter' || e.key === ',') {
            e.preventDefault();
            const v = input.value.trim();
            if (v) {
              addTag(v);
              input.value = '';
            }
          } else if (e.key === 'Backspace' && !input.value) {
            const tags = $$('.tag', wrap);
            if (tags.length) tags[tags.length - 1].remove();
            update();
          }
        });
        // Initialize from hidden
        if (hidden && hidden.value) {
          hidden.value
            .split(',')
            .map((s) => s.trim())
            .filter(Boolean)
            .forEach(addTag);
        }
      });
    },
  };

  // ------- Command palette (Cmd/Ctrl+K) -------
  const CommandPalette = {
    // Destinations used to be a fixed citizen-only list, so an officer or
    // admin hitting Ctrl+K got links into a section they cannot open. The
    // set is now chosen from the role the server rendered onto <body>.
    ROUTES: {
      CITIZEN: [
        { icon: 'space_dashboard', label: 'Dashboard', href: '/citizen/dashboard' },
        { icon: 'apps', label: 'Digital services', href: '/citizen/services' },
        { icon: 'emergency', label: 'Emergency contacts', href: '/citizen/emergency' },
        { icon: 'report', label: 'Complaints', href: '/citizen/complaints' },
        { icon: 'add_circle', label: 'File a complaint', href: '/citizen/complaints/new' },
        { icon: 'forum', label: 'Discussions', href: '/citizen/discussions' },
        { icon: 'how_to_vote', label: 'Polls', href: '/citizen/polls' },
        { icon: 'lightbulb', label: 'Suggestion box', href: '/citizen/suggestions' },
        { icon: 'trophy', label: 'Leaderboard', href: '/citizen/leaderboard' },
        { icon: 'history', label: 'My activity', href: '/citizen/activity' },
        { icon: 'notifications', label: 'Notifications', href: '/citizen/notifications' },
        { icon: 'bookmark', label: 'Saved items', href: '/citizen/saved' },
        { icon: 'verified_user', label: 'Get verified', href: '/citizen/verification' },
        { icon: 'person', label: 'Profile and settings', href: '/citizen/profile' },
      ],
      OFFICER: [
        { icon: 'space_dashboard', label: 'Dashboard', href: '/officer/dashboard' },
        { icon: 'assignment', label: 'Complaint queue', href: '/officer/complaints' },
        { icon: 'gavel', label: 'Discussion approval', href: '/officer/discussions' },
        { icon: 'rate_review', label: 'Suggestion review', href: '/officer/suggestions' },
        { icon: 'how_to_vote', label: 'Create a poll', href: '/officer/polls/new' },
        { icon: 'apps', label: 'Manage services', href: '/officer/services' },
      ],
      ADMIN: [
        { icon: 'space_dashboard', label: 'Overview', href: '/admin/dashboard' },
        { icon: 'group', label: 'Citizens and officers', href: '/admin/users' },
        { icon: 'apps', label: 'Services', href: '/admin/services' },
        { icon: 'forum', label: 'Discussions', href: '/admin/discussions' },
        { icon: 'summarize', label: 'Reports', href: '/admin/reports' },
        { icon: 'monitoring', label: 'Analytics', href: '/admin/analytics' },
      ],
      PUBLIC: [
        { icon: 'login', label: 'Log in', href: '/login' },
        { icon: 'person_add', label: 'Create an account', href: '/register' },
        { icon: 'badge', label: 'Officer or admin login', href: '/login?tab=officer' },
        { icon: 'groups', label: 'Credits', href: '/credits' },
      ],
    },

    init() {
      const overlay = $('[data-cmd-overlay]');
      const input = $('[data-cmd-input]');
      const results = $('[data-cmd-results]');
      if (!overlay || !input) return;

      const role = (document.body.getAttribute('data-role') || 'PUBLIC').toUpperCase();
      this.actions = this.ROUTES[role] || this.ROUTES.PUBLIC;
      let lastFocus = null;

      const open = () => {
        lastFocus = document.activeElement;
        overlay.classList.add('show');
        this.search('', results);
        setTimeout(() => input.focus(), 50);
      };
      const close = () => {
        overlay.classList.remove('show');
        input.value = '';
        if (results) results.innerHTML = '';
        if (lastFocus && lastFocus.focus) lastFocus.focus();
      };

      document.addEventListener('keydown', (e) => {
        if ((e.metaKey || e.ctrlKey) && e.key && e.key.toLowerCase() === 'k') {
          e.preventDefault();
          if (overlay.classList.contains('show')) close();
          else open();
        } else if (e.key === 'Escape' && overlay.classList.contains('show')) {
          close();
        }
      });
      overlay.addEventListener('click', (e) => {
        if (e.target === overlay) close();
      });
      input.addEventListener('input', () => this.search(input.value, results));
      input.addEventListener('keydown', (e) => {
        const focused = results && results.querySelector('.cmd-item.focused');
        if (e.key === 'ArrowDown') {
          e.preventDefault();
          this.moveFocus(results, 1);
        } else if (e.key === 'ArrowUp') {
          e.preventDefault();
          this.moveFocus(results, -1);
        } else if (e.key === 'Enter' && focused) {
          e.preventDefault();
          focused.click();
        }
      });
      $$('[data-cmd-open]').forEach((el) =>
        el.addEventListener('click', (e) => {
          e.preventDefault();
          open();
        })
      );
    },

    search(q, results) {
      if (!results) return;
      const needle = q.trim().toLowerCase();
      const filtered = needle
        ? this.actions.filter((a) => a.label.toLowerCase().includes(needle))
        : this.actions;

      results.innerHTML = '';
      if (!filtered.length) {
        // An empty result set used to leave a blank panel with no explanation.
        const empty = document.createElement('p');
        empty.className = 'cmd-empty';
        empty.textContent = 'No page matches that search.';
        results.appendChild(empty);
        return;
      }
      filtered.forEach((a, i) => {
        const item = document.createElement('a');
        item.className = 'cmd-item' + (i === 0 ? ' focused' : '');
        item.href = a.href;
        item.setAttribute('role', 'option');
        const icon = document.createElement('span');
        icon.className = 'material-symbols-rounded';
        icon.textContent = a.icon;
        const label = document.createElement('span');
        label.textContent = a.label;
        item.append(icon, label);
        results.appendChild(item);
      });
    },

    moveFocus(results, dir) {
      if (!results) return;
      const all = $$('.cmd-item', results);
      if (!all.length) return;
      const idx = all.findIndex((el) => el.classList.contains('focused'));
      all.forEach((el) => el.classList.remove('focused'));
      const next = (idx + dir + all.length) % all.length;
      all[next].classList.add('focused');
      all[next].scrollIntoView({ block: 'nearest' });
    },
  };

  // ------- Copy to clipboard -------
  const Clipboard = {
    init() {
      $$('[data-copy]').forEach((btn) => {
        btn.addEventListener('click', () => {
          const text = btn.getAttribute('data-copy');
          navigator.clipboard
            .writeText(text)
            .then(() => Toast.show('Copied to clipboard', 'success'))
            .catch(() => Toast.show('Failed to copy', 'error'));
        });
      });
    },
  };

  // ------- Smooth scroll for hash links -------
  const SmoothScroll = {
    init() {
      $$('a[href^="#"]').forEach((a) => {
        a.addEventListener('click', (e) => {
          const id = a.getAttribute('href');
          if (id === '#' || id.length < 2) return;
          const target = document.querySelector(id);
          if (target) {
            e.preventDefault();
            target.scrollIntoView({ behavior: 'smooth', block: 'start' });
          }
        });
      });
    },
  };

  // ------- Lazy load images -------
  const LazyLoad = {
    init() {
      if (!('IntersectionObserver' in window)) return;
      const imgs = $$('img[data-src]');
      const io = new IntersectionObserver(
        (entries) => {
          entries.forEach((entry) => {
            if (entry.isIntersecting) {
              const img = entry.target;
              img.src = img.getAttribute('data-src');
              img.removeAttribute('data-src');
              io.unobserve(img);
            }
          });
        },
        { rootMargin: '200px' }
      );
      imgs.forEach((img) => io.observe(img));
    },
  };

  // ------- Confetti on form success (decorative) -------
  const SuccessBurst = {
    init() {
      $$('[data-success-burst]').forEach((form) => {
        form.addEventListener('submit', (e) => {
          if (!form.checkValidity()) return;
          // Tiny celebratory effect
          for (let i = 0; i < 18; i++) {
            const c = document.createElement('div');
            c.className = 'confetti';
            c.style.position = 'fixed';
            c.style.left = 50 + (Math.random() - 0.5) * 30 + '%';
            c.style.top = '50%';
            c.style.zIndex = 9999;
            c.style.background = ['#0B4F8A', '#046A38', '#E8A317', '#DC2626'][
              i % 4
            ];
            c.style.animationDelay = Math.random() * 0.3 + 's';
            document.body.appendChild(c);
            setTimeout(() => c.remove(), 2500);
          }
        });
      });
    },
  };

  // ------- Spotlight cursor tracking -------
  // Publishes the pointer position on the hovered card so its border can
  // illuminate under the cursor instead of the whole card glowing at once.
  const Spotlight = {
    init() {
      if (reduceMotion) return;
      $$('.card.hoverable').forEach((card) => {
        card.addEventListener(
          'pointermove',
          (e) => {
            const rect = card.getBoundingClientRect();
            card.style.setProperty('--mx', e.clientX - rect.left + 'px');
            card.style.setProperty('--my', e.clientY - rect.top + 'px');
          },
          { passive: true }
        );
      });
    },
  };

  // ------- Inline form validation -------
  // Forms relied entirely on the browser's native bubble, which is unstyled,
  // vanishes on the next click, and is invisible to a screen reader once gone.
  // Messages now render next to the field they belong to.
  const Validate = {
    MESSAGES: {
      valueMissing: 'This field is required.',
      typeMismatch: 'Check the format of this value.',
      tooShort: 'This value is too short.',
      tooLong: 'This value is too long.',
      patternMismatch: 'This value is not in the expected format.',
      rangeUnderflow: 'This value is too low.',
      rangeOverflow: 'This value is too high.',
    },

    messageFor(field) {
      for (const key of Object.keys(this.MESSAGES)) {
        if (field.validity[key]) {
          if (key === 'typeMismatch' && field.type === 'email') {
            return 'Enter a valid email address, like name@example.com.';
          }
          return this.MESSAGES[key];
        }
      }
      return field.validationMessage || 'Check this value.';
    },

    slot(field) {
      const wrap = field.closest('.field') || field.parentElement;
      if (!wrap) return null;
      let slot = wrap.querySelector('.field-error');
      if (!slot) {
        slot = document.createElement('p');
        slot.className = 'field-error';
        slot.id = 'err-' + (field.id || field.name || Math.random().toString(36).slice(2));
        const icon = document.createElement('span');
        icon.className = 'material-symbols-rounded';
        icon.textContent = 'error';
        slot.appendChild(icon);
        slot.appendChild(document.createTextNode(''));
        wrap.appendChild(slot);
      }
      return slot;
    },

    mark(field, message) {
      const wrap = field.closest('.field') || field.parentElement;
      const slot = this.slot(field);
      if (!wrap || !slot) return;
      if (message) {
        slot.lastChild.nodeValue = message;
        wrap.classList.add('has-error');
        field.setAttribute('aria-invalid', 'true');
        field.setAttribute('aria-describedby', slot.id);
      } else {
        wrap.classList.remove('has-error');
        field.removeAttribute('aria-invalid');
        field.removeAttribute('aria-describedby');
      }
    },

    init() {
      $$('form').forEach((form) => {
        if (form.hasAttribute('data-no-validate')) return;
        const fields = $$('input, select, textarea', form).filter(
          (el) => el.type !== 'hidden' && el.willValidate
        );
        if (!fields.length) return;
        form.setAttribute('novalidate', '');

        fields.forEach((field) => {
          // Re-check only after a first failure, so the form does not scold
          // the user mid-typing on their very first pass through it.
          field.addEventListener('blur', () => {
            if (field.value) this.mark(field, field.checkValidity() ? '' : this.messageFor(field));
          });
          field.addEventListener('input', () => {
            const wrap = field.closest('.field') || field.parentElement;
            if (wrap && wrap.classList.contains('has-error') && field.checkValidity()) {
              this.mark(field, '');
            }
          });
        });

        form.addEventListener('submit', (e) => {
          let firstInvalid = null;
          fields.forEach((field) => {
            const ok = field.checkValidity();
            this.mark(field, ok ? '' : this.messageFor(field));
            if (!ok && !firstInvalid) firstInvalid = field;
          });
          if (firstInvalid) {
            e.preventDefault();
            firstInvalid.focus();
            firstInvalid.scrollIntoView({ block: 'center', behavior: reduceMotion ? 'auto' : 'smooth' });
          }
        });
      });
    },
  };

  // ------- Back navigation -------
  // Replaces href="javascript:history.back()", which does nothing without JS
  // and is blocked outright by a script-src Content-Security-Policy.
  const BackLink = {
    init() {
      $$('[data-history-back]').forEach((btn) => {
        // A direct entry has no history to go back to; send those users home.
        if (window.history.length <= 1) {
          btn.addEventListener('click', () => {
            window.location.href = btn.getAttribute('data-history-back') || '/';
          });
          return;
        }
        btn.addEventListener('click', () => window.history.back());
      });
    },
  };

  // ------- Password reveal -------
  // Replaces an inline onclick that mutated the field type directly and never
  // told assistive tech which state the toggle was in.
  const PasswordReveal = {
    init() {
      $$('[data-reveal-password]').forEach((btn) => {
        const field = document.getElementById(btn.getAttribute('data-reveal-password'));
        if (!field) return;
        const icon = btn.querySelector('.material-symbols-rounded');
        btn.addEventListener('click', () => {
          const shown = field.type === 'text';
          field.type = shown ? 'password' : 'text';
          btn.setAttribute('aria-pressed', String(!shown));
          btn.setAttribute('aria-label', shown ? 'Show password' : 'Hide password');
          if (icon) icon.textContent = shown ? 'visibility_off' : 'visibility';
          // Keep the caret where the user left it.
          const pos = field.value.length;
          field.focus();
          field.setSelectionRange(pos, pos);
        });
      });
    },
  };

  // ------- Copy current URL -------
  // The share control used to overwrite its own label with "Copied!" forever,
  // and swallowed the failure case entirely.
  const CopyLink = {
    init() {
      $$('[data-copy-current-url]').forEach((btn) => {
        const label = btn.querySelector('span:last-child');
        const original = label ? label.textContent : '';
        btn.addEventListener('click', () => {
          const done = (ok) => {
            Toast.show(
              ok ? 'Link copied to your clipboard' : 'Could not copy the link. Copy it from the address bar.',
              ok ? 'success' : 'error'
            );
            if (!label) return;
            label.textContent = ok ? 'Copied' : original;
            setTimeout(() => {
              label.textContent = original;
            }, 2000);
          };
          if (!navigator.clipboard) {
            done(false);
            return;
          }
          navigator.clipboard
            .writeText(window.location.href)
            .then(() => done(true))
            .catch(() => done(false));
        });
      });
    },
  };

  // ------- Boot -------
  document.addEventListener('DOMContentLoaded', () => {
    Theme.init();
    Sidebar.init();
    Flash.init();
    Progress.init();
    AutoSubmit.init();
    Reveal.init();
    Tabs.init();
    ReplyToggle.init();
    Upload.init();
    Celebration.init();
    Notif.init();
    Mention.init();
    Popover.init();
    Tilt.init();
    FloatingCTA.init();
    SlidingPill.init();
    Counters.init();
    Accordion.init();
    TagInput.init();
    CommandPalette.init();
    Clipboard.init();
    SmoothScroll.init();
    LazyLoad.init();
    SuccessBurst.init();
    Spotlight.init();
    Validate.init();
    BackLink.init();
    PasswordReveal.init();
    CopyLink.init();
  });

  // ------- Expose Toast globally -------
  window.ogbdToast = (message, type, options) => Toast.show(message, type, options);
})();
