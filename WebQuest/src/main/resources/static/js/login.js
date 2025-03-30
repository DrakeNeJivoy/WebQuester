Vue.createApp({
    data() {
        return {
            email: "",
            password: "",
            message: "",
            messageType: ""
        };
    },
    methods: {
        async submitLogin() {
            try {
                const response = await fetch('/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        email: this.email,
                        password: this.password
                    })
                });
                const result = await response.json();
                if (result.success) {
                    this.message = result.message || 'Успешный вход!';
                    this.messageType = 'success';
                    // Очищаем форму
                    this.email = '';
                    this.password = '';
                    // Можно перенаправить пользователя, например:
                    // window.location.href = '/dashboard';
                } else {
                    this.message = result.message || 'Ошибка входа';
                    this.messageType = 'error';
                }
            } catch (error) {
                this.message = 'Ошибка сервера. Попробуйте позже.';
                this.messageType = 'error';
            }
        },
        showRegister() {
            document.getElementById("loginApp").classList.add("d-none");
            document.getElementById("registerApp").classList.remove("d-none");
        }
    }
}).mount("#loginApp");