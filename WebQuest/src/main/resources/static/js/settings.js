const { createApp } = Vue;

const app = createApp({
    delimiters: ['{{', '}}'], // Изменяем синтаксис Vue
    data() {
        return {
            isMenuOpen: false,
            activeMenu: 'settings',
            theme: localStorage.getItem('theme') || 'light',
            language: localStorage.getItem('language') || 'ru'
        };
    },
    methods: {
        toggleMenu() {
            this.isMenuOpen = !this.isMenuOpen;
        },
        setActiveMenu(menu) {
            this.activeMenu = menu;
            this.isMenuOpen = false;
        },
        setTheme(theme) {
            this.theme = theme;
            localStorage.setItem('theme', theme);
        },
        setLanguage() {
            localStorage.setItem('language', this.language);
            this.$forceUpdate(); // Принудительно обновляем рендеринг
        }
    },
    computed: {
        translations() {
            return translations[this.language];
        }
    }
}).mount('#app');