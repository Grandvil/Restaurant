# Ресторан

Написана реализация небольшого ресторанчика, где несколько официантов и повар, и иногда заходят посетители, выбирают блюдо, едят и уходят.

### Работа программы
1. Создание потоков-официантов, потоков-посетителей и потока-повара
2. Посетитель заходит в ресторан и через какое-то время придумывает, что заказать
3. Официант принимает заказ и через некоторое время приносит его
4. Поток-посетитель завершает свою работу с задержкой, эмулируя таким образом прием пищи.

### Требования к программе
1. Каждый ключевой этап должен сопровождаться выводом в консоль текущего статуса, например: "Официант 1 несет заказ"
2. Все задержки (время приготовления блюда, таймаут захода посетителей) должны быть оформлены в константах (никаких "Магических чисел")
3. Ресторан должен обслужить 5 посетителей и закрыться

### Результат работы программы
```
Повар на работе!
Официант 1 на работе!
Официант 2 на работе!
Официант 3 на работе!
Посетитель 1 в ресторане!
Посетитель 2 в ресторане!
Официант 1 взял заказ
Повар  готовит блюдо
Посетитель 3 в ресторане!
Официант 2 взял заказ
Повар  закончил готовить
Повар  готовит блюдо
Официант 1 несет заказ
Посетитель 1 приступил к еде
Официант 3 взял заказ
Посетитель 4 в ресторане!
Повар  закончил готовить
Повар  готовит блюдо
Официант 2 несет заказ
Посетитель 2 приступил к еде
Посетитель 1 вышел из ресторана
Официант 1 взял заказ
Посетитель 5 в ресторане!
Повар  закончил готовить
Повар  готовит блюдо
Официант 3 несет заказ
Посетитель 3 приступил к еде
Посетитель 2 вышел из ресторана
Официант 2 взял заказ
Повар  закончил готовить
Повар  готовит блюдо
Официант 1 несет заказ
Посетитель 3 вышел из ресторана
Посетитель 4 приступил к еде
Повар  закончил готовить
Посетитель 4 вышел из ресторана
Официант 2 несет заказ
Посетитель 5 приступил к еде
Посетитель 5 вышел из ресторана
```