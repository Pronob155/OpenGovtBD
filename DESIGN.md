<div align="center">

# 🎨 OpenGovtBD — UI/UX Design Specification

### উন্মুক্ত সরকার · Government Citizen Engagement Platform

**Design System Reference v1.0** · *July 2026*

> This document covers the complete design system and all page specifications for OpenGovtBD,  
> **excluding** the Login page (`/login`).

</div>

---

## 📑 Table of Contents

1. [Design Philosophy](#1-design-philosophy)
2. [Design Tokens](#2-design-tokens)
   - [2.1 Color Palette](#21-color-palette)
   - [2.2 Typography](#22-typography)
   - [2.3 Spacing & Radius](#23-spacing--radius)
   - [2.4 Shadows](#24-shadows)
3. [Layout Systems](#3-layout-systems)
   - [3.1 Public Layout](#31-public-layout)
   - [3.2 App Shell](#32-app-shell)
4. [Component Library](#4-component-library)
   - [4.1 Buttons](#41-buttons)
   - [4.2 Cards](#42-cards)
   - [4.3 Status Pills](#43-status-pills)
   - [4.4 Forms](#44-forms)
   - [4.5 Alerts](#45-alerts-flash-messages)
   - [4.6 Data Display](#46-data-display)
   - [4.7 Social & Engagement](#47-social--engagement)
   - [4.8 Toggle Switch](#48-toggle-switch)
5. [Page Specifications](#5-page-specifications)
   - [5.1 Landing Page](#51-landing-page---)
   - [5.2 Registration](#52-registration----register)
   - [5.3 OTP Verification](#53-otp-verification----otp)
   - [5.4 Citizen Workspace](#54-citizen-workspace)
   - [5.5 Government Officer Workspace](#55-government-officer-workspace)
   - [5.6 Super Admin Workspace](#56-super-admin-workspace)
   - [5.7 Error Pages](#57-error-pages)
6. [Navigation Maps](#6-navigation-maps)
7. [Responsive Breakpoints](#7-responsive-breakpoints)
8. [Dark Mode](#8-dark-mode)
9. [Motion & Animation](#9-motion--animation)
10. [Iconography](#10-iconography)
11. [File Reference](#11-file-reference)
12. [Out of Scope](#12-out-of-scope)

---

## 1. Design Philosophy

OpenGovtBD uses a **government-grade, minimalist, and trustworthy** visual language. The core metaphor is **Setu (Bridge)** — a living, animated connection between citizens and their government.

| Principle | Icon | Description |
| :--- | :---: | :--- |
| **Trust** | 🏛️ | Clean layouts, an official blue/green palette, and a clear visual hierarchy convey institutional authority |
| **Accessibility** | ♿ | Focus rings, skip links, reduced-motion support, and WCAG-compliant contrast ratios ensure inclusivity |
| **Participation** | 🤝 | Cards, polls, and discussions make civic engagement feel approachable and rewarding |
| **Transparency** | 🔍 | Status pills, timelines, live metrics, and open data visualizations reinforce accountability |

**Signature Motif:** The **bridge arc SVG** on the landing page — two animated nodes (Citizen ↔ Government) joined by pulsing dots travelling along a gradient arc, symbolizing the platform's core mission.

---

## 2. Design Tokens

### 2.1 Color Palette

#### 🎨 Brand & Primary

| Token | Hex | Usage |
| :--- | :---: | :--- |
| `--setu-blue` | `#0B4F8A` | Primary actions, links, active nav, avatar rings |
| `--setu-blue-dark` | `#073761` | Hover states, auth side-panel gradient |
| `--setu-blue-light` | `#EAF2FB` | Active nav background, icon containers, info pills |
| `--bd-green` | `#046A38` | Secondary actions, success accents, government node |
| `--bd-green-light` | `#E7F5EC` | Green icon backgrounds, hero eyebrow pill |

#### ⚪ Neutral

| Token | Hex | Usage |
| :--- | :---: | :--- |
| `--bg` | `#F4F7FB` | Page background |
| `--surface` | `#FFFFFF` | Cards, sidebar, modal backgrounds, inputs |
| `--ink` | `#101828` | Primary body text |
| `--muted` | `#667085` | Secondary text, labels, placeholders |
| `--border` | `#E4E9F0` | Dividers, card and input borders |

#### 🔴 Semantic

| Token | Hex | Usage |
| :--- | :---: | :--- |
| `--success` | `#16A34A` | Resolved status, verified badges |
| `--warning` | `#F59E0B` | Pending status, important notices |
| `--error` | `#DC2626` | Emergency alerts, validation errors, unread dot |

---

### 2.2 Typography

| Element | Font Family | Weight | Size |
| :--- | :--- | :---: | :--- |
| Body text | Inter, Noto Sans Bengali | 400 | 14–15px |
| Page headings | Inter | 700–800 | 18–46px |
| Navigation links | Inter | 600 | 14px |
| Section labels | Inter | 700 | 11px (uppercase + letter-spacing) |
| Stat / metric values | Inter | 800 | 24–26px |

> **Loaded fonts:** Inter (400–800), Noto Sans Bengali (400, 600, 700), Material Symbols Rounded.

---

### 2.3 Spacing & Radius

| Token | Value | Notes |
| :--- | :--- | :--- |
| `--radius` | `16px` | Cards, modals |
| `--radius-sm` | `10px` | Buttons, inputs, pills |
| `--sidebar-w` | `264px` | Fixed sidebar width |
| `--topbar-h` | `72px` | Fixed top navigation bar |
| Content padding | `28px` desktop · `18px` mobile | Inner section padding |
| Container max-width | `1240px` | Main content column |

---

### 2.4 Shadows

```
--shadow-sm: 0 1px 2px rgba(16, 24, 40, 0.05);    /* Subtle depth for inputs */
--shadow-md: 0 4px 16px rgba(11, 79, 138, 0.08);   /* Cards, dropdowns          */
--shadow-lg: 0 12px 40px rgba(11, 79, 138, 0.14);  /* Modals, floating panels   */
```

---

## 3. Layout Systems

### 3.1 Public Layout

*Used on: Landing (`/`), Registration, OTP, Error pages*

```
┌──────────────────────────────────────────────────────┐
│  🌐 Public Nav  (sticky, backdrop-filter: blur 10px) │
│  Brand mark · Log in · Get Started                   │
├──────────────────────────────────────────────────────┤
│                                                      │
│  🏠 Hero / Form / Error Illustration                 │
│                                                      │
├──────────────────────────────────────────────────────┤
│  📄 Site Footer                                      │
└──────────────────────────────────────────────────────┘
```

| Detail | Specification |
| :--- | :--- |
| Nav height | 76px, sticky top, glass blur |
| Brand mark | 42×42px gradient tile (blue→green), `account_balance` icon |
| Bengali subtitle | `উন্মুক্ত সরকার` in muted weight beneath the brand name |

---

### 3.2 App Shell

*Used on: All authenticated Citizen, Officer, and Admin dashboards*

```
┌───────────┬──────────────────────────────────────┐
│           │  📌 Topbar (sticky, 72px)            │
│  📂       │  Page title · 🔔 Bell · 👤 Avatar    │
│  Sidebar  ├──────────────────────────────────────┤
│  264px    │  ⚡ Flash alerts (success / error)   │
│           │                                      │
│  Nav      │  📊 Content area (max-width: 1240px) │
│  sections │                                      │
│           │                                      │
│  Logout   │                                      │
└───────────┴──────────────────────────────────────┘
```

| Detail | Specification |
| :--- | :--- |
| Sidebar | Sticky, full viewport height, grouped nav sections (Overview / Participate / Account) |
| Topbar | Frosted glass background, notification bell with red unread dot |
| Mobile (<960px) | Sidebar collapses to a slide-in drawer; hamburger icon + full-screen overlay |

---

## 4. Component Library

### 4.1 Buttons

| Variant | Style | Preview | Intended Use |
| :--- | :--- | :---: | :--- |
| `btn-primary` | Blue fill, white text | <kbd>🔵 Primary</kbd> | Primary call-to-action |
| `btn-green` | Green fill, white text | <kbd>🟢 Green</kbd> | Officer affirmative actions |
| `btn-outline` | Blue border, blue text | <kbd>🔷 Outline</kbd> | Secondary CTA |
| `btn-ghost` | Border only, muted text | <kbd>⬜ Ghost</kbd> | Tertiary / navigational |
| `btn-danger` | Red fill, white text | <kbd>🔴 Danger</kbd> | Emergency actions, account suspension |
| `btn-sm` | Compact padding | — | Inline row-level actions |
| `btn-block` | Full-width | — | Form submit buttons |

> **Interaction:** `:active` scale `0.97` (0.15s ease), hover shadow lift (`--shadow-md`).

---

### 4.2 Cards

| Class | Description |
| :--- | :--- |
| `.card` | White surface, 1px `--border`, `--radius`, `--shadow-sm` |
| `.card.hoverable` | Adds `-2px` Y-axis lift on `:hover` |
| `.card-pad` | Standard `24px` inner padding |
| `.stat-card` | Icon + large metric value + descriptive label row |

---

### 4.3 Status Pills

| Class | Color | Preview | Example Statuses |
| :--- | :--- | :---: | :--- |
| `pill-info` | Blue | <kbd>🔵 Submitted</kbd> | Submitted, Notice |
| `pill-success` | Green | <kbd>🟢 Resolved</kbd> | Resolved, Accepted, Implemented |
| `pill-warning` | Amber | <kbd>🟠 Pending</kbd> | Pending, Under Review, In Progress |
| `pill-error` | Red | <kbd>🔴 Rejected</kbd> | Rejected, Emergency |
| `pill-muted` | Gray | <kbd>⚫ Draft</kbd> | Draft, Inactive, Archived |

---

### 4.4 Forms

| Element | Specification |
| :--- | :--- |
| **Label** | 13.5px, weight 600, `--ink` |
| **Input / Select / Textarea** | 11px radius, 1.5px border, blue focus ring (4px glow) |
| **Input Group** | Prefix badge for context (e.g., `+88` for phone fields) |
| **OTP Input** | 6 individual boxes, 48×56px each, centered digit typography |
| **Checkbox Row** | Terms acceptance on registration form |

---

### 4.5 Alerts (Flash Messages)

| Type | Icon | Background | Preview |
| :--- | :---: | :--- | :--- |
| `alert-success` | `check_circle` | Green tint (`--bd-green-light`) | 🟢 Operation successful |
| `alert-error` | `error` | Red tint | 🔴 Something went wrong |
| `alert-warning` | `security` | Amber tint | 🟠 Important notice |
| `alert-info` | `shield` | Blue tint (`--setu-blue-light`) | 🔵 Information update |

> Flash messages use the `.pop-in` animation on page load for a non-jarring entrance.

---

### 4.6 Data Display

| Component | Specification |
| :--- | :--- |
| **Data Table** (`.data-table`) | Uppercase 11px column headers, hover row highlight |
| **Timeline** | Vertical line + colored dot markers (blue = active, green = completed) |
| **Progress Ring** | SVG `<circle>`, green stroke, percentage label centered in the ring |
| **Progress Bar** | 8px height, blue fill, animated width transition (1s ease) |
| **Empty State** | Large centered icon block + heading + CTA button |

---

### 4.7 Social & Engagement

| Component | Specification |
| :--- | :--- |
| **Reaction Buttons** | Like, Dislike, Bookmark — border-pill style, fills blue when active |
| **Star Rating** | 5-star hover/select for complaint satisfaction feedback |
| **Comment Row** | Avatar + text + relative timestamp |

---

### 4.8 Toggle Switch

| Property | Value |
| :--- | :--- |
| Track size | 40×24px |
| Knob size | 18px diameter |
| Active color | `--setu-blue` |
| Locations | Citizen sidebar (instant toggle) · Profile preferences (persistent save) |

---

## 5. Page Specifications

> **Note:** The Login page (`/login`) is intentionally excluded from this document.

---

### 5.1 Landing Page — `/`

**Layout:** Public nav → Hero → Feature grid → Announcements → Footer

| Section | Content |
| :--- | :--- |
| Hero eyebrow | "Official Government of Bangladesh Platform" (green pill badge) |
| Hero headline | *"The digital bridge between citizens and government."* |
| Hero sub-text | Brief platform description |
| Hero CTAs | "Create Citizen Account" (primary) · "Officer / Admin Entry" (outline) |
| Hero stats | Registered Citizens · Complaints Filed · Resolution Rate |
| Bridge SVG | Animated arc with citizen/government nodes and flow dots |
| Feature cards (×3) | Report Issue · Join Conversation · Digital Services |
| Announcements section | Government notices with priority pills (NORMAL / IMPORTANT / EMERGENCY) |

> **Animations:** `fade-up` stagger on hero elements and feature cards; bridge dot `flow` looping animation.

---

### 5.2 Registration — `/register`

**Layout:** Auth shell (split-screen) — left gradient side panel + right form card

| Field | Input Type | Hint |
| :--- | :--- | :--- |
| Full Name | `text` | As per NID |
| Mobile Number | `tel` | Format: `01XXXXXXXXX` |
| National ID | `text` | 10 or 17 digits |
| Date of Birth | `date` | ISO format |
| Password | `password` | Minimum strength indicator |
| Terms & Conditions | `checkbox` | Required to proceed |

**Primary CTA:** "Continue to OTP Verification →"  
**Error State:** Red alert banner (e.g., duplicate phone number)

---

### 5.3 OTP Verification — `/otp`

**Layout:** Auth shell, centered card

| Element | Description |
| :--- | :--- |
| Phone display | Masked number retrieved from session |
| OTP boxes | 6-digit individual inputs; demo code (`123456`) displayed on page |
| Submit button | Verifies and redirects to the Citizen Dashboard |
| Back link | Returns to the Registration form |

---

### 5.4 Citizen Workspace

#### 📊 Dashboard — `/citizen/dashboard`

| Widget | Description |
| :--- | :--- |
| Welcome banner | Personalized greeting + SVG profile completion ring |
| Emergency banner | Red gradient alert strip (shown when an EMERGENCY announcement is active) |
| Stat cards (×4) | My Complaints · Resolved · Points & Badge · Active Polls |
| Active Complaints | List with status pills and "View all →" link |
| Active Polls | Question preview + vote count |
| Public Discussions | Title + like/comment metadata |
| Announcements | Government notices, sorted by priority |
| Quick Services | NID · E-Passport · Tax (3-column grid) |

> `body.dark` class is applied from the citizen's saved dark mode preference.

---

#### 🌐 Digital Services — `/citizen/services`

Grid of service cards: icon, service name, description, external link indicator.

#### 🚨 Emergency Hub — `/citizen/emergency`

Emergency contact cards (999, 109, 333), hotline numbers, location-aware help links.

---

#### 📝 Complaints List — `/citizen/complaints`

| Element | Detail |
| :--- | :--- |
| Page CTA | "File New Complaint" button in the header |
| List columns | Tracking ID · Title · Category · Status pill · Date filed |
| Empty state | Inbox icon + descriptive prompt + CTA |

#### 📄 Complaint Form — `/citizen/complaints/new`

| Field | Required |
| :--- | :---: |
| Title | ✅ |
| Category | ✅ (Select dropdown) |
| District / Upazila | ✅ |
| Description | ✅ (Textarea) |
| Photo upload | ➖ Optional (multipart) |

#### 📋 Complaint Detail — `/citizen/complaints/{id}`

- Status pill + Tracking ID in the page header
- Full timeline component (Submitted → Assigned → In Progress → Resolved)
- Officer reply thread
- Actions: Star rating (1–5), Reopen button (if status is Resolved)

---

#### 💬 Discussions — `/citizen/discussions`

- Inline "Start a Discussion" form (title + content fields)
- Feed: title, author avatar, like/comment counts, bookmark, moderation status badge

#### 💭 Discussion Detail — `/citizen/discussions/{id}`

- Full post body
- Reaction row: `thumb_up` · `thumb_down` · `bookmark`
- Comment list + "Add a constructive comment..." input

---

#### 🗳️ Polls — `/citizen/polls`

- Active poll cards with radio-button options
- Live percentage bars rendered after voting
- Vote count per option

#### 💡 Suggestions — `/citizen/suggestions`

- Submit new suggestion form (inline)
- Feed: title, upvote/downvote counts, status pill

#### 💡 Suggestion Detail — `/citizen/suggestions/{id}`

- Full suggestion text + official government feedback block
- Vote buttons + comment section

---

#### 🏆 Leaderboard — `/citizen/leaderboard`

| Column | Description |
| :--- | :--- |
| Position | Rank number with medal styling for top 3 |
| Citizen | Avatar + Name |
| Points | Total earned points |
| Badge | Tier pill (New / Bronze / Silver / Gold / Platinum) |

#### 🔔 Notifications — `/citizen/notifications`

- Visual distinction between unread (bold, accent dot) and read entries
- Type icons: `notifications` (NOTICE), `warning` (ALERT), `emoji_events` (REWARD)
- Click to mark individual notifications as read

#### ⚙️ Profile & Settings — `/citizen/profile`

- Editable fields: address, division, communication preferences
- Toggle switches: email notifications, dark mode
- Verification badges: phone ✅, NID ✅
- Points total + current badge tier display

---

### 5.5 Government Officer Workspace

#### 📊 Dashboard — `/officer/dashboard`

| Widget | Description |
| :--- | :--- |
| Stat cards | Open complaints · Pending discussions · Suggestions in queue |
| Complaint Queue preview | Top 5 entries with "View all →" link |
| Recent activity | Timestamped log of status changes and new submissions |

#### 📋 Complaint Queue — `/officer/complaints`

- Filterable data table
- Columns: Tracking ID · Title · Citizen · Category · Status · Date
- Row action: "Open" link to detail view

#### 📄 Complaint Detail — `/officer/complaints/{id}`

- Two-column layout: complaint information (left) + action panel (right)
- **Update Status** — dropdown selection + optional officer note
- **Reply to Citizen** — free-text textarea + "Send Reply" action
- **Assign Officer** — officer dropdown selector

#### 💬 Discussion Moderation — `/officer/discussions`

- Pending discussions queue
- Per-row actions: Approve · Reject · Pin · Lock · Post Official Response

#### 💡 Suggestion Review — `/officer/suggestions`

- Suggestion list with status filter tabs
- Status update dropdown: Under Review → Accepted / Implemented / Rejected
- Government feedback textarea

#### 🗳️ Create Poll — `/officer/polls/new`

| Field | Description |
| :--- | :--- |
| Question | Poll title text |
| Category | Category select dropdown |
| Options | Dynamic add/remove fields (minimum 2 options required) |
| End Date | Date picker |

---

### 5.6 Super Admin Workspace

#### 📊 System Overview — `/admin/dashboard`

| Widget | Description |
| :--- | :--- |
| System stat cards | Total citizens · Total officers · Total complaints · Resolution rate |
| Quick links | User management · Analytics |
| Recent activity feed | Platform-wide timestamped event log |

#### 👥 User Management — `/admin/users`

- **Citizens table** — Columns: Name · Phone · Status · Points · Actions
- **Officers table** — Columns: Name · Email · Department · Status · Actions
- Action: **Suspend** / **Reactivate** toggle button per row

#### 📈 Analytics Dashboard — `/admin/analytics`

| Chart | Type | Data |
| :--- | :--- | :--- |
| Complaints by Category | Bar chart | Count per category |
| Complaints by Division | Doughnut chart | Geographic distribution |
| Resolution Trend | Line chart | Resolution rate over time |

> Plus metric cards: total complaints, average resolution days, active citizen count.

---

### 5.7 Error Pages

#### 404 — Not Found

- Centered layout, large gradient "404" display text
- Message: *"Page not found"*
- CTA: Return Home button

#### 500 — Server Error

- Same centered layout pattern as 404
- Message: *"Something went wrong on our end"*
- CTA: Return Home button

---

## 6. Navigation Maps

### 📂 Citizen Sidebar

```
📁 Overview
  ├── 📄 Dashboard
  ├── 📄 Digital Services
  └── 📄 Emergency Hub

📁 Participate
  ├── 📄 My Complaints
  ├── 📄 Discussions
  ├── 📄 Polls
  ├── 📄 Suggestion Box
  └── 📄 Leaderboard

📁 Account
  ├── 📄 Notifications  [🔴 unread count]
  ├── 📄 Profile & Settings
  ├── 🌙 Dark Mode toggle
  └── 🚪 Logout
```

### 📂 Officer Sidebar

```
📁 Officer Workspace
  ├── 📄 Dashboard
  ├── 📄 Complaint Queue
  ├── 📄 Discussion Moderation
  ├── 📄 Suggestion Review
  └── 📄 Create Poll

📁 Account
  └── 🚪 Logout
```

### 📂 Super Admin Sidebar

```
📁 Super Admin
  ├── 📄 System Overview
  ├── 📄 Citizens & Officers
  └── 📄 Analytics

📁 Account
  └── 🚪 Logout
```

---

## 7. Responsive Breakpoints

| Breakpoint | Icon | Layout Behavior |
| :--- | :---: | :--- |
| **> 960px** | 🖥️ | Full sidebar visible, multi-column grid layouts |
| **≤ 960px** | 💻 | Sidebar hidden — slides in from left as a drawer; hamburger + overlay |
| **≤ 860px** | 📱 | Auth shell hides the left gradient side panel |
| **≤ 640px** | 📱 | All grids collapse to single-column; hero font size reduced |

---

## 8. Dark Mode

Applied via the `body.dark` class on all citizen-facing pages when the preference is enabled.

| Token | Light Value | Dark Value |
| :--- | :---: | :---: |
| `--bg` | `#F4F7FB` | `#0B1220` |
| `--surface` | `#FFFFFF` | `#131C2E` |
| `--ink` | `#101828` | `#F2F5FA` |
| `--muted` | `#667085` | `#92A0B8` |
| `--border` | `#E4E9F0` | `#223049` |

**Toggle Locations:**

| # | Location | Behavior |
| :---: | :--- | :--- |
| 1 | **Citizen sidebar** | Instant POST to `/citizen/theme/toggle` + redirect to current page |
| 2 | **Profile & Settings page** | Persistent save via form submission |

---

## 9. Motion & Animation

| Animation Name | Duration | Trigger / Use Case |
| :--- | :---: | :--- |
| `fadeUp` | 0.5s | Page section entrance (staggered) |
| `popIn` | 0.3s | Flash alert appearance, modal entry |
| `flow` | 3.2s loop | Bridge SVG dot travel animation on landing page |
| `shimmer` | 1.4s loop | Skeleton loading placeholder state |
| Button `:active` scale | 0.15s | Scale `0.97` press feedback |
| Progress bar width | 1s ease | Poll results bars, profile completion ring |

> ♿ **Accessibility:** All animations collapse to an effective `0.001ms` duration when `prefers-reduced-motion: reduce` is detected in the user's OS settings.

---

## 10. Iconography

**Library:** Material Symbols Rounded — loaded via Google Fonts CDN.

| Context | Icon Name |
| :--- | :--- |
| 🏛️ Brand / Platform | `account_balance` |
| 📊 Dashboard | `space_dashboard` |
| 📝 Complaints | `report` · `assignment` |
| 💬 Discussions | `forum` · `gavel` |
| 🗳️ Polls | `how_to_vote` |
| 💡 Suggestions | `lightbulb` · `rate_review` |
| 🔔 Notifications | `notifications` |
| 🚨 Emergency | `emergency` |
| 🏆 Leaderboard | `trophy` |
| 📈 Analytics | `monitoring` |
| 👥 User Management | `group` |
| 🌙 Dark Mode | `dark_mode` · `light_mode` |

---

## 11. File Reference

| Asset | Path | Type |
| :--- | :--- | :---: |
| 🎨 Design system CSS | `src/main/resources/static/css/style.css` | `CSS` |
| 📐 Layout fragments | `src/main/resources/templates/fragments/layout.html` | `HTML` |
| ⚙️ Client JavaScript | `src/main/resources/static/js/main.js` | `JS` |
| 📄 Page templates (Thymeleaf) | `src/main/resources/templates/` | `HTML` |

---

## 12. Out of Scope

The following screen is **intentionally not documented** in this file:

| Page | Route | Reason |
| :--- | :--- | :--- |
| 🔒 Login (all roles) | `/login` | Excluded per original project scope |

> For authentication entry points, refer to the [Registration](#52-registration----register) and [OTP Verification](#53-otp-verification----otp) sections above.

---

<div align="center">

### 📐 OpenGovtBD Design System v1.0 · July 2026

*For implementation details, refer to [README.md](README.md)*

**উন্মুক্ত সরকার · সেতু (Bridge)**

---

</div>
