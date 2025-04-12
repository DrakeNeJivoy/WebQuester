const { createApp } = Vue;

createApp({
    data() {
        return {
            isCollapsed: false, // Состояние бокового меню (свернуто/развернуто)
            activeMenu: 'home' // Активный пункт меню
        };
    },
    methods: {
        toggleSidebar() {
            this.isCollapsed = !this.isCollapsed;
        },
        setActiveMenu(menu) {
            this.activeMenu = menu;
        }
    }
}).mount('#app');