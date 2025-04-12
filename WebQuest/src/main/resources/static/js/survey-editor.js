document.addEventListener("DOMContentLoaded", function () {
    const surveyForm = document.getElementById("survey-form");
    const questionsContainer = document.getElementById("questions-container");
    const addQuestionButton = document.getElementById("add-question");
    const surveyTitleInput = document.getElementById("survey-title");

    let questionCount = document.querySelectorAll(".question").length;
    const surveyId = window.location.pathname.split('/').pop();

    function addQuestion(questionData) {
        questionCount++;
        const questionDiv = document.createElement("div");
        questionDiv.classList.add("question");
        questionDiv.innerHTML = `
            <div class="question-header">
                <label for="question-${questionCount}">
                    <i class="fas fa-question-circle"></i> Вопрос ${questionCount}:
                </label>
                <button type="button" class="delete-question" data-question="${questionCount}">
                    <i class="fas fa-trash-alt"></i>
                </button>
            </div>
            <input type="text" id="question-${questionCount}" name="questions[${questionCount}][text]" required>
            <label class="checkbox-label">
                <input type="checkbox" class="has-correct-answer" data-question="${questionCount}">
                <span>Есть правильный ответ</span>
            </label>
            <div class="answers-container" id="answers-${questionCount}">
                <button type="button" class="add-answer" data-question="${questionCount}">
                    <i class="fas fa-plus-circle"></i> Добавить вариант ответа
                </button>
            </div>
        `;
        questionsContainer.appendChild(questionDiv);

        if (questionData) {
            const questionTextInput = questionDiv.querySelector(`input[type="text"]`);
            questionTextInput.value = questionData.text;

            if (questionData.answerOptions && questionData.answerOptions.length > 0) {
                questionData.answerOptions.forEach((answerOption, index) => {
                    addAnswerOption(questionCount, answerOption, index + 1);
                });
            }
        }
        attachEventListeners();
    }

    function addAnswerOption(questionIndex, answerOptionData, answerIndex) {
        const answersContainer = document.getElementById(`answers-${questionIndex}`);
        const answerDiv = document.createElement("div");
        answerDiv.classList.add("answer");
        answerDiv.innerHTML = `
            <input type="text" name="questions[${questionIndex}][answerOptions][]" required>
            <input type="checkbox" class="correct-answer-checkbox" name="questions[${questionIndex}][correct]" value="2" style="display: none;">
            <button type="button" class="delete-answer" data-question="${questionIndex}" data-answer="${answerIndex || (answersContainer.querySelectorAll('.answer').length + 1)}">
                <i class="fas fa-trash-alt"></i>
            </button>
        `;
        answersContainer.insertBefore(answerDiv, answersContainer.querySelector(".add-answer"));

        if (answerOptionData) {
            const answerTextInput = answerDiv.querySelector("input[type='text']");
            answerTextInput.value = answerOptionData.text;
            if (answerOptionData.status === 2) {
                answerDiv.querySelector(".correct-answer-checkbox").checked = true;
                answerDiv.querySelector(".correct-answer-checkbox").style.display = "inline-block";
            }
        }
    }

    addQuestionButton.addEventListener("click", function () {
        addQuestion();
    });

    function attachEventListeners() {
        // Обработчик для добавления варианта ответа
        document.querySelectorAll(".add-answer").forEach(button => {
            button.removeEventListener("click", addAnswerHandler);
            button.addEventListener("click", addAnswerHandler);
        });

        // Обработчик для удаления варианта ответа
        document.querySelectorAll(".delete-answer").forEach(button => {
            button.removeEventListener("click", deleteAnswerHandler);
            button.addEventListener("click", deleteAnswerHandler);
        });

        // Обработчик для удаления вопроса
        document.querySelectorAll(".delete-question").forEach(button => {
            button.removeEventListener("click", deleteQuestionHandler);
            button.addEventListener("click", deleteQuestionHandler);
        });

        // Обработчик для чекбокса "Есть правильный ответ"
        document.querySelectorAll(".has-correct-answer").forEach(checkbox => {
            checkbox.removeEventListener("change", checkboxHandler);
            checkbox.addEventListener("change", checkboxHandler);
        });
    }

    function addAnswerHandler() {
        const questionIndex = this.dataset.question;
        addAnswerOption(questionIndex);
    }

    function deleteAnswerHandler() {
        const answerDiv = this.parentElement;
        answerDiv.remove();
    }

    function deleteQuestionHandler() {
        const questionDiv = this.parentElement.parentElement;
        questionDiv.remove();
    }

    function checkboxHandler() {
        const questionIndex = this.dataset.question;
        const checkboxes = document.querySelectorAll(`#answers-${questionIndex} .correct-answer-checkbox`);
        checkboxes.forEach(cb => cb.style.display = this.checked ? "inline-block" : "none");
    }

    function loadSurveyData() {
        fetch(`/surveys/survey/${surveyId}`)
            .then(response => response.json())
            .then(survey => {
                surveyTitleInput.value = survey.title;
                fetch(`/questions/survey/${surveyId}`)
                    .then(response => response.json())
                    .then(questions => {
                        questions.forEach(question => {
                            addQuestion(question);
                        });
                    });
            });
    }

    surveyForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const surveyData = {
            title: surveyTitleInput.value,
            questions: []
        };

        document.querySelectorAll(".question").forEach((questionDiv, index) => {
            const questionText = questionDiv.querySelector(`input[type="text"]`).value;
            const hasCorrectAnswer = questionDiv.querySelector(".has-correct-answer").checked;
            const answerOptions = [];

            questionDiv.querySelectorAll(".answer").forEach(answerDiv => {
                const answerText = answerDiv.querySelector("input[type='text']").value;
                const isCorrect = hasCorrectAnswer && answerDiv.querySelector(".correct-answer-checkbox").checked;
                const status = hasCorrectAnswer ? (isCorrect ? 2 : 1) : 0;
                answerOptions.push({
                    text: answerText,
                    status: status
                });
            });

            surveyData.questions.push({
                text: questionText,
                answerOptions: answerOptions
            });
        });

        fetch(`/surveys/survey/${surveyId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(surveyData)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(err => {
                        throw new Error(err || "Ошибка при сохранении анкеты");
                    });
                }
                return response.text();
            })
            .then(data => {
                alert(data);
                window.location.href = '/home';
            })
            .catch(error => {
                console.error("Ошибка:", error);
                alert("Ошибка при сохранении анкеты: " + error.message);
            });
    });

    loadSurveyData();
    attachEventListeners();
});