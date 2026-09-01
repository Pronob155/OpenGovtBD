# OpenGovtBD — Agent / Developer Guide

> Government Citizen Engagement Platform for Bangladesh
> (Java 17 · Spring Boot 3.2 · Thymeleaf · Vanilla CSS/JS)

## Build & Run

```bash
# Build & run
mvn spring-boot:run

# Build JAR
mvn clean package
java -jar target/opengovtbd.jar

# Run tests
mvn test
```

The app starts on `http://localhost:8080` by default.

## Frontend Asset Pipeline

This project uses **vanilla CSS/JS** with optional Node-based linting/formatting.

```bash
# Install dev tooling (one-time)
npm install

# Lint
npm run lint           # CSS + JS

# Format
npm run format         # CSS + JS

# Production minify
npm run build:assets
```

## Project Structure

```
src/main/
├── java/com/opengovtbd/        # Java sources
│   ├── config/                 # WebConfig, DataSeeder, Interceptors
│   ├── controller/             # Spring MVC controllers (14)
│   ├── model/                  # Domain entities (20+)
│   ├── repository/             # In-memory repositories
│   └── service/                # Business logic services
└── resources/
    ├── static/                 # CSS, JS, images
    │   ├── css/style.css       # Design system
    │   └── js/main.js          # Frontend interactions
    ├── templates/              # Thymeleaf HTML
    │   ├── fragments/          # Reusable layout fragments
    │   ├── auth/               # Login, register, OTP
    │   ├── citizen/            # Citizen dashboard pages
    │   ├── officer/            # Officer workspace
    │   ├── admin/              # Admin console
    │   └── public/             # Public profile pages
    └── messages*.properties    # i18n (en, bn)
```

## Design System

- **Tokens**: Defined as CSS custom properties in `:root` (light) and `body.dark` (dark).
- **Components**: Cards, pills, buttons, forms, modals, toasts, tabs, accordions, etc.
- **Layout**: Sidebar + topbar shell, public-nav, hero landing, bento grid.
- **Theming**: Light/dark via `body.dark` class, persisted in `localStorage`.

### Brand Colors

| Token | Value | Usage |
| --- | --- | --- |
| `--setu-blue` | `#0B4F8A` | Primary actions, links |
| `--bd-green` | `#046A38` | Success, government, growth |
| `--warning` | `#F59E0B` | Pending, attention |
| `--error` | `#DC2626` | Destructive, errors |

## Frontend Architecture

`main.js` is organized as a single IIFE with modules:

- **Theme** — Light/dark toggle, system preference detection
- **Sidebar** — Mobile drawer, ESC/overlay close
- **Flash** — Auto-dismiss alerts
- **Progress** — Ring + bar animations
- **Reveal** — IntersectionObserver-based scroll reveal
- **Tabs** — Tab strip interactions (`.tab-strip`, `.tab-underline`)
- **Notif** — Notification dropdown panel
- **Mention** — `@username` autocomplete + profile preview popover
- **Tilt** — Landing card 3D hover
- **Counters** — Animated number counters
- **Accordion** — Collapsible sections
- **TagInput** — Chip-style multi-input
- **CommandPalette** — Ctrl/Cmd+K quick search
- **Toast** — Rich notification toasts (`.toast-rich`)

## Conventions

### CSS
- Use the design tokens (`--setu-blue`, `--surface`, `--radius`, etc.) — never hardcode colors.
- Mobile-first responsive: 380px, 600px, 960px breakpoints.
- Honor `prefers-reduced-motion`.
- Theme-aware: every color used on a tinted surface must have a `--on-tint-*` counterpart for dark mode.

### JavaScript
- No frameworks. Vanilla ES2022.
- Single IIFE in `main.js`, modules as objects with `init()` methods.
- Use `data-*` attributes as hooks, not class selectors.
- All UI must work without JS (progressive enhancement).

### Thymeleaf
- Shared fragments live in `templates/fragments/layout.html`.
- Reuse `~{fragments/layout :: head(title)}`, `:: topbar(...)`, `:: sidebar-*`, `:: command-palette`.
- Use `#strings`, `#lists`, `#temporals` for utility methods.
- Use i18n keys (`#{nav.dashboard}`) for all user-facing text.

## Adding a New Page

1. **Create template** in the appropriate role folder.
2. **Reuse fragments** for head, topbar, sidebar.
3. **Add nav link** in `fragments/layout.html` (`sidebar-citizen`, etc.).
4. **Add controller route** in the role's controller.
5. **Add i18n keys** in `messages.properties` and `messages_bn.properties`.

## Accessibility

- All interactive elements must be keyboard-reachable.
- Use `aria-label` on icon-only buttons.
- Honor `prefers-reduced-motion` for animations.
- Maintain color contrast ≥ 4.5:1 for text.
- Focus styles are global via `:focus-visible`.
