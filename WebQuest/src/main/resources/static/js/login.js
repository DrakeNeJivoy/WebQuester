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
                console.log('Submitting login form...');
                console.log('Email:', this.email, 'Password:', this.password);

                const formData = new URLSearchParams();
                formData.append("email", this.email);
                formData.append("password", this.password);

                const response = await fetch('/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: formData
                });

                console.log('Response status:', response.status);
                console.log('Response redirected:', response.redirected);
                console.log('Response URL:', response.url);

                if (response.redirected) {
                    // Успешный логин, Spring Security перенаправляет
                    window.location.href = response.url;
                } else {
                    // Ошибка логина, Spring Security перенаправляет на /login?error
                    if (response.url.includes('error')) {
                        this.message = 'Неверный email или пароль';
                        this.messageType = 'error';
                    } else {
                        this.message = 'Ошибка входа';
                        this.messageType = 'error';
                    }
                }
            } catch (error) {
                console.error('Error during login:', error);
                this.message = 'Ошибка сервера. Попробуйте позже.';
                this.messageType = 'error';
            }
        }
    },
    mounted() {
        // Проверяем, есть ли параметр error в URL при загрузке страницы
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has('error')) {
            this.message = 'Неверный email или пароль';
            this.messageType = 'error';
        }
    }
}).mount("#loginForm");