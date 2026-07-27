[product-spec.md](https://github.com/user-attachments/files/30428778/product-spec.md)
# Nagorik Setu — Product Specification (product-spec.md)

**নাগরিক সেতু** · Government Citizen Engagement Platform
Version 1.0 · Status: Functional demo / portfolio build

---

## 1. Overview

### 1.1 What it is
Nagorik Setu ("Citizen Bridge") is a digital platform that connects citizens of Bangladesh with government services and officials. It gives citizens one place to report problems, participate in public decision-making, and reach government digital services — and gives government officers and administrators the tools to respond, moderate, and measure it all.

### 1.2 Problem it solves
Today, a citizen who wants to report a broken streetlight, weigh in on a local policy, or access a government service has to know which office to call, which website to use, and has no visibility into whether anything happened after they raised an issue. Nagorik Setu centralizes this into a single, transparent, trackable system — every complaint has a status, every suggestion gets a response, every poll shows real participation.

### 1.3 Who it's for
| Role | Who | Primary goal |
|---|---|---|
| Citizen | Any registered Bangladeshi resident | Report issues, get updates, participate, access services |
| Government Officer | Department-level staff (e.g. City Corporation, LGED) | Triage and resolve citizen input for their department |
| Super Admin | Platform/system administrator | Oversee users, moderate at a system level, monitor analytics |

### 1.4 Current implementation status
This exists today as **two parallel builds**:
1. **Java / Spring Boot build** — server-rendered (Thymeleaf), real layered OOP architecture, in-memory data. This is the primary reference implementation.
2. **React / AI Studio build** — client-simulated version of the same product (state + localStorage instead of a server), used for rapid UI/UX iteration.

Both are **functional demos**, not production systems — see Section 6 for what that means concretely.

---

## 2. How It Works (End-to-End)

### 2.1 Authentication & Roles
- A single `/login` page with three tabs — **Citizen**, **Officer**, **Admin** — routes to role-specific login logic.
- Citizens self-register: full name → phone number → National ID → date of birth → password → **OTP verification** (simulated: a fixed demo code is shown on screen instead of a real SMS) → profile created automatically.
- Officers and Admins are provisioned accounts (seeded, not self-registered) with an additional simulated 2FA/MFA step at login.
- After login, the server stores `userId` + `role` in the session; a role-based access control (RBAC) interceptor blocks `/citizen/**`, `/officer/**`, `/admin/**` from anyone without the matching role, redirecting to `/login` otherwise.

### 2.2 Complaint Lifecycle
This is the core workflow of the platform:

```
Citizen files complaint
   → SUBMITTED
   → PENDING            (queued, unassigned)
   → ASSIGNED           (an officer takes ownership)
   → UNDER_REVIEW / IN_PROGRESS   (officer working it)
   → WAITING_FOR_CITIZEN (officer needs more info)
   → RESOLVED / REJECTED
   → CLOSED
   → (citizen may REOPEN if unsatisfied, back to UNDER_REVIEW)
```
Every transition writes a timestamped, attributed entry to the complaint's timeline, which the citizen sees in full. Once resolved, the citizen can leave a star rating + written feedback.

### 2.3 Public Discussions (moderated)
Citizens post a discussion → it enters a **pending** state → an officer must **approve** or **reject** it before it becomes publicly visible → once public, citizens can like, dislike, bookmark, comment, and share; officers can additionally pin, lock, or attach an official government response.

### 2.4 Polls
Officers create official polls (question, category, options, deadline). Each citizen may vote exactly once per poll; results are shown as live-updating percentages. Closed polls move to an archive with final results.

### 2.5 Suggestion Box
Citizens submit improvement ideas → community upvotes/downvotes signal support → officers move it through **Submitted → Under Review → Accepted / Implemented / Rejected**, attaching feedback the citizen can see.

### 2.6 Gamification Loop
Actions earn points (vote +5, complaint +8, discussion +10, suggestion +15). Points map to badge tiers (New → Bronze → Silver → Gold → Platinum), and all citizens are ranked on a leaderboard. This exists to encourage continued civic participation, not as a vanity feature.

### 2.7 Notifications
Any state change that affects a citizen (status change, approval, reply, poll result, suggestion update) generates a notification, shown with an unread badge count and marked read on view.

### 2.8 Admin Oversight
Super Admin sees system-wide numbers (citizens, verified citizens, complaint volume, resolution rate), can suspend/reactivate any citizen or officer account, and has an analytics dashboard (charts by category, by division, resolution rate trend).

---

## 3. Feature Inventory (What's Built)

| Area | Status | Notes |
|---|---|---|
| Citizen registration + OTP | ✅ Working | OTP simulated, no real SMS gateway |
| Citizen / Officer / Admin login | ✅ Working | 2FA/MFA simulated |
| RBAC route protection | ✅ Working | Session-based interceptor |
| Complaint filing + full lifecycle | ✅ Working | Timeline, rating, reopen all functional |
| Discussion feed + moderation | ✅ Working | Search/sort/filter, like/dislike/bookmark/comment, approve/reject/pin/lock |
| Polls (vote + archive) | ✅ Working | One-vote-per-citizen enforced |
| Suggestion box | ✅ Working | Full status workflow + feedback |
| Notifications | ✅ Working | Per-user, typed, read/unread |
| Points, badges, leaderboard | ✅ Working | |
| Emergency services page | ✅ Working (static data) | Quick-call links, nearby facility list is hardcoded, not geolocated |
| Digital services directory | ✅ Working (links out) | Links to real gov.bd domains where they exist |
| Admin user management | ✅ Working | Suspend/reactivate toggle |
| Admin analytics dashboard | ✅ Working | Chart.js, computed from live seeded data |
| Light/dark mode | ✅ Working | Persisted, animated transition |
| Language toggle (EN/BN) | ⚠️ Partial | Toggle exists on profile; only some UI strings are actually translated |
| File/image upload on complaints | ⚠️ UI only | Upload widget is present but does not store files |
| GPS location detection | ⚠️ UI only | Checkbox present, does not call browser geolocation API |

---

## 4. What Needs to Be Worked On

This is the honest gap list — organized by priority for anyone picking this project up next.

### 4.1 Must-fix before this could be a real product (P0)
- [ ] **Persistent database.** All data is currently in-memory (`ConcurrentHashMap`) and is wiped on every restart. Needs PostgreSQL/MySQL + Spring Data JPA, with proper migrations (Flyway/Liquibase).
- [ ] **Real authentication security.** Passwords are stored in plain text. Needs hashing (BCrypt), real session/token security review, and rate-limiting on login attempts.
- [ ] **Real OTP/SMS + 2FA/MFA.** Currently a hardcoded demo code shown on screen. Needs an actual SMS gateway (e.g. a Bangladeshi SMS API provider) and a real TOTP-based 2FA for officers/admins.
- [ ] **File uploads.** Complaint photo/document upload needs real storage (S3-compatible object storage or equivalent) with size/type validation and virus scanning.
- [ ] **Input validation & sanitization.** Server-side validation exists loosely via required form fields; needs formal `@Valid` DTOs with constraint annotations, and output escaping audit for stored user content (discussion/comment text) to prevent stored XSS.

### 4.2 Important gaps (P1)
- [ ] **GPS-based complaint location.** Wire up the browser Geolocation API and reverse-geocoding so "detect my location" actually works, and feed it into the officer's nearby-complaints view.
- [ ] **Real nearby-facility data.** Emergency page's "nearby police stations/hospitals" list is static text — needs a real facility dataset or maps API integration.
- [ ] **Full Bangla localization.** Only a handful of strings toggle with the language switch; needs a proper i18n setup (Spring MessageSource or i18next on the frontend) covering every screen, including RTL-safe number/date formatting for Bangla numerals if desired.
- [ ] **Officer department scoping.** Currently any officer can act on any complaint/discussion/suggestion; a real system needs complaints routed and restricted by department/jurisdiction.
- [ ] **Audit logging.** Admin dashboard mentions "Officer Activity Log" and "Audit Logs" conceptually, but there's no persisted, queryable audit trail of who-did-what-when yet.
- [ ] **Pagination.** All list views (complaints, discussions, citizens) currently render everything unpaginated; fine for demo data, will not scale.
- [ ] **Search.** Discussion search is a simple substring match in memory; a global cross-entity search (complaints + discussions + suggestions + services) described in the original spec is not yet built.

### 4.3 Nice-to-have polish (P2)
- [ ] Automated tests (unit tests for services, integration tests for controllers) — currently none exist.
- [ ] API layer (REST/JSON endpoints) separate from the server-rendered views, to support the AI Studio/React build talking to a real backend instead of localStorage.
- [ ] Email notifications in addition to in-app notifications.
- [ ] Complaint heatmap visualization (mentioned in original spec, not yet built).
- [ ] Device/session management ("log out of all devices," active session list) under citizen security settings.
- [ ] Accessibility audit against the checklist in `design.md` Section 10 — believed compliant by design but not formally tested with screen readers.
- [ ] CI/CD pipeline (build, test, containerize, deploy) — none configured yet.

### 4.4 Explicitly out of scope for this project
- Real government system integration (this does not actually talk to NID, BRTA, land record, or tax systems — service cards link out to the real public sites, they are not embedded).
- Payment processing for utility bills / fees.
- Legal/compliance review for handling real National ID numbers (would require Bangladesh government data-handling approval in a real deployment).

---

## 5. Non-Functional Requirements (Target State)

| Area | Current | Target for production |
|---|---|---|
| Data persistence | In-memory | Relational DB with backups |
| Availability | Single instance, dev-only | Multi-instance, load balanced |
| Security | Demo-grade (plaintext passwords, simulated MFA) | Hashed credentials, real MFA, pen-tested |
| Performance | Untested (small seeded dataset) | Load-tested for expected citizen volume |
| Accessibility | Designed to WCAG AA per `design.md` | Independently audited |
| Localization | Partial EN/BN | Full EN/BN parity |
| Observability | Console logging only | Structured logging + monitoring/alerting |

---

## 6. Known Limitations (Read Before Demoing)

State this plainly to anyone evaluating the project:
- **Data does not persist** across a server restart — this is intentional for demo simplicity, not a bug, but it means it cannot be used for anything real as-is.
- **Authentication is not production-grade.** Do not reuse this login system, as written, for anything handling real personal data.
- **OTP/2FA are simulated** — the "security" screens exist to demonstrate the UX flow, not actual protection.
- **Two builds, not fully in sync.** The Spring Boot build is the more complete/authoritative one; the AI Studio build may lag behind in feature parity since it was generated separately.

---

## 7. Suggested Next Steps (Roadmap Order)

1. Add a real database + password hashing (P0 items) — this unlocks everything else being meaningful.
2. Add automated tests around the service layer before adding more features, to avoid regressions.
3. Build out the REST API layer, so the React/AI Studio build can become a real client instead of a localStorage simulation.
4. Tackle department-scoped officer permissions + audit logging (needed for any real government pilot).
5. Full localization pass.
6. Everything in Section 4.3 as capacity allows.
