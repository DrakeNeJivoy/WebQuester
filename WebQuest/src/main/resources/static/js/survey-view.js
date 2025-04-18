const { createApp } = Vue;

const app = createApp({
    delimiters: ['{{', '}}'],
    data() {
        return {
            theme: localStorage.getItem('theme') || 'light',
            language: localStorage.getItem('language') || 'ru'
        };
    },
    computed: {
        translations() {
            return translations[this.language];
        }
    },
    mounted() {
        const surveyForm = document.getElementById("survey-form");

        surveyForm.addEventListener("submit", (event) => {
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
                        alert(`${this.translations.selectAtLeastOne} ${questionIndex}`);
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
                            throw new Error(err || this.translations.submitError);
                        });
                    }
                    return response.text();
                })
                .then(data => {
                    alert(data);
                    window.location.href = "/home";
                })
                .catch(error => {
                    console.error(this.translations.error, error);
                    alert(`${this.translations.submitError}: ${error.message}`);
                });
        });
    }
}).mount('#app');