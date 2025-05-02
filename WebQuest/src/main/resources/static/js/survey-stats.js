const { createApp } = Vue;

createApp({
    data() {
        return {
            isMenuOpen: false,
            activeMenu: ''
        };
    },
    methods: {
        toggleMenu() {
            this.isMenuOpen = !this.isMenuOpen;
        },
        setActiveMenu(menu) {
            this.activeMenu = menu;
        }
    }
}).mount('#app');
