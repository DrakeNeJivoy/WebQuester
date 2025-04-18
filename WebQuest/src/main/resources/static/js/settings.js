const { createApp } = Vue;

const app = createApp({
    delimiters: ['{{', '}}'], // Изменяем синтаксис Vue
    data() {
        return {
            isMenuOpen: false,
            activeMenu: 'settings',
            theme: localStorage.getItem('theme') || 'light',
            language: localStorage.getItem('language') || 'ru',
            translations: {
                ru: {
                    settings: 'Настройки',
                    surveys: 'Анкеты',
                    profile: 'Профиль',
                    about: 'О нас',
                    logout: 'Выйти',
                    theme: 'Тема',
                    light: 'Светлая',
                    dark: 'Темная',
                    language: 'Язык',
                    russian: 'Русский',
                    english: 'Английский'
                },
                en: {
                    settings: 'Settings',
                    surveys: 'Surveys',
                    profile: 'Profile',
                    about: 'About Us',
                    logout: 'Logout',
                    theme: 'Theme',
                    light: 'Light',
                    dark: 'Dark',
                    language: 'Language',
                    russian: 'Russian',
                    english: 'English'
                }
            }
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
        currentTranslations() {
            return this.translations[this.language];
        }
    }
}).mount('#app');