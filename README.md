# উন্মুক্ত সরকার · OpenGovtBD

Repository: **[github.com/pbs002-s/OpenGovtBD](https://github.com/pbs002-s/OpenGovtBD)**

**OpenGovtBD (Citizen Bridge)** is a full-stack Government Citizen Engagement Platform for Bangladesh, built as a demo-grade, production-styled Java web application. It connects citizens and government through complaint management, public discussions, official polls, a suggestion box, digital service shortcuts, and role-based dashboards for **Citizens**, **Government Officers**, and a **Super Admin**.

> Built with **Java 17 + Spring Boot 3 (MVC, OOP)**, **Thymeleaf**, and a clean in-memory data layer — no external database required to run the demo.

---

## 🆕 Recent updates

- Rebranded from *Nagorik Setu* to **OpenGovtBD**.
- Fixed: the dark-mode preference saved on the Profile page never actually rendered on any page — the `<body>` tag never received the `dark` class. Now applied across all citizen views.
- Fixed: the topbar notification bell only showed a real unread count on the Dashboard; every other citizen page hardcoded it to `0`. A shared `@ControllerAdvice` now computes it once for every page.
- Added: an instant dark-mode switch in the citizen sidebar (posts to `/citizen/theme/toggle` and redirects back to the current page), plus a matching toggle-switch component styled to replace the old plain checkbox on the Profile page.

---

## ✨ Highlights

- **Three roles, three experiences** — Citizen, Government Officer, and Super Admin each get a dedicated authenticated workspace with role-based access control (RBAC).
- **Full complaint lifecycle** — submit → assign → in progress → resolved/rejected, with a live timeline, officer replies, citizen ratings, and reopen flow.
- **Moderated public discussions** — citizens post, officers approve/reject/pin/lock, with likes, dislikes, bookmarks, comments, and official government responses.
- **Official polls** — one vote per citizen, live percentage bars, poll archive.
- **Suggestion box** — upvote/downvote, status workflow (Submitted → Under Review → Accepted/Implemented/Rejected), government feedback.
- **Gamification** — citizens earn points and badges (New → Bronze → Silver → Gold → Platinum) for participating, with a leaderboard.
- **Notification center**, **emergency services hub**, **digital services directory**, and an **analytics dashboard** with interactive charts for the Super Admin.
- **Premium, minimalist, animated UI** — a government-grade design system (Inter font, Material Symbols Rounded icons, 16px rounded corners, soft shadows, glassmorphism accents) with a signature "bridge" motif on the landing page symbolizing citizen ↔ government connection. Smooth entrance animations, animated progress rings/bars, skeleton-ready empty states, and a fully responsive layout down to mobile.

---

## 🏗️ Architecture & OOP Design

This is a genuine object-oriented Java project, not just a template renderer:

```
com.nagoriksetu
├── model/         Domain entities — abstract User base class with Citizen, Officer,
│                  Admin subclasses (inheritance + polymorphism), Complaint, Discussion,
│                  Poll, Suggestion, Notification, Announcement, plus enums for
│                  Role, ComplaintStatus, SuggestionStatus, Badge.
├── repository/    In-memory repositories (thread-safe ConcurrentHashMap-backed),
│                  one per aggregate — no database setup needed for the demo.
├── service/       Business logic layer — AuthService, ComplaintService,
│                  DiscussionService, PollService, SuggestionService,
│                  NotificationService, RewardService, AnalyticsService.
├── controller/    Spring MVC controllers per role/feature, thin and focused —
│                  delegate all logic to services.
└── config/        Session-based RBAC interceptor, MVC config, and a DataSeeder
                   that populates realistic demo data on startup.
```

Key OOP decisions:
- `User` is an **abstract class** — `Citizen`, `Officer`, and `Admin` extend it and override `getDashboardUrl()`, `getDisplayRole()`, and `getLoginIdentifier()`, demonstrating inheritance and polymorphism throughout the auth and routing layers.
- Enums (`ComplaintStatus`, `SuggestionStatus`, `Badge`) encapsulate both data and behavior (labels, color "tones", point thresholds).
- Services are interfaced through constructor injection and contain all business rules (points, notifications, timelines); controllers stay thin.

---

## 🚀 Getting Started

### Prerequisites
- **Java 17+**
- **Maven 3.8+**

### Run locally
```bash
git clone https://github.com/<your-username>/opengovtbd.git
cd opengovtbd
mvn spring-boot:run
```
Then open **http://localhost:8080**

### Demo accounts (auto-seeded on startup — no signup needed)

| Role | Login | Password |
|---|---|---|
| Citizen | Phone: `01700000000` | `citizen123` |
| Citizen (alt) | Phone: `01812345678` | `citizen123` |
| Government Officer | ID: `OFC-1001`, Email: `kamrul.hasan@dhaka.gov.bd` | `officer123` |
| Super Admin | Email: `admin@opengovtbd.gov.bd` | `admin123` |

New citizens can also register from scratch — the OTP screen displays the demo one-time code (`123456`) directly on the page, since no real SMS gateway is wired up.

---

## 📱 Feature Tour

**Citizen:** Dashboard · Digital Services directory · Emergency hub · Complaint filing & tracking · Public discussions · Polls · Suggestion box · Leaderboard · Notifications · Profile & preferences

**Officer:** Dashboard · Complaint queue (assign / update status / reply) · Discussion moderation (approve / reject / pin / lock / official response) · Suggestion review · Poll creation

**Super Admin:** System overview · Citizen & officer management (suspend/reactivate) · Analytics dashboard with live charts (complaints by category/division, resolution rate)

---

## 🛠️ Tech Stack

- Java 17, Spring Boot 3.2 (Web MVC, Thymeleaf)
- Server-side session-based authentication with a custom RBAC interceptor (no external auth library — intentionally transparent for a learning/demo project)
- In-memory, thread-safe repositories (no database configuration required)
- Chart.js (CDN) for the analytics dashboard
- Google Fonts **Inter** + **Material Symbols Rounded**

---

## ⚠️ Demo Disclaimer

This is an educational/portfolio demo of a government platform concept. Passwords are stored in plain text in memory, OTP is simulated, and data resets on every restart. **Do not use this authentication approach in production** — a real deployment would need a persistent database, hashed credentials, a real SMS/2FA gateway, and a security review.

---

## 📄 License

MIT — free to use for learning and portfolio purposes.
