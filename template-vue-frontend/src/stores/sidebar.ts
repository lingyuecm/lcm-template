import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useSidebarStore = defineStore('sidebar', () => {
  const sidebarExpanded = ref(true)

  function toggleSidebar() {
    sidebarExpanded.value = !sidebarExpanded.value
  }

  return { sidebarExpanded, toggleSidebar }
})