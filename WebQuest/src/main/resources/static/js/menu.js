const { createApp } = Vue;

const app = createApp({
    delimiters: ['{{', '}}'], // Изменяем синтаксис Vue
    data() {
        return {
            isMenuOpen: false,
            activeMenu: 'surveys',
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
        }
    },
    computed: {
        translations() {
            return translations[this.language];
        }
    }
}).mount('#app');