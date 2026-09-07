.\gradlew clean testClasses
#компіляція

.\gradlew clean test
#фулл тест

.\gradlew clean test --tests ringotel.tests.LoginTests
# тест логіна

.\gradlew clean test --tests ringotel.tests.ContactTests
# створення та перевірка контакту

.\gradlew clean test --tests ringotel.tests.PresenceTests
# зміна Presence статусів - адмінка

.\gradlew clean test --tests ringotel.tests.ChatTests
# перевірка чатів: звичайний контакт + imported contact без можливості messaging

.\gradlew clean test --tests ringotel.tests.ContactMessagingAuditTests
# перевірка messaging для кожного контакту:
# відправляє "test", видаляє створений чат,
# якщо messaging недоступний — робить screenshot і пише WARN

.\gradlew clean test --tests ringotel.tests.ChatCleanupTests
# видалити всі чати

.\gradlew clean test --tests ringotel.tests.CallTests
# базові дзвінки 4321 ↔ 1234:
# виклик → прийняття → 15 секунд → завершення

.\gradlew clean test --tests ringotel.tests.CallControlsTests
# перевірка call controls під час дзвінка:
# Answer, Reject, Hold/Resume, Mute/Unmute,
# Record, More actions, Transfer

.\gradlew clean test --tests ringotel.tests.CallTests.user4321Calls1234AndHangUpAfter15Seconds
# 4321 → 1234 → Answer → 15 сек → Hang Up

.\gradlew clean test --tests ringotel.tests.CallTests.user1234Calls4321AndHangUpAfter15Seconds
# 1234 → 4321 → Answer → 15 сек → Hang Up

.\gradlew clean test --tests ringotel.tests.CallControlsTests.answerCallAndCheckCallControls
# перевірка call controls:
# Answer → Hold/Resume → Mute/Unmute → Record →
# More actions → Transfer → вибір контакту


.\gradlew clean test --tests ringotel.tests.CallControlsTests.rejectIncomingCall
# 4321 → 1234 → Reject вхідного дзвінка

.\gradlew clean test --tests ringotel.tests.ChatTests.checkMessagingForImportedContact
# перевірка imported contact test1import1:
# якщо messaging недоступний → screenshot + WARN, тест не падає


.\gradlew clean test --tests ringotel.tests.ContactMessagingAuditTests.checkMessagingAvailabilityForAllContacts
# перевірка можливості messaging для всіх контактів