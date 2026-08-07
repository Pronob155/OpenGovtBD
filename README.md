<div align="center">

# 🏛️ OpenGovtBD — Citizen Bridge

### উন্মুক্ত সরকার · Government Citizen Engagement Platform for Bangladesh

<br/>

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-3B82F6?style=for-the-badge)](LICENSE)

<br/>

> A full-stack, production-styled Java web application that digitally connects Bangladeshi citizens with their government — built as a demo-grade civic engagement platform.

<br/>

[🚀 Quick Start](#-getting-started) · [✨ Features](#-features) · [🏗️ Architecture](#%EF%B8%8F-architecture--oop-design) · [🛠️ Tech Stack](#%EF%B8%8F-technology-stack) · [⚠️ Disclaimer](#%EF%B8%8F-disclaimer)

</div>

---

## 📖 Overview

LIVE:[LIVE](https://opengovtbd.onrender.com/)

**OpenGovtBD** (formerly *Nagorik Setu*) is a comprehensive, role-driven government-citizen engagement platform demonstrating what a modern, transparent public digital service could look like in Bangladesh. The platform provides a unified workspace for complaint management, public discourse, civic polling, and administrative oversight — all in a single, cohesive application.

Built with **Java 17 + Spring Boot 3 (MVC/OOP)** and **Thymeleaf**, the project uses an **in-memory data layer** — no external database is needed to run the demo, making it instantly portable for evaluation and learning.

---

## ✨ Features

### 👥 Role-Based Workspaces

Three distinct, secure workspaces with full Role-Based Access Control (RBAC):

| Role | Capabilities |
| :--- | :--- |
| **Citizen** | File complaints, join discussions, vote in polls, submit suggestions, earn badges |
| **Govt. Officer** | Manage complaint queues, moderate discussions, review suggestions, create polls |
| **Super Admin** | System oversight, citizen & officer management, analytics dashboard |

### 🗂️ Core Modules

- **📋 Complaint Management** — Full lifecycle: Submit → Assign → In Progress → Resolved/Rejected, with timeline tracking, officer replies, citizen ratings, and a reopen flow.
- **💬 Public Discussions** — Citizen-posted threads with moderation controls (approve, reject, pin, lock, official responses) and social reactions (likes, dislikes, bookmarks, comments).
- **🗳️ Official Polls** — One vote per citizen, real-time percentage bars, and a full poll archive.
- **💡 Suggestion Box** — Community-driven idea submission with upvote/downvote and a structured status workflow (Submitted → Under Review → Accepted/Implemented/Rejected).
- **🏆 Gamification & Leaderboard** — Citizens earn points and badges (New → Bronze → Silver → Gold → Platinum) for civic participation.
- **🔔 Notification Center** — Real-time unread counts across all pages via a shared `@ControllerAdvice`.
- **🚨 Emergency Services Hub** — Curated emergency contacts and digital service shortcuts.
- **📊 Analytics Dashboard** — Interactive Chart.js visualizations for Super Admin (complaints by category/division, resolution rate trends).
- **🌙 Dark Mode** — Persistent user preference with an instant toggle in the citizen sidebar.

---

## 🏗️ Architecture & OOP Design

OpenGovtBD is a genuine object-oriented Java project demonstrating core OOP principles throughout its design.

### Package Structure

```
com.nagoriksetu
├── model/         Domain entities
│                  ├── Abstract User base class (Citizen, Officer, Admin subclasses)
│                  ├── Complaint, Discussion, Poll, Suggestion, Notification, Announcement
│                  └── Enums: Role, ComplaintStatus, SuggestionStatus, Badge
│
├── repository/    In-memory, thread-safe (ConcurrentHashMap-backed) repositories
│                  └── One per aggregate — no database setup required
│
├── service/       Business logic layer
│                  └── AuthService, ComplaintService, DiscussionService,
│                      PollService, SuggestionService, NotificationService,
│                      RewardService, AnalyticsService
│
├── controller/    Thin Spring MVC controllers, delegate all logic to services
│
└── config/        Session-based RBAC interceptor, MVC config,
                   and a DataSeeder that populates realistic demo data on startup
```

### Key OOP Decisions

- **Inheritance & Polymorphism:** `User` is an `abstract` class — `Citizen`, `Officer`, and `Admin` extend it and override `getDashboardUrl()`, `getDisplayRole()`, and `getLoginIdentifier()`.
- **Behavior-Rich Enums:** `ComplaintStatus`, `SuggestionStatus`, and `Badge` encapsulate both data and behavior (UI labels, color tones, point thresholds).
- **Thin Controllers, Rich Services:** All business rules (points, notifications, timelines) live in the service layer; controllers stay focused on routing.

---

## 🚀 Getting Started

### Prerequisites

| Tool | Required Version |
| :--- | :--- |
| Java (JDK) | 17 or higher |
| Apache Maven | 3.8 or higher |

### Run Locally

```bash
# 1. Clone the repository
git clone https://github.com/pbs002-s/OpenGovtBD.git
cd OpenGovtBD

# 2. Start the application
mvn spring-boot:run

# 3. Open your browser at:
#    http://localhost:8080
```

> The application auto-seeds all demo data on startup. No manual setup or database configuration required.

### 🔑 Demo Accounts

| Role | Login | Password |
| :--- | :--- | :--- |
| **Citizen** | Phone: `01700000000` | `citizen123` |
| **Citizen (Alt)** | Phone: `01812345678` | `citizen123` |
| **Government Officer** | ID: `OFC-1001` · Email: `kamrul.hasan@dhaka.gov.bd` | `officer123` |
| **Super Admin** | Email: `admin@opengovtbd.gov.bd` | `admin123` |

> 💡 **New citizen registration** is supported. The simulated OTP (`123456`) is displayed directly on the verification page.

---

## 🗺️ Feature Tour

<details>
<summary><strong>🧑‍💼 Citizen Workspace</strong></summary>

| Page | Key Capabilities |
| :--- | :--- |
| Dashboard | Welcome banner, profile completion ring, emergency alerts, stat cards, quick services |
| Digital Services | Directory of e-government portals (NID, e-Passport, Tax Portal, etc.) |
| Emergency Hub | Hotline numbers (999, 109, 333) and emergency contacts |
| Complaints | File, track, rate, and reopen; full timeline view with officer replies |
| Discussions | Post, comment, like, bookmark, follow government responses |
| Polls | Vote and view live percentage results |
| Suggestion Box | Submit ideas, upvote/downvote, track government feedback |
| Leaderboard | Community rankings by points and badge tier |
| Notifications | Unread/read distinction with type-based icons |
| Profile & Settings | Edit details, dark mode, earned points and badges |

</details>

<details>
<summary><strong>🏢 Government Officer Workspace</strong></summary>

| Page | Key Capabilities |
| :--- | :--- |
| Dashboard | Stats on open complaints, pending discussions, suggestion queue |
| Complaint Queue | Filterable table; assign, update status, reply to citizens |
| Discussion Approval | Approve, reject, pin, lock threads; post official responses |
| Suggestion Review | Update status workflow; provide formal government feedback |
| Create Poll | Dynamic option builder with category and end date |

</details>

<details>
<summary><strong>🛡️ Super Admin Workspace</strong></summary>

| Page | Key Capabilities |
| :--- | :--- |
| System Overview | Platform-wide metrics and recent activity feed |
| User Management | Citizen and officer tables with suspend/reactivate controls |
| Analytics | Interactive charts: complaints by category (bar), by division (doughnut), resolution trend (line) |

</details>

---

## 🛠️ Technology Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.2 (Web MVC) |
| **Templating Engine** | Thymeleaf |
| **Data Layer** | In-Memory (`ConcurrentHashMap`) — zero configuration |
| **Security** | Custom session-based RBAC interceptor (no external auth library) |
| **Data Visualization** | Chart.js (CDN) |
| **Typography** | Google Fonts — Inter, Noto Sans Bengali |
| **Icons** | Material Symbols Rounded (Google Fonts CDN) |
| **Build Tool** | Apache Maven |

---

## 📋 Changelog

**Recent Updates**

- ✅ **Rebranded** from *Nagorik Setu* to **OpenGovtBD**
- 🐛 **Fixed:** Dark mode preference saved on Profile page never applied the `dark` class to `<body>` — now correctly applied across all citizen views
- 🐛 **Fixed:** Notification bell unread count was hardcoded to `0` on all pages except the Dashboard — a shared `@ControllerAdvice` now computes it once globally
- ✨ **Added:** Instant dark mode toggle in the citizen sidebar (POST `/citizen/theme/toggle` + redirect) and a styled toggle-switch component replacing the old plain checkbox on Profile

---

## ⚠️ Disclaimer

This project is an **educational and portfolio demonstration** of a government platform concept.

> **Not production-ready.** Passwords are stored as plain text in memory, OTPs are simulated, and all data resets on every restart. A real-world deployment requires a persistent database (e.g., PostgreSQL), password hashing (BCrypt/Argon2), a genuine SMS/2FA gateway, and a full security audit. **Do not use this authentication approach in production.**

---

## 📄 License

Distributed under the **MIT License** — free to use for learning, modification, and portfolio purposes. See [`LICENSE`](LICENSE) for details.

---

<div align="center">

*Built with ❤️ for a more transparent and digitally connected Bangladesh.*

</div>
