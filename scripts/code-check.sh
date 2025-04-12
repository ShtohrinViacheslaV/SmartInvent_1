#!/bin/bash
echo "🔍 Виконується повна перевірка коду (лінтер + статичний аналіз)..."

# Перевірка стилю коду
./gradlew :app:checkstyleMain
./gradlew :backend:checkstyleMain checkstyleTest

# Статичний аналіз
./gradlew :backend:spotbugsMain spotbugsTest

# Юніт тести
./gradlew test

echo "✅ Перевірка завершена"
