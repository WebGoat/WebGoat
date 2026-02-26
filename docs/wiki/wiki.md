# Complete Guide: Publishing Markdown Files to GitHub Wiki

## Table of Contents
- [Understanding GitHub Wikis](#understanding)
- [Prerequisites](#prerequisites)
- [Method 1: Manual Publishing](#method1)
- [Method 2: Git Command Line](#method2)
- [Method 3: GitHub Actions (Automated)](#method3)
- [Method 4: Third-Party Actions](#method4)
- [Advanced Techniques](#advanced)
- [Troubleshooting](#troubleshooting)
- [Best Practices](#best-practices)

<a name="understanding"></a>
## Understanding GitHub Wikis

### What is a GitHub Wiki?

A GitHub wiki is:

- A separate Git repository located at `https://github.com/username/repo.wiki.git`
- A collection of Markdown pages that document your project
- Searchable within GitHub (if public)
- Accessible via the Wiki tab in your repository
- Fully version controlled with Git history

### Wiki Structure

### Key Differences: Repo vs Wiki

| Aspect | Repository | Wiki |
|--------|-----------|------|
| Location | github.com/user/repo | github.com/user/repo/wiki |
| Git URL | github.com/user/repo.git | github.com/user/repo.wiki.git |
| Purpose | Source code | Documentation |
| File structure | Full project structure | Flat markdown files |
| Access | Based on repo permissions | Same as repo permissions |
| Searchable | Code search | Wiki search |

<a name="prerequisites"></a>
## Prerequisites

### 1. Enable Wiki on Your Repository

**Steps:**

1. Navigate to your repository on GitHub
2. Click **Settings** (top right)
3. Scroll to **Features** section
4. Check ✅ **Wikis**
5. Wiki tab appears in navigation
