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
                const formData = new FormData();
                formData.append("username", this.username);
                formData.append("email", this.email);
                formData.append("password", this.password);

                const response = await fetch('/register', {
                    method: 'POST',
                    body: formData
                });

                if (response.redirected) {
                    window.location.href = response.url; // Перенаправление
                } else {
                    const data = await response.text();
                    this.message = data || 'Ошибка регистрации';
                    this.messageType = 'error';
                }
            } catch (error) {
                this.message = 'Ошибка сервера. Попробуйте позже.';
                this.messageType = 'error';
            }
        }
    }
}).mount("#registerForm");