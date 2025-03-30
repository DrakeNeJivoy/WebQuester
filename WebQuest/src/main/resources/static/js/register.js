Vue.createApp({
    data() {
        return {
            username: "",
            email: "",
            password: "",
            message: "",
            messageType: ""
        };
    },
    methods: {
        async submitRegister() {
            try {
                const response = await fetch('/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        username: this.username,
                        email: this.email,
                        password: this.password
                    })
                });
                const result = await response.json();
                if (result.success) {
                    this.message = result.message || 'Успешная регистрация!';
                    this.messageType = 'success';
                    // Очищаем форму
                    this.username = '';
                    this.email = '';
                    this.password = '';
                } else {
                    this.message = result.message || 'Ошибка регистрации';
                    this.messageType = 'error';
                }
            } catch (error) {
                this.message = 'Ошибка сервера. Попробуйте позже.';
                this.messageType = 'error';
            }
        },
        showLogin() {
            document.getElementById("registerApp").classList.add("d-none");
            document.getElementById("loginApp").classList.remove("d-none");
        }
    }
}).mount("#registerApp");