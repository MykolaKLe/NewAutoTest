

1. Ringotel Test Runner — це графічний інструмент для запуску автоматизованих UI-перевірок Ringotel Web.

Основна ідея: користувачу не потрібно редагувати код або вручну налаштовувати test class.
Потрібно запустити Test Runner, вказати тестовий domain та користувачів, вибрати потрібні сценарії і браузер, після чого натиснути Run Selected.

Тести працюють з:
https://app.shell.ringotel.co/login

Використовується:
- Java 21
- Selenium WebDriver
- TestNG
- Gradle
- WebDriverManager


2. ЩО ПОТРІБНО ДЛЯ ЗАПУСКУ

Потрібно:

- Java 21
- скачаний або клонований проєкт
- Google Chrome, Mozilla Firefox або Microsoft Edge

Якщо в проєкті є Gradle Wrapper (`gradlew.bat`), окремо встановлювати Gradle не потрібно.


3. ШВИДКИЙ ЗАПУСК

Windows:

1. Відкрити кореневу папку проєкту.
2. Це повинна бути папка, де знаходиться `build.gradle`.
3. Відкрити CMD у цій папці.
4. Виконати:

gradlew.bat run

Після цього відкриється вікно:

Ringotel Test Runner


macOS / Linux:

./gradlew run


4. ЯК КОРИСТУВАТИСЯ TEST RUNNER

Після запуску відкривається графічне вікно.

Основний порядок роботи:

1. Перевірити Domain.
2. Ввести Extension і Password для User 1.
3. Якщо потрібні Internal Calls або Call Controls — додати другого користувача через `+ Add User`.
4. Вибрати потрібні Scenarios.
5. Вибрати Browser.
6. Якщо вибрано Chats — заповнити Chat Configuration.
7. Натиснути `Run Selected`.
8. Слідкувати за результатом у вкладках Logs.


5. TEST CONFIGURATION

Domain

За замовчуванням:

testwebsoftphone

Domain можна змінити прямо у вікні Runner.


User 1

Для кожного користувача потрібно вказати:

- Extension
- Password

Перший User створюється автоматично.


Додатковий User

Щоб додати другого користувача:

+ Add User

Другий користувач потрібен для:

- Internal Calls
- Call Controls

Без другого користувача ці сценарії недоступні.


6. ПОТОЧНІ TEST USERS

Для стандартного test environment використовуються:

Domain:
testwebsoftphone


User 1:

Extension:
4321

Password:
obFxbmYKYy9pwjAD


User 2:

Extension:
1234

Password:
R0eE6jAMUmyck7uD


Для більшості сценаріїв використовується User 1.

Для Internal Calls та Call Controls використовуються User 1 і User 2.


7. ДОСТУПНІ СЦЕНАРІЇ

У Test Runner доступні:

- Login
- Contacts
- Presence
- Chats
- Messaging Audit
- Chat Cleanup
- Internal Calls
- Call Controls
- Real Call
- Real SMS


8. LOGIN

Перевіряє авторизацію тестових користувачів.

Сценарій:

1. Вводиться Domain.
2. Вводяться credentials.
3. Виконується Log in.
4. Перевіряється успішний вхід.
5. Створюється screenshot як evidence.


9. CONTACTS

Перевіряє роботу з контактами.

Сценарій:

1. Login під User 1.
2. Створюється новий унікальний Contact.
3. Для Contact генеруються унікальні:
   - Name
   - Phone
   - Email
4. Contact шукається після створення.
5. Перевіряється, що Contact знайдений.
6. Contact видаляється.
7. Перевіряється, що видалений Contact більше не знаходиться.
8. Створюються screenshots для evidence.


10. PRESENCE

Перевіряє зміну Presence status для User 1.

Перевіряються:

- Busy
- At Desk
- Online

Для кожного status перевіряється, що він реально став активним.

Також створюються screenshots для evidence.


11. CHATS

Перевіряє відправлення повідомлення конкретному контакту.

При виборі Chats у Runner з'являється:

Chat Configuration


Поля:

Specific Recipient

Ім'я контакту, якому потрібно відправити повідомлення.


Occurrence

Використовується, якщо у списку є декілька контактів з однаковим ім'ям.

Наприклад:

1

означає перший знайдений Contact з таким ім'ям.


Message

Текст повідомлення.

Якщо поле залишити порожнім, автоматично буде створено повідомлення виду:

AutoTest <timestamp>


Delete chat after send

Якщо checkbox увімкнений:

1. Message відправляється.
2. Перевіряється, що Message з'явилось.
3. Створений Chat видаляється.
4. Перевіряється, що Chat більше не присутній.

Якщо checkbox вимкнений — Chat залишається.


12. MESSAGING AUDIT

Перевіряє контакти User 1 на доступність Messaging.

Для кожного Contact перевіряється:

- чи є Phone Number;
- чи можна відкрити Contact;
- чи доступне поле для Messaging.

У логах наприкінці виводиться summary:

- Total contacts
- With phone number
- Without phone number
- Messaging available
- Messaging unavailable
- Failed to open

Якщо Contact не вдалося відкрити, створюється screenshot.


13. CHAT CLEANUP

Видаляє всі Chats для User 1.

ЦЕ DESTRUCTIVE SCENARIO.

Перед запуском Runner показує warning та просить підтвердження.

Після підтвердження тест:

1. Відкриває chats.
2. Послідовно видаляє їх.
3. Перевіряє, що наприкінці chats більше немає.

Не запускайте Chat Cleanup для акаунта, чати якого потрібно зберегти.


14. INTERNAL CALLS

Перевіряє внутрішні дзвінки між User 1 та User 2.

Для цього сценарію обов'язково потрібно мінімум два користувачі.

Runner автоматично робить Internal Calls доступним після додавання другого User.

Сценарій перевіряє дзвінки між двома extensions, Answer, Active Call та завершення дзвінка.


15. CALL CONTROLS

Для цього сценарію також потрібно мінімум два користувачі.

Перевіряється:

- Incoming Call
- Answer
- Reject
- Active Call
- Separate Window
- Hold
- Resume
- Mute
- Unmute
- Start Recording
- Stop Recording
- More Call Actions
- Video
- Device
- Add Party
- Transcript
- Call Transfer
- Transfer Search
- Blind Transfer
- Attended Transfer
- завершення дзвінка


16. REAL CALL / REAL SMS

У поточній версії поля Real Call та Real SMS вже присутні в інтерфейсі Runner.

Для Real Call можна вказати:

Destination Number


Для Real SMS можна вказати:

- Destination Number
- Message


ВАЖЛИВО:

На цей момент зовнішні Selenium-сценарії для Real Call та Real SMS ще не підключені.

Якщо вибрати ці сценарії, Runner повідомить:

External Selenium scenarios are not connected yet and will be skipped.

Тобто фактичний Real Call або Real SMS у поточній версії не виконується.


17. ВИБІР БРАУЗЕРА

У вікні Runner відображаються:

- Google Chrome
- Mozilla Firefox
- Microsoft Edge
- Brave
- Opera
- Safari

Google Chrome вибраний за замовчуванням.


ВАЖЛИВО ДЛЯ ПОТОЧНОЇ ВЕРСІЇ:

Реалізований WebDriver запуск для:

- Chrome
- Firefox
- Edge

Саме ці три браузери потрібно використовувати для запуску тестів.

Brave, Opera та Safari присутні в UI, але їх WebDriver implementation у поточній версії не підключений.

Safari також доступний у UI тільки на macOS.


18. ЗАПУСК В ОДНОМУ АБО ДЕКІЛЬКОХ БРАУЗЕРАХ

Можна вибрати декілька підтримуваних браузерів одночасно.

Наприклад:

[x] Google Chrome
[x] Mozilla Firefox
[x] Microsoft Edge

Runner послідовно запустить вибрані сценарії для кожного браузера.

Для кожного browser є окрема вкладка Log.


19. КНОПКИ TEST RUNNER

Run Selected

Запускає вибрані сценарії.


Select All Tests

Вибирає основні автоматизовані сценарії.

Real Call та Real SMS автоматично через Select All Tests не вибираються.


Clear Selection

Знімає вибір зі сценаріїв.


Export Logs

Копіює logs та evidence поточного запуску у вибрану папку.


Clear Logs

Очищає текст у Log tabs поточного вікна.


+ Add User

Додає ще одного користувача у Test Configuration.


Remove

Видаляє додаткового користувача.

User 1 видалити не можна.


20. LOGS

У нижній частині Runner є окремі вкладки:

- General
- Chrome
- Firefox
- Edge
- Brave
- Opera
- Safari


General

Показує загальну інформацію про запуск:

- Domain
- Users
- вибрані Browsers
- directory для logs
- результат для кожного browser


Browser tabs

Показують Gradle / TestNG output конкретного browser.


При успішному виконанні:

BUILD SUCCESSFUL

і в General:

browser: PASSED


При помилці:

BUILD FAILED

і в General:

browser: FAILED


21. ДЕ ЗБЕРІГАЮТЬСЯ LOGS

Для кожного запуску Runner автоматично створює окрему папку:

runner_logs

Всередині створюється папка з датою та часом запуску.

Приклад:

runner_logs/2026-09-23_14-30-15-123/


У ній зберігаються:

- browser log files;
- run-config.json;
- screenshots;
- failure evidence.


22. EXPORT LOGS

Після запуску можна натиснути:

Export Logs

Після цього:

1. Вибрати потрібну папку.
2. Runner створить окрему папку виду:

RingotelTestRunner_<date_time>

3. Туди буде скопійований весь evidence поточного запуску.

Цю папку можна передати іншій людині для аналізу результатів.


23. SCREENSHOTS ТА FAILURE EVIDENCE

Тести створюють screenshots під час деяких успішних перевірок.

Якщо test падає, система також збирає failure evidence.

При запуску через Runner evidence зберігається всередині папки конкретного run у:

runner_logs

Це дозволяє разом з log побачити стан Ringotel Web у момент перевірки або помилки.


24. VALIDATION ПЕРЕД ЗАПУСКОМ

Runner не дозволить запустити сценарій, якщо не заповнені обов'язкові поля.

Наприклад:

- Domain не може бути пустим.
- Extension не може бути пустим.
- Password не може бути пустим.
- Для Internal Calls потрібні мінімум 2 Users.
- Для Call Controls потрібні мінімум 2 Users.
- Для Chats потрібен Specific Recipient.
- Occurrence має бути числом від 1.


25. ПОПЕРЕДЖЕННЯ ПРО DESTRUCTIVE ACTIONS

Перед потенційно небезпечною дією Runner показує confirmation window.

Наприклад:

Chat Cleanup will delete ALL chats for User 1.

Потрібно підтвердити дію через Yes.

Якщо вибрати No — запуск цієї конфігурації не продовжиться.


26. АЛЬТЕРНАТИВНИЙ ЗАПУСК ОКРЕМОГО TEST ЧЕРЕЗ CMD

Основний рекомендований спосіб роботи з цим проєктом — через:

gradlew.bat run

та графічний Test Runner.

Але окремі test classes можна запускати напряму.


Login:

gradlew.bat test --tests "ringotel.tests.LoginTests" -Pbrowser=chrome


Presence:

gradlew.bat test --tests "ringotel.tests.PresenceTests" -Pbrowser=chrome


Contacts:

gradlew.bat test --tests "ringotel.tests.ContactTests" -Pbrowser=chrome


Internal Calls:

gradlew.bat test --tests "ringotel.tests.CallTests" -Pbrowser=chrome


Call Controls:

gradlew.bat test --tests "ringotel.tests.CallControlsTests" -Pbrowser=chrome


Firefox:

-Pbrowser=firefox


Edge:

-Pbrowser=edge


Для Chats рекомендовано використовувати GUI Runner, оскільки Recipient, Occurrence, Message та Delete after send передаються з Chat Configuration.


27. ЯКИЙ СПОСІБ ЗАПУСКУ ВИКОРИСТОВУВАТИ

Для звичайного використання:

gradlew.bat run

Далі все налаштовується у GUI.


Прямий запуск через:

gradlew.bat test ...

потрібен в основному тоді, коли потрібно запустити конкретний test class без відкриття Runner.


28. КОРОТКА ІНСТРУКЦІЯ

1. Відкрити CMD у кореневій папці проєкту.

2. Запустити:

gradlew.bat run

3. У Ringotel Test Runner:

- вказати Domain;
- ввести Extension та Password;
- додати User 2, якщо потрібні Calls;
- вибрати Scenarios;
- вибрати Chrome / Firefox / Edge;
- для Chats заповнити Chat Configuration;
- натиснути Run Selected.

4. Перевірити результат у:

General

та у вкладці відповідного Browser.

5. Якщо потрібно передати результати:

Export Logs


29. ВАЖЛИВО

Тести виконують реальні дії в тестовому Ringotel environment.

Залежно від вибраного сценарію вони можуть:

- виконувати Login;
- створювати та видаляти Contacts;
- відправляти Messages;
- видаляти Chats;
- змінювати Presence;
- здійснювати Internal Calls;
- виконувати Answer / Reject;
- вмикати Call Recording.

Перед запуском перевірте правильність:

- Domain;
- Extensions;
- Passwords;
- вибраних Scenarios.

Особливо уважно використовуйте:

Chat Cleanup

