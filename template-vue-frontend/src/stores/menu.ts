import { defineStore } from 'pinia'
import { ref } from 'vue'

export type Menu = {
  title: string,
  path: string,
  children?: Menu[]
}

export const useMenuStore = defineStore('menu', () => {
  const menuTree = ref<Menu[]>([
    {
      title: 'Dashboard',
      path: '/'
    },
    {
      title: 'Backend Management',
      path: '/',
      children: [
        {
          title: 'User Management',
          path: '/backendMgmt/userMgmt'
        },
        {
          title: 'Role Management',
          path: '/backendMgmt/roleMgmt'
        },
        {
          title: 'Permission Management',
          path: '/backendMgmt/permissionMgmt'
        }
      ]
    }
  ])

  function setMenuTree(menus: Menu[]) {
    menuTree.value = menus
  }

  return { menuTree, setMenuTree }
})