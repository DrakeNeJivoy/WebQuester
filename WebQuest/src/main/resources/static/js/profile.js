document.addEventListener('DOMContentLoaded', function () {
    const app = Vue.createApp({
        data() {
            return {
                isMenuOpen: false,
                activeMenu: 'profile'
            };
        },
        methods: {
            toggleMenu() {
                this.isMenuOpen = !this.isMenuOpen;
            },
            setActiveMenu(menu) {
                this.activeMenu = menu;
                this.isMenuOpen = false;
                // Удаляем класс active со всех пунктов
                document.querySelectorAll('.menu-list a').forEach(a => a.classList.remove('active'));
                // Находим и активируем нужный пункт по значению href
                const activeLink = document.querySelector(`.menu-list a[href="/${menu}"]`);
                if (activeLink) {
                    activeLink.classList.add('active');
                }
            }
        },
        mounted() {
            // При загрузке страницы явно устанавливаем активный пункт "Профиль"
            this.setActiveMenu('profile');
        }
    }).mount('#app');

    // Закрытие меню при клике вне его
    document.addEventListener('click', function (event) {
        const menu = document.querySelector('.burger-menu');
        const button = document.querySelector('.burger-btn');
        if (!menu.contains(event.target) && !button.contains(event.target) && app.isMenuOpen) {
            app.isMenuOpen = false;
        }
    });
});