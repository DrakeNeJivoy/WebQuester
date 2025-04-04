const { createApp } = Vue;

createApp({
    data() {
        return {
            email: '',
            password: ''
        };
    },
    methods: {
        submitLogin() {
            console.log('Submitting login form...');
            console.log('Email:', this.email, 'Password:', this.password);

            // Отправка данных на сервер
            fetch('/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                    'email': this.email,
                    'password': this.password
                })
            })
            .then(response => {
                if (response.ok) {
                    console.log('Login successful');
                    window.location.href = '/'; // Перенаправление после успешного логина
                } else {
                    console.log('Login failed');
                }
            })
            .catch(error => {
                console.error('Error during login:', error);
            });
        }
    }
}).mount('#loginForm');