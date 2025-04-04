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
                const formData = new FormData();
                formData.append("email", this.email);
                formData.append("password", this.password);

                const response = await fetch('/login', {
                    method: 'POST',
                    body: formData
                });

                if (response.redirected) {
                    window.location.href = response.url; // Перенаправление при успешном входе
                } else {
                    const data = await response.text();
                    this.message = data || 'Ошибка входа';
                    this.messageType = 'error';
                }
            } catch (error) {
                this.message = 'Ошибка сервера. Попробуйте позже.';
                this.messageType = 'error';
            }
        }
    }
}).mount("#loginForm");