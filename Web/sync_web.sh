#!/bin/bash
# ==============================================================================
# Habit Hero & Indie Playbook Web Public Directory Synchronization Script
# ==============================================================================
# This script ensures that both Habit Hero and Indie Playbook public hosting folders
# are synchronized. This prevents deployments in one repository from deleting or
# overwriting the other app's assets on the shared Firebase Hosting domain.

# Paths (absolute for robust multi-repository operations)
HABIT_HERO_WEB="/Users/mattdyor/StudioProjects/HabitHero/Web/public"
INDIE_PLAYBOOK_WEB="/Users/mattdyor/StudioProjects/indie-playbook/Web/public"

echo "⏳ Starting Web Synchronization..."

# Guard checks
if [ ! -d "$HABIT_HERO_WEB" ]; then
    echo "❌ Error: Habit Hero public directory not found at $HABIT_HERO_WEB"
    exit 1
fi

if [ ! -d "$INDIE_PLAYBOOK_WEB" ]; then
    echo "❌ Error: Indie Playbook public directory not found at $INDIE_PLAYBOOK_WEB"
    exit 1
fi

# ==============================================================================
# 1. Sync Indie Playbook (Root Legal Pages) -> Habit Hero
# ==============================================================================
echo "🔹 Syncing Indie Playbook (Root Legal Pages) -> Habit Hero..."

# Copy Indie Playbook root policies
cp "$INDIE_PLAYBOOK_WEB/privacy-policy.html" "$HABIT_HERO_WEB/privacy-policy.html"
cp "$INDIE_PLAYBOOK_WEB/terms-conditions.html" "$HABIT_HERO_WEB/terms-conditions.html"

# Copy Indie Playbook unique assets
cp "$INDIE_PLAYBOOK_WEB/config-indie.js" "$HABIT_HERO_WEB/config-indie.js"
cp "$INDIE_PLAYBOOK_WEB/updateContent-indie.js" "$HABIT_HERO_WEB/updateContent-indie.js"

# ==============================================================================
# 2. Sync Habit Hero (Landing Page, Subdirectory & Assets) -> Indie Playbook
# ==============================================================================
echo "🔹 Syncing Habit Hero (Root Landing Page & Subdirectory) -> Indie Playbook..."

# Copy Habit Hero root landing pages
cp "$HABIT_HERO_WEB/index.html" "$INDIE_PLAYBOOK_WEB/index.html"
cp "$HABIT_HERO_WEB/styles.css" "$INDIE_PLAYBOOK_WEB/styles.css"
cp "$HABIT_HERO_WEB/config.js" "$INDIE_PLAYBOOK_WEB/config.js"
cp "$HABIT_HERO_WEB/updateContent.js" "$INDIE_PLAYBOOK_WEB/updateContent.js"
cp "$HABIT_HERO_WEB/404.html" "$INDIE_PLAYBOOK_WEB/404.html"

# Sync Habit Hero images
rsync -av --delete "$HABIT_HERO_WEB/images/" "$INDIE_PLAYBOOK_WEB/images/"

# Sync Habit Hero isolated subfolder (/herohabit/)
mkdir -p "$INDIE_PLAYBOOK_WEB/herohabit"
rsync -av --delete "$HABIT_HERO_WEB/herohabit/" "$INDIE_PLAYBOOK_WEB/herohabit/"

echo "✅ Web Synchronization Complete!"
echo "👉 Whichever project you deploy from now on will host a perfectly merged set of files!"
