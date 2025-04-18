const { createApp } = Vue;

createApp({
    data() {
        return {
            theme: localStorage.getItem('theme') || 'light',
            language: localStorage.getItem('language') || 'ru',
            surveyData: {
                title: '',
                questions: []
            }
        };
    },
    computed: {
        translations() {
            return translations[this.language];
        }
    },
    methods: {
        addQuestion() {
            this.surveyData.questions.push({
                text: '',
                hasCorrectAnswer: false,
                answerOptions: []
            });
        },
        deleteQuestion(questionIndex) {
            this.surveyData.questions.splice(questionIndex, 1);
        },
        addAnswer(questionIndex) {
            this.surveyData.questions[questionIndex].answerOptions.push({
                text: '',
                isCorrect: false
            });
        },
        deleteAnswer(questionIndex, answerIndex) {
            this.surveyData.questions[questionIndex].answerOptions.splice(answerIndex, 1);
        },
        toggleCorrectAnswers(questionIndex) {
            if (!this.surveyData.questions[questionIndex].hasCorrectAnswer) {
                this.surveyData.questions[questionIndex].answerOptions.forEach(answer => {
                    answer.isCorrect = false;
                });
            }
        },
        submitSurvey() {
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
                            throw new Error(err || this.translations.createError);
                        });
                    }
                    return response.text();
                })
                .then(data => {
                    alert(data);
                    this.surveyData.title = '';
                    this.surveyData.questions = [];
                    windowV2(this.translations.successCreate);
                })
                .catch(error => {
                    console.error(this.translations.error, error);
                    alert(`${this.translations.createError}: ${error.message}`);
                });
        }
    }
}).mount('#app');