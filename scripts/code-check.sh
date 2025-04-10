#!/bin/bash
echo "🔍 Виконується повна перевірка коду (лінтер + статичний аналіз)..."

# Перевірка стилю коду
./gradlew checkstyleMain checkstyleTest

# Статичний аналіз
./gradlew spotbugsMain spotbugsTest

# Юніт тести
./gradlew test

echo "✅ Перевірка завершена"
