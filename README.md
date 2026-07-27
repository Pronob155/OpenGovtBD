<div align="center">

<!-- Animated Typing Header -->
<a href="https://github.com/pbs002-s/OpenGovtBD">
  <img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=350&size=16&duration=3000&pause=1000&color=0B4F8A&center=true&vCenter=true&width=700&lines=OpenGovtBD+%C2%B7+%E0%A6%93%E0%A6%AA%E0%A7%87%E0%A6%A8+%E0%A6%97%E0%A6%AD%E0%A6%B0%E0%A7%8D%E0%A6%AE%E0%A7%87%E0%A6%A8%E0%A7%8D%E0%A6%9F+%E0%A6%AC%E0%A6%BF%E0%A6%A1%E0%A6%BF;Government+%E2%80%93+Citizen+Engagement+Platform;Bridging+Citizens+%26+Government+in+Bangladesh;Built+with+Java+%E2%98%95+%2B+Spring+Boot" alt="Typing SVG" />
</a>

<br/>

<!-- Banner GIF -->
<img src="https://raw.githubusercontent.com/trinib/trinib/main/images/banner.gif" width="100%" alt="banner"/>

<br/><br/>

<!-- Badges -->
![Java](https://img.shields.io/badge/Java%2017-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Demo%20%2F%20Portfolio-orange?style=for-the-badge)

![GitHub stars](https://img.shields.io/github/stars/pbs002-s/OpenGovtBD?style=social)
![GitHub forks](https://img.shields.io/github/forks/pbs002-s/OpenGovtBD?style=social)
![GitHub issues](https://img.shields.io/github/issues/pbs002-s/OpenGovtBD)
![Last Commit](https://img.shields.io/github/last-commit/pbs002-s/OpenGovtBD)

</div>

---

## 📖 About The Project

**OpenGovtBD** (ওপেন গভর্নমেন্ট বিডি) is a full-stack **Government Citizen Engagement Platform** built for Bangladesh, designed to close the gap between everyday citizens and the government services meant to serve them.

Instead of chasing different offices, hotlines, and disconnected government sites, citizens get **one platform** to file complaints and track their resolution, join moderated public discussions, vote in official polls, submit suggestions, and reach every digital government service — while officers and administrators get dedicated dashboards to respond, moderate, and measure it all.

This is a genuine object-oriented Java project (abstract `User` hierarchy, layered service/repository architecture) — not a template renderer — built as a functional demo with realistic seeded data so every screen is explorable immediately after cloning.

<div align="center">
<img src="https://user-images.githubusercontent.com/74038190/212284136-03988914-d899-44b4-b1d9-4eeccf656e44.gif" width="500">
</div>

---

## 🧭 Table of Contents

- [About](#-about-the-project)
- [Key Features](#-key-features)
- [System Architecture](#-system-architecture)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Demo Accounts](#-demo-accounts)
- [Design System](#-design-system)
- [What's Left To Build](#-whats-left-to-build)
- [Benefits](#-benefits)
- [Team](#-team)
- [Contributing](#-contributing)
- [License](#-license)

---

## ✨ Key Features

| Feature | Description |
|---|---|
| 🔐 **Role-Based Authentication** | Separate login for Citizen, Government Officer, and Super Admin — OTP-verified citizen registration, simulated 2FA/MFA for officials |
| 📝 **Complaint Lifecycle Tracking** | Submit → Assigned → In Progress → Resolved/Rejected, with a full audit timeline, officer replies, star ratings, and reopen flow |
| 💬 **Moderated Discussion Forums** | Citizens post, officers approve/reject/pin/lock, with likes, comments, bookmarks & official government responses |
| 📊 **Official Polls & Voting** | One vote per citizen, live animated results, poll archive with final tallies |
| 💡 **Suggestion Box** | Community upvotes/downvotes, full status workflow with government feedback |
| 🏆 **Gamification** | Points, badge tiers (New → Bronze → Silver → Gold → Platinum), citizen leaderboard |
| 🚨 **Emergency Services Hub** | One-tap Police / Fire / Ambulance / Helpline access, nearby facility directory |
| 🏛️ **Digital Services Directory** | Quick links to NID, passport, land record, tax, trade license, and more |
| 🔔 **Smart Notification Center** | Real-time, typed alerts for every status change that affects a citizen |
| 📈 **Admin Analytics Dashboard** | Live charts — complaints by category/division, resolution rate, system-wide stats |
| 🌗 **Light & Dark Mode** | Persisted, animated theme toggle |
| 📱 **Fully Responsive** | Sidebar app-shell that collapses into a slide-in mobile drawer |

<div align="center">
<img src="https://user-images.githubusercontent.com/74038190/213910845-af37a709-8995-40d6-be59-724526e3c3d7.gif" width="450">
</div>

---

## 🏗️ System Architecture

```mermaid
flowchart TD
    A[Citizen / Officer / Admin Browser] -->|HTTPS| B[Spring MVC Controllers]
    B --> C[RBAC Session Interceptor]
    B --> D[AuthService]
    B --> E[ComplaintService]
    B --> F[DiscussionService]
    B --> G[PollService]
    B --> H[SuggestionService]
    B --> I[NotificationService]
    B --> J[AnalyticsService]

    D --> K[(User Repository)]
    E --> L[(Complaint Repository)]
    F --> M[(Discussion Repository)]
    G --> N[(Poll Repository)]
    H --> O[(Suggestion Repository)]
    I --> P[(Notification Repository)]

    Q[DataSeeder] -.seeds on startup.-> K
    Q -.-> L
    Q -.-> M
    Q -.-> N
    Q -.-> O

    B --> R[Thymeleaf Views]

    style A fill:#0B4F8A,color:#fff
    style Q fill:#046A38,color:#fff
```

### Domain Model (OOP Core)

```mermaid
classDiagram
    class User {
        <<abstract>>
        +Long id
        +String fullName
        +getDashboardUrl()
        +getDisplayRole()
        +getLoginIdentifier()
    }
    class Citizen {
        +String phone
        +String nationalId
        +int points
        +getBadge()
    }
    class Officer {
        +String officerId
        +String department
    }
    class Admin {
        +String email
    }
    User <|-- Citizen
    User <|-- Officer
    User <|-- Admin

    Citizen "1" --> "*" Complaint : files
    Citizen "1" --> "*" Discussion : starts
    Officer "1" --> "*" Complaint : resolves
```

---

## 🛠️ Tech Stack

<div align="center">

![Java](https://img.shields.io/badge/Backend-Java%2017-ED8B00?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot%203-6DB33F?style=flat-square&logo=springboot)
![Thymeleaf](https://img.shields.io/badge/Templating-Thymeleaf-005F0F?style=flat-square&logo=thymeleaf)
![Chart.js](https://img.shields.io/badge/Charts-Chart.js-FF6384?style=flat-square&logo=chartdotjs)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=flat-square&logo=apachemaven)
![Git](https://img.shields.io/badge/Version%20Control-Git-F05032?style=flat-square&logo=git)

</div>

> **Note:** this demo build uses in-memory, thread-safe repositories (no external database required to run it) so it's instantly explorable after cloning. See [What's Left To Build](#-whats-left-to-build) for the path to a persistent, production-grade version.

---

## 📁 Project Structure

```
OpenGovtBD/
├── src/main/java/com/opengovtbd/
│   ├── model/           # Abstract User + Citizen/Officer/Admin, Complaint,
│   │                    # Discussion, Poll, Suggestion, Notification, enums
│   ├── repository/      # In-memory, thread-safe data access layer
│   ├── service/         # All business logic — Auth, Complaint, Discussion,
│   │                    # Poll, Suggestion, Notification, Reward, Analytics
│   ├── controller/      # Spring MVC controllers, one per role/feature
│   ├── config/          # RBAC session interceptor, MVC config, DataSeeder
│   └── OpenGovtBDApplication.java
├── src/main/resources/
│   ├── templates/       # Thymeleaf views (fragments/, auth/, citizen/, officer/, admin/, error/)
│   ├── static/          # css/style.css, js/main.js
│   └── application.properties
├── design.md            # Full design system reference
├── product-spec.md      # Product spec — how it works & what's left to build
├── pom.xml
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Installation

```bash
# Clone the repository
git clone https://github.com/pbs002-s/OpenGovtBD.git
cd OpenGovtBD

# Run the application (no database setup needed for the demo)
mvn spring-boot:run
```

The app will be available at **`http://localhost:8080`**

<div align="center">
<img src="https://user-images.githubusercontent.com/74038190/212257467-871d32b7-e401-42e8-a166-fcfd7baa4c6b.gif" width="400">
</div>

---

## 🔑 Demo Accounts

Pre-seeded on startup — no signup required to explore:

| Role | Login | Password |
|---|---|---|
| 🧑 Citizen | Phone `01700000000` | `citizen123` |
| 🧑 Citizen (alt) | Phone `01812345678` | `citizen123` |
| 🏛️ Government Officer | ID `OFC-1001`, Email `kamrul.hasan@dhaka.gov.bd` | `officer123` |
| 🛡️ Super Admin | Email `admin@opengovtbd.gov.bd` | `admin123` |

New citizens can also register from scratch — the OTP screen shows the demo code (`123456`) directly, since no real SMS gateway is wired up yet.

---

## 🎨 Design System

Full design tokens, typography, spacing, component states, and animation timing live in [`design.md`](./design.md) — including the signature **"bridge" motif** (two nodes, Citizens ↔ Government, joined by an animated connecting arc) used throughout the brand.

<div align="center">
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="100%">
</div>

---

## 🔮 What's Left To Build

This is a functional demo, not a production system yet. Full breakdown in [`product-spec.md`](./product-spec.md) — the short version:

- [ ] Persistent database (currently in-memory, resets on restart)
- [ ] Password hashing + real authentication security
- [ ] Real OTP/SMS gateway + real 2FA/MFA
- [ ] Real file upload storage for complaint attachments
- [ ] GPS-based complaint location detection
- [ ] Full Bangla localization (currently partial)
- [ ] Department-scoped officer permissions
- [ ] Automated test suite
- [ ] REST API layer for external/mobile clients

---

## 🎯 Benefits

> Increasing transparency. Improving engagement. Simplifying access.

OpenGovtBD strengthens trust between citizens and government by:

- ✅ Giving every complaint a visible, trackable status
- ✅ Centralizing scattered government services into one place
- ✅ Encouraging active civic participation through gamification
- ✅ Requiring officer moderation before public content goes live
- ✅ Making system-wide performance visible via analytics

---

## 👥 Team

<div align="center">

| Role | Name |
|---|---|
| 👨‍💻 **Team Member-1 (Lead)** | Pronob Das |
| 👨‍💻 **Team Member-2** | Pritam Biswas |
| 👨‍💻 **Team Member-3** | Md Mahabub Rahaman |
| 👨‍💻 **Team Member-4** | Shreya Golder |
| 👨‍💻 **Team Member-5** | Anurag Barmon |

| | |
|---|---|
| 🎓 **Program** | B.Tech CSE, Daffodil International University (DIU) |
| 🌐 **Portfolio** | [Pritam Biswas](https://pritam-biswas-portfolio.netlify.app) |
| 💻 **GitHub** | [@pbs002-s](https://github.com/pbs002-s) | [@mahabub-rahaman-001](https://github.com/mahabub-rahaman-001)

</div>

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to fork the repo and submit a pull request.

```bash
git checkout -b feature/AmazingFeature
git commit -m "Add some AmazingFeature"
git push origin feature/AmazingFeature
```

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

<div align="center">

### ⭐ If you find this project useful, give it a star!

<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="100%">

**Made with ☕ and Java — OpenGovtBD, a bridge between citizens and government.**

</div>
