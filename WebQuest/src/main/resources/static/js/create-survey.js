const { createApp } = Vue;

createApp({
    data() {
        return {
            surveyData: {
                title: '',
                questions: []
            }
        };
    },
    methods: {
        addQuestion() {
            this.surveyData.questions.push({
                text: '',
                hasCorrectAnswer: false,
                answerOptions: []
            });
        },
        addAnswer(questionIndex) {
            this.surveyData.questions[questionIndex].answerOptions.push({
                text: '',
                isCorrect: false
            });
        },
        toggleCorrectAnswers(questionIndex) {
            // При изменении флага "Есть правильный ответ" сбрасываем все isCorrect, если флаг снят
            if (!this.surveyData.questions[questionIndex].hasCorrectAnswer) {
                this.surveyData.questions[questionIndex].answerOptions.forEach(answer => {
                    answer.isCorrect = false;
                });
            }
        },
        submitSurvey() {
            // Формируем данные для отправки
            const formattedData = {
                title: this.surveyData.title,
                questions: this.surveyData.questions.map(question => ({
                    text: question.text,
                    answerOptions: question.answerOptions.map(answer => ({
                        text: answer.text,
                        status: question.hasCorrectAnswer ? (answer.isCorrect ? 2 : 1) : 0
                    }))
                }))
            };

            fetch('/surveys/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formattedData)
            })
                .then(response => {
                    if (!response.ok) {
                        return response.text().then(err => {
                            throw new Error(err || 'Ошибка при создании анкеты');
                        });
                    }
                    return response.text();
                })
                .then(data => {
                    alert(data);
                    this.surveyData.title = '';
                    this.surveyData.questions = [];
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                    alert('Ошибка при создании анкеты: ' + error.message);
                });
        }
    }
}).mount('#app');