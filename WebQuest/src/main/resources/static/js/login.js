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
                    this.email = '';
                    this.password = '';
                    // Ожидаем перенаправление на другую страницу
                    window.location.href = '/dashboard'; // Переход на главную страницу
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
            window.location.href = '/register'; // Перенаправление на страницу регистрации
        }
    }
}).mount("body");
