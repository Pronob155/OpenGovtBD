<div align="center">

# 🎨 OpenGovtBD — UI/UX Design Specification

### উন্মুক্ত সরকার · Government Citizen Engagement Platform

**Design System Reference v1.0** · *July 2026*

> This document covers the complete design system and all page specifications for OpenGovtBD,  
> **excluding** the Login page (`/login`).

</div>

---

## Table of Contents

1. [Design Philosophy](#1-design-philosophy)
2. [Design Tokens](#2-design-tokens)
3. [Layout Systems](#3-layout-systems)
4. [Component Library](#4-component-library)
5. [Page Specifications](#5-page-specifications)
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

| Principle | Description |
| :--- | :--- |
| **Trust** | Clean layouts, an official blue/green palette, and a clear visual hierarchy convey institutional authority |
| **Accessibility** | Focus rings, skip links, reduced-motion support, and WCAG-compliant contrast ratios ensure inclusivity |
| **Participation** | Cards, polls, and discussions make civic engagement feel approachable and rewarding |
| **Transparency** | Status pills, timelines, live metrics, and open data visualizations reinforce accountability |

**Signature Motif:** The **bridge arc SVG** on the landing page — two animated nodes (Citizen ↔ Government) joined by pulsing dots travelling along a gradient arc, symbolizing the platform's core mission.

---

## 2. Design Tokens

### 2.1 Color Palette

#### Brand & Primary

| Token | Hex | Usage |
| :--- | :---: | :--- |
| `--setu-blue` | `#0B4F8A` | Primary actions, links, active nav, avatar rings |
| `--setu-blue-dark` | `#073761` | Hover states, auth side-panel gradient |
| `--setu-blue-light` | `#EAF2FB` | Active nav background, icon containers, info pills |
| `--bd-green` | `#046A38` | Secondary actions, success accents, government node |
| `--bd-green-light` | `#E7F5EC` | Green icon backgrounds, hero eyebrow pill |

#### Neutral

| Token | Hex | Usage |
| :--- | :---: | :--- |
| `--bg` | `#F4F7FB` | Page background |
| `--surface` | `#FFFFFF` | Cards, sidebar, modal backgrounds, inputs |
| `--ink` | `#101828` | Primary body text |
| `--muted` | `#667085` | Secondary text, labels, placeholders |
| `--border` | `#E4E9F0` | Dividers, card and input borders |

#### Semantic

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

**Loaded fonts:** Inter (400–800), Noto Sans Bengali (400, 600, 700), Material Symbols Rounded.

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

```css
--shadow-sm: 0 1px 2px rgba(16, 24, 40, 0.05);    /* Subtle depth for inputs */
--shadow-md: 0 4px 16px rgba(11, 79, 138, 0.08);   /* Cards, dropdowns          */
--shadow-lg: 0 12px 40px rgba(11, 79, 138, 0.14);  /* Modals, floating panels   */
```

---

## 3. Layout Systems

### 3.1 Public Layout

*Used on: Landing (`/`), Registration, OTP, Error pages*

```
┌──────────────────────────────────────────────────┐
│  Public Nav  (sticky, backdrop-filter: blur 10px) │
│  Brand mark · Log in · Get Started               │
├──────────────────────────────────────────────────┤
│                                                  │
│  Hero / Form / Error Illustration                │
│                                                  │
├──────────────────────────────────────────────────┤
│  Site Footer                                     │
└──────────────────────────────────────────────────┘
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
│           │  Topbar (sticky, 72px)               │
│  Sidebar  │  Page title · 🔔 Bell · Avatar       │
│  264px    ├──────────────────────────────────────┤
│           │  Flash alerts (success / error)      │
│  Nav      │                                      │
│  sections │  Content area (max-width: 1240px)    │
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

| Variant | Style | Intended Use |
| :--- | :--- | :--- |
| `btn-primary` | Blue fill, white text | Primary call-to-action |
| `btn-green` | Green fill, white text | Officer affirmative actions |
| `btn-outline` | Blue border, blue text | Secondary CTA |
| `btn-ghost` | Border only, muted text | Tertiary / navigational |
| `btn-danger` | Red fill, white text | Emergency actions, account suspension |
| `btn-sm` | Compact padding | Inline row-level actions |
| `btn-block` | Full-width | Form submit buttons |

**Interaction:** `:active` scale `0.97` (0.15s ease), hover shadow lift (`--shadow-md`).

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

| Class | Color | Example Statuses |
| :--- | :--- | :--- |
| `pill-info` | Blue | Submitted, Notice |
| `pill-success` | Green | Resolved, Accepted, Implemented |
| `pill-warning` | Amber | Pending, Under Review, In Progress |
| `pill-error` | Red | Rejected, Emergency |
| `pill-muted` | Gray | Draft, Inactive, Archived |

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

| Type | Icon | Background |
| :--- | :--- | :--- |
| `alert-success` | `check_circle` | Green tint (`--bd-green-light`) |
| `alert-error` | `error` | Red tint |
| `alert-warning` | `security` | Amber tint |
| `alert-info` | `shield` | Blue tint (`--setu-blue-light`) |

Flash messages use the `.pop-in` animation on page load for a non-jarring entrance.

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

> Login page (`/login`) is **excluded** from this document per scope.

---

### 5.1 Landing Page — `/`

**Layout:** Public nav + hero + feature grid + announcements + footer

| Section | Content |
|---------|---------|
| Hero eyebrow | "Official Government of Bangladesh Platform" (green pill) |
| Hero headline | "The digital bridge between citizens and government." |
| Hero CTA | Create Citizen Account (primary) · Officer/Admin entry (outline) |
| Hero stats | Registered Citizens · Complaints Filed · Resolution Rate |
| Bridge SVG | Animated arc with citizen/gov nodes |
| Feature cards (×3) | Report Issue · Join Conversation · Digital Services |
| Announcements | Priority pills (NORMAL / IMPORTANT / EMERGENCY) |

**Animations:** `fade-up` stagger on hero and cards, bridge dot `flow` animation.

---

### 5.2 Registration — `/register`

**Layout:** Auth shell (split) — left gradient panel + right form card

| Field | Type | Validation hint |
|-------|------|-----------------|
| Full Name | text | As per NID |
| Mobile Number | tel | 01XXXXXXXXX |
| National ID | text | 10 or 17 digits |
| Date of Birth | date | ISO format |
| Password | password | Min strength hint |
| Terms checkbox | checkbox | Required |

**CTA:** "Continue to OTP Verification →"  
**Error state:** Red alert banner (e.g. duplicate phone)

---

### 5.3 OTP Verification — `/otp`

**Layout:** Auth shell, centered card

| Element | Description |
|---------|-------------|
| Phone display | Masked number from session |
| OTP boxes | 6-digit input (demo code shown on page) |
| Submit | Verify and redirect to citizen dashboard |
| Back link | Return to register |

---

### 5.4 Citizen Workspace

#### Dashboard — `/citizen/dashboard`

| Widget | Description |
|--------|-------------|
| Welcome banner | Name + profile completion ring (SVG) |
| Emergency banner | Red gradient alert if EMERGENCY announcement exists |
| Stat cards (×4) | My Complaints · Resolved · Points/Badge · Active Polls |
| Active Complaints | List with status pills, "View all →" |
| Active Polls | Question + vote count preview |
| Public Discussions | Title + like/comment meta |
| Announcements | Government notices |
| Quick Services | NID · E-Passport · Tax (3-up grid) |

**Dark mode:** `body.dark` class applied from citizen preference.

#### Digital Services — `/citizen/services`

Grid of service cards with icon, name, description, external link indicator.

#### Emergency Hub — `/citizen/emergency`

Emergency contact cards (999, 109, 333), hotline numbers, location-aware help links.

#### Complaints List — `/citizen/complaints`

- Header: "File New Complaint" CTA
- Table/list: Tracking ID, title, category, status pill, date
- Empty state: Inbox icon + CTA

#### Complaint Form — `/citizen/complaints/new`

| Field | Required |
|-------|----------|
| Title | Yes |
| Category | Select dropdown |
| District / Upazila | Yes |
| Description | Textarea |
| Photo upload | Optional (multipart) |

**Submit:** "Submit Complaint →"

#### Complaint Details — `/citizen/complaints/{id}`

- Status pill + tracking ID header
- Timeline component (Submitted → Assigned → In Progress → Resolved)
- Officer replies thread
- Actions: Rate (stars), Reopen (if resolved)

#### Discussions — `/citizen/discussions`

- "Start a Discussion" inline form (title + content)
- Feed: title, author, likes, comments, bookmark
- Status badge: Pending approval / Approved

#### Discussion Details — `/citizen/discussions/{id}`

- Full post body
- Reaction row: thumb_up · thumb_down · bookmark
- Comment list + "Add a constructive comment..." input

#### Polls — `/citizen/polls`

- Active poll cards with radio options
- Live percentage bars after voting
- Vote count display

#### Suggestions — `/citizen/suggestions`

- Submit new suggestion form
- List with upvote/downvote counts, status pills

#### Suggestion Details — `/citizen/suggestions/{id}`

- Full suggestion + government feedback
- Vote buttons + comment section

#### Leaderboard — `/citizen/leaderboard`

- Ranked table: position, name, points, badge tier
- Badge tiers: New → Bronze → Silver → Gold → Platinum

#### Notifications — `/citizen/notifications`

- Unread/read visual distinction
- Type icons (NOTICE, ALERT, REWARD)
- Click to mark read

#### Profile — `/citizen/profile`

- Editable: address, division, preferences
- Toggle switches: email notifications, dark mode
- Verification badges: phone, NID
- Points + badge display

---

### 5.5 Officer Workspace

#### Dashboard — `/officer/dashboard`

| Widget | Description |
|--------|-------------|
| Stat cards | Open complaints · Pending discussions · Suggestions queue |
| Complaint Queue preview | Top 5 with "View all →" |
| Recent activity | Status changes, new submissions |

#### Complaint Queue — `/officer/complaints`

- Filterable data table
- Columns: Tracking ID, Title, Citizen, Category, Status, Date
- Row action: "Open" link

#### Complaint Details — `/officer/complaints/{id}`

- Two-column layout: complaint info (left) + action panel (right)
- **Update Status** dropdown + optional note
- **Reply to Citizen** textarea + Send Reply
- Assign officer dropdown

#### Discussion Approval — `/officer/discussions`

- Pending discussions queue
- Actions per row: Approve · Reject · Pin · Lock · Official Response

#### Suggestion Review — `/officer/suggestions`

- Suggestion list with status filter
- Status update dropdown: Under Review → Accepted / Implemented / Rejected
- Government feedback textarea

#### Create Poll — `/officer/polls/new`

| Field | Description |
|-------|-------------|
| Question | Poll title |
| Category | Select |
| Options | Dynamic add/remove (min 2) |
| End date | Date picker |

---

### 5.6 Super Admin Workspace

#### Overview — `/admin/dashboard`

| Widget | Description |
|--------|-------------|
| System stats | Total citizens, officers, complaints, resolution rate |
| Quick links | Users · Analytics |
| Recent activity feed | Platform-wide events |

#### Citizens & Officers — `/admin/users`

- Two sections: Citizens table · Officers table
- Columns: Name, ID/Phone/Email, Status, Points/Dept, Actions
- Action: Suspend / Reactivate toggle button

#### Analytics — `/admin/analytics`

- **Chart.js** visualizations (CDN)
- Charts: Complaints by category (bar), Complaints by division (doughnut), Resolution trend (line)
- Metric cards: Total complaints, avg resolution days, active citizens

---

### 5.7 Error Pages

#### 404 — `/404`

- Centered layout, gradient "404" text
- Message: "Page not found"
- CTA: Return home

#### 500 — Server Error

- Same layout pattern as 404
- Message: "Something went wrong"
- CTA: Return home

---

## 6. Navigation Maps

### Citizen Sidebar

```
Overview
  ├── Dashboard
  ├── Digital Services
  └── Emergency
Participate
  ├── Complaints
  ├── Discussions
  ├── Polls
  ├── Suggestion Box
  └── Leaderboard
Account
  ├── Notifications
  ├── Profile & Settings
  ├── Dark mode toggle
  └── Logout
```

### Officer Sidebar

```
Officer Workspace
  ├── Dashboard
  ├── Complaint Queue
  ├── Discussion Approval
  ├── Suggestion Review
  └── Create Poll
Account
  └── Logout
```

### Admin Sidebar

```
Super Admin
  ├── Overview
  ├── Citizens & Officers
  └── Analytics
Account
  └── Logout
```

---

## 7. Responsive Breakpoints

| Breakpoint | Behavior |
|------------|----------|
| **>960px** | Full sidebar visible, multi-column grids |
| **≤960px** | Sidebar hidden (slide-in drawer), 2-column grids |
| **≤640px** | Single-column grids, reduced hero font size |
| **≤860px** | Auth shell hides left gradient panel |

---

## 8. Dark Mode

Applied via `body.dark` class on citizen pages when preference is enabled.

| Token (dark) | Value |
|--------------|-------|
| `--bg` | `#0B1220` |
| `--surface` | `#131C2E` |
| `--ink` | `#F2F5FA` |
| `--muted` | `#92A0B8` |
| `--border` | `#223049` |

**Toggle locations:**
- Citizen sidebar (instant POST + redirect)
- Profile preferences page (persistent save)

---

## 9. Motion & Animation

| Animation | Duration | Use |
|-----------|----------|-----|
| `fadeUp` | 0.5s | Page section entrance |
| `popIn` | 0.3s | Flash alerts, modals |
| `flow` | 3.2s loop | Bridge SVG dots |
| `shimmer` | 1.4s loop | Skeleton loading |
| Button `:active` | 0.15s | Scale 0.97 |
| Progress bar width | 1s | Poll results, profile ring |

**Reduced motion:** All animations collapse to 0.001ms when `prefers-reduced-motion: reduce`.

---

## 10. Iconography

**Library:** Material Symbols Rounded (Google Fonts CDN)

| Context | Icon |
|---------|------|
| Brand | `account_balance` |
| Dashboard | `space_dashboard` |
| Complaints | `report` / `assignment` |
| Discussions | `forum` / `gavel` |
| Polls | `how_to_vote` |
| Suggestions | `lightbulb` / `rate_review` |
| Notifications | `notifications` |
| Emergency | `emergency` |
| Leaderboard | `trophy` |
| Analytics | `monitoring` |
| Users | `group` |

---

## 11. File Reference

| Asset | Path |
|-------|------|
| Design system CSS | `src/main/resources/static/css/style.css` |
| Layout fragments | `src/main/resources/templates/fragments/layout.html` |
| Client JS (mobile menu, progress rings) | `src/main/resources/static/js/main.js` |
| Page templates | `src/main/resources/templates/` |

---

## 12. Out of Scope

The following screen is intentionally **not documented** in this file:

- **Login page** (`/login`) — citizen, officer, and admin tabbed login forms

For authentication flow entry points, refer to the Register and OTP sections above.

---

*Last updated: July 2026 · OpenGovtBD Design System v1.0*
