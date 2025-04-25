document.addEventListener("DOMContentLoaded", function () {
    const surveyForm = document.getElementById("survey-form");

    surveyForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Предотвращаем стандартную отправку формы

        const questions = document.querySelectorAll(".question");
        console.log("Найдено вопросов:", questions.length);

        const answersToSend = {};

        questions.forEach((question, index) => {
            console.log("Проверка вопроса:", index + 1, question);

            const questionHeader = question.querySelector("h2");
            console.log("Заголовок вопроса (h2):", questionHeader);

            let questionIndex = "не определен";

            if (questionHeader && questionHeader.textContent) {
                const match = questionHeader.textContent.match(/Вопрос\s*(\d+)/);
                if (match && match[1]) {
                    questionIndex = match[1];
                } else {
                    console.warn("Не удалось извлечь номер вопроса из текста h2:", questionHeader.textContent);
                }
            } else {
                console.warn("Не удалось найти заголовок h2 или его текст для вопроса:", index + 1, question);
            }

            const radioInputs = question.querySelectorAll("input[type='radio']:checked");
            const checkboxInputs = question.querySelectorAll("input[type='checkbox']:checked");

            const selectedAnswers = [];
            radioInputs.forEach(input => selectedAnswers.push(input.value));
            checkboxInputs.forEach(input => selectedAnswers.push(input.value));

            answersToSend[questionIndex] = selectedAnswers;
        });

        console.log("Данные для отправки на сервер:", answersToSend); // Логируем данные перед отправкой

        // Отправляем форму асинхронно
        fetch(surveyForm.action, {
            method: "POST",
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: Object.keys(answersToSend).map(key => {
                return answersToSend[key].map(value => `answers[${key}][]=${value}`).join('&');
            }).join('&')
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(err => {
                        throw new Error(err || "Ошибка при отправке ответов");
                    });
                }
                return response.text(); // Получаем ответ сервера (сообщение об успехе)
            })
            .then(message => {
                console.log("Ответ сервера:", message); // Можно оставить для отладки
                window.location.href = "/submission-result"; // Перенаправляем на страницу результатов
            })
            .catch(error => {
                console.error("Ошибка:", error);
                alert("Ошибка при отправке ответов: " + error.message);
            });
    });
});