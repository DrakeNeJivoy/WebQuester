document.addEventListener("DOMContentLoaded", function () {
    const surveyForm = document.getElementById("survey-form");

    surveyForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Предотвращаем стандартную отправку формы

        const questions = document.querySelectorAll(".question");

        // Проверяем, что для каждого вопроса выбран хотя бы один ответ
        for (let question of questions) {
            const questionIndex = question.querySelector("h2 span:first-child").textContent;
            const radioInputs = question.querySelectorAll("input[type='radio']");
            const checkboxInputs = question.querySelectorAll("input[type='checkbox']");

            if (radioInputs.length > 0) {
                // Для радиокнопок проверка встроена через required
                continue;
            } else if (checkboxInputs.length > 0) {
                // Для чекбоксов проверяем, что хотя бы один выбран
                const checked = Array.from(checkboxInputs).some(input => input.checked);
                if (!checked) {
                    alert(`Пожалуйста, выберите хотя бы один вариант ответа для вопроса ${questionIndex}`);
                    return;
                }
            }
        }

        // Если все проверки пройдены, отправляем форму
        fetch(surveyForm.action, {
            method: "POST",
            body: new FormData(surveyForm)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(err => {
                        throw new Error(err || "Ошибка при отправке ответов"); // Убрана лишняя скобка
                    });
                }
                return response.text();
            })
            .then(data => {
                alert(data);
                window.location.href = "/home";
            })
            .catch(error => {
                console.error("Ошибка:", error);
                alert("Ошибка при отправке ответов: " + error.message);
            });
    });
});