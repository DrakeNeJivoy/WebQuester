const { createApp } = Vue;

createApp({
    data() {
        return {
            isCollapsed: false,
            activeMenu: 'home'
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