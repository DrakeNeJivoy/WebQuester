const { createApp } = Vue;

createApp({
    data() {
        return {
            isMenuOpen: false,
            activeMenu: 'surveys'
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
    }
}).mount('#app');