import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Menu } from '@/model/model'

export const useMenuStore = defineStore('menu', () => {
  const menuTree = ref<Menu[]>([])

  function setMenuTree(menus: Menu[]) {
    menuTree.value = menus
  }

  return { menuTree, setMenuTree }
})